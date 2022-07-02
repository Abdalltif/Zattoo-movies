package com.zattoo.movies.data.model

import com.squareup.moshi.Json

data class MoviesOffersResponse(
    val image_base: String,
    @Json(name = "movie_offers")
    val offers: List<MovieOffer>
) {
    data class MovieOffer(
        val available: Boolean,
        val image: String,
        val movie_id: Int,
        val price: String
    )
}
