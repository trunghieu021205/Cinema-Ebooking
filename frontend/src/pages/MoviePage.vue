<template>
  <div class="py-8">
    <h1 class="text-3xl font-bold mb-6">
      Danh sách phim
    </h1>

    <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      <div
        v-for="movie in movies"
        :key="movie.id"
        class="bg-zinc-900 rounded-xl overflow-hidden shadow-lg hover:scale-[1.02] transition-transform"
      >
        <img
          :src="movie.posterUrl"
          :alt="movie.title"
          class="w-full h-64 object-cover"
        >

        <div class="p-4">
          <h2 class="text-lg font-semibold mb-2">
            {{ movie.title }}
          </h2>

          <p class="text-sm text-zinc-400 mb-1">
            ⏱ Thời lượng: <span class="text-white">{{ movie.duration }} phút</span>
          </p>

          <p class="text-sm text-zinc-400 mb-1">
            🔞 Độ tuổi: <span class="text-white">{{ movie.ageRating }}</span>
          </p>

          <p class="text-sm text-zinc-400 mb-3">
            📅 Khởi chiếu: <span class="text-white">{{ movie.releaseDate }}</span>
          </p>

          <span
            class="inline-block px-3 py-1 text-xs font-semibold rounded-full"
            :class="
              movie.status === MovieStatus.NOW_SHOWING
                ? 'bg-green-600 text-white'
                : movie.status === MovieStatus.COMING_SOON
                  ? 'bg-yellow-500 text-black'
                  : 'bg-red-600 text-white'
            "
          >
            {{ movie.status }}
          </span>

          <button
            class="mt-4 w-full bg-red-600 hover:bg-red-700 transition-colors py-2 rounded-lg font-semibold"
          >
            Xem chi tiết
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Movie } from '@/types/movie'
import { AgeRating, MovieStatus } from '@/types/movie'

const movies: Movie[] = [
  {
    id: '1',
    title: 'Avengers: Endgame',
    description: 'Marvel blockbuster movie',
    duration: 181,
    ageRating: AgeRating.T13,
    releaseDate: '2026-01-01',
    status: MovieStatus.NOW_SHOWING,
    posterUrl: 'https://i.imgur.com/9XqJm2R.jpeg',
    bannerUrl: '',
    director: 'Anthony Russo',
    actors: 'Robert Downey Jr.',
    genres: [{ id: '1', name: 'Action' }],
    rating: 4.8,
    ratingCount: 1200,
  },
  {
    id: '2',
    title: 'Spider-Man: No Way Home',
    duration: 148,
    ageRating: AgeRating.T13,
    releaseDate: '2026-02-10',
    status: MovieStatus.COMING_SOON,
    posterUrl: 'https://i.imgur.com/3yYz8fO.jpeg',
    genres: [{ id: '2', name: 'Adventure' }],
    rating: 4.5,
    ratingCount: 800,
  },
  {
    id: '3',
    title: 'The Conjuring 5',
    duration: 120,
    ageRating: AgeRating.T18,
    releaseDate: '2026-03-20',
    status: MovieStatus.ENDED,
    posterUrl: 'https://i.imgur.com/7V4lVjv.jpeg',
    genres: [{ id: '3', name: 'Horror' }],
    rating: 4.1,
    ratingCount: 400,
  },
]
</script>
