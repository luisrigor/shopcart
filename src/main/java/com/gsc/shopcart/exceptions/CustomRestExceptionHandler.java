package com.gsc.shopcart.exceptions;

import com.gsc.shopcart.constants.ApiErrorConstants;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@Log4j
@ControllerAdvice
public class CustomRestExceptionHandler {

    @ExceptionHandler(value = {ShopCartException.class})
    public ResponseEntity<ApiError> ShopCartException(ShopCartException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false), ex.getCause().getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }

    @ExceptionHandler(value = {ObjectMappingException.class})
    public ResponseEntity<ApiError> objectMappingException(ObjectMappingException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        ApiError apiError = new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), request.getDescription(false), ex.getCause().getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String cause = String.format("Invalid parameter value for %s: '%s'. Expected type: %s",
                ex.getParameter().getParameterName(),
                ex.getValue(),
                ex.getParameter().getParameterType().getName());
        return new ApiError(ApiErrorConstants.INCORRECT_DATA, HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getDescription(false), cause);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ApiError handleBeanValidationError(MethodArgumentNotValidException ex, WebRequest request) {
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();

        StringBuilder sbErrorMsg = new StringBuilder();
        String delim = "; ";

        for (ObjectError error : allErrors) {
            sbErrorMsg.append(error.getObjectName());
            if (error instanceof FieldError) {
                sbErrorMsg.append("." + ((FieldError) error).getField());
            }
            sbErrorMsg.append(" " + error.getDefaultMessage());
            sbErrorMsg.append(delim);
        }

        return new ApiError(ApiErrorConstants.ERROR_PROCESSING_REQUEST, HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getDescription(false), sbErrorMsg.toString());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError missingServletRequestParameterException(MissingServletRequestParameterException ex, WebRequest request) {
        String cause = String.format("Parameter %s is mandatory.",
                ex.getParameterName());
        return new ApiError(ApiErrorConstants.INCORRECT_DATA, HttpStatus.BAD_REQUEST,
                ex.getMessage(), request.getDescription(false), cause);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseBody
    public ApiError handleNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return new ApiError(ApiErrorConstants.NOT_FOUND, HttpStatus.NOT_FOUND, ex.getMessage(), request.getDescription(false), ex.getCause().getMessage());
    }
}
