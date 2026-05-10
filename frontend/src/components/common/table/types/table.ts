// ─── Column definition ───────────────────────────────────────────────────────
// Mỗi column khai báo một lần, dùng lại ở cả table lẫn detail panel

export type FieldType = 'text' | 'number' | 'email' | 'enum' | 'date' | 'textarea' | 'multiselect' | 'boolean'

export interface ColumnDef<T = Record<string, unknown>> {
  key: keyof T & string
  label: string
  type: FieldType
  options?: string[] | { id: number; name: string }[]
  readonly?: boolean
  readonlyInEdit?: boolean
  hideInCreate?: boolean
  hideInTable?: boolean
  required?: boolean
  displayKey?: string
  width?: string
}

// ─── Row item ─────────────────────────────────────────────────────────────────
// T là kiểu data cụ thể của từng domain (Cinema, Movie, User, ...)
// Bắt buộc có id để làm key và xử lý delete/update

export type RowItem = { id: string | number; [key: string]: unknown }

// ─── Emit events của DataTable ────────────────────────────────────────────────

export interface DataTableEmits<T extends RowItem> {
  create: []
  delete: [item: T]
  save: [item: T, done: () => void]
}