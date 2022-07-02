package com.zattoo.movies.di

import com.zattoo.movies.data.remote.MovieService
import com.zattoo.movies.data.repository.MoviesRepository
import com.zattoo.movies.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        api: MovieService,
        @DispatcherModule.IoDispatcher ioDispatcher: CoroutineDispatcher
    ): MoviesRepository = MovieRepositoryImpl(api, ioDispatcher)
}