package cu.wilb3r.codechallengetm.data.local.db.dao

import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.room.*
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.MovieTop
import kotlinx.parcelize.Parcelize

@Dao
interface MovieTopDao : BaseDao<MovieTop> {
    @Query("SELECT * FROM movie_top ORDER BY page DESC")
    fun getMovieTop(): PagingSource<Int, MovieTop>

    @Transaction
    @Query("SELECT * FROM movie WHERE id IN (SELECT DISTINCT(movie_id) FROM movie_top) ORDER BY vote_average DESC, popularity ASC ")
    fun getTopMovies(): PagingSource<Int, MovieAndMovieTop>

    @Query("SELECT * FROM movie_top ORDER BY page ASC")
    suspend fun getKeys(): List<MovieTop>

}

@Parcelize
data class MovieAndMovieTop(
    @Embedded
    val movie: DBMovie,
    @Relation(parentColumn = "id", entityColumn = "movie_id")
    val MovieTop: List<MovieTop> = emptyList()
) : Parcelable