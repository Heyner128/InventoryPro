package me.heyner.inventorypro.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.proxy.HibernateProxy;

/** An application user. */
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ApplicationUser {

  @Id @GeneratedValue private Long id;

  @NotBlank(message = "The email can't be empty")
  private String email;

  @NotBlank(message = "The username can't be empty")
  @Pattern(
      regexp = "\\w{4,}",
      message = "The username should contain at least 4 letters and numbers")
  private String username;

  @NotBlank(message = "The password can't be empty")
  private String password;

  @NotBlank(message = "The authority can't be empty")
  private String authority;

  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Inventory> inventories;

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null) {
      return false;
    }
    Class<?> objectEffectiveClass =
        o instanceof HibernateProxy
            ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass()
            : o.getClass();
    Class<?> thisEffectiveClass =
        this instanceof HibernateProxy
            ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass()
            : this.getClass();
    if (thisEffectiveClass != objectEffectiveClass) {
      return false;
    }
    ApplicationUser applicationUser = (ApplicationUser) o;
    return getId() != null && Objects.equals(getId(), applicationUser.getId());
  }

  @Override
  public final int hashCode() {
    return this instanceof HibernateProxy
        ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode()
        : getClass().hashCode();
  }
}
