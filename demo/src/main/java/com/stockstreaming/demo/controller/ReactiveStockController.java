package com.stockstreaming.demo.controller;

import com.stockstreaming.demo.model.Stock;
import com.stockstreaming.demo.service.impl.ReactiveStockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;

@RestController
@RequestMapping("/reactive/stocks")
@RequiredArgsConstructor
public class ReactiveStockController {
    private final ReactiveStockService stockService;

    @GetMapping(value="/stream",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Stock> streamStocks() {
        return stockService.streamStocksReactive();
    }
    @GetMapping(value="/live" ,produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamStockPrice(){
        return Flux.interval(Duration.ofSeconds(1))
                .take(20)
                .map(i-> {
                    Stock stock = stockService.getRandomStock();
                    return "Stock: " + stock.getSymbol() + " Price: " + stock.getPrice();
                });
    }
}
