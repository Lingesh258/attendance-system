# Smart Attendance Management System 🎓

A full-stack attendance management system that automates attendance tracking using dynamic QR codes, preventing proxy attendance through token expiry and duplicate-scan detection.

## 🚀 Features

- **Dynamic QR Code Generation** — Faculty generates a new QR code per class session, valid for 60 seconds
- **Proxy Prevention** — QR codes expire after 60 seconds; duplicate scans are blocked
- **JWT Authentication** — Secure login for Student, Faculty, and Admin roles
- **Role-Based Access Control** — Each role has separate permissions and endpoints
- **Attendance History** — View attendance records per student
- **BCrypt Password Encryption** — All passwords are securely hashed before storing

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 25, Spring Boot 4.1 |
| Database | MySQL 8.0 |
| Security | Spring Security 7 + JWT (jjwt 0.12.6) |
| ORM | Spring Data JPA (Hibernate 7) |
| Build Tool | Maven |
| API Testing | Postman |

## 📁 Project Structure

```
src/main/java/com/attendance/attendance_system/
├── controller/
│   ├── AuthController.java
...
└── exception/
    └── GlobalExceptionHandler.java
```
├── controller/
│   ├── AuthController.java
│   ├── StudentController.java
│   ├── FacultyController.java
│   ├── SubjectController.java
│   ├── ClassSessionController.java
│   └── AttendanceController.java
├── service/
│   ├── StudentService.java
│   ├── FacultyService.java
│   ├── SubjectService.java
│   ├── ClassSessionService.java
│   └── AttendanceService.java
├── repository/
│   ├── StudentRepository.java
│   ├── FacultyRepository.java
│   ├── AdminRepository.java
│   ├── SubjectRepository.java
│   ├── ClassSessionRepository.java
│   └── AttendanceRepository.java
├── entity/
│   ├── Student.java
│   ├── Faculty.java
│   ├── Admin.java
│   ├── Subject.java
│   ├── ClassSession.java
│   └── Attendance.java
├── dto/
│   ├── request/
│   │   └── LoginRequest.java
│   └── response/
│       └── LoginResponse.java
├── security/
│   ├── JwtUtil.java
│   ├── JwtFilter.java
│   └── UserDetailsServiceImpl.java
├── config/
│   └── SecurityConfig.java
└── exception/
└── GlobalExceptionHandler.java

## 🗄️ Database Design

| Table | Description |
|---|---|
| `students` | Student accounts with department, year, section |
| `faculty` | Faculty accounts with department |
| `admins` | Admin accounts |
| `subjects` | Course subjects with semester and department |
| `class_sessions` | Class sessions with auto-generated QR tokens |
| `attendance` | Attendance records linking students to sessions |

## 🔐 API Endpoints

### Authentication (Public — No token required)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register/student` | Register a new student |
| POST | `/api/auth/register/faculty` | Register a new faculty |
| POST | `/api/auth/register/admin` | Register a new admin |
| POST | `/api/auth/login` | Login and receive JWT token |

### Students (Protected)

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/students` | Get all students |
| GET | `/api/students/{id}` | Get student by ID |

### Subjects (Protected)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/subjects/add` | Add a new subject |
| GET | `/api/subjects` | Get all subjects |
| GET | `/api/subjects/{id}` | Get subject by ID |

### Class Sessions (Protected)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/sessions/create` | Faculty creates a session and gets QR token |
| GET | `/api/sessions` | Get all sessions |
| GET | `/api/sessions/{id}` | Get session by ID |

### Attendance (Protected)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/attendance/mark?qrToken=X&studentId=Y` | Mark attendance using QR token |
| GET | `/api/attendance/student/{studentId}` | Get attendance history for a student |
| GET | `/api/attendance` | Get all attendance records |

## 🔄 QR Attendance Flow

```
Faculty Login
     ↓
Create Class Session
     ↓
QR Token Generated (valid for 60 seconds)
     ↓
QR shown on projector/screen
     ↓
Student scans QR
     ↓
Server checks:
  ✅ Is token valid?
  ✅ Has it expired? (60 second limit)
  ✅ Has student already marked attendance?
     ↓
All checks pass → Attendance saved as PRESENT
```
↓
Create Class Session
↓
QR Token Generated (valid for 60 seconds)
↓
QR shown on projector/screen
↓
Student scans QR
↓
Server checks:
✅ Is token valid?
✅ Has it expired? (60 second limit)
✅ Has student already marked attendance?
↓
All checks pass → Attendance saved as PRESENT

## ⚙️ How to Run Locally

### Prerequisites
- Java 17 or higher
- MySQL 8.0
- Maven (or use IntelliJ IDEA built-in Maven)
- Postman (for API testing)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/Lingesh258/attendance-system.git
cd attendance-system
```

**2. Create the database in MySQL**
```sql
CREATE DATABASE attendance_db;
```

**3. Configure database credentials**

Open `src/main/resources/application.properties` and update:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/attendance_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

**4. Run the application**
```bash
mvn spring-boot:run
```
Or run directly from IntelliJ IDEA by clicking the green play button.

**5. App starts at**
http://localhost:8081
**6. Test APIs using Postman**

First register a student:
POST http://localhost:8081/api/auth/register/student
Content-Type: application/json
{
"name": "John Doe",
"email": "john@student.com",
"password": "password123",
"department": "CSE",
"year": "2",
"section": "A"
}
Then login to get your token:
POST http://localhost:8081/api/auth/login
Content-Type: application/json
{
"email": "john@student.com",
"password": "password123"
}
Use the returned token in the Authorization header for all other requests:
Authorization: Bearer <your_token_here>
## 🔒 Security

- All passwords are encrypted using **BCrypt** before storing in the database
- Every protected endpoint requires a valid **JWT token** in the Authorization header
- JWT tokens expire after **30 days** (configurable in `JwtUtil.java`)
- Tokens contain the user's email and role, verified on every request by `JwtFilter`

## 👨‍💻 Author

**Lingesh**
- GitHub: [Lingesh258](https://github.com/Lingesh258)