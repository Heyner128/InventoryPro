package me.heyner.inventorypro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterUserDto {
  @NotBlank(message = "The email can't be empty")
  private String email;

  @NotBlank(message = "The username can't be empty")
  @Pattern(
      regexp = "\\w{4,}",
      message = "The username should contain at least 4 letters and numbers")
  private String username;

  @NotBlank(message = "The password can't be empty")
  private String password;

  @NotBlank(message = "The fullName can't be empty")
  private String fullName;
}
