package com.rana.performance_engineering_1.repository;

import com.rana.performance_engineering_1.model.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @EntityGraph(attributePaths = "categories")
    @Override
    List<Product> findAll();

    // --- THIS IS OUR NEW, FULLY OPTIMIZED METHOD ---
    /**
     * Finds related products directly in the database.
     * This query tells the DB:
     * 1. Find all products 'p'
     * 2. Join them with their categories 'c'
     * 3. Find *only* the products where 'c' is IN the list of categories
     * belonging to the target product (the :productId)
     * 4. Make sure 'p' is not the target product itself.
     * 5. Return 'p' without duplicates (DISTINCT).
     */
    @Query("SELECT DISTINCT p " +
            "FROM Product p " +
            "JOIN p.categories c " +
            "WHERE c IN (SELECT c2 FROM Product p2 JOIN p2.categories c2 WHERE p2.id = :productId) " +
            "AND p.id != :productId")
    List<Product> findRelatedProducts(@Param("productId") Long productId);
}
