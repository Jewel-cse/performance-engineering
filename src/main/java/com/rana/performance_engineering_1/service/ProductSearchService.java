package com.rana.performance_engineering_1.service;

import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.repository.ProductRepository;
import jakarta.annotation.PostConstruct; // Import this
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;

    private List<String> productNames;

    /***
     * Initially loaded all the product in-memory
     */
    @PostConstruct
    public void loadProductNames() {
        System.out.println("Loading all product names into memory...");

        this.productNames = productRepository.findAll().stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        System.out.println("Loaded " + this.productNames.size() + " names.");
    }

    // --- Approach 1: THIS IS OUR INEFFICIENT (BRUTE-FORCE) METHOD ---O(n)
    public List<String> autocomplete(String prefix) {
        return this.productNames.stream()
                .filter(name -> name.startsWith(prefix))
                .collect(Collectors.toList());
    }
}