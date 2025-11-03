package com.stockstreaming.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dealer_locations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DealerLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "location_id", unique = true, nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private String locationId;

    private String name;

    private String address;

    private String city;

    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_group_id", nullable = false)
    private DealerGroup dealerGroup;
}
