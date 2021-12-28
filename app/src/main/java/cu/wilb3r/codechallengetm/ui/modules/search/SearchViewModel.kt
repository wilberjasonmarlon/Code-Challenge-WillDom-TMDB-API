package cu.wilb3r.codechallengetm.ui.modules.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResult
import cu.wilb3r.codechallengetm.domain.usecases.SearchAnyUseCase
import cu.wilb3r.codechallengetm.ui.base.BaseViewModel
import cu.wilb3r.codechallengetm.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val useCase: SearchAnyUseCase
) : BaseViewModel() {

    private val _result = MutableLiveData<Event<List<SearchResult>>>()
    val result: LiveData<Event<List<SearchResult>>>
        get() = _result

    fun searchAny(s: String) = viewModelScope.launch {
        useCase(s).collectLatest {
            it.data?.let { data ->
                _result.postValue(Event(data))
            }
        }
    }
}