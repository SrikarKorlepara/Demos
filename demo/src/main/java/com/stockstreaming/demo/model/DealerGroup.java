package com.stockstreaming.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Table(name = "dealer_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class DealerGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;

    @Column(name = "business_id", unique = true, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String businessId;

    private String description;

    @Builder.Default
    @OneToMany(mappedBy = "dealerGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("city ASC")
    @ToString.Exclude // or "name ASC", or "createdAt DESC"
    private Set<DealerLocation> dealerLocations = new LinkedHashSet<>();

    public void addDealerLocation(DealerLocation location) {
        dealerLocations.add(location);
        location.setDealerGroup(this);
    }

    public void removeDealerLocation(DealerLocation location) {
        dealerLocations.remove(location);
        location.setDealerGroup(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        DealerGroup that = (DealerGroup) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
