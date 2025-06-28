package com.mate.carsharing.service.payment;

import com.mate.carsharing.dto.payment.PaymentDto;
import com.mate.carsharing.dto.payment.PaymentRequestDto;
import com.mate.carsharing.model.User;
import com.stripe.exception.StripeException;
import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentRequestDto requestDto, User user) throws Exception;

    void updatePaymentStatus(String sessionId) throws StripeException;

    List<PaymentDto> getUserPayments(Long userId);
}
