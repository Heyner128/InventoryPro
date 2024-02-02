package me.heyner.inventorypro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class SKUValue {
    @Id
    @GeneratedValue
    private Long id;

    @DecimalMin(value = "0.0", inclusive = false, message = "The cost price should be positive")
    @Digits(integer = 20, fraction = 2, message = "The cost price can't have more than 20 integer digits or more then 2 decimal digits")
    private BigDecimal costPrice;

    private Long amountAvailable;

    private int marginPercentage;

    @ManyToOne
    private Option option;

    @ManyToOne
    private OptionValue optionValue;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;
}
