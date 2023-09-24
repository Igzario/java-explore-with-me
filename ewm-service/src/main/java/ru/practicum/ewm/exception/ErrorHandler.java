package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.ewm.exception.exceptions.*;
import ru.practicum.ewm.utility.Constants;

import static org.springframework.http.HttpStatus.*;

import java.time.LocalDateTime;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
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
        log.error(HttpStatus.valueOf(404) + " " + e.getCause());
        return new ApiError(e.getStackTrace(),
                NOT_FOUND.name(),
                "404 Объект не найден",
                e.getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler({ValidationException.class,
            WrongStateForUpdateEvent.class,
            UserNotInitiatorEventException.class,
            CategoryAlreadyExists.class,
            EmailAlreadyExists.class,
            NameAlreadyExists.class,
            CategoryDeleteException.class,
            EventStatusUpdateException.class})
    @ResponseStatus(CONFLICT)
    public ApiError conflictException(final Exception e) {
        log.error(HttpStatus.valueOf(409) + " " + e.getCause());
        return new ApiError(e.getCause().getStackTrace(),
                CONFLICT.name(),
                "409 Ошибка валидации",
                e.getCause().getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }

    @ExceptionHandler({IncorrectRequestException.class,
            EventDateException.class})
    @ResponseStatus(BAD_REQUEST)
    public ApiError IncorrectRequestException(final Exception e) {
        log.error(HttpStatus.valueOf(400) + " " + e.getCause());
        return new ApiError(e.getCause().getStackTrace(),
                BAD_REQUEST.name(),
                "400 Не корректный запрос",
                e.getCause().getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }
}