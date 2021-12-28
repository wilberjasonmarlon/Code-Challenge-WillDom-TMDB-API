package cu.wilb3r.codechallengetm.domain.usecases

import android.content.Context
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.data.remote.model.Resource
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResult
import cu.wilb3r.codechallengetm.domain.repository.CommonRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class SearchAnyUseCase @Inject constructor(
    private val repo: CommonRepository,
    @ApplicationContext val context: Context
) {
    operator fun invoke(search: String): Flow<Resource<List<SearchResult>>> = flow {
        try {
            emit(Resource.Loading<List<SearchResult>>())
            val response = repo.searchAny(search)
            if (response.isSuccessful)
                response.body()?.results?.let {
                    emit(Resource.Success<List<SearchResult>>(it))
                }
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<SearchResult>>(
                    e.localizedMessage ?: context.resources.getString(R.string.unexpected_error)
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<SearchResult>>(context.resources.getString(R.string.no_internet)))
        }
    }
}