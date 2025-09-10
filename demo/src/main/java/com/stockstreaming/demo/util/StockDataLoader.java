package com.stockstreaming.demo.config;

import com.stockstreaming.demo.model.Stock;
import com.stockstreaming.demo.repository.StockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class StockDataLoader implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(StockDataLoader.class);
    private final StockRepository stockRepository;
    private final Random random = new Random();

    // Sample data arrays
    private static final String[] SECTORS = {
            "Technology", "Healthcare", "Financial Services", "Consumer Discretionary",
            "Consumer Staples", "Energy", "Industrials", "Materials", "Real Estate",
            "Utilities", "Communication Services"
    };

    private static final String[] TECH_INDUSTRIES = {
            "Software", "Semiconductors", "Consumer Electronics", "Internet Services",
            "IT Services", "Networking", "Gaming", "Cybersecurity", "Cloud Computing",
            "Artificial Intelligence", "E-commerce", "Digital Media"
    };

    private static final String[] HEALTHCARE_INDUSTRIES = {
            "Pharmaceuticals", "Biotechnology", "Medical Devices", "Health Insurance",
            "Healthcare Facilities", "Diagnostics", "Telemedicine", "Medical Equipment"
    };

    private static final String[] FINANCIAL_INDUSTRIES = {
            "Banks", "Insurance", "Investment Services", "Payment Processing",
            "Real Estate Investment", "Asset Management", "Credit Services", "Fintech"
    };

    private static final String[] COMPANY_PREFIXES = {
            "Global", "Advanced", "Premier", "Innovative", "Dynamic", "Strategic",
            "United", "American", "International", "National", "First", "Capital",
            "Digital", "Smart", "Future", "Next", "Modern", "Elite", "Prime", "Alpha"
    };

    private static final String[] COMPANY_SUFFIXES = {
            "Corp", "Inc", "LLC", "Group", "Technologies", "Systems", "Solutions",
            "Enterprises", "Holdings", "Partners", "Associates", "Industries", "Services"
    };

    public StockDataLoader(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        long existingCount = stockRepository.count();

        if (existingCount > 0) {
            log.info("Database already contains {} stock records. Skipping data generation.", existingCount);
            return;
        }

        log.info("Generating 1000 stock records...");

        List<Stock> stocks = generateStockData();
        stockRepository.saveAll(stocks);

        log.info("Successfully created {} stock records in the database", stocks.size());
        log.info("H2 Console available at: http://localhost:8080/h2-console");
    }

    private List<Stock> generateStockData() {
        List<Stock> stocks = new ArrayList<>();

        for (int i = 1; i <= 1000; i++) {
            Stock stock = new Stock();

            String sector = SECTORS[random.nextInt(SECTORS.length)];
            String industry = getIndustryForSector(sector);

            stock.setName(generateCompanyName(industry));
            stock.setSymbol(generateSymbol(i));
            stock.setPrice(generatePrice());
            stock.setSector(sector);
            stock.setIndustry(industry);

            stocks.add(stock);
        }

        return stocks;
    }

    private String getIndustryForSector(String sector) {
        return switch (sector) {
            case "Technology" -> TECH_INDUSTRIES[random.nextInt(TECH_INDUSTRIES.length)];
            case "Healthcare" -> HEALTHCARE_INDUSTRIES[random.nextInt(HEALTHCARE_INDUSTRIES.length)];
            case "Financial Services" -> FINANCIAL_INDUSTRIES[random.nextInt(FINANCIAL_INDUSTRIES.length)];
            case "Consumer Discretionary" -> random.nextBoolean() ? "Retail" : "Entertainment";
            case "Consumer Staples" -> random.nextBoolean() ? "Food & Beverages" : "Household Products";
            case "Energy" -> random.nextBoolean() ? "Oil & Gas" : "Renewable Energy";
            case "Industrials" -> random.nextBoolean() ? "Manufacturing" : "Transportation";
            case "Materials" -> random.nextBoolean() ? "Chemicals" : "Mining";
            case "Real Estate" -> "Real Estate Investment";
            case "Utilities" -> random.nextBoolean() ? "Electric Utilities" : "Water Utilities";
            case "Communication Services" -> random.nextBoolean() ? "Telecommunications" : "Media";
            default -> "General " + sector;
        };
    }

    private String generateSymbol(int index) {
        // Generate realistic stock symbols
        String[] prefixes = {"TECH", "HLTH", "FIN", "CONS", "ENRG", "IND", "MAT", "UTIL", "COM", "REAL"};
        String prefix = prefixes[random.nextInt(prefixes.length)];
        return prefix + String.format("%03d", index);
    }

    private String generateCompanyName(String industry) {
        String prefix = COMPANY_PREFIXES[random.nextInt(COMPANY_PREFIXES.length)];
        String suffix = COMPANY_SUFFIXES[random.nextInt(COMPANY_SUFFIXES.length)];

        // Sometimes include the industry in the name
        if (random.nextDouble() < 0.6) {
            return prefix + " " + industry + " " + suffix;
        } else {
            return prefix + " " + suffix;
        }
    }

    private String generatePrice() {
        // Generate realistic stock prices between $5 and $500
        double price = 5 + (random.nextDouble() * 495);
        return BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP).toString();
    }
}