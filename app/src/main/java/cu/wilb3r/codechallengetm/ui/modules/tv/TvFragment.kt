package cu.wilb3r.codechallengetm.ui.modules.tv

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
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.local.mapper.toMedia
import cu.wilb3r.codechallengetm.databinding.FragmentTvBinding
import cu.wilb3r.codechallengetm.ui.base.BaseFragment
import cu.wilb3r.codechallengetm.ui.customs.OscillatingScrollListener
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.CategoryType
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.LoadingAdapter
import cu.wilb3r.codechallengetm.ui.modules.tv.adapter.TvPageAdapter
import cu.wilb3r.codechallengetm.utils.StartSnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TvFragment @Inject constructor() : BaseFragment<FragmentTvBinding>() {

    @Inject
    lateinit var popularAdapter: TvPageAdapter

    @Inject
    lateinit var topAdapter: TvPageAdapter

    private var popularRvState: Parcelable? = null
    private var topRatedRvState: Parcelable? = null

    private val vm by viewModels<TvViewModel>()


    private var loadPopularJob: Job? = null
    private var loadTopJob: Job? = null

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentTvBinding.inflate(layoutInflater, container, false)

    override fun initTransition() {
        super.initTransition()
        postponeEnterTransition()
    }

    override fun initView() {
        initRecyclerView()
        loadData()
    }

    private fun loadData() {
        loadPopularJob?.cancel()
        loadTopJob?.cancel()
        loadPopularJob = lifecycleScope.launch {
            popularAdapter.submitData(PagingData.from(DBTv.LOADING_LIST))
            vm.getPopularTv().collectLatest { pagingData ->
                binding.recyclerPopular.layoutManager?.onRestoreInstanceState(popularRvState).also {
                    popularAdapter.submitData(pagingData.map { it.tv })
                }
            }
        }
        loadTopJob = lifecycleScope.launch {
            topAdapter.submitData(PagingData.from(DBTv.LOADING_LIST))
            vm.getTopTv().collectLatest { pagingData ->
                binding.recyclerTopRated.layoutManager?.onRestoreInstanceState(topRatedRvState)
                    .also {
                        topAdapter.submitData(pagingData.map { it.tv })
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
            adapter = popularAdapter.withLoadStateHeaderAndFooter(
                header = LoadingAdapter { popularAdapter.retry() },
                footer = LoadingAdapter { popularAdapter.retry() }
            )
            popularAdapter.addLoadStateListener { loadState ->
                // show empty list
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && popularAdapter.itemCount == 0
                //showEmptyList(isListEmpty)
                setUpClickListener(loadState.source.refresh !is LoadState.Loading)
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
            adapter = topAdapter.apply {
                withLoadStateFooter(footer = LoadingAdapter { topAdapter.retry() })
                setCategory(CategoryType.TOP_RATED)
                setUpClickListener(true)
                addLoadStateListener { loadState ->
                    // show empty list
                    val isListEmpty =
                        loadState.refresh is LoadState.NotLoading && topAdapter.itemCount == 0
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
                    //shimmerView()
                    println("LoadStateLoading ${loadState.refresh}, Items = ${popularAdapter.itemCount}")
                }
                is LoadState.Error -> {
                    println("LoadStateError ${loadState.refresh}, Items = ${popularAdapter.itemCount}")
                    val errorState = loadState.source.append as? LoadState.Error
                        ?: loadState.source.prepend as? LoadState.Error
                        ?: loadState.append as? LoadState.Error
                        ?: loadState.prepend as? LoadState.Error
                    //errorView(errorState)
                }
                is LoadState.NotLoading -> {

                }
            }
        } else {
            //dataView()
        }
    }

    private fun setUpClickListener(active: Boolean) {
        if (active) {
            popularAdapter.setOnItemClickListener { view, title, item, _ ->
                loadPopularJob?.cancel()
                val extras = FragmentNavigatorExtras(
                    view to item.poster_path.toString(),
                    title to item.name.toString()
                )
                findNavController().navigate(
                    TvFragmentDirections.actionTvFragmentToDetailNormalFragment(
                        item.toMedia()
                    ), extras
                )
            }
            topAdapter.setOnItemClickListener { view, title, item, _ ->
                val extras = FragmentNavigatorExtras(
                    view to item.poster_path.toString(),
                    title to item.name.toString()
                )
                findNavController().navigate(
                    TvFragmentDirections.actionTvFragmentToDetailNormalFragment(
                        item.toMedia()
                    ), extras
                )
            }
        } else {
            popularAdapter.setOnItemClickListener { _, _, _, _ ->

            }
            topAdapter.setOnItemClickListener { _, _, _, _ ->

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