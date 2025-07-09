package com.mate.carsharing.service.payment;

public interface BorrowingValidationService {
    void validateNoPendingPayments(Long userId);
}
