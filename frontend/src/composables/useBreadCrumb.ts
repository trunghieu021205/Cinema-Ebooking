import { computed } from 'vue'
import { useRoute } from 'vue-router'

export function useBreadcrumb() {
  const route = useRoute()

  // Lấy tất cả matched routes có meta.sidebar (bỏ qua layout wrapper)
  const crumbs = computed(() =>
    route.matched
      .filter((r) => r.meta?.sidebar)
      .map((r) => (r.meta!.sidebar as { label: string }).label)
  )

  // Tên trang hiện tại = crumb cuối cùng
  const pageTitle = computed(() => crumbs.value.at(-1) ?? '')

  // Breadcrumb string: "Cinemas / Seat Layout"
  const breadcrumb = computed(() => crumbs.value.join(' / '))

  return { pageTitle, breadcrumb, crumbs }
}