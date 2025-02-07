package me.heyner.inventorypro.exception;

public class ExistingUserException extends RuntimeException {
  public ExistingUserException(String username) {
    super("The user " + username + " already exists");
  }
}
