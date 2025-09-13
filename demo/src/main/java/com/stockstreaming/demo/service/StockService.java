package com.stockstreaming.demo.service;



import com.fasterxml.jackson.databind.ObjectMapper;
import com.stockstreaming.demo.repository.StockRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.stockstreaming.demo.model.Stock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final PlatformTransactionManager transactionManager;

    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    public StreamingResponseBody streamStocks(HttpServletResponse response) {
        response.setContentType("text/event-stream");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Cache-Control", "no-cache");
        ObjectMapper objectMapper = new ObjectMapper();
        return outputStream -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setReadOnly(true);
            transactionTemplate.execute(status ->{
            try (var stockStream = stockRepository.streamAllStocks()) {
                stockStream.forEach(
                        stock -> {
                            try {
                                String json = objectMapper.writeValueAsString(stock) + "\n";
                                outputStream.write(json.getBytes());
                                outputStream.flush();
                                if (Thread.currentThread().isInterrupted()) {
                                    log.warn("Stream interrupted by client disconnection {}",status);
                                    return;
                                }
                                Thread.sleep(100);
                            }
                            catch (Exception e) {
                                throw new RuntimeException("Error streaming stock data", e);
                            }
                        });
            }
                return null;
            });
    };
}

    public StreamingResponseBody streamStocksV2(HttpServletResponse response) {
        response.setContentType("text/event-stream");

        return outputStream -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setReadOnly(true);

            transactionTemplate.execute(status -> {
                log.info("=== STREAMING TEST START ===");
                Runtime runtime = Runtime.getRuntime();

                // Force garbage collection for accurate measurement
                System.gc();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
                log.info("Memory before streaming: {} MB", memoryBefore / 1024 / 1024);

                try (Stream<Stock> stockStream = stockRepository.streamAllStocks()) {
                    log.info("Stream created, measuring memory...");

                    // Force garbage collection again
                    System.gc();
                    Thread.sleep(50);

                    long memoryAfterStreamCreation = runtime.totalMemory() - runtime.freeMemory();
                    log.info("Memory after stream creation: {} MB", memoryAfterStreamCreation / 1024 / 1024);
                    log.info("Memory increase for stream: {} MB",
                            (memoryAfterStreamCreation - memoryBefore) / 1024 / 1024);

                    AtomicInteger count = new AtomicInteger(0);
                    ObjectMapper mapper = new ObjectMapper();

                    stockStream.forEach(stock -> {
                        try {
                            int currentCount = count.incrementAndGet();

                            // Log every 25 records for more granular view
                            if (currentCount % 25 == 0) {
                                long currentMemory = runtime.totalMemory() - runtime.freeMemory();
                                log.info("Records: {}, Memory: {} MB, Increase: {} MB",
                                        currentCount,
                                        currentMemory / 1024 / 1024,
                                        (currentMemory - memoryBefore) / 1024 / 1024);
                            }

                            String json = mapper.writeValueAsString(stock) + "\n";
                            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                            Thread.sleep(10);

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    long finalMemory = runtime.totalMemory() - runtime.freeMemory();
                    log.info("=== FINAL STATS ===");
                    log.info("Total records processed: {}", count.get());
                    log.info("Final memory: {} MB", finalMemory / 1024 / 1024);
                    log.info("Total memory increase: {} MB", (finalMemory - memoryBefore) / 1024 / 1024);
                    log.info("Average memory per record: {} KB",
                            ((finalMemory - memoryBefore) * 1024) / count.get());

                } catch (Exception e) {
                    log.error("Error during streaming", e);
                    throw new RuntimeException(e);
                }
                return null;
            });
        };
    }


    public StreamingResponseBody streamStocksWithPagination(
            int page, int size, HttpServletResponse response) {

        response.setContentType("text/event-stream");

        return outputStream -> {
            TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
            transactionTemplate.setReadOnly(true);

            transactionTemplate.execute(status -> {
                // Create pageable with sorting
                Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

                try (Stream<Stock> stockStream = stockRepository.streamAllStocks(pageable)) {
                    ObjectMapper mapper = new ObjectMapper();
                    AtomicInteger count = new AtomicInteger(0);

                    stockStream.forEach(stock -> {
                        try {
                            String json = mapper.writeValueAsString(stock) + "\n";
                            outputStream.write(json.getBytes(StandardCharsets.UTF_8));
                            outputStream.flush();
                            Thread.sleep(100);

                            count.incrementAndGet();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });

                    log.info("Streamed {} records from page {} (size {})", count.get(), page, size);
                } catch (Exception e) {
                    throw new RuntimeException("Error streaming stocks", e);
                }
                return null;
            });
        };
    }
}
