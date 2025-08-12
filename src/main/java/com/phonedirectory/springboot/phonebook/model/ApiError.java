package com.phonedirectory.springboot.phonebook.model;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ApiError {

    private String message;
    private String stackTrace;

    public ApiError(String message) {
        this.message = message;
    }


    public ApiError(String message, String stackTrace) {
        this.message = message;
        this.stackTrace = stackTrace;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static String getStackTraceAsString(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }
}