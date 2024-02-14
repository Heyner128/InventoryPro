package me.heyner.inventorypro.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"product", "name"}))
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
    @ToString.Exclude
    private Set<OptionValue> values;

    @OneToMany(mappedBy = "option", fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<SKUValue> skuValues;

    @CreatedDate
    private LocalDate createdDate;

    @UpdateTimestamp
    private LocalDate updateDate;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Option option = (Option) o;
        return getId() != null && Objects.equals(getId(), option.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
