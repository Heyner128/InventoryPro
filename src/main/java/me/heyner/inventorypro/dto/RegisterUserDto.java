package me.heyner.inventorypro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@EqualsAndHashCode
public class RegisterUserDto {
  /**
   * TODO - i can't validate the password with a second password see
   * https://www.baeldung.com/registration-with-spring-mvc-and-spring-security
   */
  @NotBlank(message = "The email can't be empty")
  @Email
  private String email;

  @NotBlank(message = "The username can't be empty")
  @Pattern(
      regexp = "\\w{4,}",
      message = "The username has to be at least 4 alphanumeric characters long")
  private String username;

  @NotBlank(message = "The password can't be empty")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
      message =
          "The password should contain an uppercase letter, a lowercase letter, a digit an special character in @$!%*?& and be at least 8 characters long")
  private String password;
}