import { defineStore } from 'pinia'

interface User {
  id: string
  name: string
}

interface AuthState {
  user: User | null
  token: string | null
}

export const useAuthStore = defineStore('auth', {
  state: (): AuthState => ({
    user: null,
    token: null,
  }),

  actions: {
    setAuth(user: User, token: string) {
      this.user = user
      this.token = token
    },

    logout() {
      this.user = null
      this.token = null
    },
  },
})
