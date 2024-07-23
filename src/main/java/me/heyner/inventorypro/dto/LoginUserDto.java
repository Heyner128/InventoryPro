package me.heyner.inventorypro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginUserDto {
  @NotBlank(message = "The email can't be empty")
  private String email;

  @NotBlank(message = "The password can't be empty")
  private String password;
}
