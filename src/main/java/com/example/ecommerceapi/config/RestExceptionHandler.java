package com.example.ecommerceapi.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.*;

/**
 * The {@code RestExceptionHandler} class is a Spring Framework controller advice that handles exceptions
 * and returns meaningful error responses in a RESTful API format.
 *
 * It provides methods to handle specific types of exceptions and convert them into HTTP responses with
 * appropriate status codes and error messages.
 *
 * This class is responsible for transforming exceptions into standardized error responses, making it easier
 * for clients to understand and handle errors when interacting with the API.
 *
 * @author Tyler Cairney
 * @version 1.0
 */
@RestControllerAdvice
public class RestExceptionHandler {

    /**
     * Handles exceptions of type {@link ResponseStatusException}.
     *
     * @param theEx       The {@link ResponseStatusException} instance representing the exception.
     * @param theRequest  The web request associated with the exception.
     * @return A {@link ResponseEntity} containing an error response with the appropriate HTTP status code
     *         and error message.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(final ResponseStatusException theEx, final WebRequest theRequest) {
        HttpStatus status = HttpStatus.resolve(theEx.getStatusCode().value());
        if (status == null) {
            // Handle the case where the status code is not standard
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        ApiError apiError = new ApiError(status, theEx.getReason());
        return new ResponseEntity<>(apiError, status);
    }

    /**
     * Handles exceptions of type {@link ConstraintViolationException}.
     *
     * @param thEx    The {@link ConstraintViolationException} instance representing the validation exception.
     * @param theRequest The web request associated with the exception.
     * @return A {@link ResponseEntity} containing an error response with a HTTP status code of
     *         {@link HttpStatus#BAD_REQUEST} and a message indicating a validation error. It also includes
     *         details of validation errors.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(final ConstraintViolationException thEx,
                                                                     final WebRequest theRequest) {
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, "Validation error");
        apiError.addValidationErrors(thEx.getConstraintViolations());
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    /**
     * An inner class representing the structure of a standardized API error response.
     * It includes HTTP status, a message, and a list of validation errors.
     */
    private static class ApiError {

        private HttpStatus myStatus;

        private String myMessage;

        private List<String> validationErrors;

        /**
         * Constructs an {@code ApiError} instance with the given HTTP status and message.
         *
         * @param theStatus  The HTTP status code for the error.
         * @param theMessage The error message.
         */
        public ApiError(final HttpStatus theStatus, final String theMessage) {
            this.myStatus = theStatus;
            this.myMessage = theMessage;
            this.validationErrors = new ArrayList<>();
        }

        /**
         * Gets the HTTP status code of the error.
         *
         * @return The HTTP status code.
         */
        public HttpStatus getStatus() {
            return myStatus;
        }

        /**
         * Sets the HTTP status code of the error.
         *
         * @param theStatus The HTTP status code to set.
         */
        public void setStatus(final HttpStatus theStatus) {
            this.myStatus = theStatus;
        }

        /**
         * Gets the error message.
         *
         * @return The error message.
         */
        public String theMessage() {
            return myMessage;
        }

        /**
         * Sets the error message.
         *
         * @param theMessage The error message to set.
         */
        public void setMessage(final String theMessage) {
            this.myMessage = theMessage;
        }

        /**
         * Gets a list of validation error messages.
         *
         * @return A list of validation error messages.
         */
        public List<String> getValidationErrors() {
            return validationErrors;
        }

        /**
         * Adds validation errors to the list.
         *
         * @param constraintViolations A set of {@link ConstraintViolation} instances representing validation errors.
         */
        public void addValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
            constraintViolations.forEach(violation ->
                    validationErrors.add(violation.getPropertyPath() + ": " + violation.getMessage()));
        }
    }


}
