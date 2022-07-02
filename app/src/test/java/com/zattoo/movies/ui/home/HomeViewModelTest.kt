package com.zattoo.movies.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.zattoo.movies.core.movieListMockProvider
import com.zattoo.movies.data.repository.MoviesRepository
import com.zattoo.movies.utils.Resource
import com.zattoo.movies.utils.UIState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val movieRepository: MoviesRepository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @Test
    fun `loading state when fetching data`() = runTest {
        viewModel = HomeViewModel(movieRepository)

        Truth.assertThat(viewModel.uiState.value).isEqualTo(
            HomeState(uiState = UIState.LOADING)
        )
    }

    @Test
    fun `fetch movies successfully and update state`() = runTest {
        coEvery { movieRepository.fetchMovies() } returns Resource.Success(data = movieListMockProvider())

        viewModel = HomeViewModel(movieRepository)

        coVerify { movieRepository.fetchMovies() }

        Truth.assertThat(viewModel.uiState.value?.uiState).isEqualTo(UIState.MOVIES_DATA)
    }

    @Test
    fun `fetch data with error message and update state`() = runTest {
        coEvery { movieRepository.fetchMovies() } returns Resource.Error(message = "Network error!")

        viewModel = HomeViewModel(movieRepository)

        coVerify { movieRepository.fetchMovies() }

        Truth.assertThat(viewModel.uiState.value?.message).isNotNull()
        Truth.assertThat(viewModel.uiState.value?.uiState).isEqualTo(UIState.ERROR)
    }

    @Test
    fun `fetch data with an empty result`() {
        coEvery { movieRepository.fetchMovies() } returns Resource.Success(data = emptyList())

        viewModel = HomeViewModel(movieRepository)

        coVerify { movieRepository.fetchMovies() }

        Truth.assertThat(viewModel.uiState.value?.movies).isEmpty()
    }
}