package com.stockstreaming.demo.repository;

import com.stockstreaming.demo.model.Stock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.stream.Stream;

@Repository
public interface StockRepository extends JpaRepository<Stock,Long> {
    @Query("SELECT s FROM Stock s")
    Stream<Stock> streamAllStocks();

    @Query("SELECT s FROM Stock s ORDER BY s.id")
    Stream<Stock> streamAllStocks(Pageable pageable);
}
