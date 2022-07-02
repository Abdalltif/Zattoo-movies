package com.zattoo.movies.core

import com.zattoo.movies.data.model.*
import io.mockk.every
import io.mockk.mockk
import kotlin.random.Random

fun movieOffersResponseMockProvider() = mockk<MovieListOffers> {
    every { image_base } returns "na"
    every { offers } returns listOf(
        mockk {
            every { available } returns true
            every { image } returns "na"
            every { movie_id } returns 1
            every { price } returns "33£"
        },
        mockk {
            every { available } returns true
            every { image } returns "na"
            every { movie_id } returns 2
            every { price } returns "27£"
        },
        mockk {
            every { available } returns true
            every { image } returns "na"
            every { movie_id } returns 32
            every { price } returns "33£"
        }
    )
}

fun moviesResponseMockProvider() = mockk<MovieListEntity> {
    every { movie_data } returns listOf(
        mockk {
            every { movie_id } returns 1
            every { title } returns "title 1"
            every { sub_title } returns "subtitle 1"
        },
        mockk {
            every { movie_id } returns 2
            every { title } returns "title 2"
            every { sub_title } returns "subtitle 2"
        },
        mockk {
            every { movie_id } returns 52
            every { title } returns "title 52"
            every { sub_title } returns "subtitle 52"
        }
    )
}

private fun getMockMovie(id: Int = 2) =
    Movie(
        id = id,
        title = "Title $id",
        subtitle = "Subtitle $id",
        price = Price(32f, Currency("£")),
        image = Image("url"),
        availability = Random.nextBoolean()
    )

fun movieListMockProvider() =
    (1..5).map {
        getMockMovie(id = it)
    }