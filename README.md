# Twitter Backend

A scalable backend application built using **Spring Boot**, implementing authentication, posts, comments, and like system with clean architecture.

---

## ->Features

### 🔐 Authentication

* JWT-based authentication
* Secure login API
* User validation from database

---

### 📝 Post Management

* Create, update, delete posts
* Fetch posts with pagination
* Clean DTO-based architecture

---

### ❤️ Like System

* Users can like/unlike posts
* One user → one like per post (enforced)
* Like count derived from database (no redundant column)
* `likedByUser` support for UI

---

### 💬 Comment System

* Users can add comments on posts
* Update/delete only by owner or admin
* Soft delete support (`deleted = true`)
* Fetch all comments per post

---

### 🧱 Architecture

* BaseController for reusable CRUD APIs
* EntityService abstraction layer
* DTO pattern (request/response separation)
* Service → Service communication (no direct repo misuse)

---

## 🛠️ Tech Stack

* Java 21
* Spring Boot 3
* Spring Security
* JWT (JJWT)
* Spring Data JPA
* MySQL
* Lombok
* Maven

---

## 🔑 API Endpoints

### 🔐 Auth

```
POST /api/auth/login
```

---

### 📝 Posts

```
POST   /api/v1/posts
GET    /api/v1/posts/{id}
PUT    /api/v1/posts/{id}
DELETE /api/v1/posts/{id}
```

---

### ❤️ Likes

```
POST   /api/v1/likes/posts/{postId}
DELETE /api/v1/likes/posts/{postId}
GET    /api/v1/likes/posts/{postId}/count
```

---

### 💬 Comments

```
POST   /api/v1/posts/{postId}/comments
GET    /api/v1/posts/{postId}/comments
PUT    /api/v1/comments/{commentId}
DELETE /api/v1/comments/{commentId}
```

---


## ⚙️ Setup

### 1. Clone repo

```
git clone https://github.com/your-username/task-backend.git
```

### 2. Configure DB

Update `application.properties`:

```
spring.datasource.url=jdbc:mysql://localhost:3306/your_db
spring.datasource.username=root
spring.datasource.password=your_password
```

---

### 3. Run app

```
mvn spring-boot:run
```

---

## 🧪 Testing

Use Postman:

1. Login → get JWT
2. Add token in headers:

```
Authorization: Bearer <token>
```

---

## 🚀 Future Improvements

* Pagination optimization (remove N+1 queries)
* Sorting by likes using JOIN
* Comment count in posts
* MapStruct for DTO mapping
* Caching (Redis)

---

## 👨‍💻 Author

Madhav Mehta

---

## ⭐ Notes

This project demonstrates:

* Clean architecture
* Scalable design patterns
* Real-world backend practices
