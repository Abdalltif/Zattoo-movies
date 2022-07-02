package com.zattoo.movies.ui.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zattoo.movies.MoviesApp
import com.zattoo.movies.R
import com.zattoo.movies.data.remote.MovieService
import com.zattoo.movies.data.model.Movie
import com.zattoo.movies.databinding.FragmentHomeBinding
import com.zattoo.movies.utils.Constants.ANIMATION_DURATION
import com.zattoo.movies.utils.NetworkUtils
import com.zattoo.movies.utils.createMovies
import com.zattoo.movies.utils.fetchMovieList
import com.zattoo.movies.utils.fetchMovieListOffers
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)  {
    private val adapter by lazy { HomeAdapter() }
    private val viewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var networkUtils: NetworkUtils

    @Inject
    lateinit var movieService: MovieService

    private lateinit var binding: FragmentHomeBinding

    private var success: Result.Success? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUiElements()
    }

    override fun onStart() {
        super.onStart()
        fetchData()
        handleNetwork()
    }

    private fun initUiElements() {
        binding.recyclerView.adapter = adapter
        binding.swipeRefreshLayout.setOnRefreshListener { fetchData() }
    }

    private fun handleLoading() {
        showLoading(true)
    }

    private fun handleResults(result: Result.Success) {
        showLoading(false)
        if (result.movies.isEmpty()) {
            handleError()
        } else {
            success = result
//            adapter.setList(this)
        }
    }

    private fun handleError(error: String) {
        handleError(false, error)
    }

    private fun handleError(isEmptyList: Boolean = true, error: String = "") {
        showLoading(false)
        if (isEmptyList) {
            val errorMessage = getString(R.string.empty_list)
            showEmptyList(errorMessage)
        } else {
            showError(error)
        }
    }

    private fun showEmptyList(message: String) {
        binding.recyclerView.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.emptyView.text = message
    }

    private fun showError(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isLoading
    }

    private fun handleNetwork() {
        networkUtils.getNetworkLiveData().observe(
            this
        ) { isConnected: Boolean? ->
            if (!isConnected!!) {
                binding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
                binding.networkStatusLayout.visibility = View.VISIBLE
                binding.networkStatusLayout.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources,
                        R.color.colorStatusNotConnected,
                        null
                    )
                )
                binding.swipeRefreshLayout.isRefreshing = false
            } else {
                fetchData()
                binding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
                binding.networkStatusLayout.setBackgroundColor(
                    ResourcesCompat.getColor(
                        resources, R.color.colorStatusConnected, null
                    )
                )
                binding.networkStatusLayout.animate().alpha(1f)
                    .setStartDelay(ANIMATION_DURATION)
                    .setDuration(ANIMATION_DURATION)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            binding.networkStatusLayout.visibility = View.GONE
                        }
                    })
            }
        }
    }

    private fun fetchData() {
        FetchDataAsyncTask().execute()
    }

    val movies: List<Movie>
        get() = success!!.movies

    internal inner class FetchDataAsyncTask :
        AsyncTask<Void?, Void?, Result>() {
        override fun onPreExecute() {
            super.onPreExecute()
            handleLoading()
        }

        override fun doInBackground(vararg p0: Void?): Result {
            return try {
                val responseMovieDataList = fetchMovieList(movieService)
                val responseMovieListOffers = fetchMovieListOffers(
                    movieService
                )
                if (responseMovieDataList.movie_data.isNotEmpty() && responseMovieListOffers.offers.isNotEmpty()) {
                    Result.Success(
                        createMovies(
                            responseMovieDataList,
                            responseMovieListOffers
                        )
                    )
                } else {
                    Result.Failure("fetchMovieList or fetchMovieListOffers failed")
                }
            } catch (exception: Exception) {
                Result.Failure(exception.message)
            }
        }

        override fun onPostExecute(result: Result) {
            super.onPostExecute(result)
            if (result is Result.Success) {
                handleResults(result)
            } else if (result is Result.Failure) {
                handleError(result.message)
            }
        }
    }
}