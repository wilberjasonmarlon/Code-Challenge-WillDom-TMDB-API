package cu.wilb3r.codechallengetm.domain.usecases

import cu.wilb3r.codechallengetm.data.remote.model.MediaResult
import cu.wilb3r.codechallengetm.data.remote.model.Resource
import cu.wilb3r.codechallengetm.domain.model.MediaType
import cu.wilb3r.codechallengetm.domain.repository.CommonRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetVideosUseCase @Inject constructor(private val repo: CommonRepository) {
    operator fun invoke(@MediaType type: String, id: Int): Flow<Resource<List<MediaResult>>> =
        flow {
            try {
                emit(Resource.Loading<List<MediaResult>>(emptyList<MediaResult>()))
                val response = repo.getVideosForMedia(type, id)
                if (response.isSuccessful)
                    emit(Resource.Success<List<MediaResult>>(response.body()!!.results))
            } catch (e: HttpException) {
                emit(Resource.Error<List<MediaResult>>(e.localizedMessage ?: "An error occured"))
            } catch (e: IOException) {
                emit(Resource.Error<List<MediaResult>>("Are you online?. Check your internet connection."))
            }
        }
}
