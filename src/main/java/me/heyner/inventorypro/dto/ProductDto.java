package me.heyner.inventorypro.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {
  @NotBlank(message = "The name of a product can't be empty")
  private String name;

  @NotBlank(message = "The description of a product can't be empty")
  private String description;

  @NotBlank(message = "The brand of a product can't be empty")
  private String brand;
}
