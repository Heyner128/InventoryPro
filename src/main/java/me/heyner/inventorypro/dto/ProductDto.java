package me.heyner.inventorypro.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ProductDto {
  private final String name;

  private final String description;

  private final String brand;
}
