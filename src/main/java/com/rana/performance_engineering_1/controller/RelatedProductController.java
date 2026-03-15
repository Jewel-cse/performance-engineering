package com.rana.performance_engineering_1.controller;

import com.rana.performance_engineering_1.model.Product;
import com.rana.performance_engineering_1.service.ProductSearchService;
import com.rana.performance_engineering_1.service.RelatedProductService;
import com.rana.performance_engineering_1.service.TotpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RelatedProductController {

    private final RelatedProductService relatedProductService;
    private final ProductSearchService productSearchService;
    private final TotpService totpService;


    /**
     * GET /products/{productId}?username=john&otp=123456
     * Requires valid TOTP OTP to access the product.
     */
    @GetMapping("/products/{productId}")
    public ResponseEntity<?> getProducts(
            @PathVariable Long productId,
            @RequestParam String username,
            @RequestParam String otp) {

        // Verify OTP before returning the product
        if (!totpService.verifyOtp(username, otp)) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid or expired OTP.",
                    "message", "Please provide a valid OTP from Google Authenticator."
            ));
        }

        // OTP is valid — proceed to fetch the product
        StopWatch stopWatch = new StopWatch("RelatedProducts");

        stopWatch.start();
        Optional<Product> relatedProducts = relatedProductService.getProduct(productId);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

        return ResponseEntity.ok(relatedProducts);
    }


    /**
     * GET /products/{productId}/related?username=john&otp=123456
     * Requires valid TOTP OTP to access related products.
     */
    @GetMapping("/products/{productId}/related")
    public ResponseEntity<?> getRelatedProducts(
            @PathVariable Long productId,
            @RequestParam String username,
            @RequestParam String otp) {

        // Verify OTP before returning related products
        if (!totpService.verifyOtp(username, otp)) {
            return ResponseEntity.status(401).body(Map.of(
                    "error", "Invalid or expired OTP.",
                    "message", "Please provide a valid OTP from Google Authenticator."
            ));
        }

        // OTP is valid — proceed
        StopWatch stopWatch = new StopWatch("RelatedProducts");

        stopWatch.start();
        List<Product> relatedProducts = relatedProductService.getRelatedProducts(productId);
        stopWatch.stop();

        System.out.println(stopWatch.prettyPrint());

        return ResponseEntity.ok(relatedProducts);
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
