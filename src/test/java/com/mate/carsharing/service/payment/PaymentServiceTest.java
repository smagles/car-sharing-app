package com.mate.carsharing.service.payment;

import com.mate.carsharing.dto.payment.PaymentCompletedEvent;
import com.mate.carsharing.dto.payment.PaymentDto;
import com.mate.carsharing.dto.payment.PaymentRequestDto;
import com.mate.carsharing.exception.custom.InvalidFineApplicationException;
import com.mate.carsharing.exception.custom.PaymentAlreadyPaidException;
import com.mate.carsharing.mapper.PaymentMapper;
import com.mate.carsharing.model.Payment;
import com.mate.carsharing.model.Rental;
import com.mate.carsharing.model.User;
import com.mate.carsharing.repository.PaymentRepository;
import com.mate.carsharing.service.payment.strategy.PaymentCalculationStrategy;
import com.mate.carsharing.service.payment.strategy.PaymentCalculationStrategyFactory;
import com.mate.carsharing.service.rental.RentalService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private RentalService rentalService;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock()
    private StripeService stripeService;
    @Mock
    private PaymentCalculationStrategyFactory strategyFactory;
    @Mock
    private PaymentCompletedEventListener eventListener;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User user;
    private Rental rental;
    private PaymentRequestDto requestDto;
    private PaymentDto paymentDto;
    private Payment payment;
    private Session session;
    private PaymentCalculationStrategy strategy;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        rental = new Rental();
        rental.setId(1L);
        rental.setReturnDate(LocalDate.now().minusDays(1));

        requestDto = new PaymentRequestDto( 1L, Payment.PaymentType.FINE);

        session = new Session();
        session.setId("session123");
        session.setUrl("http://stripe-session");
        session.setStatus("complete");

        payment = new Payment();
        payment.setId(1L);
        payment.setStatus(Payment.PaymentStatus.PENDING);

        paymentDto = new PaymentDto();
        paymentDto.setId(1L);

        strategy = mock(PaymentCalculationStrategy.class);
    }

    @Test
    @DisplayName("Verify createPayment(): should create payment successfully")
    void createPayment_shouldCreateSuccessfully() throws StripeException {
        when(rentalService.findRentalByIdAndUser(1L, 1L)).thenReturn(rental);
        when(strategyFactory.getStrategy(Payment.PaymentType.FINE)).thenReturn(strategy);
        when(strategy.calculateAmount(rental)).thenReturn(BigDecimal.TEN);
        when(stripeService.createStripeSession(BigDecimal.TEN)).thenReturn(session);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.createPayment(requestDto, user);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(paymentRepository).save(any(Payment.class));
        verify(stripeService).createStripeSession(BigDecimal.TEN);
    }

    @Test
    @DisplayName("Verify createPayment(): should throw when fine is not applicable")
    void createPayment_shouldThrowForInvalidFine() {
        rental.setReturnDate(LocalDate.now());
        when(rentalService.findRentalByIdAndUser(1L, 1L)).thenReturn(rental);

        assertThatThrownBy(() -> paymentService.createPayment(requestDto, user))
                .isInstanceOf(InvalidFineApplicationException.class);
    }

    @Test
    @DisplayName("Verify updatePaymentStatus(): should mark payment as paid when Stripe session is complete")
    void updatePaymentStatus_shouldMarkAsPaid() throws StripeException {
        payment.setStatus(Payment.PaymentStatus.PENDING);

        when(stripeService.retrieveSession("session123")).thenReturn(session);
        when(paymentRepository.findBySessionId("session123")).thenReturn(Optional.of(payment));
        when(paymentRepository.save(payment)).thenReturn(payment);

        paymentService.updatePaymentStatus("session123");

        assertThat(payment.getStatus()).isEqualTo(Payment.PaymentStatus.PAID);
        verify(eventListener).handlePaymentCompleted(any(PaymentCompletedEvent.class));
    }

    @Test
    @DisplayName("Verify getUserPayments(): should return payments for user")
    void getUserPayments_shouldReturnList() {
        when(paymentRepository.findPaymentsByUserId(1L)).thenReturn(List.of(payment));
        when(paymentMapper.toDto(payment)).thenReturn(paymentDto);

        List<PaymentDto> result = paymentService.getUserPayments(1L);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("Verify renewPayment(): should renew pending payment and return updated dto")
    void renewPayment_shouldRenewPendingPayment() throws StripeException {
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setAmount(BigDecimal.TEN);

        when(paymentRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(payment));
        when(stripeService.createStripeSession(BigDecimal.TEN)).thenReturn(session);
        when(paymentRepository.save(payment)).thenReturn(payment);
        when(paymentMapper.toDto(payment)).thenReturn(paymentDto);

        PaymentDto result = paymentService.renewPayment(1L, user);

        assertThat(result).isEqualTo(paymentDto);
        verify(stripeService).createStripeSession(BigDecimal.TEN);
        assertThat(payment.getStatus()).isEqualTo(Payment.PaymentStatus.PENDING);
    }

    @Test
    @DisplayName("Verify renewPayment(): should throw when payment is already paid")
    void renewPayment_shouldThrowIfPaid() {
        payment.setStatus(Payment.PaymentStatus.PAID);
        when(paymentRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(payment));

        assertThatThrownBy(() -> paymentService.renewPayment(1L, user))
                .isInstanceOf(PaymentAlreadyPaidException.class);
    }
}
