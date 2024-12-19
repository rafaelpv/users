package br.com.weblinker.users.exceptions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private Environment environment;

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(
            Exception ex, WebRequest request) {

        HttpStatus status = getHttpStatusFromException(ex);

        String message;
        if (status == HttpStatus.INTERNAL_SERVER_ERROR) {
            message = isDebugMode() ? ex.getMessage() : "Server Internal Error";
            System.err.println("Internal Server Error: " + ex.getMessage());
        } else {
            message = ex.getMessage();
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                new Date(),
                status.value(),
                message,
                request.getDescription(false));

        return new ResponseEntity<>(exceptionResponse, status);
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

    private HttpStatus getHttpStatusFromException(Exception ex) {
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            return responseStatus.value();
        }
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}
