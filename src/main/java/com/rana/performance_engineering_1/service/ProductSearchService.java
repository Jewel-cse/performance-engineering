package com.rana.performance_engineering_1.service;

import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.repository.ProductRepository;
import com.rana.performance_engineering_1.service.autocomplete.Trie;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductSearchService {

    private final ProductRepository productRepository;

    private final Trie productTrie;

    private List<String> productNames;


    /***
     * Initially loaded all the product in-memory
     */
    @PostConstruct
    public void loadProductNames() {
        System.out.println("Loading all product names into Trie...");

        List<Product> products = productRepository.findAll();

        for (Product product : products) {
            productTrie.insert(product.getName());
        }
        System.out.println("Loaded " + products.size() + " names into Trie.");
    }

    // --- Approach 1: THIS IS OUR INEFFICIENT (BRUTE-FORCE) METHOD ---O(n)
//    public List<String> autocomplete(String prefix) {
//        return this.productNames.stream()
//                .filter(name -> name.startsWith(prefix))
//                .collect(Collectors.toList());
//    }

    // --- THIS IS OUR OPTIMIZED O(K) METHOD ---
    public List<String> autocomplete(String prefix) {
        return productTrie.search(prefix);
    }
}