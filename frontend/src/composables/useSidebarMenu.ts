import { computed } from 'vue'
import { useRouter } from 'vue-router'
import type { Component } from 'vue'

// Shape sau khi composable xử lý xong
export interface SidebarChild {
  key: string
  label: string
  to: string
}

export interface SidebarMenuEntry {
  key: string
  label: string
  icon?: Component
  to?: string        // chỉ có nếu là direct link
  children?: SidebarChild[] // chỉ có nếu là group
}

export function useSidebarMenu(adminPath = '/admin') {
  const router = useRouter()

  const menu = computed<SidebarMenuEntry[]>(() => {
    // 1. Tìm route /admin trong config
    const adminRoute = router.options.routes.find((r) => r.path === adminPath)
    if (!adminRoute?.children) return []

    // 2. Chỉ lấy những route có meta.sidebar
    return adminRoute.children
      .filter((route) => route.meta?.sidebar)
      .map((route) => {
        const { label, icon } = route.meta!.sidebar as { label: string; icon?: Component }

        // 3. Sub-routes có meta.sidebar → đây là group
        const sidebarChildren = route.children?.filter((c) => c.meta?.sidebar)

        if (sidebarChildren?.length) {
          return {
            key: route.path,
            label,
            icon,
            children: sidebarChildren.map((child) => ({
              key: String(child.name ?? child.path),
              label: (child.meta!.sidebar as { label: string }).label,
              // router.resolve để lấy path tuyệt đối, tránh nối string thủ công
              to: router.resolve({ name: child.name }).path,
            })),
          }
        }

        // 4. Không có sub-routes → direct link
        return {
          key: route.path,
          label,
          icon,
          to: router.resolve({ name: route.name }).path,
        }
      })
  })

  return menu
}