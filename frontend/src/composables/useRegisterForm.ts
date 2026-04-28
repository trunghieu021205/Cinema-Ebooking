import { ref, computed } from 'vue'
import { authApi } from '@/api/auth.api'

export function useRegisterForm(emit: (event: string) => void) {

    const form = ref({
        fullName: '',
        email: '',
        phoneNumber: '',
        gender: '' as 'male' | 'female' | '',
        dateOfBirth: null as Date | null,
        password: '',
        confirmPassword: '',
        agreeTerms: false,
    })

    const errors = ref({
        fullName: '',
        email: '',
        phoneNumber: '',
        gender: '',
        dateOfBirth: '',
        password: '',
        confirmPassword: '',
    })

    const showPassword = ref(false)
    const showConfirmPassword = ref(false)
    const loading = ref(false)
    const generalError = ref('')
    const success = ref(false)
    const successMessage = ref('')

    type Field = keyof typeof errors.value

    // ─── Chỉ disable khi đang có lỗi hoặc chưa tick điều khoản ───
    const isDisabled = computed(() =>
        loading.value ||
        !form.value.agreeTerms ||
        Object.values(errors.value).some(Boolean)
    )

    // ─── Clear error khi user sửa field (không re-validate) ───────
    const handleInput = (field: Field) => {
        errors.value[field] = ''
        generalError.value = ''
    }

    const selectGender = (value: 'male' | 'female') => {
        form.value.gender = value
        errors.value.gender = ''
    }

    const handleDateSelect = (date: Date | null) => {
    form.value.dateOfBirth = date    // ← đổi
    errors.value.dateOfBirth = ''   // ← đổi
}

    // ─── Validate toàn bộ — chỉ chạy khi submit ──────────────────
    const validateAll = (): boolean => {
        const v = form.value

        errors.value.fullName = v.fullName.trim() ? '' : 'Vui lòng nhập họ và tên'
        errors.value.email = v.email.trim() ? '' : 'Vui lòng nhập email'
        errors.value.phoneNumber = v.phoneNumber.trim() ? '' : 'Vui lòng nhập số điện thoại'
        errors.value.gender = v.gender ? '' : 'Vui lòng chọn giới tính'
        errors.value.dateOfBirth = v.dateOfBirth ? '' : 'Vui lòng chọn ngày sinh'
        errors.value.password = v.password ? '' : 'Vui lòng nhập mật khẩu'
        errors.value.confirmPassword = !v.confirmPassword
            ? 'Vui lòng nhập lại mật khẩu'
            : v.confirmPassword !== v.password
                ? 'Mật khẩu không khớp'
                : ''

        return !Object.values(errors.value).some(Boolean)
    }

    const handleBackendError = (err: any) => {
        errors.value = {
            fullName: '', email: '', phoneNumber: '', gender: '',
            dateOfBirth: '', password: '', confirmPassword: '',
        }
        generalError.value = ''

        if (err.fieldErrors) {
            for (const [field, message] of Object.entries(err.fieldErrors)) {
                if (field in errors.value) {
                    errors.value[field as Field] = message as string
                }
            }
        }

        const hasFieldError = Object.values(errors.value).some(Boolean)

        if (!hasFieldError) {
            generalError.value = err.globalErrors?.[0] ?? err.message ?? 'Không thể kết nối đến máy chủ, thử lại sau'
        }
    }
    // ─── Submit ───────────────────────────────────────────────────
    const handleRegister = async () => {
        if (!validateAll()) return

        loading.value = true
        generalError.value = ''
        success.value = false

        try {
            await authApi.register({
                fullName: form.value.fullName,
                email: form.value.email,
                password: form.value.password,
                phoneNumber: form.value.phoneNumber,
                dateOfBirth: form.value.dateOfBirth!.toISOString().split('T')[0], // Date → "2005-12-02"
                gender: form.value.gender.toUpperCase() as 'MALE' | 'FEMALE',  
            })

            success.value = true
            successMessage.value = 'Đăng ký thành công!\nVui lòng đăng nhập để tiếp tục.'

        } catch (err: any) {
            handleBackendError(err)
        } finally {
            loading.value = false
        }
    }
    const canSubmit = computed(() => {
        return (
            !loading.value &&
            !Object.values(errors.value).some(Boolean) &&
            form.value.email &&
            form.value.password &&
            form.value.confirmPassword &&
            form.value.agreeTerms
        )
    })

    const submit = () => {
        if (!canSubmit.value) return
        handleRegister()
    }

    return {
        form, errors,
        showPassword, showConfirmPassword,
        loading, generalError,
        isDisabled,
        handleInput, selectGender, handleDateSelect, submit,
        success, successMessage,
    }
}