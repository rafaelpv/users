package br.com.weblinker.users.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private Environment environment;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ApiError> handleUnexpectedException(
            Exception ex, HttpServletRequest request) {

        StackTraceElement[] stackTrace = ex.getStackTrace();
        String debugMessage = "Message: " + ex.getMessage() + "; ";
        debugMessage += "File: " + stackTrace[0].getFileName() + "; ";
        debugMessage += "Line: " + stackTrace[0].getLineNumber() + "; ";

        String responseMessage = isDebugMode() ? debugMessage : "Server Internal Error";
        LOG.error("Internal Server Error: " + debugMessage);

        return this.getApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, responseMessage, request);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiError> handleUnauthorizedAccessException(
            UnauthorizedAccessException ex,
            HttpServletRequest request) {
        return this.getApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage(), request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {
        return this.getApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getMessage(), request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFoundExceptionException(
            NoResourceFoundException ex,
            HttpServletRequest request) {
        return this.getApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFoundException(
            ResourceNotFoundException ex,
            HttpServletRequest request) {
        return this.getApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request) {
        return this.getApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        StringBuilder details = new StringBuilder();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            details.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        }

        return this.getApiErrorResponse(HttpStatus.BAD_REQUEST, details.toString().trim(), request);
    }

    private ResponseEntity<ApiError> getApiErrorResponse(
            HttpStatus status,
            String message,
            HttpServletRequest request
            ) {
        ApiError error = new ApiError(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI(),
                request.getMethod()
        );

        return new ResponseEntity<>(error, status);
    }

    private boolean isDebugMode() {
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equals("dev") || profile.equals("debug")) {
                return true;
            }
        }
        return false;
    }
}