package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(
    uniqueConstraints = @UniqueConstraint(columnNames = {"sku_id", "option_id", "option_value_id"}))
public class SKUValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @ManyToOne
  @JoinColumn(nullable = false)
  private SKU sku;

  @Column(nullable = false)
  private BigDecimal costPrice;

  @Column(nullable = false)
  private Long amountAvailable;

  @Column(nullable = false)
  private int marginPercentage;

  @ManyToOne
  @JoinColumn(nullable = false)
  private Option option;

  @ManyToOne
  @JoinColumn(nullable = false)
  private OptionValue optionValue;

  @CreatedDate private Date createdDate;

  @UpdateTimestamp private Date updateDate;

  @Override
  public final boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class<?> oEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != oEffectiveClass) return false;
    SKUValue skuValue = (SKUValue) o;
    return getId() != null && Objects.equals(getId(), skuValue.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
