package com.gsc.shopcart.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final String errorMessage;

    public ResourceNotFoundException(String resourceName, String parameterName, String resourceValue) {
        super();
        this.errorMessage = String.format("Resource %s by %s = %s Not Found!", resourceName, parameterName, resourceValue);
    }

    @Override
    public String getMessage() {
        return this.errorMessage;
    }
}
