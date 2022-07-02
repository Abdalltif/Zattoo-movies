package com.zattoo.movies.utils

import com.zattoo.movies.data.model.MoviesDataResponse
import com.zattoo.movies.data.model.MoviesOffersResponse
import com.zattoo.movies.data.model.Currency
import com.zattoo.movies.data.model.Image
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.data.model.Price

fun createMovies(
    moviesDataResponse: MoviesDataResponse,
    moviesOffersResponse: MoviesOffersResponse
): List<Movie> {
    return moviesOffersResponse.offers.mapNotNull { offers ->
        val details = moviesDataResponse.movie_data.find { it.movie_id == offers.movie_id }
        details?.let {
            val movieOfferPrice = offers.price
            val currency = Currency(movieOfferPrice.last().toString())
            val price = movieOfferPrice.substring(0 until movieOfferPrice.length - 1).toFloat()
            Movie(
                id = it.movie_id,
                title = it.title,
                subtitle = it.sub_title,
                price = Price(price, currency),
                image = Image(moviesOffersResponse.image_base + offers.image),
                availability = offers.available
            )
        }
    }
}