package cu.wilb3r.codechallengetm.ui.modules.movies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.mapper.toMedia
import cu.wilb3r.codechallengetm.databinding.FragmentMoviesBinding
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.codechallengetm.ef.hide
import cu.wilb3r.codechallengetm.ef.show
import cu.wilb3r.codechallengetm.ui.base.BaseFragment
import cu.wilb3r.codechallengetm.ui.customs.OscillatingScrollListener
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.CategoryType
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.LoadingAdapter
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.MoviePageAdapter
import cu.wilb3r.codechallengetm.ui.modules.movies.detail.DetailFragment
import cu.wilb3r.codechallengetm.ui.modules.movies.detail.DetailFragment.Companion.MEDIA_EXTRA
import cu.wilb3r.codechallengetm.utils.StartSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment @Inject constructor() : BaseFragment<FragmentMoviesBinding>() {

    @Inject
    lateinit var popularAdapter: MoviePageAdapter

    @Inject
    lateinit var topRateAdapter: MoviePageAdapter

    @Inject
    lateinit var detailFragment: DetailFragment

    private val vm by viewModels<MoviesViewModel>()

    private var loadPopularJob: Job? = null
    private var loadTopJob: Job? = null

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentMoviesBinding.inflate(layoutInflater, container, false)

    override fun initView() {
        binding.shimmer.showShimmer(true)
        initRecyclerView()
        loadData()
    }


    private fun loadData() {
        loadPopularJob?.cancel()
        loadTopJob?.cancel()
        loadPopularJob = lifecycleScope.launch {
            popularAdapter.submitData(PagingData.from(DBMovie.LOADING_LIST))
            vm.getPopularMovies().collectLatest { pagingData ->
                popularAdapter.submitData(pagingData.map { it.movie })
            }
        }
        loadTopJob = lifecycleScope.launch {
            topRateAdapter.submitData(PagingData.from(DBMovie.LOADING_LIST))
            vm.getTop().collectLatest { pagingData ->
                topRateAdapter.submitData(pagingData.map { it.movie })
            }
        }
    }

    private fun initRecyclerView() {

        binding.recyclerPopular.apply {
            StartSnapHelper().attachToRecyclerView(this)
            layoutManager = LinearLayoutManager(requireContext()).apply {
                reverseLayout = false
                orientation = LinearLayoutManager.HORIZONTAL
            }
            adapter = popularAdapter.apply {

                addLoadStateListener { loadState ->
                    parseLoadState(loadState)
                    setUpClickListener(loadState.source.refresh !is LoadState.Loading)
                }
            }
            postponeEnterTransition()
            doOnPreDraw {
                startPostponedEnterTransition()
            }
        }

        binding.recyclerTopRated.apply {
            StartSnapHelper().attachToRecyclerView(this)
            layoutManager = GridLayoutManager(requireContext(), 1).apply {
                reverseLayout = false
                orientation = GridLayoutManager.VERTICAL
            }
            addOnScrollListener(
                OscillatingScrollListener(10)
            )
            setHasFixedSize(true)
            adapter = topRateAdapter.apply {
                withLoadStateFooter(footer = LoadingAdapter { topRateAdapter.retry() })
                setCategory(CategoryType.TOP_RATED)
                setUpClickListener(true)
                addLoadStateListener { loadState ->
                    // show empty list
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && topRateAdapter.itemCount == 0
                    //showEmptyList(isListEmpty)
                    //setUpClickListener(loadState.source.refresh !is LoadState.Loading)
                    parseLoadState(loadState)
                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorState?.let {
                        Toast.makeText(
                            requireContext(),
                            "\uD83D\uDE28 Wooops ${it.error}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            postponeEnterTransition()
            doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    private fun parseLoadState(loadState: CombinedLoadStates) {
        if (popularAdapter.itemCount == 0) {
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    shimmerView()
                    println("LoadStateLoading ${loadState.refresh}, Items = ${popularAdapter.itemCount}")
                }
                is LoadState.Error -> {
                    println("LoadStateError ${loadState.refresh}, Items = ${popularAdapter.itemCount}")
                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    errorView(errorState)
                }
                is LoadState.NotLoading -> {

                }
            }
        } else {
            dataView()
        }
    }

    private fun shimmerView() {
        binding.shimmer.show()
        binding.clyData.hide()
    }

    private fun errorView(e: LoadState.Error? = null) {
        binding.shimmer.hide()
        binding.clyData.hide()
        binding.shimmer.hide()
        binding.includedEmptyView.emptyView.show()
        e?.let {
            binding.includedEmptyView.txtMsg.text = it.error.toString()
        }
    }

    private fun dataView() {
        binding.shimmer.hide()
        binding.includedEmptyView.emptyView.hide()
        binding.clyData.show()
    }

    private fun setUpClickListener(active: Boolean) {
        binding.includedEmptyView.btnRetry.setOnClickListener {
            onRetry()
        }
        if (active) {
            popularAdapter.setOnItemClickListener { _, item, _ ->
                loadPopularJob?.cancel()
                navigateToDetailFragment(item.toMedia())
            }
            topRateAdapter.setOnItemClickListener { _, item, _ ->
                loadTopJob?.cancel()
                navigateToDetailFragment(item.toMedia())
            }
        } else {
            popularAdapter.setOnItemClickListener { _, _, _ ->
                null
            }
            topRateAdapter.setOnItemClickListener { _, _, _ ->
                null
            }
        }
    }

    private fun navigateToDetailFragment(item: Media) {
        val bundle = Bundle()
        bundle.putParcelable(MEDIA_EXTRA, item)
        try {
            if (!detailFragment.isAdded)
                detailFragment.apply {
                    arguments = bundle
                }.show(childFragmentManager, DetailFragment::class.java.name)
        } catch (ex: Exception) {

        }
    }

    override fun onPause() {
        super.onPause()
        loadPopularJob?.cancel()
        loadTopJob?.cancel()
    }

    private fun onRetry() {
        loadData()
    }

}