package com.mate.carsharing.service.payment;

import com.mate.carsharing.exception.custom.ForbiddenOperationException;
import com.mate.carsharing.model.Payment;
import com.mate.carsharing.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowingValidationServiceImpl implements BorrowingValidationService {
    private final PaymentRepository paymentRepository;

    @Override
    public void validateNoPendingPayments(Long userId) {
        boolean hasPending = paymentRepository.existsByUserIdAndStatus(userId,
                Payment.PaymentStatus.PENDING);
        if (hasPending) {
            throw new ForbiddenOperationException(
                    "You must pay your pending payments before borrowing");
        }
    }
}
