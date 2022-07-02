package com.zattoo.movies.utils

import com.google.common.truth.Truth.assertThat
import com.zattoo.movies.core.movieOffersResponseMockProvider
import com.zattoo.movies.core.moviesResponseMockProvider
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.data.model.MovieListEntity
import com.zattoo.movies.data.model.MovieListOffers
import io.mockk.verifyOrder
import org.junit.Test

class DataUtilsKtTest {

    private lateinit var moviesResponse: MovieListEntity
    private lateinit var movieOffersResponse: MovieListOffers

    @Test
    fun `mapResponseToMovieList should return list of movies`() {
        moviesResponse = moviesResponseMockProvider()
        movieOffersResponse = movieOffersResponseMockProvider()

        val result : List<Movie> = createMovies(
            movieDetails = moviesResponse,
            movieListOffers = movieOffersResponse
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
            movieDetails = moviesResponse,
            movieListOffers = movieOffersResponse
        )

        assertThat(result.size).isEqualTo(2)
    }

    @Test
    fun `mapResponseToMovieList should return empty list`() {

        moviesResponse = MovieListEntity(emptyList())
        movieOffersResponse = MovieListOffers("", emptyList())

        val result : List<Movie> = createMovies(
            movieDetails = moviesResponse,
            movieListOffers = movieOffersResponse
        )

        assertThat(result.size).isEqualTo(0)
    }
}