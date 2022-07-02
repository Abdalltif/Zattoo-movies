package com.zattoo.movies.ui.home

import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.utils.UIState

data class HomeState(
    val uiState: UIState = UIState.LOADING,
    val movies: List<Movie>? = emptyList(),
    val message: String? = null
)