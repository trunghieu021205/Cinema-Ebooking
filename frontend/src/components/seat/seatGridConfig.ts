// ─── Seat type style ──────────────────────────────────────────────────────────
// Key = seatTypeId từ backend. Sửa id cho đúng với data thực tế.
// Hiện tại giả định: 1 = STANDARD, 2 = VIP, 3 = COUPLE

export interface SeatTypeConfig {
  label: string

  // Admin: bg thể hiện loại ghế, hover brightness-110
  adminBg:     string   // Tailwind bg class
  adminBorder: string   // Tailwind border class
  adminText:   string   // Tailwind text class

  // Web: border thể hiện loại ghế, bg chung là dark
  // khi selected → webSelectedBg được tô
  webBorder:       string
  webSelectedBg:   string
  webSelectedText: string
}

// ── Màu từng loại ghế ─────────────────────────────────────

export const SEAT_TYPE_CONFIGS: Record<number, SeatTypeConfig> = {
  1: {
    label:           'Ghế thường',
    adminBg:         'bg-slate-200',
    adminBorder:     'border-slate-400',
    adminText:       'text-slate-700',
    webBorder:       'border-white/30',
    webSelectedBg:   'bg-amber-500',
    webSelectedText: 'text-white',
  },
  2: {
    label:           'Ghế VIP',
    adminBg:         'bg-amber-100',
    adminBorder:     'border-amber-400',
    adminText:       'text-amber-800',
    webBorder:       'border-amber-400',
    webSelectedBg:   'bg-amber-500',
    webSelectedText: 'text-white',
  },
  3: {
    label:           'Ghế đôi',
    adminBg:         'bg-violet-100',
    adminBorder:     'border-violet-400',
    adminText:       'text-violet-800',
    webBorder:       'border-violet-500',
    webSelectedBg:   'bg-violet-500',
    webSelectedText: 'text-white',
  },
}

export const SEAT_TYPE_FALLBACK: SeatTypeConfig = {
  label:           'Unknown',
  adminBg:         'bg-gray-200',
  adminBorder:     'border-gray-300',
  adminText:       'text-gray-500',
  webBorder:       'border-gray-500',
  webSelectedBg:   'bg-gray-500',
  webSelectedText: 'text-white',
}

// ─── Grid config ──────────────────────────────────────────────────────────────

export interface SeatGridConfig {
  mode: 'admin' | 'web'
  seatSize: string   
  seatRadius: string   
  screenPosition: 'top' | 'bottom'
  rtl: boolean 
  showRowLabel: boolean
  rowLabelBothSides: boolean
  showColNumber: boolean  
  gap: string  
  cellWidth: string
  cellHeight: string
  legendSize: string       
  legendSizeWide: string  
}

// ── Admin config ──────────────────────────────────────────────────────────────
export const adminSeatGridConfig: SeatGridConfig = {
  mode: 'admin',
  seatSize: 'h-8 w-8',
  seatRadius: 'rounded-md',
  screenPosition: 'bottom',
  rtl: true,
  showRowLabel: true,
  rowLabelBothSides: true,
  showColNumber: true,
  gap: 'gap-1.5',
  cellWidth: '2rem',
  cellHeight: '2rem',
  legendSize: 'h-5 w-5',     
  legendSizeWide: 'h-5 w-9',
}

// ── Web config ────────────────────────────────────────────────────────────────
export const webSeatGridConfig: SeatGridConfig = {
  mode: 'web',
  seatSize: 'h-8 w-8',
  seatRadius: 'rounded-md',
  screenPosition: 'bottom',
  rtl: true,
  showRowLabel: true,
  rowLabelBothSides: true,
  showColNumber: true,
  gap: 'gap-2',
  cellWidth: '2rem',
  cellHeight: '2rem',
    legendSize: 'h-5 w-5',
  legendSizeWide: 'h-5 w-9',
}

// ── Preview config (dùng trong form tạo/sửa suất chiếu) ───────────────────────
export const previewSeatGridConfig: SeatGridConfig = {
    mode: 'admin',
    seatSize: 'h-5 w-5',     
    seatRadius: 'rounded-sm', 
    screenPosition: 'bottom',
    rtl: true,
    showRowLabel: true,
    rowLabelBothSides: false, 
    showColNumber: true,
    gap: 'gap-2',              
    cellWidth: '1.25rem',
    cellHeight: '1.25rem',
    legendSize: 'h-4 w-4',       
    legendSizeWide: 'h-4 w-7',

}