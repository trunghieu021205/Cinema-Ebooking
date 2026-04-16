
---

# Cinema E-Booking Backend Guidelines

## Table of Contents

* [1. Mục tiêu](#1-mục-tiêu)
* [2. Kiến trúc hệ thống](#2-kiến-trúc-hệ-thống)
* [3. Entity Design Rule](#3-entity-design-rule)
* [4. Mapper Rule](#4-mapper-rule)
* [5. Naming Convention](#5-naming-convention)
* [6. Domain Design Rule](#6-domain-design-rule)
* [7. Development Workflow](#7-development-workflow)
* [8. Team Working Rule](#8-team-working-rule)
* [9. Code Review Rule](#9-code-review-rule)
* [10. Git Convention](#10-git-convention-conventional-commits)
* [11. Branch Naming](#11-branch-naming)
* [12. Git Workflow](#12-git-workflow)
* [13. API Versioning](#13-api-versioning)
* [14. URL Structure](#14-url-structure)
* [15. Controller Rule](#15-controller-rule)
* [16. RESTful Convention](#16-restful-convention)
* [17. API Testing Rule (Postman)](#17-api-testing-rule-postman)
* [18. Frontend Rule](#18-frontend-rule)
* [19. CI/CD & Quality Gate](#19-cicd--quality-gate)
* [20. PR Checklist](#20-pr-checklist)
* [21. Core Principle](#21-core-principle)
* [22. Team Message](#22-team-message)

---

## 1. Mục tiêu

* Đảm bảo code dễ đọc, dễ maintain
* Thống nhất cách làm việc giữa các thành viên
* Giảm lỗi kiến trúc
* Tăng tốc độ phát triển
* Hỗ trợ scale hệ thống

---

## 2. Kiến trúc hệ thống

Dự án áp dụng:

* Clean Architecture
* Domain-Driven Design (DDD)

### Cấu trúc mỗi domain

domain/
application/
infrastructure/
presentation/

### Dependency Rule (bắt buộc)

* presentation → application → domain
* infrastructure → domain

### Nguyên tắc quan trọng

* Domain không phụ thuộc Infrastructure
* Domain không import JPA (jakarta.persistence)
* Không để JpaEntity extends Domain
* Không expose JpaEntity ra ngoài application

---

## 3. Entity Design Rule

### Domain Entity

* Chỉ chứa business logic
* Sử dụng EntityId (ví dụ: CinemaId)
* Không có annotation JPA

### JpaEntity

* Đại diện cho database
* Sử dụng Long idJpa
* Có annotation @Entity

### Mapping

Domain ↔ Mapper ↔ JpaEntity

---

## 4. Mapper Rule

### Required

Mapper (interface)
MapperImpl (implementation)

### Nguyên tắc

* Convert Domain ↔ JpaEntity
* Không chứa business logic
* Không expose JpaEntity

---

## 5. Naming Convention

### Class

Type: Domain
Format: PascalCase
Example: Cinema

Type: JpaEntity
Format: PascalCase + JpaEntity
Example: CinemaJpaEntity

Type: Mapper
Format: PascalCase + Mapper
Example: CinemaMapper

Type: Repository
Format: PascalCase + Repository
Example: CinemaRepository

### Variable

camelCase

### Enum

UPPER_CASE

---

## 6. Domain Design Rule

### Không được

* Over-design
* Tạo field không dùng
* Tạo quan hệ khi chưa cần

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

* Làm theo module được assign
* Tuân thủ structure và convention

### Không được

* Tự ý thay đổi kiến trúc
* Tự thiết kế lại structure

### Khi có vấn đề

Hỏi trước khi sửa

---

## 8.5 Exception Handling Rule (IMPORTANT)

Hệ thống backend sử dụng **Domain Exception Factory Pattern** để thống nhất toàn bộ xử lý lỗi.

---

### 8.5.1 Mục tiêu

* Chuẩn hóa toàn bộ exception trong hệ thống
* Không cho phép throw exception tùy ý
* Dễ debug + dễ trace lỗi (traceId)
* Response frontend luôn đồng nhất

---

### 8.5.2 Quy tắc bắt buộc

## ❌ KHÔNG ĐƯỢC

```java id="ex-a1"
new RuntimeException()
new IllegalArgumentException()
new BaseException(ErrorCode.XXX)
```

---

## ❌ KHÔNG ĐƯỢC dùng ErrorCode trực tiếp

```java id="ex-a2"
throw new BaseException(ErrorCode.CINEMA_NOT_FOUND);
```

---

## ✅ CHỈ ĐƯỢC DÙNG FACTORY

```java id="ex-a3"
throw CinemaExceptions.notFound();
throw CinemaExceptions.alreadyExists();
throw CommonExceptions.invalidInput();
```

---

### 8.5.3 Phân loại Exception

### 🔹 CommonExceptions (system-level)

Dùng cho:

* input sai
* auth / authorization
* lỗi hệ thống chung

```java id="ex-a4"
CommonExceptions.invalidInput()
CommonExceptions.resourceNotFound()
CommonExceptions.unauthorized()
```

---

### 🔹 Domain Exceptions

Dùng theo từng module:

```text id="ex-a5"
CinemaExceptions
RoomExceptions
BookingExceptions
UserExceptions
```

---

Ví dụ:

```java id="ex-a6"
CinemaExceptions.notFound()
RoomExceptions.alreadyExists()
BookingExceptions.invalidStatus()
```

---

### 8.5.4 Quy trình thêm exception mới

### Bước 1:

Kiểm tra `ErrorCode.java` đã tồn tại chưa

---

### Bước 2:

Nếu chưa có → báo Backend Lead:

Cần cung cấp:

* Tên error (VD: ROOM_NOT_FOUND)
* Message default
* HTTP status

---

### Bước 3:

Lead thêm vào `ErrorCode`

---

### Bước 4:

Tạo Factory tương ứng

```java id="ex-a7"
throw RoomExceptions.notFound("Room ID: " + id);
```

---

## ### 8.5.5 Rule về message

## ✔ Được phép

```java id="ex-a8"
CinemaExceptions.notFound("Cinema ID: " + id);
```

---

## ❌ Không nên

```java id="ex-a9"
CinemaExceptions.notFound("error");
CinemaExceptions.notFound("something wrong");
```

---

## ### 8.5.6 Exception Flow

```text id="ex-a10"
Controller
   ↓
Service
   ↓
ExceptionFactory
   ↓
BaseException
   ↓
GlobalExceptionHandler
   ↓
ErrorResponse (traceId + details)
   ↓
Frontend
```

---

### 8.5.7 Red Flags (FAIL PR)

Nếu có bất kỳ case nào sau:

* ❌ new RuntimeException
* ❌ new BaseException
* ❌ dùng ErrorCode trực tiếp
* ❌ throw exception trong controller logic
* ❌ message không rõ ràng

👉 **PR bị reject ngay**

---

## ### 8.5.8 Quick Cheat Sheet

| Case                  | Dùng gì                         |
| --------------------- | ------------------------------- |
| Input sai             | CommonExceptions.invalidInput() |
| Không tìm thấy Cinema | CinemaExceptions.notFound()     |
| Lỗi Room              | RoomExceptions.xxx()            |
| Lỗi hệ thống          | CommonExceptions.xxx()          |

---

### 8.5.9 Message cho team

Hệ thống exception này giúp:

* code thống nhất toàn team
* debug nhanh hơn
* giảm bug production
* dễ mở rộng module mới

Ban đầu có thể hơi khác thói quen cũ, nhưng sẽ quen rất nhanh sau vài ngày sử dụng.

---

# 🎯 KẾT QUẢ SAU CHUẨN HÓA

✔ rõ ràng hơn cho dev mới
✔ giảm overload chữ
✔ scan nhanh trong PR
✔ enforce rule mạnh hơn
✔ đúng style “tech spec production system”


## 9. Code Review Rule

### Focus

* Đúng layer chưa
* Có leak JPA không
* Mapper đúng chưa
* Naming rõ ràng

### Không focus

* Style cá nhân
* Tối ưu sớm

---

## 10. Git Convention (Conventional Commits)

### Cấu trúc

type(scope): mô tả ngắn gọn

body (optional)
footer (optional)

### Type

feat: Thêm tính năng mới
fix: Sửa lỗi
refactor: Tái cấu trúc code
chore: Setup, config
build: Thay đổi build system
docs: Tài liệu
style: Format code
test: Test
perf: Tối ưu hiệu suất

### Scope (khuyến khích)

backend, frontend, booking, movie, payment, user, auth

### Ví dụ

feat(booking): implement API đặt vé

* Tạo entity Booking, Ticket
* Viết service và controller
* Kiểm tra ghế trống

### Không được dùng

update code
fix bug
commit 1

---

## 11. Branch Naming

### Format

feature/ten-tinh-nang
bugfix/mota-loi
hotfix/mota
chore/mota
refactor/mota
release/v1.0.0

### Quy tắc

* Viết thường toàn bộ
* Dùng dấu -
* Không dùng khoảng trắng hoặc _

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

## 14. URL Structure

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

Lấy danh sách: GET /api/v1/movies
Lấy 1 object: GET /api/v1/movies/{id}
Tạo mới: POST /api/v1/movies
Cập nhật: PUT /api/v1/movies/{id}
Xóa: DELETE /api/v1/movies/{id}

### Quy tắc

* Dùng danh từ số nhiều
* Không dùng động từ
* Dùng dấu - nếu cần

---

## 17. API Testing Rule (Postman)

Backend ưu tiên test API theo hướng manual testing bằng Postman trong giai đoạn MVP.

### Tool

* Postman

### Base URL

[http://localhost:8080/api/v1](http://localhost:8080/api/v1)

### Environment (Recommended)

Tạo Postman Environment:

baseUrl = [http://localhost:8080/api/v1](http://localhost:8080/api/v1)

Sử dụng theo format:

{{baseUrl}}/cinemas
{{baseUrl}}/movies

### Postman Collection Convention

Tên collection:

Cinema E-Booking API - v1

Mỗi module là một folder riêng trong collection:

* Cinema
* Movie
* Showtime
* Booking
* Room
* Seat
* User
* Payment
* Coupon
* ...

### Required Test Flow (Manual)

Mỗi API module trước khi merge PR phải test:

* GET list
* GET detail
* POST create
* PUT update
* DELETE remove (soft delete nếu có)

### Rule

* API trước khi merge phải test qua Postman
* Nếu có lỗi, phải attach response log hoặc screenshot trong PR

### Optional (Recommended)

Nếu team muốn share chung Postman collection:

* Export Postman collection (.json)
* Lưu tại: docs/postman/

---

## 18. Frontend Rule

Frontend áp dụng kiến trúc Feature-based theo mindset Clean Architecture.

Frontend sử dụng:

* Vue 3 + TypeScript
* Vite
* Pinia
* Vue Router
* TailwindCSS
* Axios
* ESLint

---

### 18.1 Frontend Architecture Rule

Nguyên tắc:

* features/ là nơi chứa toàn bộ domain modules
* shared/ chỉ chứa component/utils dùng chung, không chứa logic domain
* app/ chỉ chứa config bootstrap (router/providers/global store)
* không import chéo domain trực tiếp giữa các feature

---

### 18.2 Frontend Folder Structure

```txt
src/
├── app/
│   ├── providers/
│   ├── router/
│   └── store/
│
├── features/
│   ├── movie/
│   │   ├── api/
│   │   ├── components/
│   │   ├── composables/
│   │   ├── pages/
│   │   ├── store/
│   │   └── types/
│   │
│   ├── cinema/
│   └── ...
│
├── shared/
│   ├── assets/
│   ├── components/
│   │   ├── common/
│   │   └── ui/
│   ├── constants/
│   ├── hooks/
│   ├── layouts/
│   ├── styles/
│   ├── types/
│   └── utils/
│
└── plugins/
```

---

### 18.3 Feature Module Rule

Mỗi feature domain bắt buộc có các folder sau:

* api/: gọi API của module
* types/: interface, enum, model, dto
* pages/: các page chính của module
* components/: UI component thuộc module
* composables/: xử lý use-case (fetch, form, validate, pagination)
* store/: Pinia store nếu module cần state

---

### 18.4 Naming Convention (Frontend)

Folder:

* feature folder dùng snake_case
  ví dụ: audit_log, booking_combo, booking_coupon

File:

* component: PascalCase.vue
  ví dụ: MovieCard.vue
* page: PascalCasePage.vue
  ví dụ: MoviePage.vue
* layout: PascalCase.vue
  ví dụ: MainLayout.vue
* composable: useXxx.ts
  ví dụ: useMovies.ts
* api file: xxx.api.ts
  ví dụ: movie.api.ts
* store file: xxx.store.ts
  ví dụ: movie.store.ts
* model file: xxx.model.ts
  ví dụ: movie.model.ts
* enum file: xxx.enum.ts
  ví dụ: movie.enum.ts

---

### 18.5 Import Rule

Bắt buộc dùng alias @/ để import:

import Navbar from '@/shared/components/common/Navbar.vue
import type { Movie } from '@/features/movie/types

Không dùng relative path quá sâu:

không được dùng ../../../../shared/...

---

### 18.6 API Rule

Base URL phải lấy từ env.

Không commit file .env.development.
Chỉ commit file .env.example.

.env.example:

VITE_API_URL=[http://localhost:8080/api/v1](http://localhost:8080/api/v1)

Axios instance phải nằm trong:

src/app/providers/axios.ts

Feature API chỉ gọi endpoint relative:

axiosClient.get('/movies')
axiosClient.get('/cinemas')

Không hardcode full url:

không được dùng [http://localhost:8080/api/v1/movies](http://localhost:8080/api/v1/movies)

---

### 18.7 Store Rule

* Global store đặt tại src/app/store
* Feature store đặt tại src/features/{domain}/store
* Không dùng src/stores (tránh trùng và gây nhầm lẫn)

---

### 18.8 Layout Rule

* Layout đặt tại src/shared/layouts
* Router mount layout qua children routes
* App.vue chỉ chứa router-view
* Không render layout trực tiếp trong App.vue để tránh duplicate Navbar/Footer

---

### 18.9 Frontend Quality Gate

Trước khi tạo PR hoặc merge vào develop, bắt buộc chạy:

npm run build
npm run lint

---

## 19. CI/CD & Quality Gate

Trước khi merge PR vào develop, bắt buộc:

npm run build
npm run lint

Recommended GitHub Actions file:

.github/workflows/frontend-ci.yml

Nội dung:

```yml
name: Frontend CI

on:
  pull_request:
    branches: [develop]
  push:
    branches: [develop]

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout repository
        uses: actions/checkout@v4

      - name: Setup Node
        uses: actions/setup-node@v4
        with:
          node-version: 20

      - name: Install dependencies
        run: npm install
        working-directory: frontend

      - name: Run lint
        run: npm run lint
        working-directory: frontend

      - name: Build project
        run: npm run build
        working-directory: frontend
```

Note:

* working-directory: frontend chỉ dùng khi frontend nằm trong folder frontend/
* nếu frontend nằm root repo thì xóa dòng đó

---

## 20. PR Checklist

### Backend Checklist

* Code đúng layer (presentation → application → domain)
* Không leak JPA ra ngoài application
* Mapper convert đúng Domain ↔ JpaEntity
* Controller mapping đúng /api/v1
* API URL đúng RESTful convention
* API đã test bằng Postman

### Frontend Checklist

Architecture:

* Code nằm đúng module features/{domain}
* Không để logic business trong shared/
* Không import chéo domain trực tiếp
* Không hardcode API URL

UI:

* Layout không bị duplicate Navbar/Footer
* Responsive cơ bản

API:

* Axios call dùng axiosClient từ app/providers
* Types trả về map đúng backend DTO

Quality Gate:

* npm run build pass
* npm run lint pass
* không commit .env.development

---

## 21. Core Principle

Code đúng kiến trúc quan trọng hơn code phức tạp

---

## 22. Team Message

* Không cần code hoàn hảo ngay từ đầu
* Quan trọng là đúng structure
* Code rõ ràng, dễ hiểu

---
