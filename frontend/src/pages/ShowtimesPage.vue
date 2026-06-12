<template>
  <div class="container mx-auto px-16 py-8">
    <h1 class="text-display font-bold text-text-primary mb-6">Lịch chiếu phim</h1>

    <ShowtimeFilters :cities="cities" :cinemas="filteredCinemas" :selectedCity="selectedCity"
      :selectedCinemaId="selectedCinemaId" :selectedDate="selectedDate" @update:selectedCity="selectedCity = $event"
      @update:selectedCinemaId="selectedCinemaId = $event" @update:selectedDate="selectedDate = $event" class="mb-8" />

    <div class="space-y-8">
      <template v-if="loading">
        <Skeleton :blocks="showtimeListSkeleton(5)" />
      </template>

      <template v-else-if="groupedShowtimes.length === 0">
        <div class="text-center py-12 text-text-secondary">
          Không có suất chiếu nào.
        </div>
      </template>

      <template v-else>
        <div v-for="group in groupedShowtimes" :key="group.date" class="space-y-4">
          <h2 class="text-title font-semibold text-text-primary border-b border-border-default pb-2">
            {{ formatDateHeader(group.date) }}
          </h2>
          <ShowtimeCard v-for="(g, idx) in group.groups" :key="idx" :showtimes="g.showtimes"
            :movie="getMovieById(g.showtimes[0].movieId)" :cinema="getCinemaById(g.showtimes[0].cinemaId)"
            :get-format-by-id="getFormatById" @book="handleBook" />
        </div>
      </template>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import ShowtimeFilters from '@/components/showtime/ShowtimeFilters.vue'
import ShowtimeCard from '@/components/showtime/ShowtimeCard.vue'
import Skeleton from '@/components/ui/skeleton/Skeleton.vue'
import { useShowtimes } from '@/composables/useShowtimes'
import { useAuthStore } from '@/stores/auth.store'
import { showtimeListSkeleton } from '@/skeletons/showtime.skeleton'
import type { CreateBookingRequest } from '@/types/booking.types'
import type { ShowtimeResponse } from '@/types/showtime'
import { formatDateHeaderVN, getDateKeyVN } from '@/utils/dateFormat'
import { watch } from 'vue'

const {
  showtimes,
  loading,
  cities,
  filteredCinemas,
  selectedCity,
  selectedCinemaId,
  selectedDate,
  getCinemaById,
  getMovieById,
  getFormatById,
} = useShowtimes()

const authStore = useAuthStore()
const router = useRouter()
const route = useRoute()

let updatingFromUrl = false

// 1. Khi URL query thay đổi → cập nhật selectedDate
watch(
  () => route.query.date,
  (newDate) => {
    if (updatingFromUrl) return
    updatingFromUrl = true
    const dateValue = typeof newDate === 'string' ? newDate : ''
    if (dateValue && /^\d{4}-\d{2}-\d{2}$/.test(dateValue)) {
      if (selectedDate.value !== dateValue) {
        selectedDate.value = dateValue
      }
    } else if (!dateValue && selectedDate.value !== '') {
      selectedDate.value = ''
    }
    updatingFromUrl = false
  },
  { immediate: true }
)

// 2. Khi selectedDate thay đổi → cập nhật URL
watch(
  () => selectedDate.value,
  (newDate) => {
    if (updatingFromUrl) return
    updatingFromUrl = true
    const currentQuery = { ...route.query }
    if (newDate && newDate.trim() !== '') {
      router.replace({ query: { ...currentQuery, date: newDate } })
    } else {
      const { date, ...restQuery } = currentQuery
      router.replace({ query: restQuery })
    }
    updatingFromUrl = false
  }
)

const groupedShowtimes = computed(() => {
  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const dateMap = new Map<string, Map<string, ShowtimeResponse[]>>()

  showtimes.value.forEach(showtime => {
    const startInstant = showtime.startTime as string
    const startDateVN = new Date(startInstant)

    if (isNaN(startDateVN.getTime())) {
      console.error('Invalid startTime:', startInstant)
      return
    }

    if (startDateVN < today) return

    const dateKey = getDateKeyVN(startInstant)

    const groupKey = [
      showtime.movieId,
      showtime.cinemaId,
    ].join('|')

    if (!dateMap.has(dateKey)) dateMap.set(dateKey, new Map())
    const dayMap = dateMap.get(dateKey)!
    if (!dayMap.has(groupKey)) dayMap.set(groupKey, [])
    dayMap.get(groupKey)!.push(showtime)
  })

  return Array.from(dateMap.keys())
    .sort()
    .map(date => ({
      date,
      groups: Array.from(dateMap.get(date)!.values()).map(list => ({
        showtimes: list.sort(
          (a, b) => new Date(a.startTime).getTime() - new Date(b.startTime).getTime()
        ),
      })),
    }))
})

const formatDateHeader = (dateStr: string) => formatDateHeaderVN(dateStr)

const handleBook = (showtimeId: number) => {
  if (!authStore.user) {
    alert('Vui lòng đăng nhập để đặt vé')
    return
  }
  const bookingRequest: CreateBookingRequest = {
    userId: authStore.user.id,
    showtimeId,
    showTimeSeatIds: [],
    couponCode: null,
    combos: []
  }
  sessionStorage.setItem('tempBooking', JSON.stringify(bookingRequest))
  router.push({ name: 'booking-seats', params: { showtimeId } })
}
</script>