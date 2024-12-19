package br.com.weblinker.users.exceptions;

import java.io.Serializable;
import java.util.Date;

public class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Date timestamp;
    private int statusCode;
    private String message;
    private String details;

    public ExceptionResponse(Date timestamp, int statusCode, String message, String details) {
        this.timestamp = timestamp;
        this.statusCode = statusCode;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getDetails() {
        return details;
    }
}
