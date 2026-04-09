# Cinema E-Booking Backend Guidelines

## Table of Contents

- [1. Mục tiêu](#1-mục-tiêu)
- [2. Kiến trúc hệ thống](#2-kiến-trúc-hệ-thống)
- [3. Entity Design Rule](#3-entity-design-rule)
- [4. Mapper Rule](#4-mapper-rule)
- [5. Naming Convention](#5-naming-convention)
- [6. Domain Design Rule](#6-domain-design-rule)
- [7. Development Workflow](#7-development-workflow)
- [8. Team Working Rule](#8-team-working-rule)
- [9. Code Review Rule](#9-code-review-rule)
- [10. Git Convention](#10-git-convention)
- [11. Branch Naming](#11-branch-naming)
- [12. Git Workflow](#12-git-workflow)
- [13. API Versioning](#13-api-versioning)
- [14. URL Structure](#14-url-structure)
- [15. Controller Rule](#15-controller-rule)
- [16. RESTful Convention](#16-restful-convention)
- [17. Frontend Rule](#17-frontend-rule)
- [18. Core Principle](#18-core-principle)
- [19. Team Message](#19-team-message)

---

## 1. Mục tiêu

- Đảm bảo code dễ đọc, dễ maintain  
- Thống nhất cách làm việc giữa các thành viên  
- Giảm lỗi kiến trúc  
- Tăng tốc độ phát triển  
- Hỗ trợ scale hệ thống  

---

## 2. Kiến trúc hệ thống

Dự án áp dụng:

- Clean Architecture  
- Domain-Driven Design (DDD)  

### Cấu trúc mỗi domain


domain/
application/
infrastructure/
presentation/


### Dependency Rule (bắt buộc)

- presentation → application → domain  
- infrastructure → domain  

### Nguyên tắc quan trọng

- Domain không phụ thuộc Infrastructure  
- Domain không import JPA (`jakarta.persistence`)  
- Không để JpaEntity extends Domain  
- Không expose JpaEntity ra ngoài application  

---

## 3. Entity Design Rule

### Domain Entity

- Chỉ chứa business logic  
- Sử dụng EntityId (ví dụ: `CinemaId`)  
- Không có annotation JPA  

### JpaEntity

- Đại diện cho database  
- Sử dụng `Long idJpa`  
- Có annotation `@Entity`  

### Mapping


Domain ↔ Mapper ↔ JpaEntity


---

## 4. Mapper Rule

### Required


Mapper (interface)
MapperImpl (implementation)


### Nguyên tắc

- Convert Domain ↔ JpaEntity  
- Không chứa business logic  
- Không expose JpaEntity  

---

## 5. Naming Convention

### Class

| Type       | Format                  | Example          |
|------------|------------------------|------------------|
| Domain     | PascalCase             | Cinema           |
| JpaEntity  | PascalCase + JpaEntity | CinemaJpaEntity  |
| Mapper     | PascalCase + Mapper    | CinemaMapper     |
| Repository | PascalCase + Repository| CinemaRepository |

### Variable

camelCase

### Enum

UPPER_CASE

---

## 6. Domain Design Rule

### Không được

- Over-design  
- Tạo field không dùng  
- Tạo quan hệ khi chưa cần  

### Nguyên tắc

Simple → chạy được → rồi refine

---

## 7. Development Workflow

### Flow chuẩn

1. Design entity đơn giản  
2. Code 1 flow (CRUD / booking)  
3. Refactor nếu cần  

### Không được

Design full hệ thống rồi mới code  

---

## 8. Team Working Rule

### Mỗi thành viên

- Làm theo module được assign  
- Tuân thủ structure và convention  

### Không được

- Tự ý thay đổi kiến trúc  
- Tự thiết kế lại structure  

### Khi có vấn đề

Hỏi trước khi sửa  

---

## 9. Code Review Rule

### Focus

- Đúng layer chưa  
- Có leak JPA không  
- Mapper đúng chưa  
- Naming rõ ràng  

### Không focus

- Style cá nhân  
- Tối ưu sớm  

---

## 10. Git Convention (Conventional Commits)

### Cấu trúc

<type>(<scope>): <mô tả ngắn gọn>

<body> (optional)  
<footer> (optional)

### Type

| Type     | Ý nghĩa                     |
|----------|----------------------------|
| feat     | Thêm tính năng mới         |
| fix      | Sửa lỗi                    |
| refactor | Tái cấu trúc code          |
| chore    | Setup, config              |
| build    | Thay đổi build system      |
| docs     | Tài liệu                   |
| style    | Format code                |
| test     | Test                       |
| perf     | Tối ưu hiệu suất           |

### Scope (khuyến khích)

backend, frontend, booking, movie, payment, user, auth

### Ví dụ

feat(booking): implement API đặt vé

- Tạo entity Booking, Ticket  
- Viết service và controller  
- Kiểm tra ghế trống  

### Không được dùng

update code  
fix bug  
commit 1  

---

## 11. Quy chuẩn đặt tên Branch

### Format

feature/ten-tinh-nang  
bugfix/mota-loi  
hotfix/mota  
chore/mota  
refactor/mota  
release/v1.0.0  

### Quy tắc

- Viết thường toàn bộ  
- Dùng dấu `-`  
- Không dùng khoảng trắng hoặc `_`  

---

## 12. Git Workflow


git checkout -b feature/booking-ticket
git commit -m "feat(booking): implement API đặt vé"
git push origin feature/booking-ticket


---

## 13. API Versioning

Tất cả API phải có prefix:

/api/v1

---

## 14. Cấu trúc URL

/api/v1/{module}/{resource}

### Ví dụ

/api/v1/movies  
/api/v1/auth/login  
/api/v1/bookings  
/api/v1/showtimes  

---

## 15. Controller Rule

@RestController  
@RequestMapping("/api/v1")

---

## 16. RESTful Convention

| Hành động        | Method | URL                     |
|------------------|--------|--------------------------|
| Lấy danh sách    | GET    | /api/v1/movies           |
| Lấy 1 object     | GET    | /api/v1/movies/{id}      |
| Tạo mới          | POST   | /api/v1/movies           |
| Cập nhật         | PUT    | /api/v1/movies/{id}      |
| Xóa              | DELETE | /api/v1/movies/{id}      |

### Quy tắc

- Dùng danh từ số nhiều  
- Không dùng động từ  
- Dùng dấu `-` nếu cần  

---

## 17. Frontend Rule

const API_BASE_URL = '/api/v1';

---

## 18. Nguyên tắc quan trọng nhất

Code đúng kiến trúc quan trọng hơn code phức tạp

---

## 19. Thông điệp cho team

- Không cần code hoàn hảo ngay từ đầu  
- Quan trọng là đúng structure  
- Code rõ ràng, dễ hiểu  
