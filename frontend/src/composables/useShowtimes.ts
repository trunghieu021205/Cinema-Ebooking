import { ref, computed, watch, onMounted } from 'vue'
import { showtimeApi } from '@/api/showtime.api'
import { cinemaApi } from '@/api/cinema.api'
import { movieApi } from '@/api/movie.api'
import type { ShowtimeResponse, ShowtimeFormatResponse } from '@/types/showtime'
import type { CinemaResponse } from '@/types/cinema'
import type { MovieResponse } from '@/types/movie'
import { getDateKeyVN, dateToISOString } from '@/utils/dateFormat'

const FORMATS: ShowtimeFormatResponse[] = [
  { id: 1, name: '2D',   extraPrice: 0     },
  { id: 2, name: '3D',   extraPrice: 30000 },
  { id: 3, name: 'IMAX', extraPrice: 60000 },
]

const getPreviousDateKey = (dateKey: string): string => {
  const [year, month, day] = dateKey.split('-').map(Number)
  const date = new Date(Date.UTC(year, month - 1, day))
  date.setUTCDate(date.getUTCDate() - 1)
  return date.toISOString().slice(0, 10)
}

const mergeShowtimesById = (lists: ShowtimeResponse[][]): ShowtimeResponse[] => {
  const map = new Map<number, ShowtimeResponse>()
  lists.flat().forEach(showtime => map.set(showtime.id, showtime))
  return Array.from(map.values())
}

export function useShowtimes(movieId?: number, options?: { autoFetch?: boolean; includeActive?: boolean }) {
  const autoFetch = options?.autoFetch ?? true
  // includeActive: true → không filter status (dùng cho switcher, cần cả ONGOING)
  // mặc định false → chỉ lấy SCHEDULED (dùng cho trang chọn suất chiếu)
  const includeActive = options?.includeActive ?? false
  const showtimes = ref<ShowtimeResponse[]>([])
  const cinemas = ref<CinemaResponse[]>([])
  const movies = ref<MovieResponse[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  const selectedCity = ref<string>('')
  const selectedCinemaId = ref<number | null>(null)
  const selectedDate = ref<string>('')

  const cities = computed(() => {
    const citySet = new Set(cinemas.value.map(c => c.city))
    return Array.from(citySet).sort()
  })

  const filteredCinemas = computed(() => {
    if (!selectedCity.value) return cinemas.value
    return cinemas.value.filter(c => c.city === selectedCity.value)
  })

  const fetchMasterData = async () => {
    try {
      const [cinemasRes, moviesRes] = await Promise.all([
        cinemaApi.getList(0, 100),
        movieApi.getList({ size: 100 })
      ])
      cinemas.value = cinemasRes.content
      movies.value = moviesRes.content
    } catch (error) {
      console.error('Failed to fetch master data', error)
    }
  }

  const fetchShowtimes = async () => {
    loading.value = true
    error.value = null
    try {
      const params: any = {
        page: 0,
        size: 200,
        sort: 'startTime,asc'
      }

      // includeActive: true → không gửi status → backend trả về tất cả trạng thái
      // includeActive: false (default) → chỉ lấy SCHEDULED, bỏ qua ONGOING/FINISHED
      if (!includeActive) {
        params.status = 'SCHEDULED'
      }

      if (movieId != null) {
        params.movieId = movieId
      }

      if (selectedCinemaId.value != null) {
        params.cinemaId = selectedCinemaId.value
      } else if (selectedCity.value) {
        params.city = selectedCity.value
      }

      const selectedDateKey = selectedDate.value

      if (selectedDateKey) {
        // Chọn ngày cụ thể → chỉ lấy ngày đó
        params.date = selectedDateKey
      } else {
        // Không chọn ngày → giới hạn 7 ngày từ hôm nay
        const today = new Date()
        today.setHours(0, 0, 0, 0)
        const endDate = new Date(today)
        endDate.setDate(today.getDate() + 6)
        params.dateFrom = dateToISOString(today, false)
        params.dateTo = dateToISOString(endDate, false)
      }

      if (selectedDateKey) {
        const previousDateParams = {
          ...params,
          date: getPreviousDateKey(selectedDateKey),
        }

        const [selectedDateRes, previousDateRes] = await Promise.all([
          showtimeApi.getPublicShowtimes(params),
          showtimeApi.getPublicShowtimes(previousDateParams),
        ])

        showtimes.value = mergeShowtimesById([
          selectedDateRes.content,
          previousDateRes.content,
        ]).filter(showtime => getDateKeyVN(showtime.startTime) === selectedDateKey)
      } else {
        const res = await showtimeApi.getPublicShowtimes(params)
        showtimes.value = res.content
      }
    } catch (e) {
      console.error('Failed to fetch showtimes', e)
      showtimes.value = []
      error.value = 'Không thể tải suất chiếu. Vui lòng thử lại.'
    } finally {
      loading.value = false
    }
  }

  // Watch chỉ active khi autoFetch = true
  // Khi autoFetch = false, caller tự gọi fetchShowtimes thủ công
  if (autoFetch) {
    watch(selectedCity, () => {
      selectedCinemaId.value = null
      fetchShowtimes()
    })

    watch([selectedCinemaId, selectedDate], () => {
      fetchShowtimes()
    })
  }

  const getCinemaById = (id: number) => cinemas.value.find(c => c.id === id)
  const getMovieById = (id: number) => movies.value.find(m => m.id === id)
  const getFormatById = (id: number) => FORMATS.find(f => f.id === id)

  onMounted(async () => {
    await fetchMasterData()
    if (autoFetch) {
      await fetchShowtimes()
    }
  })

  return {
    showtimes,
    loading,
    error,
    cities,
    filteredCinemas,
    selectedCity,
    selectedCinemaId,
    selectedDate,
    getCinemaById,
    getMovieById,
    getFormatById,
    fetchShowtimes,
  }
}
