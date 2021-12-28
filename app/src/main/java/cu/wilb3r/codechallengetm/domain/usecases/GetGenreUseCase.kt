package cu.wilb3r.codechallengetm.domain.usecases

import android.content.Context
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.data.local.db.dao.GenreDao
import cu.wilb3r.codechallengetm.data.local.entities.DBGenre
import cu.wilb3r.codechallengetm.data.remote.model.Resource
import cu.wilb3r.codechallengetm.domain.repository.GenreRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetGenreUseCase @Inject constructor(
    private val repo: GenreRepository,
    private val genreDao: GenreDao,
    @ApplicationContext val context: Context
) {
    operator fun invoke() = flow {
        try {
            emit(Resource.Loading<List<DBGenre>>())
            val genres = repo.getGenre()
            genreDao.insertAll(genres)
            emit(Resource.Success<List<DBGenre>>(genres))
        } catch (e: HttpException) {
            emit(
                Resource.Error<List<DBGenre>>(
                    e.localizedMessage ?: context.resources.getString(R.string.unexpected_error)
                )
            )
        } catch (e: IOException) {
            emit(Resource.Error<List<DBGenre>>(context.resources.getString(R.string.no_internet)))
        }
    }
}