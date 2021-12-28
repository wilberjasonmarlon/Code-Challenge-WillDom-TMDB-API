package cu.wilb3r.codechallengetm.domain.usecases

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvPopular
import cu.wilb3r.codechallengetm.domain.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPopularTvUseCase @Inject constructor(private val repo: TvRepository) {
    suspend fun invoke(): Flow<PagingData<TvAndTvPopular>> = repo.getPopularTv()
}