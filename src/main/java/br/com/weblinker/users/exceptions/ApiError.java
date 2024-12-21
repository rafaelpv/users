package br.com.weblinker.users.exceptions;

import java.time.LocalDateTime;
//TODO here add user ID
public class ApiError {
    private int status;
    private String error;
    private String message;
    private String path;
    private String method;
    private LocalDateTime timestamp;

    public ApiError(int status, String error, String message, String path, String method) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.method = method;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

