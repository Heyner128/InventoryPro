package me.heyner.inventorypro.exception;

import me.heyner.inventorypro.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
  public UserNotFoundException() {
    super("User not found");
  }

  public UserNotFoundException(String message) {
    super(message);
  }

  public UserNotFoundException(Long userId) {
    super("User with the id " + userId + "not found");
  }

  public UserNotFoundException(User user) {
    super("User " + user + " not found");
  }
}
