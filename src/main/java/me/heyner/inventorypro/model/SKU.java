package me.heyner.inventorypro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class SKU {
    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "SKU name can't be blank")
    @Column(unique = true, nullable = false)
    private String sku;

    @ManyToOne
    private Product product;

    @OneToMany(mappedBy = "sku")
    private Set<SKUValue> values;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;
}
