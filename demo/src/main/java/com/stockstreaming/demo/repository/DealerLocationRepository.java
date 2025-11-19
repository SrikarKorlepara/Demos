package com.stockstreaming.demo.repository;

import com.stockstreaming.demo.model.DealerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerLocationRepository extends JpaRepository<DealerLocation, Long> {
    Optional<DealerLocation> findByLocationId(String locationId);
}
