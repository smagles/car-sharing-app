package com.mate.carsharing.controller;

import com.mate.carsharing.controller.docs.PaymentControllerDocs;
import com.mate.carsharing.dto.payment.PaymentDto;
import com.mate.carsharing.dto.payment.PaymentRequestDto;
import com.mate.carsharing.model.User;
import com.mate.carsharing.service.payment.PaymentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController implements PaymentControllerDocs {
    private final PaymentService paymentService;

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid PaymentRequestDto requestDto,
                                    @AuthenticationPrincipal User user)
            throws StripeException {
        return paymentService.createPayment(requestDto, user);
    }

    @GetMapping("/my")
    public List<PaymentDto> getMyPayments(@AuthenticationPrincipal User user) {
        return paymentService.getUserPayments(user.getId());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('MANAGER')")
    public List<PaymentDto> getPaymentsByUser(@PathVariable Long userId) {
        return paymentService.getUserPayments(userId);
    }

    @PostMapping("/{paymentId}/renew")
    public PaymentDto renewPayment(@PathVariable Long paymentId,
                                   @AuthenticationPrincipal User user) throws StripeException {
        return paymentService.renewPayment(paymentId, user);
    }

    @GetMapping("/success")
    public String paymentSuccess(@RequestParam("session_id") String sessionId)
            throws StripeException {
        paymentService.updatePaymentStatus(sessionId);
        return "Payment successful!";
    }

    @GetMapping("/cancel")
    public String paymentCancel(@RequestParam("session_id") String sessionId) {
        return "Payment was canceled. You can try again within 24 hours";
    }

}
