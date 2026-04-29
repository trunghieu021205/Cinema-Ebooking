// ─── Column definition ───────────────────────────────────────────────────────
// Mỗi column khai báo một lần, dùng lại ở cả table lẫn detail panel

export type FieldType = 'text' | 'number' | 'email' | 'enum' | 'date' | 'textarea'

export interface ColumnDef<T = Record<string, unknown>> {
  key: keyof T & string   // tên field trong data object
  label: string           // label hiển thị (table header + panel label)
  type: FieldType         // quyết định input nào render trong detail panel
  options?: string[]      // chỉ cần khi type === 'enum' → render <select>
  readonly?: boolean
  hideInCreate?: boolean   // true → hiển thị nhưng không cho chỉnh sửa
  hideInTable?: boolean   // true → ẩn khỏi table nhưng vẫn hiện trong panel
  required?: boolean      // default true — false nếu field được phép trống
}

// ─── Row item ─────────────────────────────────────────────────────────────────
// T là kiểu data cụ thể của từng domain (Cinema, Movie, User, ...)
// Bắt buộc có id để làm key và xử lý delete/update

export type RowItem = { id: string | number; [key: string]: unknown }

// ─── Emit events của DataTable ────────────────────────────────────────────────

export interface DataTableEmits<T extends RowItem> {
  create: []
  delete: [item: T]
  save: [item: T]
}