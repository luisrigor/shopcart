package com.gsc.shopcart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;
import java.util.StringJoiner;


@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class SQLCustomException  extends RuntimeException{

    private final String errorMessage;
    private final Exception exception;

    public SQLCustomException(String query, Map<String, Object> parameters, Exception exception) {
        super();
        this.errorMessage = generateErrorMessage(query, parameters);
        this.exception = exception;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    private String generateErrorMessage(String query, Map<String, Object> parameters) {
        StringJoiner joiner = new StringJoiner(", ");
        parameters.forEach((key, value) -> joiner.add(key + ": " + value));
        return "SQL exception for query: " + query + " with parameters: " + joiner;
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }

    public Exception getException(){
        return this.exception;
    }
}
