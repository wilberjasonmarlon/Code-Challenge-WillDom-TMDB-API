package cu.wilb3r.codechallengetm.ui.modules.movies

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.mapper.toMedia
import cu.wilb3r.codechallengetm.databinding.FragmentMoviesBinding
import cu.wilb3r.codechallengetm.ef.hide
import cu.wilb3r.codechallengetm.ef.show
import cu.wilb3r.codechallengetm.ui.base.BaseFragment
import cu.wilb3r.codechallengetm.ui.customs.OscillatingScrollListener
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.CategoryType
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.LoadingAdapter
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.MoviePageAdapter
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

    private var popularRvState: Parcelable? = null
    private var topRatedRvState: Parcelable? = null

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

    override fun initTransition() {
        super.initTransition()
        postponeEnterTransition()
    }


    private fun loadData() {
        loadPopularJob?.cancel()
        loadTopJob?.cancel()
        loadPopularJob = lifecycleScope.launch {
            popularAdapter.submitData(PagingData.from(DBMovie.LOADING_LIST))
            vm.getPopularMovies().collectLatest { pagingData ->
                binding.recyclerPopular.layoutManager?.onRestoreInstanceState(popularRvState).also {
                    popularAdapter.submitData(pagingData.map { it.movie })
                }
            }
        }
        loadTopJob = lifecycleScope.launch {
            topRateAdapter.submitData(PagingData.from(DBMovie.LOADING_LIST))
            vm.getTop().collectLatest { pagingData ->
                binding.recyclerTopRated.layoutManager?.onRestoreInstanceState(topRatedRvState)
                    .also {
                        topRateAdapter.submitData(pagingData.map { it.movie })
                    }
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
            popularAdapter.setOnItemClickListener { view, title, item, _ ->
                loadPopularJob?.cancel()
                val extras = FragmentNavigatorExtras(
                    view to item.poster_path.toString(),
                    title to item.title.toString()
                )
                findNavController().navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToDetailNormalFragment(
                        item.toMedia()
                    ), extras
                )
            }
            topRateAdapter.setOnItemClickListener { view, title, item, _ ->
                loadTopJob?.cancel()
                val extras = FragmentNavigatorExtras(
                    view to item.poster_path.toString(),
                    title to item.title.toString()
                )
                findNavController().navigate(
                    MoviesFragmentDirections.actionMoviesFragmentToDetailNormalFragment(
                        item.toMedia()
                    ), extras
                )
            }
        } else {
            popularAdapter.setOnItemClickListener { _, _, _, _ ->
                null
            }
            topRateAdapter.setOnItemClickListener { _, _, _, _ ->
                null
            }
        }
    }

    override fun onPause() {
        super.onPause()
        loadPopularJob?.cancel()
        loadTopJob?.cancel()
        popularRvState = binding.recyclerPopular.layoutManager?.onSaveInstanceState()
        topRatedRvState = binding.recyclerTopRated.layoutManager?.onSaveInstanceState()
    }

    private fun onRetry() {
        loadData()
    }

}