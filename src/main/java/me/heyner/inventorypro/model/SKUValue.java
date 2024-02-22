package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"sku_id", "option_id", "optionValue_id"}))
public class SKUValue {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @ManyToOne
    private SKU sku;

    @DecimalMin(value = "0.0", inclusive = false, message = "The cost price should be positive")
    @Digits(integer = 20, fraction = 2, message = "The cost price can't have more than 20 integer digits or more then 2 decimal digits")
    private BigDecimal costPrice;

    @NotNull
    @Min(0)
    private Long amountAvailable;

    @NotNull
    @Min(0)
    private int marginPercentage;

    @ManyToOne
    private Option option;

    @ManyToOne
    private OptionValue optionValue;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;

    @JsonProperty("id")
    public final int getIndex() {
        return sku.getValues().indexOf(this);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SKUValue skuValue = (SKUValue) o;
        return getId() != null && Objects.equals(getId(), skuValue.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
