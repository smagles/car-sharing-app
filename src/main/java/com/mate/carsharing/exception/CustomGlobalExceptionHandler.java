package com.mate.carsharing.exception;

import com.mate.carsharing.exception.custom.RegistrationException;
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

    /**
     * Handles validation errors by returning a structured response with a timestamp, HTTP 400 status, and a list of validation error messages.
     *
     * @param ex the exception containing validation errors
     * @param headers the HTTP headers to use for the response
     * @param status the HTTP status code
     * @param request the current web request
     * @return a response entity with error details for invalid method arguments
     */
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

    /****
     * Handles JwtException by returning a 401 Unauthorized response with the exception message.
     *
     * @param ex the JwtException thrown during JWT processing
     * @return a ResponseEntity containing the error details and HTTP status 401
     */
    @ExceptionHandler({JwtException.class})
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        return buildResponseEntity(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    /**
     * Handles AccessDeniedException by returning a 403 Forbidden response with the exception message.
     *
     * @return a ResponseEntity containing the error details and HTTP status 403
     */
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    /**
     * Handles cases where a requested entity is not found by returning a 404 Not Found response with the error message.
     *
     * @return a response entity containing the error details and HTTP status 404
     */
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleEntityNotFoundException(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    /****
     * Handles `RegistrationException` by returning a response with HTTP 409 Conflict and the exception message.
     *
     * @param ex the registration exception to handle
     * @return a response entity containing the error message and HTTP 409 status
     */
    @ExceptionHandler({RegistrationException.class})
    public ResponseEntity<Object> handleConflictExceptions(RuntimeException ex) {
        return buildResponseEntity(HttpStatus.CONFLICT, ex.getMessage());
    }

    /**
     * Constructs a ResponseEntity with a standardized error response body containing a timestamp, status code, and error message.
     *
     * @param status the HTTP status to set in the response
     * @param error the error message or details to include in the response body
     * @return a ResponseEntity with the constructed error body and specified HTTP status
     */
    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, Object error) {
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timestamp", LocalDateTime.now());
        responseBody.put("status", status.value());
        responseBody.put("error", error);
        return new ResponseEntity<>(responseBody, status);
    }

    /**
     * Formats a validation error message, including the field name if available.
     *
     * @param error the validation error to format
     * @return a formatted error message, prefixed with the field name for field errors
     */
    private String getErrorMessage(ObjectError error) {
        if (error instanceof FieldError) {
            String field = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            return field + ": " + errorMessage;
        }
        return error.getDefaultMessage();
    }
}
