import { ref } from 'vue'

const theme = ref('dark')

export function useTheme() {
  const applyTheme = (value) => {
    theme.value = value
    document.documentElement.setAttribute('data-theme', value)
    localStorage.setItem('theme', value)
  }

  const setTheme = (value) => {
    applyTheme(value)
  }

  const toggleTheme = () => {
    console.log('Toggling theme')
    applyTheme(theme.value === 'dark' ? 'light' : 'dark')
  }

  const initTheme = () => {
    const saved = localStorage.getItem('theme')

    const initial = saved || 'dark'

    applyTheme(initial)
  }

  return { theme, setTheme, toggleTheme, initTheme }
}