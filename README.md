# RESTful API Assignment - Spring Boot

## How to Run

1. Open the project in IntelliJ IDEA or VS Code
2. Make sure you have **JDK 17** installed (update `pom.xml` `<java.version>` if needed)
3. Run `RestfullApiAssignmentApplication.java`
4. The server starts on `http://localhost:8080`
5. Test with Postman or your browser

---

## Project Structure

```
src/main/java/auca/ac/rw/restfullApiAssignment/
├── controller/
│   ├── library/       → BookController
│   ├── student/       → StudentController
│   ├── restaurant/    → MenuController
│   ├── ecommerce/     → ProductController
│   ├── task/          → TaskController
│   └── user/          → UserProfileController
└── model/
    ├── library/       → Book
    ├── student/       → Student
    ├── restaurant/    → MenuItem
    ├── ecommerce/     → Product
    ├── task/          → Task
    └── user/          → UserProfile, ApiResponse
```

---

## Question 1: Library Book Management API

**Base URL:** `/api/books`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/books` | Get all books | 200 |
| GET | `/api/books/{id}` | Get book by ID | 200/404 |
| GET | `/api/books/search?title=clean` | Search by title | 200 |
| POST | `/api/books` | Add a new book | 201 |
| DELETE | `/api/books/{id}` | Delete a book | 204/404 |

**Sample POST Body:**
```json
{
  "title": "Spring Boot in Action",
  "author": "Craig Walls",
  "isbn": "978-1617292545",
  "publicationYear": 2016
}
```

---

## Question 2: Student Registration API

**Base URL:** `/api/students`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/students` | Get all students | 200 |
| GET | `/api/students/{studentId}` | Get student by ID | 200/404 |
| GET | `/api/students/major/Computer Science` | Filter by major | 200 |
| GET | `/api/students/filter?gpa=3.5` | Filter by min GPA | 200 |
| POST | `/api/students` | Register new student | 201 |
| PUT | `/api/students/{studentId}` | Update student | 200/404 |

**Sample POST Body:**
```json
{
  "firstName": "Grace",
  "lastName": "Mukamana",
  "email": "grace@auca.ac.rw",
  "major": "Computer Science",
  "gpa": 3.8
}
```

---

## Question 3: Restaurant Menu API

**Base URL:** `/api/menu`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/menu` | Get all menu items | 200 |
| GET | `/api/menu/{id}` | Get item by ID | 200/404 |
| GET | `/api/menu/category/Appetizer` | Get items by category | 200 |
| GET | `/api/menu/available?available=true` | Get available items | 200 |
| GET | `/api/menu/search?name=chicken` | Search by name | 200 |
| POST | `/api/menu` | Add new item | 201 |
| PUT | `/api/menu/{id}/availability` | Toggle availability | 200/404 |
| DELETE | `/api/menu/{id}` | Delete item | 204/404 |

---

## Question 4: E-Commerce Product API

**Base URL:** `/api/products`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/products` | Get all products | 200 |
| GET | `/api/products?page=1&limit=5` | Paginated products | 200 |
| GET | `/api/products/{productId}` | Get product by ID | 200/404 |
| GET | `/api/products/category/Electronics` | By category | 200 |
| GET | `/api/products/brand/Apple` | By brand | 200 |
| GET | `/api/products/search?keyword=laptop` | Search by keyword | 200 |
| GET | `/api/products/price-range?min=100&max=500` | By price range | 200 |
| GET | `/api/products/in-stock` | In-stock only | 200 |
| POST | `/api/products` | Add product | 201 |
| PUT | `/api/products/{productId}` | Update product | 200/404 |
| PATCH | `/api/products/{productId}/stock?quantity=10` | Update stock | 200/404 |
| DELETE | `/api/products/{productId}` | Delete product | 204/404 |

---

## Question 5: Task Management API

**Base URL:** `/api/tasks`

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/tasks` | Get all tasks | 200 |
| GET | `/api/tasks/{taskId}` | Get task by ID | 200/404 |
| GET | `/api/tasks/status?completed=false` | Filter by status | 200 |
| GET | `/api/tasks/priority/HIGH` | Filter by priority | 200 |
| POST | `/api/tasks` | Create new task | 201 |
| PUT | `/api/tasks/{taskId}` | Update task | 200/404 |
| PATCH | `/api/tasks/{taskId}/complete` | Mark as completed | 200/404 |
| DELETE | `/api/tasks/{taskId}` | Delete task | 204/404 |

---

## Bonus: User Profile API

**Base URL:** `/api/users`

All responses use `ApiResponse<T>` wrapper:
```json
{ "success": true, "message": "...", "data": { ... } }
```

| Method | Endpoint | Description | Status |
|--------|----------|-------------|--------|
| GET | `/api/users` | Get all users | 200 |
| GET | `/api/users/{userId}` | Get user by ID | 200/404 |
| GET | `/api/users/search?username=john` | Search by username | 200 |
| GET | `/api/users/country/Rwanda` | Filter by country | 200 |
| GET | `/api/users/age-range?min=20&max=30` | Filter by age range | 200 |
| POST | `/api/users` | Create user | 201 |
| PUT | `/api/users/{userId}` | Update user | 200/404 |
| PATCH | `/api/users/{userId}/activate` | Activate profile | 200/404 |
| PATCH | `/api/users/{userId}/deactivate` | Deactivate profile | 200/404 |
| DELETE | `/api/users/{userId}` | Delete user | 200/404 |
