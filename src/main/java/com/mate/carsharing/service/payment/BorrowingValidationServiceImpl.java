package com.mate.carsharing.service.payment;

import com.mate.carsharing.exception.custom.ForbiddenOperationException;
import com.mate.carsharing.model.Payment;
import com.mate.carsharing.repository.PaymentRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowingValidationServiceImpl implements BorrowingValidationService {
    private final PaymentRepository paymentRepository;

    @Override
    public void validateNoPendingPayments(Long userId) {
        boolean hasUnresolvedPayments = paymentRepository.existsByUserIdAndStatusIn(
                userId, List.of(Payment.PaymentStatus.PENDING,
                        Payment.PaymentStatus.EXPIRED));

        if (hasUnresolvedPayments) {
            throw new ForbiddenOperationException(
                    "You must resolve your pending or "
                            + "expired payments before borrowing");
        }
    }

}
