package ru.practicum.statistics.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.statistics.utility.Constants;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@ControllerAdvice
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler(BadDateInRequestException.class)
    @ResponseStatus(BAD_REQUEST)
    public ApiError badDateInRequestException(final Exception e) {
        log.error(HttpStatus.valueOf(400) + " " + e.getCause());
        return new ApiError(e.getCause().getStackTrace(),
                BAD_REQUEST.name(),
                "400 Не корректный запрос",
                e.getCause().getMessage(),
                LocalDateTime.now().format(Constants.TIME_FORMATTER));
    }
}
