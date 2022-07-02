package com.zattoo.movies.data.repository

import com.google.common.truth.Truth.assertThat
import com.zattoo.movies.core.movieOffersResponseMockProvider
import com.zattoo.movies.core.moviesResponseMockProvider
import com.zattoo.movies.data.model.MovieListEntity
import com.zattoo.movies.data.remote.MovieService
import com.zattoo.movies.utils.Resource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class MovieRepositoryImplTest {

    private lateinit var repository: MovieRepositoryImpl
    private var movieService = mockk<MovieService>()
    private val dispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        repository = MovieRepositoryImpl(movieService, dispatcher)
    }

    @Test
    fun `fetch movies successfully by given mock data`() = runTest {
        coEvery { movieService.fetchMovieList() } returns moviesResponseMockProvider()
        coEvery { movieService.fetchMovieListOffers() } returns movieOffersResponseMockProvider()

        val result = repository.fetchMovies()

        coVerify {
            movieService.fetchMovieList()
            movieService.fetchMovieListOffers()
        }

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data).isNotNull()
    }

    @Test
    fun `fetch movies should return two items`() = runTest {
        coEvery { movieService.fetchMovieList() } returns moviesResponseMockProvider()
        coEvery { movieService.fetchMovieListOffers() } returns movieOffersResponseMockProvider()

        val result = repository.fetchMovies()

        coVerify {
            movieService.fetchMovieList()
            movieService.fetchMovieListOffers()
        }

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data?.size).isEqualTo(2)
    }

    @Test
    fun `fetch movies should return empty list`() = runTest {
        coEvery { movieService.fetchMovieList() } returns MovieListEntity(emptyList())
        coEvery { movieService.fetchMovieListOffers() } returns movieOffersResponseMockProvider()

        val result = repository.fetchMovies()

        coVerify {
            movieService.fetchMovieList()
            movieService.fetchMovieListOffers()
        }

        assertThat(result).isInstanceOf(Resource.Success::class.java)
        assertThat(result.data?.size).isEqualTo(0)
    }

    @Test
    fun `fetch movies should return error with message`() = runTest {
        coEvery { movieService.fetchMovieList() } throws IOException("Network error!")
        coEvery { movieService.fetchMovieListOffers() } returns movieOffersResponseMockProvider()

        val result = repository.fetchMovies()

        coVerify {
            movieService.fetchMovieList()
        }

        assertThat(result).isInstanceOf(Resource.Error::class.java)
        assertThat(result.message).isEqualTo("Network error!")
        assertThat(result.data).isNull()
    }
}