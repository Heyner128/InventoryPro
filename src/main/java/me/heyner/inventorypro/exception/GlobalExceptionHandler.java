package me.heyner.inventorypro.exception;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return ResponseEntity.badRequest().body(errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(ExistingUserException.class)
  public ResponseEntity<Map<String, String>> handleUniqueUserException(ExistingUserException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    logger.error(ex.getMessage(), ex);
    return ResponseEntity.badRequest().body(errors);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEntityNotFoundException(
      EntityNotFoundException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    logger.error(ex.getMessage(), ex);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
  }

  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAnyException(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    logger.error(ex.getMessage(), ex);
    return ResponseEntity.badRequest().body(errors);
  }
}
