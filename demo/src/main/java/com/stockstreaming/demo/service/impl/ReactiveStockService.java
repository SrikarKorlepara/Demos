package com.stockstreaming.demo.service.impl;



import com.stockstreaming.demo.repository.StockRepository;
//import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.stockstreaming.demo.model.Stock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReactiveStockService {

    private final StockRepository stockRepository;
    private final PlatformTransactionManager transactionManager;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public Flux<Stock> streamStocksReactive() {
        return Flux.fromIterable(stockRepository.findAll())
                .delayElements(Duration.ofSeconds(1));
    }

    public Stock getRandomStock() {
        long count = stockRepository.count();
        int randomIndex = (int) (Math.random() * count);
        Pageable pageable = PageRequest.of(randomIndex, 1);
        List<Stock> stocks = stockRepository.findAll(pageable).getContent();
        return stocks.isEmpty() ? null : stocks.get(0);
    }
}
