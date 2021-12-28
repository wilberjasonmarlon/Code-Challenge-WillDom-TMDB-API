package cu.wilb3r.codechallengetm.data.local

import androidx.paging.PagingSource
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.DBTv

interface DBRepository {
    suspend fun insertTv(item: DBTv)

    suspend fun getAllTvs(): PagingSource<Int, DBTv>

    suspend fun insertMovie(item: DBMovie)

    fun getAllMovies(): PagingSource<Int, DBMovie>

    suspend fun getMovieKeys(): List<DBMovie>
}