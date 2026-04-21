

# 🎬 Cinema E-Booking System

> Full-stack system gồm:
>
> * Backend: Clean Architecture + DDD
> * Frontend: Vue 3 + Feature-based Architecture

---

# 📌 Table of Contents

1. Mục tiêu hệ thống
2. Kiến trúc tổng thể
3. Backend Architecture Rule
4. Frontend Architecture Rule
5. Feature Structure
6. API Convention
7. Git Workflow
8. Naming Convention
9. Development Workflow
10. Exception Handling Rule
11. Frontend Rule
12. CI/CD
13. PR Checklist
14. Core Principle

---

# 🎯 1. Mục tiêu hệ thống

* Xây dựng hệ thống đặt vé phim online (Cinema E-Booking)
* Dễ mở rộng (scalable)
* Dễ maintain (clean architecture)
* Team workflow rõ ràng
* Chuẩn production-level codebase

---

# 🏗️ 2. Kiến trúc tổng thể

## Backend

* Clean Architecture
* Domain-Driven Design (DDD)

```
presentation → application → domain
infrastructure → domain
```

## Frontend

* Vue 3 + Vite + TypeScript
* Pinia + Vue Router
* Feature-based architecture

---

# 🧠 3. Backend Architecture Rule

## Layer Rule

* Domain: business logic (KHÔNG JPA)
* Application: use-case layer
* Infrastructure: DB / external system
* Presentation: REST API

---

## Entity Rule

* Domain Entity: không annotation
* JpaEntity: chỉ dùng cho database
* Mapping: Domain ↔ Mapper ↔ JpaEntity

---

## Exception Rule (QUAN TRỌNG)

❌ Không dùng:

```java
new RuntimeException()
new BaseException()
```

✅ Chỉ dùng Factory:

```java
CinemaExceptions.notFound();
RoomExceptions.alreadyExists();
CommonExceptions.invalidInput();
```

---

## Flow xử lý lỗi

```
Controller → Service → ExceptionFactory → GlobalHandler → Response
```

---

# 🎨 4. Frontend Architecture Rule (Vue 3)

## Tech Stack

* Vue 3
* Pinia
* Vue Router
* TailwindCSS
* Axios

---

## Core Structure

```
src/
├── app/         (bootstrap: router, store, main)
├── features/    (domain modules)
├── shared/      (UI + utils dùng chung)
├── plugins/
└── assets/
```

---

## Rule quan trọng

* features/ = business logic theo domain
* shared/ = chỉ UI reusable (KHÔNG logic domain)
* app/ = config system

---

# 🧩 5. Feature Structure

Mỗi feature:

```
features/movie/
├── api/
├── components/
├── composables/
├── pages/
├── store/
└── types/
```

---

# 🌐 6. API Convention

## Base URL

```
/api/v1
```

## RESTful rules

| Action   | Endpoint     |
| -------- | ------------ |
| GET list | /movies      |
| GET one  | /movies/{id} |
| POST     | /movies      |
| PUT      | /movies/{id} |
| DELETE   | /movies/{id} |

---

## Rule

* Không hardcode URL
* Dùng axios instance
* Prefix env: VITE_API_URL

---

# 🔀 7. Git Workflow

## Branch naming

```
feature/booking-ticket
bugfix/login-error
hotfix/payment-fail
```

---

## Commit format

```
feat(booking): add booking API
fix(auth): fix login bug
refactor(movie): restructure service layer
```

---

## Workflow

```bash
git checkout -b feature/xxx
git commit -m "feat(...)"
git push origin feature/xxx
```

---

# 🧾 8. Naming Convention

## Backend

* Domain: `Cinema`
* JpaEntity: `CinemaJpaEntity`
* Mapper: `CinemaMapper`

---

## Frontend

* Component: `MovieCard.vue`
* Page: `MoviePage.vue`
* Store: `movie.store.ts`
* Composable: `useMovie.ts`

---

# ⚙️ 9. Development Workflow

1. Design entity đơn giản
2. Implement 1 flow nhỏ trước
3. Test API (Postman)
4. Refactor sau

❌ Không design full system trước khi code

---

# 💥 10. Exception Handling Rule

## Flow

```
Controller
 → Service
 → ExceptionFactory
 → GlobalHandler
 → Frontend Response
```

---

## Rule

* Không throw runtime exception trực tiếp
* Không dùng ErrorCode trực tiếp
* Chỉ dùng Factory

---

# 🎨 11. Frontend Rule

## Rule chính

* Không logic trong shared/
* Không import chéo feature
* Không hardcode API URL
* Dùng alias `@/`

---

## API layer

```
src/app/providers/axios.ts
```

---

## Store rule

* Global store: app/store
* Feature store: features/{domain}/store

---

## Layout rule

* Layout nằm trong shared/layouts
* App.vue chỉ chứa router-view

---

# 🚀 12. CI/CD

Trước khi merge:

```bash
npm run lint
npm run build
```

---

# 📋 13. PR Checklist

## Backend

* đúng layer architecture
* không leak JPA
* test Postman OK

## Frontend

* đúng feature structure
* không hardcode API
* build + lint pass

---

# 🧠 14. Core Principle

> Code đúng structure quan trọng hơn code nhanh

---

# 🎯 KẾT LUẬN

Hệ thống này đảm bảo:

* Clean Architecture backend
* Scalable Vue 3 frontend
* Team workflow rõ ràng
* Dễ maintain + dễ mở rộng

---


