package ru.practicum.ewm.server.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ExceptionApiHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ConditionsNotMetException.class)
    public ErrorMessage handleConditionsNotMetException(ConditionsNotMetException exception) {
        log.error(exception.getMessage());
        return new ErrorMessage(HttpStatus.NOT_FOUND.toString(),
                "The required object was not found.",
                exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String error = exception.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        log.error(error);
        return new ErrorMessage(HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                error, LocalDateTime.now());
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorMessage handleConflictException(ConflictException exception) {
        log.error(exception.getMessage());
        return new ErrorMessage(HttpStatus.CONFLICT.toString(),
                "Integrity constraint has been violated.",
                exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidArgumentException(InvalidArgumentException exception) {
        log.error(exception.getMessage());
        return new ErrorMessage(HttpStatus.BAD_REQUEST.toString(),
                "Incorrectly made request.",
                exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorMessage handleAllExceptions(Throwable ex) {
        log.error(ex.getMessage());
        return new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                "An unexpected error occurred", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(InvalidDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleInvalidDateException(InvalidDateException ex) {
        log.error(ex.getMessage());
        return new ErrorMessage(HttpStatus.BAD_REQUEST.toString(),
                "For the requested operation the conditions are not met", ex.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handleMissingParams(MissingServletRequestParameterException ex) {
        log.error(ex.getMessage());
        return new ErrorMessage(HttpStatus.BAD_REQUEST.toString(),
                "For the requested operation the conditions are not met", ex.getMessage(), LocalDateTime.now());
    }
}
