package ru.outofmemory.zelixmonitorbackend;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.outofmemory.zelixmonitorbackend.dto.ErrorResponseDto;

import java.util.Arrays;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex) {
        ErrorResponseDto error = new ErrorResponseDto(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponseDto> handleException(Throwable ex) {
        ErrorResponseDto error = new ErrorResponseDto("Внутренняя ошибка сервера", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        if (ex.getRequiredType() != null && ex.getRequiredType().isEnum()) {
            Object[] allowed = ex.getRequiredType().getEnumConstants();
            String message = "Invalid value '" + ex.getValue()
                    + "' for parameter '" + ex.getName()
                    + "'. Allowed values: " + Arrays.toString(allowed);

            return ResponseEntity.badRequest().body(new ErrorResponseDto(message, 400));
        }

        return ResponseEntity.badRequest().body(new ErrorResponseDto("Invalid request parameter", 400));
    }
}