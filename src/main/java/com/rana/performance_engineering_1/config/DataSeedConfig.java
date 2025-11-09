package com.rana.performance_engineering_1.config;

import com.rana.performance_engineering_1.model.Category;
import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.repository.CategoryRepository;
import com.rana.performance_engineering_1.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//@Component
public class DataSeedConfig implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public DataSeedConfig(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Seeding data...");

        // 1. Create 500 Categories
        List<Category> categories = new ArrayList<>();
        for (int i = 1; i <= 500; i++) {
            categories.add(new Category("Category " + i));
        }
        categoryRepository.saveAll(categories);

        // 2. Create 10 lakh Products
        List<Product> products = new ArrayList<>();
        for (int i = 1; i <= 1000000; i++) {
            products.add(new Product("Product " + i, 10.0 + i));
        }
        productRepository.saveAll(products);

        Random random = new Random();
        for (Product product : products) {
            // Assign 2 to 5 random categories to each product
            int categoryCount = random.nextInt(4) + 2;
            for (int i = 0; i < categoryCount; i++) {
                Category randomCategory = categories.get(random.nextInt(categories.size()));
                product.getCategories().add(randomCategory);
            }
        }
        // Save the products *again* to update the relationship
        productRepository.saveAll(products);

        System.out.println("Data seeding complete.");
    }
}

