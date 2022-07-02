package com.zattoo.movies.data.repository

import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.utils.Resource

interface MoviesRepository {
    suspend fun fetchMovies() : Resource<List<Movie>>
}