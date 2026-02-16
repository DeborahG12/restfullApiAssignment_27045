package auca.ac.rw.restfullApiAssignment.controller.restaurant;

import auca.ac.rw.restfullApiAssignment.model.restaurant.MenuItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Restaurant Menu Management API - Question 3.
 */
@RestController
@RequestMapping("/api/menu")
public class MenuController {

    // In-memory list acting as our data store
    private List<MenuItem> menuItems = new ArrayList<>();
    private Long nextId = 10L; // Counter for auto-generating new IDs

    // Initialize with 9 sample menu items across all 4 categories (challenge: at least 8)
    public MenuController() {
        menuItems.add(new MenuItem(1L, "Spring Rolls", "Crispy vegetable spring rolls served with sweet chili sauce", 4.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Garlic Bread", "Toasted sourdough bread with garlic herb butter", 3.49, "Appetizer", true));
        menuItems.add(new MenuItem(3L, "Grilled Chicken", "Herb-marinated grilled chicken breast with roasted vegetables", 14.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Beef Burger", "Angus beef burger with lettuce, tomato, and fries", 12.99, "Main Course", true));
        menuItems.add(new MenuItem(5L, "Veggie Pasta", "Penne pasta with seasonal vegetables in tomato basil sauce", 10.99, "Main Course", false));
        menuItems.add(new MenuItem(6L, "Chocolate Lava Cake", "Warm dark chocolate cake with a molten center", 6.99, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Mango Ice Cream", "Three scoops of fresh mango-flavored ice cream", 4.49, "Dessert", true));
        menuItems.add(new MenuItem(8L, "Fresh Orange Juice", "Freshly squeezed orange juice served with ice", 3.99, "Beverage", true));
        menuItems.add(new MenuItem(9L, "Iced Coffee", "Cold brew coffee with milk and vanilla syrup", 4.49, "Beverage", false));
    }

    /**
     * GET /api/menu - Get all menu items
     */
    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems); // 200 OK
    }

    /**
     * GET /api/menu/{id} - Get specific menu item by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        Optional<MenuItem> item = menuItems.stream()
                .filter(m -> m.getId().equals(id))
                .findFirst();
        // 200 OK if found, 404 if not
        return item.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * GET /api/menu/category/{category} - Get items by category
     */
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/menu/available?available=true - Get available or unavailable items
     */
    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getMenuItemsByAvailability(
            @RequestParam boolean available) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.isAvailable() == available)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/menu/search?name={name} - Search menu items by name (case-insensitive)
     */
    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> result = menuItems.stream()
                .filter(m -> m.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * POST /api/menu - Add new menu item
     */
    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem item) {
        item.setId(nextId++); // Auto-assign a new ID
        menuItems.add(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item); // 201 Created
    }

    /**
     * PUT /api/menu/{id}/availability - Toggle item availability (true -> false, false -> true)
     */
    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        for (MenuItem item : menuItems) {
            if (item.getId().equals(id)) {
                item.setAvailable(!item.isAvailable()); // Flip the boolean
                return ResponseEntity.ok(item); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    /**
     * DELETE /api/menu/{id} - Remove menu item
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        boolean removed = menuItems.removeIf(m -> m.getId().equals(id));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}
