package com.zattoo.movies.data.model

data class MoviesDataResponse(
    val movie_data: List<MovieData>
) {
    data class MovieData(
        val movie_id: Int,
        val sub_title: String,
        val title: String
    )
}
