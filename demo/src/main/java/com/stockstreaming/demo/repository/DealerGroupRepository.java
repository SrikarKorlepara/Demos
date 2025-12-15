package com.stockstreaming.demo.repository;

import com.stockstreaming.demo.model.DealerGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DealerGroupRepository extends JpaRepository<DealerGroup, Long>, JpaSpecificationExecutor<DealerGroup> {

    boolean existsByBusinessId(String businessId);

    Optional<DealerGroup> findByBusinessId(String businessId);

}
