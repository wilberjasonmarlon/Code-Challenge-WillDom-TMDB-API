package cu.wilb3r.codechallengetm.data.repository.movie.datasource

import cu.wilb3r.codechallengetm.BuildConfig
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.data.remote.model.CastResponse
import cu.wilb3r.codechallengetm.domain.datasource.CastDataSource
import cu.wilb3r.constant.Api
import retrofit2.Response
import javax.inject.Inject

class CastDataSourceImpl @Inject constructor(
    private val api: TMDBApiService
): CastDataSource {

    private val param = HashMap<String, String>().apply {
        put(Api.API_KEY_STR, BuildConfig.API_KEY)
    }

    override suspend fun getMovieCast(video_id: Int): Response<CastResponse> {
        return api.getMovieCredits(video_id, param)
    }

    override suspend fun getTvCast(video_id: Int): Response<CastResponse> {
        return api.getTvCredits(video_id, param)
    }


}