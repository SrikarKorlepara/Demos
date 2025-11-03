package com.stockstreaming.demo.repository;

import com.stockstreaming.demo.model.DealerLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DealerLocationRepository extends JpaRepository<DealerLocation, Long> {
}
