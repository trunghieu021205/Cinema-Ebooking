import { computed, watch, watchEffect } from 'vue'
import { useShowtimes } from '@/composables/useShowtimes'
import { getDateKeyVN } from '@/utils/dateFormat'

export function useShowtimeSwitcher(booking: any) {
    const currentShowtime = computed(() => booking.selectedShowtime.value)

    const {
        showtimes: otherShowtimesRaw,
        loading: loadingOtherShowtimes,
        selectedCinemaId,
        selectedDate,
        fetchShowtimes: fetchOtherShowtimes
    } = useShowtimes(undefined, { autoFetch: false, includeActive: true })

    const allShowtimes = computed(() => {
        const current = currentShowtime.value
        if (!current) return []

        const sameMovieShowtimes = otherShowtimesRaw.value.filter(
            st => st.movieId === current.movieId && st.cinemaId === current.cinemaId
        )

        const hasCurrent = sameMovieShowtimes.some(st => st.id === current.id)
        const merged = hasCurrent
            ? sameMovieShowtimes
            : [current, ...sameMovieShowtimes]

        return [...merged].sort(
            (a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime()
        )
    })

    // Dùng watchEffect thay vì watch với immediate
    // watchEffect tự track dependency và chạy lại khi bất kỳ dep nào thay đổi
    watchEffect(async () => {
        const st = booking.selectedShowtime.value
        const cinema = booking.selectedCinema.value

        // Guard: chỉ fetch khi cả 2 đều có data
        if (!st || !cinema) return

        selectedCinemaId.value = cinema.id
        selectedDate.value = getDateKeyVN(st.startTime)

        // autoFetch: false → watch trong composable không fire
        // gọi thủ công sau khi state đã được set đồng bộ
        await fetchOtherShowtimes()
    })

    const changeShowtime = (newShowtime: any) => {
        if (!newShowtime) return
        if (newShowtime.id === currentShowtime.value?.id) return
        booking.selectedShowtime.value = newShowtime
        booking.selectedSeats.value = []
    }

    function getFormatBadge(st: any): string {
        if (!st) return ''
        if (st.formatName) return st.formatName
        const map: Record<number, string> = { 1: '2D', 2: '3D', 3: 'IMAX' }
        return map[st.formatId] ?? '2D'
    }

    return {
        allShowtimes,
        loadingOtherShowtimes,
        currentShowtime,
        changeShowtime,
        getFormatBadge,
        isCurrentShowtime: (id: number) => currentShowtime.value?.id === id
    }
}