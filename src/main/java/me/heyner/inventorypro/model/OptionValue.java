package me.heyner.inventorypro.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Data
public class OptionValue {
    @Id
    @GeneratedValue
    private Long id;

    private String value;

    @ManyToOne
    private Option option;

    @OneToMany(mappedBy = "optionValues", fetch = FetchType.LAZY)
    private Set<SKUValue> skuValues;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;
}
