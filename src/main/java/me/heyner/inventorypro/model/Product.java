package me.heyner.inventorypro.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "The name of a product can't be empty")
    private String name;

    @NotBlank(message = "The description of a product can't be empty")
    private String description;

    @NotBlank(message = "The brand of a product can't be empty")
    private String brand;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    @NotEmpty(message = "A product must have at least one option")
    private Set<Option> options;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private Set<SKU> skus;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;
}
