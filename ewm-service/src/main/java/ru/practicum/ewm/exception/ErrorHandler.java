package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.exceptions.*;
import ru.practicum.ewm.utility.Constants;

import static org.springframework.http.HttpStatus.*;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice(("ru.practicum.ewm"))
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ApiError entityNotFoundException(final EntityNotFoundException e) {
        log.error(HttpStatus.valueOf(404) + " " + e.getObjClass() + " " + e.getId());
        return new ApiError(e.getStackTrace(),
                NOT_FOUND.name(),
                "404 Объект не найден",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ApiError entityNotFoundException(final EventNotPublishedException e) {
        log.error(HttpStatus.valueOf(404) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                NOT_FOUND.name(),
                "404 Объект не найден",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError conflictException(final EventStatusUpdateException e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError CategoryDeleteException(final CategoryDeleteException e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError NameAlreadyExists(final NameAlreadyExists e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError EmailAlreadyExists(final EmailAlreadyExists e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError CategoryAlreadyExists(final CategoryAlreadyExists e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError UserNotInitiatorEventException(final UserNotInitiatorEventException e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError WrongStateForUpdateEvent(final WrongStateForUpdateEvent e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ApiError conflictException(final ValidationException e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError EventDateException(final EventDateException e) {
        log.error(HttpStatus.valueOf(400) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                BAD_REQUEST.name(),
                "400 Не корректный запрос",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ApiError incorrectRequestException(final IncorrectRequestException e) {
        log.error(HttpStatus.valueOf(400) + " " + e.getMessage());
        return new ApiError(e.getStackTrace(),
                BAD_REQUEST.name(),
                "400 Не корректный запрос",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }
}