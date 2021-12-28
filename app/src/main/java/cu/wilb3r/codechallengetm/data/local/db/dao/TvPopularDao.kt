package cu.wilb3r.codechallengetm.data.local.db.dao

import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.room.*
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.local.entities.TvPopular
import kotlinx.parcelize.Parcelize

@Dao
interface TvPopularDao : BaseDao<TvPopular> {
    @Query("SELECT * FROM tv_popular ORDER BY page DESC")
    fun getTvPopular(): PagingSource<Int, TvPopular>

    @Transaction
    @Query("SELECT * FROM tv WHERE id IN (SELECT DISTINCT(tv_id) FROM tv_popular) ORDER BY popularity DESC")
    fun getPopularTv(): PagingSource<Int, TvAndTvPopular>

    @Query("SELECT * FROM tv_popular ORDER BY page ASC")
    suspend fun getKeys(): List<TvPopular>

}

@Parcelize
data class TvAndTvPopular(
    @Embedded
    val tv: DBTv,
    @Relation(parentColumn = "id", entityColumn = "tv_id")
    val TvPopular: List<TvPopular> = emptyList()
) : Parcelable