package com.mate.carsharing.dto.payment;

import com.mate.carsharing.model.Payment;

public record PaymentCompletedEvent(Payment payment) {}
