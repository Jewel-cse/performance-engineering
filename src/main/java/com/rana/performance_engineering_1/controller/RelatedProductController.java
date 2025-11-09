package com.rana.performance_engineering_1.controller;

import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.service.ProductSearchService;
import com.rana.performance_engineering_1.service.RelatedProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RelatedProductController {

    private final RelatedProductService relatedProductService;
    private final ProductSearchService productSearchService;


    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProducts(@PathVariable Long productId) {

        // 1. Create and start the stopwatch
        StopWatch stopWatch = new StopWatch("RelatedProducts");

        stopWatch.start();
        Optional<Product> relatedProducts = relatedProductService.getProduct(productId);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

        return ResponseEntity.ok(relatedProducts);
    }



    @GetMapping("/products/{productId}/related")
    public List<Product> getRelatedProducts(@PathVariable Long productId) {

        // 1. Create and start the stopwatch
        StopWatch stopWatch = new StopWatch("RelatedProducts");

        stopWatch.start();
        List<Product> relatedProducts = relatedProductService.getRelatedProducts(productId);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

        return relatedProducts;
    }

    @GetMapping("/products/autocomplete")
    public List<String> autocompleteProducts(@RequestParam String prefix) {

        StopWatch stopWatch = new StopWatch("Autocomplete");
        stopWatch.start();

        List<String> suggestions = productSearchService.autocomplete(prefix);

        stopWatch.stop();
        System.out.println(stopWatch.prettyPrint());

        return suggestions;
    }
}
