package com.rana.performance_engineering_1.config;

import com.github.javafaker.Faker;
import com.rana.performance_engineering_1.model.Category;
import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.repository.CategoryRepository;
import com.rana.performance_engineering_1.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
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

        Faker faker = new Faker();
        Random random = new Random();

        // 1. Create 500 Categories
        List<Category> categories = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            categories.add(new Category(faker.commerce().department()));
        }
        categoryRepository.saveAll(categories);

        // 2. Create 1,000,000 Products
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            Product product = new Product(
                    faker.commerce().productName(),
                    Double.parseDouble(faker.commerce().price(5.0, 1000.0))
            );
            products.add(product);
        }
        productRepository.saveAll(products);

        // 3. Assign 2-5 random categories to each product
        for (Product product : products) {
            int categoryCount = random.nextInt(4) + 2; // 2 to 5 categories
            for (int i = 0; i < categoryCount; i++) {
                Category randomCategory = categories.get(random.nextInt(categories.size()));
                product.getCategories().add(randomCategory);
            }
        }

        // Save again to update relationships
        productRepository.saveAll(products);

        System.out.println("Data seeding complete.");
    }
}
