package com.zattoo.movies.data.remote

import com.zattoo.movies.data.model.MoviesDataResponse
import com.zattoo.movies.data.model.MoviesOffersResponse
import retrofit2.http.GET

interface MovieService {
    @GET("movie_offers.json")
    suspend fun fetchMovieListOffers(): MoviesOffersResponse

    @GET("movie_data.json")
    suspend fun fetchMovieList(): MoviesDataResponse
}