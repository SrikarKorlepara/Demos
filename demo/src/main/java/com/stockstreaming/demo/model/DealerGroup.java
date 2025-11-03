package com.stockstreaming.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.Set;


@Entity
@Table(name = "dealer_groups")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
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
    @OrderBy("city ASC") // or "name ASC", or "createdAt DESC"
    private Set<DealerLocation> dealerLocations = new LinkedHashSet<>();

    public void addDealerLocation(DealerLocation location) {
        dealerLocations.add(location);
        location.setDealerGroup(this);
    }

    public void removeDealerLocation(DealerLocation location) {
        dealerLocations.remove(location);
        location.setDealerGroup(null);
    }

}
