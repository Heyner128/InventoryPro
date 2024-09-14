package me.heyner.inventorypro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"option_id", "value"}))
public class OptionValue {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @JsonIgnore
  private Long id;

  @Column(nullable = false)
  private String value;

  @ManyToOne private Option option;

  @OneToMany(mappedBy = "optionValue", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<SKUValue> skuValues;

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
    OptionValue optionValue = (OptionValue) o;
    return getId() != null && Objects.equals(getId(), optionValue.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
