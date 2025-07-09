package com.mate.carsharing.controller.docs;

import com.mate.carsharing.dto.payment.PaymentDto;
import com.mate.carsharing.dto.payment.PaymentRequestDto;
import com.mate.carsharing.model.User;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Payment Management",
        description = "Endpoints for managing user payments and Stripe integration")
public interface PaymentControllerDocs {

    @Operation(
            summary = "Create a new payment",
            description = "Creates a payment session for rent or fine. "
                    + "Only overdue rentals are eligible.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment created successfully",
                            content = @Content(schema = @Schema(
                                    implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid input or rental not overdue")
            }
    )
    PaymentDto createPayment(@Valid @RequestBody PaymentRequestDto requestDto,
                             @AuthenticationPrincipal User user) throws StripeException;

    @Operation(
            summary = "Get my payments",
            description = "Retrieve payments for the currently authenticated user",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Payments retrieved successfully",
                            content = @Content(schema = @Schema(implementation = PaymentDto.class)))
            }
    )
    List<PaymentDto> getMyPayments(@AuthenticationPrincipal User user);

    @Operation(
            summary = "Get user payments",
            description = "Retrieve payments for a specific user by user ID. "
                    + "Requires MANAGER role.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Payments retrieved successfully",
                            content = @Content(schema = @Schema(
                                    implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "403",
                            description = "Forbidden - insufficient privileges")
            }
    )
    List<PaymentDto> getPaymentsByUser(
            @Parameter(description = "User ID") @PathVariable Long userId);

    @Operation(
            summary = "Renew payment",
            description = "Renew a pending payment if it has expired."
                    + " Cannot be renewed if already paid.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment renewed successfully",
                            content = @Content(schema = @Schema(
                                    implementation = PaymentDto.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Payment is already marked as PAID")
            }
    )
    PaymentDto renewPayment(
            @Parameter(description = "Payment ID") @PathVariable Long paymentId,
            @AuthenticationPrincipal User user) throws StripeException;

    @Operation(
            summary = "Stripe payment success",
            description = "Callback endpoint for successful Stripe payment session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment marked as successful")
            }
    )
    String paymentSuccess(
            @Parameter(description = "Stripe session ID")
            @RequestParam("session_id") String sessionId) throws StripeException;

    @Operation(
            summary = "Stripe payment cancel",
            description = "Callback endpoint for canceled Stripe payment session",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Payment was cancelled")
            }
    )
    String paymentCancel(
            @Parameter(description = "Stripe session ID")
            @RequestParam("session_id") String sessionId
    );
}
