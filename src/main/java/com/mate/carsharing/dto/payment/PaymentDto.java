package com.mate.carsharing.dto.payment;

import com.mate.carsharing.model.Payment;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    private Long id;
    private String sessionUrl;
    private String sessionId;
    private BigDecimal amount;
    private Long rentalId;
    private Payment.PaymentStatus status;
    private Payment.PaymentType type;
}
