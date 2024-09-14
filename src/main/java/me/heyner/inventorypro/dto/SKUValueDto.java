package me.heyner.inventorypro.dto;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@EqualsAndHashCode
public class SKUValueDto {

  private final BigDecimal costPrice;

  private final Long amountAvailable;

  private final int marginPercentage;
}
