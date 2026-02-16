package auca.ac.rw.restfullApiAssignment.controller.user;

import auca.ac.rw.restfullApiAssignment.model.user.ApiResponse;
import auca.ac.rw.restfullApiAssignment.model.user.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for User Profile Management API - Bonus Question.
 * All responses are wrapped in ApiResponse for consistent response format.
 */
@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    // In-memory list acting as our data store
    private List<UserProfile> users = new ArrayList<>();
    private Long nextId = 6L; // Counter for auto-generating new IDs

    // Initialize with 5 sample user profiles
    public UserProfileController() {
        users.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe", 25, "Rwanda", "Software developer passionate about Spring Boot", true));
        users.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith", 30, "Kenya", "Full-stack developer and tech blogger", true));
        users.add(new UserProfile(3L, "bob_martin", "bob@example.com", "Bob Martin", 22, "Uganda", "Computer science student at AUCA", false));
        users.add(new UserProfile(4L, "alice_wonder", "alice@example.com", "Alice Wonder", 28, "Rwanda", "UI/UX designer with 5 years of experience", true));
        users.add(new UserProfile(5L, "charlie_k", "charlie@example.com", "Charlie Kamanzi", 35, "Tanzania", "DevOps engineer and cloud architect", false));
    }

    /**
     * GET /api/users - Get all user profiles
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true, "Users retrieved successfully", users);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * GET /api/users/{userId} - Get user profile by ID
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserById(@PathVariable Long userId) {
        Optional<UserProfile> user = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst();

        if (user.isPresent()) {
            ApiResponse<UserProfile> response = new ApiResponse<>(
                    true, "User found", user.get());
            return ResponseEntity.ok(response); // 200 OK
        }
        ApiResponse<UserProfile> errorResponse = new ApiResponse<>(
                false, "User with ID " + userId + " not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
    }

    /**
     * GET /api/users/search?username={username} - Search by username (partial match)
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserProfile>>> searchByUsername(
            @RequestParam String username) {
        List<UserProfile> result = users.stream()
                .filter(u -> u.getUsername().toLowerCase().contains(username.toLowerCase()))
                .collect(Collectors.toList());
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true, "Search results for username: " + username, result);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * GET /api/users/country/{country} - Get users by country
     */
    @GetMapping("/country/{country}")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByCountry(
            @PathVariable String country) {
        List<UserProfile> result = users.stream()
                .filter(u -> u.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true, "Users from " + country, result);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * GET /api/users/age-range?min={min}&max={max} - Get users within an age range
     */
    @GetMapping("/age-range")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByAgeRange(
            @RequestParam int min,
            @RequestParam int max) {
        List<UserProfile> result = users.stream()
                .filter(u -> u.getAge() >= min && u.getAge() <= max)
                .collect(Collectors.toList());
        ApiResponse<List<UserProfile>> response = new ApiResponse<>(
                true, "Users between age " + min + " and " + max, result);
        return ResponseEntity.ok(response); // 200 OK
    }

    /**
     * POST /api/users - Create a new user profile
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(
            @RequestBody UserProfile user) {
        user.setUserId(nextId++); // Auto-assign a new ID
        users.add(user);
        ApiResponse<UserProfile> response = new ApiResponse<>(
                true, "User profile created successfully", user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response); // 201 Created
    }

    /**
     * PUT /api/users/{userId} - Update all user profile details
     */
    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(
            @PathVariable Long userId,
            @RequestBody UserProfile updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(userId)) {
                updatedUser.setUserId(userId); // Preserve the original ID
                users.set(i, updatedUser);
                ApiResponse<UserProfile> response = new ApiResponse<>(
                        true, "User profile updated successfully", updatedUser);
                return ResponseEntity.ok(response); // 200 OK
            }
        }
        ApiResponse<UserProfile> errorResponse = new ApiResponse<>(
                false, "User with ID " + userId + " not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
    }

    /**
     * PATCH /api/users/{userId}/activate - Activate a user profile
     */
    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateUser(@PathVariable Long userId) {
        for (UserProfile user : users) {
            if (user.getUserId().equals(userId)) {
                user.setActive(true);
                ApiResponse<UserProfile> response = new ApiResponse<>(
                        true, "User profile activated successfully", user);
                return ResponseEntity.ok(response); // 200 OK
            }
        }
        ApiResponse<UserProfile> errorResponse = new ApiResponse<>(
                false, "User with ID " + userId + " not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
    }

    /**
     * PATCH /api/users/{userId}/deactivate - Deactivate a user profile
     */
    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateUser(@PathVariable Long userId) {
        for (UserProfile user : users) {
            if (user.getUserId().equals(userId)) {
                user.setActive(false);
                ApiResponse<UserProfile> response = new ApiResponse<>(
                        true, "User profile deactivated successfully", user);
                return ResponseEntity.ok(response); // 200 OK
            }
        }
        ApiResponse<UserProfile> errorResponse = new ApiResponse<>(
                false, "User with ID " + userId + " not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
    }

    /**
     * DELETE /api/users/{userId} - Delete a user profile
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        boolean removed = users.removeIf(u -> u.getUserId().equals(userId));
        if (removed) {
            ApiResponse<Void> response = new ApiResponse<>(
                    true, "User profile deleted successfully", null);
            return ResponseEntity.ok(response); // 200 OK
        }
        ApiResponse<Void> errorResponse = new ApiResponse<>(
                false, "User with ID " + userId + " not found", null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse); // 404
    }
}
