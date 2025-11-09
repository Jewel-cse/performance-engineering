package com.rana.performance_engineering_1.service;

import com.rana.performance_engineering_1.model.Category;
import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.repository.CategoryRepository;
import com.rana.performance_engineering_1.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RelatedProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    // --- THIS IS OUR INEFFICIENT METHOD ---
    public List<Product> getRelatedProducts(Long productId) {

        // 1. Find the target product
        Product targetProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // 2. Get its categories
        Set<Category> targetCategories = targetProduct.getCategories();

        // Fetching ALL products from the DB into memory
        List<Product> allProducts = productRepository.findAll();

        List<Product> relatedProducts = new ArrayList<>();

        // We are looping through N products (O(N) loop)
        for (Product otherProduct : allProducts) {
            if (otherProduct.getId().equals(productId)) {
                continue; // Don't compare with itself
            }

            Set<Category> otherCategories = otherProduct.getCategories();

            for (Category targetCat : targetCategories) {
                if (otherCategories.contains(targetCat)) {
                    relatedProducts.add(otherProduct);
                    break; // Found a match, move to the next product
                }
            }
        }

        return relatedProducts;
    }

}
