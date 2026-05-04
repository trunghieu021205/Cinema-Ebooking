import { ref, computed } from 'vue'
import { useAuthStore } from '@/store/auth.store'
import { useRouter } from 'vue-router'
import { authApi } from '@/api/auth.api'
import { userApi } from '@/api/user.api'

export function useLoginForm(emit: (event: string) => void) {
    const auth = useAuthStore()
    const router = useRouter()

    const email = ref('')
    const password = ref('')
    const showPassword = ref(false)
    const loading = ref(false)
    const generalError = ref('')

    const fieldErrors = ref({ email: '', password: '' })

    const isDisabled = computed(() => loading.value)

    // ─── Validate local ────────────────────────────────────
    const validateAll = (): boolean => {
        fieldErrors.value = { email: '', password: '' }

        if (!email.value) fieldErrors.value.email = 'Vui lòng nhập email'
        if (!password.value) fieldErrors.value.password = 'Vui lòng nhập mật khẩu'

        return !Object.values(fieldErrors.value).some(Boolean)
    }

    // ─── Clear error ───────────────────────────────────────
    const handleInput = (field: keyof typeof fieldErrors.value) => {
        fieldErrors.value[field] = ''
        generalError.value = ''
    }

    // ─── Handle backend error (UPDATED) ────────────────────
    const handleBackendError = (err: any) => {
        // reset
        fieldErrors.value = { email: '', password: '' }
        generalError.value = ''

        // map field errors
        if (err.fieldErrors) {
            Object.assign(fieldErrors.value, err.fieldErrors)
        }

        const hasFieldError = Object.values(fieldErrors.value).some(Boolean)

        if (!hasFieldError && err.globalErrors?.length) {
            generalError.value = err.globalErrors[0]
            return
        }

        // fallback
        if (!hasFieldError) {
            generalError.value = err.message || 'Đăng nhập thất bại'
        }
    }

    // ─── Submit ────────────────────────────────────────────
    const handleLogin = async () => {
        if (!validateAll()) return

        loading.value = true
        generalError.value = ''

        try {
            const loginData = await authApi.login({
                email: email.value,
                password: password.value
            })

            localStorage.setItem('accessToken', loginData.accessToken)
            localStorage.setItem('refreshToken', loginData.refreshToken)

            const userProfile = await userApi.getMe(loginData.accessToken)

            auth.setAuth(userProfile, loginData.accessToken)

            router.push(
                loginData.role === 'ADMIN'
                    ? '/admin/analystics/dashboard'
                    : '/'
            )

            emit('close')
        } catch (err: any) {
            handleBackendError(err)
        } finally {
            loading.value = false
        }
    }
    const canSubmit = computed(() => {
        return (
            !loading.value &&
            email.value &&
            password.value &&
            !fieldErrors.value.email &&
            !fieldErrors.value.password
        )
    })
    const submit = () => {
        if (!canSubmit.value) return
        handleLogin()
    }

    return {
        email, password, showPassword,
        loading, generalError,
        fieldErrors,
        isDisabled,
        handleInput, submit
    }
}