package cu.wilb3r.codechallengetm.ui.modules.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import cu.wilb3r.codechallengetm.data.local.mapper.toMedia
import cu.wilb3r.codechallengetm.databinding.FragmentSearchBinding
import cu.wilb3r.codechallengetm.ui.base.BaseFragment
import cu.wilb3r.codechallengetm.ui.modules.search.adapter.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment @Inject constructor() : BaseFragment<FragmentSearchBinding>() {

    @Inject
    lateinit var searchAdapter: SearchAdapter

    private val vm by viewModels<SearchViewModel>()
    private var searchJob: Job? = null

    private val listener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                search(it)
                if (it.isEmpty())
                    searchAdapter.addItems(emptyList())
            }
            return false
        }
    }

    override fun getBindingView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentSearchBinding.inflate(layoutInflater, container, false)

    override fun initView() {
        super.initView()
        initReciclerView()
        binding.searchField.setOnQueryTextListener(listener)
        binding.searchField.isIconified = false
        binding.searchField.requestFocus()
    }

    private fun initReciclerView() {
        binding.recycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2).apply {
                orientation = GridLayoutManager.VERTICAL
            }
            adapter = searchAdapter.apply {
                setOnItemClickListener { searchResult, i ->
                    findNavController().navigate(
                        SearchFragmentDirections.actionSearchFragmentToDetailNormalFragment(
                            searchResult.toMedia()
                        )
                    )
                }
            }
        }
    }

    private fun search(s: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            vm.searchAny(s)
            vm.result.observe(viewLifecycleOwner, { data ->
                data?.let { event ->
                    event.getContentIfNotHandled()?.let {
                        searchAdapter.addItems(it)
                    }
                }
            })
        }
    }

    override fun onPause() {
        super.onPause()
        searchJob?.cancel()
    }
}