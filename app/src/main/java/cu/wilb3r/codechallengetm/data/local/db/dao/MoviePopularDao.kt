package cu.wilb3r.codechallengetm.data.local.db.dao

import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.room.*
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.MoviePopular
import kotlinx.parcelize.Parcelize

@Dao
interface MoviePopularDao : BaseDao<MoviePopular> {
    @Query("SELECT * FROM movie_popular ORDER BY page DESC")
    fun getMoviePopular(): PagingSource<Int, MoviePopular>

    @Transaction
    @Query("SELECT * FROM movie WHERE id IN (SELECT DISTINCT(movie_id) FROM movie_popular) ORDER BY popularity DESC")
    fun getPopularMovies(): PagingSource<Int, MovieAndMoviePopular>

    @Query("SELECT * FROM movie_popular ORDER BY page ASC")
    suspend fun getKeys(): List<MoviePopular>

}

@Parcelize
data class MovieAndMoviePopular(
    @Embedded
    val movie: DBMovie,
    @Relation(parentColumn = "id", entityColumn = "movie_id")
    val MoviePopular: List<MoviePopular> = emptyList()
) : Parcelable