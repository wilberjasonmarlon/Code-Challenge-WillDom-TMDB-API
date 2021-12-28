package cu.wilb3r.codechallengetm.data.repository

import cu.wilb3r.codechallengetm.BuildConfig
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.data.remote.model.VideosResponse
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResponse
import cu.wilb3r.codechallengetm.domain.model.MediaType
import cu.wilb3r.codechallengetm.domain.repository.CommonRepository
import cu.wilb3r.constant.Api
import cu.wilb3r.constant.Api.PAGE
import retrofit2.Response
import javax.inject.Inject

class CommonRepositoryImp @Inject constructor(
    private val api: TMDBApiService
) : CommonRepository {

    override suspend fun getVideosForMedia(
        @MediaType type: String,
        id: Int
    ): Response<VideosResponse> {
        val param = HashMap<String, String>().apply {
            put(Api.API_KEY_STR, BuildConfig.API_KEY)
        }
        when (type) {
            MediaType.VIDEO -> {
                return api.getMovieVideos(id, param)
            }
            else -> {
                return api.getTvVideos(id, param)
            }
        }
    }

    override suspend fun searchAny(query: String): Response<SearchResponse> {
        val param = HashMap<String, String>().apply {
            put(Api.API_KEY_STR, BuildConfig.API_KEY)
            put(Api.QUERY, query)
            put(PAGE, "1")
        }
        return api.searchAny(param)
    }
}
