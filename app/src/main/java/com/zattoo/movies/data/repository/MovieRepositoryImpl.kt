package com.zattoo.movies.data.repository

import com.zattoo.movies.data.model.*
import com.zattoo.movies.data.remote.MovieService
import com.zattoo.movies.di.DispatcherModule
import com.zattoo.movies.utils.Resource
import com.zattoo.movies.utils.createMovies
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class MovieRepositoryImpl(
    private val movieService: MovieService,
    @DispatcherModule.IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : MoviesRepository {

    override suspend fun fetchMovies(): Resource<List<Movie>> {
        return try {
            withContext(ioDispatcher) {

                val moviesResponse = async {
                    movieService.fetchMovieList()
                }
                val movieOffersResponse = async {
                    movieService.fetchMovieListOffers()
                }

                val movies = createMovies(moviesResponse.await(), movieOffersResponse.await())
                Resource.Success(movies)

            }
        } catch (exception: HttpException) {
            Resource.Error(exception.localizedMessage ?: "An error occurred!")
        } catch (exception: IOException) {
            Resource.Error(exception.localizedMessage ?: "Network error!")
        }
    }
}