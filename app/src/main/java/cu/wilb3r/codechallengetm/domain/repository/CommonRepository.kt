package cu.wilb3r.codechallengetm.domain.repository

import cu.wilb3r.codechallengetm.data.remote.model.VideosResponse
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResponse
import cu.wilb3r.codechallengetm.domain.model.MediaType
import retrofit2.Response

interface CommonRepository {
    suspend fun getVideosForMedia(@MediaType type: String, id: Int): Response<VideosResponse>
    suspend fun searchAny(query: String): Response<SearchResponse>
}