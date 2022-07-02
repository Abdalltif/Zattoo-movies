package com.zattoo.movies.utils

import com.google.common.truth.Truth.assertThat
import com.zattoo.movies.core.movieOffersResponseMockProvider
import com.zattoo.movies.core.moviesResponseMockProvider
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.data.model.MoviesDataResponse
import com.zattoo.movies.data.model.MoviesOffersResponse
import io.mockk.verifyOrder
import org.junit.Test

class DataUtilsKtTest {

    private lateinit var moviesResponse: MoviesDataResponse
    private lateinit var movieOffersResponse: MoviesOffersResponse

    @Test
    fun `mapResponseToMovieList should return list of movies`() {
        moviesResponse = moviesResponseMockProvider()
        movieOffersResponse = movieOffersResponseMockProvider()

        val result : List<Movie> = createMovies(
            moviesDataResponse = moviesResponse,
            moviesOffersResponse = movieOffersResponse
        )

        verifyOrder {
            movieOffersResponse.offers
            moviesResponse.movie_data
        }

        assertThat(result.size).isGreaterThan(0)
    }

    @Test
    fun `mapResponseToMovieList should return list of two items`() {
        moviesResponse = moviesResponseMockProvider()
        movieOffersResponse = movieOffersResponseMockProvider()

        val result : List<Movie> = createMovies(
            moviesDataResponse = moviesResponse,
            moviesOffersResponse = movieOffersResponse
        )

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `mapResponseToMovieList should return empty list`() {

        moviesResponse = MoviesDataResponse(emptyList())
        movieOffersResponse = MoviesOffersResponse("", emptyList())

        val result : List<Movie> = createMovies(
            moviesDataResponse = moviesResponse,
            moviesOffersResponse = movieOffersResponse
        )

        assertThat(result.size).isEqualTo(0)
    }
}