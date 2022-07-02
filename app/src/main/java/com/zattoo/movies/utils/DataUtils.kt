package com.zattoo.movies.utils

import com.zattoo.movies.data.model.MovieListEntity
import com.zattoo.movies.data.model.MovieListOffers
import com.zattoo.movies.data.remote.MovieService
import com.zattoo.movies.data.model.Currency
import com.zattoo.movies.data.model.Image
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.data.model.Price
import kotlinx.coroutines.runBlocking

fun createMovies(
    movieDetails: MovieListEntity,
    movieListOffers: MovieListOffers
): List<Movie> {
    return movieListOffers.offers.mapNotNull { offers ->
        val details = movieDetails.movie_data.find { it.movie_id == offers.movie_id }
        details?.let {
            val movieOfferPrice = offers.price
            val currency = Currency(movieOfferPrice.last().toString())
            val price = movieOfferPrice.substring(0 until movieOfferPrice.length - 1).toFloat()
            Movie(
                id = it.movie_id,
                title = it.title,
                subtitle = it.sub_title,
                price = Price(price, currency),
                image = Image(movieListOffers.image_base + offers.image),
                availability = offers.available
            )
        }
    }
}