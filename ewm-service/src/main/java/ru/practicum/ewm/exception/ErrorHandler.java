package ru.practicum.ewm.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
public class ErrorHandler  {

    public ResponseEntity<String> constraint(ConstraintViolationException ex) {
        log.info(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity UserNotInitiatorEventException(final UserNotInitiatorEventException exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity WrongStateForUpdateEvent(final WrongStateForUpdateEvent exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity emailAlreadyExists(final EmailAlreadyExists exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity UserNameError(final NameAlreadyExists exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }
    @ExceptionHandler(CategoryAlreadyExists.class)
    public ResponseEntity categoryAlreadyExists(final CategoryAlreadyExists exception) {
        log.error(HttpStatus.valueOf(409) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(409));
    }

    @ExceptionHandler
    public ResponseEntity entityNotFoundException(final EntityNotFoundException exception) {
        log.error(HttpStatus.valueOf(404) + " " + exception.getMessage());
        return new ResponseEntity<>(Map.of("Error: ", exception.getMessage()), HttpStatus.valueOf(404));
    }


}