<template>
    <div
        class="flex flex-col sm:flex-row gap-4 p-4 rounded-lg bg-bg-surface shadow-md hover:shadow-lg transition-shadow">
        <!-- Poster -->
        <div class="w-full sm:w-40 shrink-0 mx-auto sm:mx-0">
            <MovieCard v-if="movie" :movie="movie" :hideOverlay="true" />
        </div>

        <!-- Thông tin suất chiếu -->
        <div v-if="showtimes.length" class="flex flex-col gap-2 flex-1 min-w-0">
            <!-- Địa điểm -->
            <div class="flex items-center gap-2 text-caption text-text-secondary">
                <BaseIcon :icon="MapPin" :size="16" />
                <span>{{ cinema?.city || 'Đang cập nhật' }}</span>
            </div>

            <!-- Tên rạp -->
            <div class="flex items-center gap-2 text-body font-medium text-text-primary">
                <span>{{ cinema?.name || 'Đang cập nhật' }}</span>
            </div>

            <!-- Các giờ chiếu (ShowtimePill — format hiển thị trực tiếp trên pill) -->
            <div class="flex items-start gap-4 mt-1">
                <BaseIcon :icon="Clock" :size="16" class="text-text-secondary mt-2 shrink-0" />
                <div class="flex flex-wrap gap-4">
                    <ShowtimePill v-for="st in showtimes" :key="st.id" :showtime-id="st.id"
                        :format-name="getFormatById(st.formatId)?.name ?? '2D'" :start-time="st.startTime"
                        :end-time="st.endTime" @book="handleBook" />
                </div>
            </div>
        </div>

        <!-- Fallback -->
        <div v-else class="flex-1 text-center text-text-secondary italic py-4">
            Không có suất chiếu
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import BaseIcon from '@/components/ui/icon/BaseIcon.vue'
import MovieCard from '@/components/movie/MovieCard.vue'
import ShowtimePill from '@/components/showtime/ShowtimePill.vue'
import { MapPin, Clock } from 'lucide-vue-next'
import type { ShowtimeResponse, ShowtimeFormatResponse } from '@/types/showtime'
import type { MovieResponse } from '@/types/movie'
import type { CinemaResponse } from '@/types/cinema'

const props = defineProps<{
    showtimes: ShowtimeResponse[]
    movie: MovieResponse | undefined
    cinema: CinemaResponse | undefined
    getFormatById: (id: number) => ShowtimeFormatResponse | undefined
}>()

const router = useRouter()

const handleBook = (showtimeId: number) => {
    router.push(`/bookings?showtimeId=${showtimeId}`)
}
</script>