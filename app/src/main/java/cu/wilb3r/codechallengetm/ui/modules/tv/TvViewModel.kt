package cu.wilb3r.codechallengetm.ui.modules.tv

import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import cu.wilb3r.codechallengetm.domain.usecases.GetPopularTvUseCase
import cu.wilb3r.codechallengetm.domain.usecases.GetTopRatedTvUseCase
import cu.wilb3r.codechallengetm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TvViewModel @Inject constructor(
    private val popularTvUseCase: GetPopularTvUseCase,
    private val topRatedTvUseCase: GetTopRatedTvUseCase
) : BaseViewModel() {

    suspend fun getPopularTv() = popularTvUseCase.invoke().cachedIn(viewModelScope)

    suspend fun getTopTv() = topRatedTvUseCase.invoke().cachedIn(viewModelScope)

}