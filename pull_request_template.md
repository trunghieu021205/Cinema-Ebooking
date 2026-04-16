

# 📌 PR Checklist

## 🏗️ Architecture

* [ ] Code đúng layer (Controller → Service → Domain)
* [ ] Không leak JpaEntity ra ngoài application layer
* [ ] Mapper được dùng đúng (Domain ↔ JpaEntity)
* [ ] Domain không chứa logic infrastructure

---

## ⚠️ Exception Handling

* [ ] Không dùng `new RuntimeException()` / `new BaseException()`
* [ ] Không dùng trực tiếp `ErrorCode`
* [ ] Chỉ dùng Exception Factory (Common / Domain)
* [ ] Exception đúng domain (Cinema / Room / etc.)
* [ ] Message exception rõ ràng, có context (id, name,...)

---

## 📦 API Design

* [ ] Endpoint đúng RESTful convention
* [ ] URL đúng format `/api/v1/...`
* [ ] Không dùng verb trong URL
* [ ] HTTP method đúng (GET/POST/PUT/DELETE)

---

## 🧪 Testing

* [ ] API đã test bằng Postman
* [ ] Test case chính hoạt động đúng
* [ ] Có test error case (not found / invalid input)

---

## 🧼 Code Quality

* [ ] Code dễ đọc, không duplicate logic
* [ ] Naming rõ ràng
* [ ] Không có debug / TODO thừa
* [ ] Không over-engineering

---

## ⚙️ Build

* [ ] Build thành công trước khi push
* [ ] Không có lỗi compile / warning quan trọng

---

## 💬 Notes

* Nếu sai architecture → cần sửa trước khi merge
* Nếu sai exception rule → phải refactor lại theo factory

---

# 🎯 Tại sao bản này hợp với bạn

* Không quá dài → dev không bị “ngợp”
* Chỉ check thứ quan trọng nhất (architecture + exception + API)
* Dễ enforce bằng review thủ công
* Phù hợp team nhỏ / đang build nền

---

