package com.stockstreaming.demo.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockstreaming.demo.repository.StockRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.stockstreaming.demo.model.Stock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }
    @Transactional(readOnly = true)
    public StreamingResponseBody streamStocks(HttpServletResponse response){
        response.setContentType("text/event-stream");
        return outputStream -> {
            getAllStocks().forEach(
                    stock -> {
                        try{
                            String json = new ObjectMapper().writeValueAsString(stock)+"\n";
                            outputStream.write(json.getBytes());
                            outputStream.flush();
                            Thread.sleep(100);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
            );
        };
    }

}
