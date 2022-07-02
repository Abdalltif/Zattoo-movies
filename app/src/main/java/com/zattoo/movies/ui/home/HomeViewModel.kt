package com.zattoo.movies.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zattoo.movies.data.repository.MoviesRepository
import com.zattoo.movies.utils.Resource
import com.zattoo.movies.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<HomeState>()
    val uiState: LiveData<HomeState> = _uiState

    init {
        fetchMovies()
    }

    fun fetchMovies() {
        _uiState.value = HomeState(
            uiState = UIState.LOADING
        )

        viewModelScope.launch {
            val resource = moviesRepository.fetchMovies()
            _uiState.value = when (resource) {
                is Resource.Success -> {
                    HomeState(
                        uiState = UIState.MOVIES_DATA,
                        movies = resource.data
                    )
                }
                is Resource.Error -> {
                    HomeState(
                        uiState = UIState.ERROR,
                        message = resource.message
                    )
                }
            }
        }
    }
}