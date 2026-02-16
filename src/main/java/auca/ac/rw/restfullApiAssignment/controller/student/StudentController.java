package auca.ac.rw.restfullApiAssignment.controller.student;

import auca.ac.rw.restfullApiAssignment.model.student.Student;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST Controller for Student Registration and Information Management API - Question 2.
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {

    // In-memory list acting as our data store
    private List<Student> students = new ArrayList<>();
    private Long nextId = 6L; // Counter for auto-generating new IDs

    // Initialize with 5 sample students with different majors and GPAs
    public StudentController() {
        students.add(new Student(1L, "Alice", "Muneza", "alice@auca.ac.rw", "Computer Science", 3.9));
        students.add(new Student(2L, "Bob", "Niyonzima", "bob@auca.ac.rw", "Mathematics", 3.4));
        students.add(new Student(3L, "Carol", "Uwase", "carol@auca.ac.rw", "Computer Science", 3.7));
        students.add(new Student(4L, "David", "Hakizimana", "david@auca.ac.rw", "Physics", 3.2));
        students.add(new Student(5L, "Eva", "Ingabire", "eva@auca.ac.rw", "Computer Science", 3.6));
    }

    /**
     * GET /api/students - Get all students
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(students); // 200 OK
    }

    /**
     * GET /api/students/{studentId} - Get student by ID
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long studentId) {
        Optional<Student> student = students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst();
        // 200 OK if found, 404 if not
        return student.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /**
     * GET /api/students/major/{major} - Get all students by major (path variable)
     */
    @GetMapping("/major/{major}")
    public ResponseEntity<List<Student>> getStudentsByMajor(@PathVariable String major) {
        List<Student> result = students.stream()
                .filter(s -> s.getMajor().equalsIgnoreCase(major))
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * GET /api/students/filter?gpa={minGpa} - Filter students with GPA >= minGpa
     */
    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterStudentsByGpa(@RequestParam Double gpa) {
        List<Student> result = students.stream()
                .filter(s -> s.getGpa() >= gpa)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result); // 200 OK
    }

    /**
     * POST /api/students - Register a new student
     */
    @PostMapping
    public ResponseEntity<Student> registerStudent(@RequestBody Student student) {
        student.setStudentId(nextId++); // Auto-assign a new ID
        students.add(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(student); // 201 Created
    }

    /**
     * PUT /api/students/{studentId} - Update student information
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId,
                                                  @RequestBody Student updatedStudent) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(studentId)) {
                updatedStudent.setStudentId(studentId); // Keep the same ID
                students.set(i, updatedStudent);
                return ResponseEntity.ok(updatedStudent); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}
