package me.heyner.inventorypro.exception;

import me.heyner.inventorypro.model.Option;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class OptionNotFoundException extends RuntimeException {

  public OptionNotFoundException() {
    super("Option not found");
  }

  public OptionNotFoundException(String message) {
    super(message);
  }

  public OptionNotFoundException(Long optionId) {
    super("Option with the id " + optionId + "not found");
  }

  public OptionNotFoundException(Option option) {
    super("Option " + option + " not found");
  }
}
