package com.stockstreaming.demo.controller;


import com.stockstreaming.demo.service.StockService;
import com.stockstreaming.demo.model.Stock;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@RestController
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @GetMapping("/all")
    public List<Stock> getAllStocks() {
        return stockService.getAllStocks();
    }

    @GetMapping("/stream")
    public Object streamStocks(HttpServletResponse response) {
        return stockService.streamStocksV2(response);
    }

    @GetMapping("/stream/page")
    public StreamingResponseBody streamStocksPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size,
            HttpServletResponse response) {

        return stockService.streamStocksWithPagination(page, size, response);
    }
}
