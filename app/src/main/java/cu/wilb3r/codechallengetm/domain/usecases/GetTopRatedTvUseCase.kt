package cu.wilb3r.codechallengetm.domain.usecases

import androidx.paging.PagingData
import cu.wilb3r.codechallengetm.data.local.db.dao.TvAndTvTop
import cu.wilb3r.codechallengetm.domain.repository.TvRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class GetTopRatedTvUseCase @Inject constructor(private val repo: TvRepository) {
    suspend fun invoke(): Flow<PagingData<TvAndTvTop>> = repo.getTopRatedTv()
}