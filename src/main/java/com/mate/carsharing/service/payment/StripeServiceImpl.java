package com.mate.carsharing.service.payment;

import com.mate.carsharing.exception.custom.InvalidAmountException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
@RequiredArgsConstructor
@Profile("!develop")
public class StripeServiceImpl implements StripeService {
    private static final BigDecimal MAX_AMOUNT = new BigDecimal("999999.99");
    @Value("${app.domain}")
    private String domain;
    @Value("${stripe.success.path}")
    private String successPath;
    @Value("${stripe.cancel.path}")
    private String cancelPath;

    @Override
    public Session createStripeSession(BigDecimal amount) throws StripeException {
        validateAmount(amount);
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(buildUrl(successPath))
                .setCancelUrl(buildUrl(cancelPath))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(buildPriceData(amount))
                                .build()
                )
                .build();
        return Session.create(params);
    }

    @Override
    public Session retrieveSession(String sessionId) throws StripeException {
        return Session.retrieve(sessionId);
    }

    private SessionCreateParams.LineItem.PriceData.ProductData buildProductData() {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName("Car Rental Payment")
                .build();
    }

    private SessionCreateParams.LineItem.PriceData buildPriceData(BigDecimal amount) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount(amount.multiply(BigDecimal.valueOf(100)).longValue())
                .setProductData(buildProductData()).build();
    }

    private String buildUrl(String path) {
        return UriComponentsBuilder.fromHttpUrl(domain)
                .path(path)
                .queryParam("session_id", "{CHECKOUT_SESSION_ID}")
                .toUriString();
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0
                || amount.compareTo(MAX_AMOUNT) > 0) {
            throw new InvalidAmountException(
                    "Amount must be between $0.00 and $999,999.99");
        }
    }

}
