package cu.wilb3r.codechallengetm.data.local.db.dao

import android.os.Parcelable
import androidx.paging.PagingSource
import androidx.room.*
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.data.local.entities.TvTop
import kotlinx.parcelize.Parcelize

@Dao
interface TvTopDao : BaseDao<TvTop> {
    @Query("SELECT * FROM tv_top ORDER BY page DESC")
    fun getTvTop(): PagingSource<Int, TvTop>

    @Transaction
    @Query("SELECT * FROM tv WHERE id IN (SELECT DISTINCT(tv_id) FROM tv_top) ORDER BY vote_average DESC, popularity ASC ")
    fun getTopTv(): PagingSource<Int, TvAndTvTop>

    @Query("SELECT * FROM tv_top ORDER BY page ASC")
    suspend fun getKeys(): List<TvTop>


}

@Parcelize
data class TvAndTvTop(
    @Embedded
    val tv: DBTv,
    @Relation(parentColumn = "id", entityColumn = "tv_id")
    val TvTop: List<TvTop> = emptyList()
) : Parcelable