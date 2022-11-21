package com.ironsource.loginsystem.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.google.android.material.snackbar.Snackbar
import com.ironsource.loginsystem.R
import com.ironsource.loginsystem.data.models.Post
import com.ironsource.loginsystem.databinding.FragmentHomeBinding
import com.ironsource.loginsystem.ui.details.PostDetailsFragmentArgs
import com.ironsource.loginsystem.ui.details.PostDetailsFragmentDirections
import com.ironsource.loginsystem.util.ext.collectLatestSafeLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var homeViewModel: HomeViewModel

    private val adapter = PostAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        initMembers()
        setListeners()
        collectPostPagingFlow()
        collectAdapterLoadStateFlow()
    }

    private fun setListeners() {
        adapter.setOnPostClickListener(object : PostAdapter.OnPostClickListener {
            override fun onPostClick(post: Post) {
                val destination = HomeFragmentDirections.actionHomeFragmentToPostDetailsFragment(
                    post.id
                )
                findNavController().navigate(destination)
            }
        })
    }

    private fun initMembers() {
        binding.postsRecyclerView.postListRecyclerView.adapter = adapter
    }

    private fun collectPostPagingFlow() {
        collectLatestSafeLifecycle(
            homeViewModel.postPagingData
        ) {
            adapter.submitData(pagingData = it)
        }
    }

    private fun collectAdapterLoadStateFlow() {
        lifecycleScope.launch {
            adapter.loadStateFlow.collect { loadState ->

                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && adapter.itemCount == 0

                binding.postsRecyclerView.apply {
                    // show empty list
                    postListEmptyTextView.isVisible = isListEmpty
                    // Only show the list if refresh succeeds, either from the the local db or the remote.
                    postListRecyclerView.isVisible =
                        loadState.source.refresh is LoadState.NotLoading || loadState.mediator?.refresh is LoadState.NotLoading
                    // Show loading spinner during initial load or refresh.
                    postListProgressBar.isVisible =
                        loadState.mediator?.refresh is LoadState.Loading
                    // Show the retry state if initial load or refresh fails.
                    retryButton.isVisible =
                        loadState.mediator?.refresh is LoadState.Error && adapter.itemCount == 0

                }

                handleError(loadState)
            }
        }
    }

    private suspend fun handleError(loadState: CombinedLoadStates) {
        val errorState = loadState.source.append as? LoadState.Error
            ?: loadState.source.prepend as? LoadState.Error
            ?: loadState.source.refresh as? LoadState.Error
            ?: loadState.append as? LoadState.Error
            ?: loadState.prepend as? LoadState.Error
            ?: loadState.refresh as? LoadState.Error

        if (errorState is LoadState.Error) {
            withContext(Dispatchers.Main) {
                val message = when (errorState.error) {
                    is IOException -> getString(R.string.general_error)
                    is HttpException -> getString(R.string.network_error)
                    else -> getString(R.string.general_error)
                }

                Snackbar.make(
                    binding.root,
                    getString(
                        R.string.error_message,
                        message
                    ),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}