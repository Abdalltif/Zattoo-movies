package com.zattoo.movies.data.remote

import com.zattoo.movies.data.model.MovieListEntity
import com.zattoo.movies.data.model.MovieListOffers
import retrofit2.Response
import retrofit2.http.GET

interface MovieService {
    @GET("movie_offers.json")
    suspend fun fetchMovieListOffers(): Response<MovieListOffers>

    @GET("movie_data.json")
    suspend fun fetchMovieList(): Response<MovieListEntity>
}