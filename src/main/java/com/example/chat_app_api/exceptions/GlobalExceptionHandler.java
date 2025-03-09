package com.example.chat_app_api.exceptions;

import com.example.chat_app_api.dto.ApiResponse;

import java.util.List;
import java.util.function.Function;

import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = AppException.class)
    ResponseEntity<ApiResponse<?>> handlingAppException(AppException e) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(e.getErrorCode().getStatusCode().value())
                .message(e.getErrorCode().getMessage())
                .build();

        return ResponseEntity
                .status(e.getErrorCode().getStatusCode())
                .body(apiResponse);
    }

    @ExceptionHandler(value = RuntimeException.class)
    ResponseEntity<ApiResponse<?>> handlingRuntimeException(RuntimeException e) {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(ErrorCode.INTERAL_SERVER_ERROR.getStatusCode().value())
                .message(ErrorCode.INTERAL_SERVER_ERROR.getMessage())
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<?>> handlingMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<String> errors = fieldErrors.stream().map(new Function<FieldError, String>() {
            @Override
            public String apply(FieldError fieldError) {
                return fieldError.getDefaultMessage();
            }
        }).toList();

        ApiResponse<?> apiResponse = ApiResponse.builder()
                .code(400)
                .message(errors.size() > 1 ? errors.toString() : errors.get(0))
                .build();

        return ResponseEntity.badRequest().body(apiResponse);
    }
}
