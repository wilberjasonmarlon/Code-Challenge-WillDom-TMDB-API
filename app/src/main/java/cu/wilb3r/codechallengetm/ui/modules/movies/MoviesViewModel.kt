package cu.wilb3r.codechallengetm.ui.modules.movies

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMovieTop
import cu.wilb3r.codechallengetm.domain.usecases.GetPopularMoviesUseCase
import cu.wilb3r.codechallengetm.domain.usecases.GetTopRatedMoviesUseCase
import cu.wilb3r.codechallengetm.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val popularMoviesUseCase: GetPopularMoviesUseCase,
    private val topMoviesUseCase: GetTopRatedMoviesUseCase
) : BaseViewModel() {

    suspend fun getPopularMovies(): Flow<PagingData<MovieAndMoviePopular>> {
        return popularMoviesUseCase.invoke().cachedIn(viewModelScope)
    }

    suspend fun getTop(): Flow<PagingData<MovieAndMovieTop>> {
        return topMoviesUseCase.invoke().cachedIn(viewModelScope)
    }

}