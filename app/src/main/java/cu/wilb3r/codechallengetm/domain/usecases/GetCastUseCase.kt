package cu.wilb3r.codechallengetm.domain.usecases

import android.content.Context
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.data.remote.model.Cast
import cu.wilb3r.codechallengetm.data.remote.model.Resource
import cu.wilb3r.codechallengetm.domain.repository.MovieRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetCastUseCase @Inject constructor(private val repo: MovieRepository, @ApplicationContext val context: Context) {
    operator fun invoke(video_id: Int): Flow<Resource<List<Cast>>> = flow {
        try {
            emit(Resource.Loading<List<Cast>>())
            val response = repo.getCast(video_id)
            if(response.isSuccessful)
                emit(Resource.Success<List<Cast>>(response.body()!!.cast))
        } catch (e: HttpException) {
            emit(Resource.Error<List<Cast>>(e.localizedMessage ?: context.resources.getString(R.string.unexpected_error)))
        } catch (e: IOException) {
            emit(Resource.Error<List<Cast>>(context.resources.getString(R.string.no_internet)))
        }
    }
}