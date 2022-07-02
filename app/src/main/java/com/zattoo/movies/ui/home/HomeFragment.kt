package com.zattoo.movies.ui.home

import android.animation.Animator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.zattoo.movies.R
import com.zattoo.movies.databinding.FragmentHomeBinding
import com.zattoo.movies.utils.*
import com.zattoo.movies.utils.Constants.ANIMATION_DURATION
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private val homeAdapter by lazy { HomeAdapter() }

    @Inject
    lateinit var networkUtils: NetworkUtils

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setupRecyclerView()
        setupPullToRefresh()
        setupStateObserver()

        return binding.root
    }

    private fun setupStateObserver() {
        viewModel.uiState.observe(viewLifecycleOwner) { homeState ->
            when (homeState.uiState) {
                UIState.LOADING -> handleLoading()
                UIState.MOVIES_DATA -> handleResults()
                UIState.ERROR -> handleError(homeState.message!!)
            }
        }
    }

    private fun setupPullToRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.fetchMovies()
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.apply {
            adapter = homeAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun handleLoading() {
        showLoading(true)
    }

    private fun handleResults() {
        showLoading(false)
        showConnectedStatus()
        binding.recyclerView.visibility = View.VISIBLE
        binding.emptyView.visibility = View.GONE
    }

    private fun showConnectedStatus() {
        binding.textViewNetworkStatus.text = getString(R.string.text_connectivity)
        binding.networkStatusLayout.setBackgroundColor(ResourcesCompat.getColor(
            resources, R.color.colorStatusConnected, null
        ))

        binding.networkStatusLayout.animate().alpha(1f)
            .setStartDelay(ANIMATION_DURATION)
            .setDuration(ANIMATION_DURATION)
            .setListener( object : Animator.AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    binding.networkStatusLayout.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationRepeat(p0: Animator?) {
                }
            })
    }

    private fun showNotConnectedStatus() {
        binding.textViewNetworkStatus.text = getString(R.string.text_no_connectivity)
        binding.networkStatusLayout.visibility = View.VISIBLE
        binding.networkStatusLayout.setBackgroundColor(ResourcesCompat.getColor(
            resources,
            R.color.colorStatusNotConnected,
            null
        ))
    }

    private fun handleError(message: String) {
        showLoading(false)
        showNotConnectedStatus()
        if (homeAdapter.itemCount <= 0)
            showEmptyListError(message)
        else
            showErrorToast(message)
    }

    private fun showErrorToast(message:String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    private fun showEmptyListError(message:String) {
        binding.recyclerView.visibility = View.GONE
        binding.emptyView.visibility = View.VISIBLE
        binding.emptyView.text = message
    }

    private fun showLoading( isLoading: Boolean) {
        binding.swipeRefreshLayout.isRefreshing = isLoading
    }
}
