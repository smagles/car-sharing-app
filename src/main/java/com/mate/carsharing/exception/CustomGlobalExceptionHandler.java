package com.mate.carsharing.exception;

import com.mate.carsharing.exception.custom.InvalidFineApplicationException;
import com.mate.carsharing.exception.custom.NoAvailableCarException;
import com.mate.carsharing.exception.custom.RegistrationException;
import com.mate.carsharing.exception.custom.RentalAlreadyReturnedException;
import com.stripe.exception.StripeException;
import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", HttpStatus.BAD_REQUEST.value());
        responseBody.put("errors", ex.getBindingResult().getAllErrors().stream()
                .map(this::getErrorMessage)
                .toList());

        return new ResponseEntity<>(responseBody, headers, status);
    }

    @ExceptionHandler({IllegalArgumentException.class, NoAvailableCarException.class,
            InvalidFineApplicationException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({JwtException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({RegistrationException.class, RentalAlreadyReturnedException.class})
    public ResponseEntity<Object> handleConflictExceptions(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<Object> handleStripeException(StripeException ex) {
        return buildResponseEntity(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, Object error) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", error);
        return new ResponseEntity<>(responseBody, status);
    }

    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            return field + ": " + errorMessage;
        }
        return error.getDefaultMessage();
    }
}
