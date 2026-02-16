package auca.ac.rw.restfullApiAssignment.controller.ecommerce;

import auca.ac.rw.restfullApiAssignment.model.ecommerce.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for E-Commerce Product Catalog API - Question 4.
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // In-memory list acting as our data store
    private List<Product> products = new ArrayList<>();
    private Long nextId = 11L; // Counter for auto-generating new IDs

    // Initialize with 10 sample products across different categories and brands
    public ProductController() {
        products.add(new Product(1L, "iPhone 14", "Apple smartphone with A15 Bionic chip and 6.1-inch display", 999.99, "Electronics", 50, "Apple"));
        products.add(new Product(2L, "Samsung Galaxy S23", "Android smartphone with Snapdragon 8 Gen 2 processor", 849.99, "Electronics", 35, "Samsung"));
        products.add(new Product(3L, "Nike Air Max 270", "Lightweight running shoes with Air cushioning", 129.99, "Footwear", 100, "Nike"));
        products.add(new Product(4L, "Adidas Ultraboost 22", "High-performance running shoes with Boost midsole", 179.99, "Footwear", 75, "Adidas"));
        products.add(new Product(5L, "Dell XPS 15", "High-performance laptop with Intel Core i7 and 512GB SSD", 1499.99, "Computers", 20, "Dell"));
        products.add(new Product(6L, "MacBook Pro M2", "Apple laptop with M2 chip, 16GB RAM, 512GB SSD", 1999.99, "Computers", 15, "Apple"));
        products.add(new Product(7L, "Sony WH-1000XM5", "Industry-leading noise-cancelling wireless headphones", 349.99, "Audio", 60, "Sony"));
        products.add(new Product(8L, "Samsung 4K TV 55\"", "UHD Smart TV with HDR10+ and built-in Alexa", 699.99, "Electronics", 0, "Samsung"));
        products.add(new Product(9L, "Levi's 501 Original Jeans", "Classic straight-fit denim jeans for everyday wear", 59.99, "Clothing", 200, "Levi's"));
        products.add(new Product(10L, "Apple Watch Series 8", "Smartwatch with health monitoring, GPS, and crash detection", 399.99, "Wearables", 45, "Apple"));
    }

    /**
     * GET /api/products - Get all products (with optional pagination: ?page=1&limit=5)
     */
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer limit) {

        List<Product> result = new ArrayList<>(products);

        // Apply pagination only if both page and limit parameters are provided
        if (page != null && limit != null) {
            int start = (page - 1) * limit;
            int end = Math.min(start + limit, result.size());
            if (start >= result.size()) {
                return ResponseEntity.ok(new ArrayList<>()); // Empty page
            }
            result = result.subList(start, end);
        }
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/products/{productId} - Get product details
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        Optional<Product> product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst();
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // 404
    }

    /**
     * GET /api/products/category/{category} - Get products by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> result = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/products/brand/{brand} - Get products by brand
     */
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> result = products.stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/products/search?keyword={keyword} - Search by keyword in name or description
     */
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        List<Product> result = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(lowerKeyword)
                        || p.getDescription().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/products/price-range?min={min}&max={max} - Get products within price range
     */
    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        List<Product> result = products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/products/in-stock - Get all products with stockQuantity > 0
     */
    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> result = products.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * POST /api/products - Add new product
     */
    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        product.setProductId(nextId++); // Auto-assign a new ID
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product); // 201 Created
    }

    /**
     * PUT /api/products/{productId} - Update all product details
     */
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
                                                  @RequestBody Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(productId)) {
                updatedProduct.setProductId(productId); // Preserve the original ID
                products.set(i, updatedProduct);
                return ResponseEntity.ok(updatedProduct); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    /**
     * PATCH /api/products/{productId}/stock?quantity={quantity} - Update stock quantity only
     */
    @PatchMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long productId,
                                                @RequestParam int quantity) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                product.setStockQuantity(quantity);
                return ResponseEntity.ok(product); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    /**
     * DELETE /api/products/{productId} - Delete product
     */
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        boolean removed = products.removeIf(p -> p.getProductId().equals(productId));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}
