package auca.ac.rw.restfullApiAssignment.controller.task;

import auca.ac.rw.restfullApiAssignment.model.task.Task;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Task Management API - Question 5.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    // In-memory list acting as our data store
    private List<Task> tasks = new ArrayList<>();
    private Long nextId = 6L; // Counter for auto-generating new IDs

    // Initialize with sample tasks of varying priorities and statuses
    public TaskController() {
        tasks.add(new Task(1L, "Submit assignment", "Push all code to GitHub on the restFull_api branch", false, "HIGH", "2025-02-20"));
        tasks.add(new Task(2L, "Read Spring Boot docs", "Review chapters 1 to 5 on REST controllers", false, "MEDIUM", "2025-02-18"));
        tasks.add(new Task(3L, "Buy groceries", "Milk, bread, eggs, and seasonal vegetables", true, "LOW", "2025-02-16"));
        tasks.add(new Task(4L, "Fix login bug", "Investigate null pointer exception in authentication module", false, "HIGH", "2025-02-17"));
        tasks.add(new Task(5L, "Team meeting prep", "Prepare slides for weekly sprint review presentation", false, "MEDIUM", "2025-02-19"));
    }

    /**
     * GET /api/tasks - Get all tasks
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(tasks); // 200 OK
    }

    /**
     * GET /api/tasks/{taskId} - Get task by ID
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        Optional<Task> task = tasks.stream()
                .filter(t -> t.getTaskId().equals(taskId))
                .findFirst();
        // 200 OK if found, 404 if not
        return task.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * GET /api/tasks/status?completed=true or false - Get tasks by completion status
     */
    @GetMapping("/status")
    public ResponseEntity<List<Task>> getTasksByStatus(@RequestParam boolean completed) {
        List<Task> result = tasks.stream()
                .filter(t -> t.isCompleted() == completed)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/tasks/priority/{priority} - Get tasks by priority (LOW, MEDIUM, HIGH)
     */
    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<Task>> getTasksByPriority(@PathVariable String priority) {
        List<Task> result = tasks.stream()
                .filter(t -> t.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * POST /api/tasks - Create new task
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        task.setTaskId(nextId++); // Auto-assign a new ID
        tasks.add(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(task); // 201 Created
    }

    /**
     * PUT /api/tasks/{taskId} - Update all task fields
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId,
                                            @RequestBody Task updatedTask) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getTaskId().equals(taskId)) {
                updatedTask.setTaskId(taskId); // Preserve the original ID
                tasks.set(i, updatedTask);
                return ResponseEntity.ok(updatedTask); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    /**
     * PATCH /api/tasks/{taskId}/complete - Mark a task as completed
     */
    @PatchMapping("/{taskId}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long taskId) {
        for (Task task : tasks) {
            if (task.getTaskId().equals(taskId)) {
                task.setCompleted(true); // Mark as completed
                return ResponseEntity.ok(task); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    /**
     * DELETE /api/tasks/{taskId} - Delete task
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        boolean removed = tasks.removeIf(t -> t.getTaskId().equals(taskId));
        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}
