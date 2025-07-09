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
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private static final String STRIPE_SESSION_STATUS_COMPLETE = "complete";
    private final PaymentRepository paymentRepository;
    private final RentalService rentalService;
    private final PaymentMapper paymentMapper;
    private final StripeService stripeService;
    private final PaymentCalculationStrategyFactory calculationStrategyFactory;
    private final PaymentCompletedEventListener paymentCompletedEventListener;

    @Override
    @Transactional
    public PaymentDto createPayment(PaymentRequestDto requestDto, User user)
            throws StripeException {
        Rental rental = rentalService.findRentalByIdAndUser(requestDto.rentalId(), user.getId());
        validateFineApplicable(rental, requestDto);

        PaymentCalculationStrategy strategy = calculationStrategyFactory
                .getStrategy(requestDto.type());
        BigDecimal amount = strategy.calculateAmount(rental);

        Session session = stripeService.createStripeSession(amount);
        Payment payment = createPaymentEntity(requestDto, rental, amount, session);

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    @Override
    public void updatePaymentStatus(String sessionId) throws StripeException {
        Session session = stripeService.retrieveSession(sessionId);
        if (STRIPE_SESSION_STATUS_COMPLETE.equals(session.getStatus())) {
            Payment payment = findPaymentBySessionId(sessionId);
            payment.setStatus(Payment.PaymentStatus.PAID);
            payment = paymentRepository.save(payment);
            paymentCompletedEventListener.handlePaymentCompleted(
                    new PaymentCompletedEvent(payment));
        }
    }

    @Override
    public List<PaymentDto> getUserPayments(Long userId) {
        return paymentRepository.findPaymentsByUserId(userId).stream()
                .map(paymentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto renewPayment(Long paymentId, User user) throws StripeException {
        Payment payment = findByIdAndUserId(paymentId, user);

        if (payment.getStatus() == Payment.PaymentStatus.PAID) {
            throw new PaymentAlreadyPaidException("Cannot renew payment with ID "
                            + paymentId + " because it is already marked as PAID");
        }

        Session newSession = stripeService.createStripeSession(payment.getAmount());
        payment.setSessionId(newSession.getId());
        payment.setSessionUrl(newSession.getUrl());
        payment.setStatus(Payment.PaymentStatus.PENDING);

        return paymentMapper.toDto(paymentRepository.save(payment));
    }

    private Payment findPaymentBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }

    private Payment createPaymentEntity(PaymentRequestDto requestDto,
                                        Rental rental, BigDecimal amount, Session session) {
        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setRental(rental);
        payment.setSessionId(session.getId());
        payment.setSessionUrl(session.getUrl());
        payment.setStatus(Payment.PaymentStatus.PENDING);
        payment.setType(requestDto.type());
        return payment;
    }

    private void validateFineApplicable(Rental rental, PaymentRequestDto requestDto) {
        if (requestDto.type() == Payment.PaymentType.FINE
                && !rental.getReturnDate().isBefore(LocalDate.now())) {
            throw new InvalidFineApplicationException(
                    "Fine cannot be applied: rental is not overdue.");
        }
    }

    private Payment findByIdAndUserId(Long paymentId, User user) {
        return paymentRepository.findByIdAndUserId(paymentId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
    }
}
