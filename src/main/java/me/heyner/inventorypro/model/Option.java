package me.heyner.inventorypro.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Option {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "The name of an option can't be empty")
    private String name;

    @ManyToOne
    @NotNull(message = "An option should have an associated product")
    private Product product;

    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
    @NotEmpty(message = "")
    private Set<OptionValue> values;

    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
    private Set<SKUValue> skuValues;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;
}
