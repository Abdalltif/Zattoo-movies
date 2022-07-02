package com.zattoo.movies.utils

import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.ui.home.HomeAdapter
import com.zattoo.movies.ui.home.HomeState

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("loadImage")
    fun bindLoadImage(view: AppCompatImageView, url: String?) {
        if (url != null) {
            Glide.with(view.context).load(url)
                .into(view)
        }
    }

    @JvmStatic
    @BindingAdapter("state")
    fun setRecyclerViewProperties(recyclerView: RecyclerView, state: HomeState?) {
        if (recyclerView.adapter is HomeAdapter) {
            val adapter = recyclerView.adapter as HomeAdapter
            state?.let { homeState ->
                when (homeState.uiState) {
                    UIState.MOVIES_DATA -> adapter.setList(homeState.movies as List<Movie>)
                }
            }
        }
    }
}