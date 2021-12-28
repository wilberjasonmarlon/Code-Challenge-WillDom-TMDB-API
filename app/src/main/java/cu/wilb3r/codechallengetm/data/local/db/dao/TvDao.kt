package cu.wilb3r.codechallengetm.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import cu.wilb3r.codechallengetm.data.local.entities.DBTv

@Dao
interface TvDao : BaseDao<DBTv> {
    @Query("SELECT * FROM tv")
    fun getAllTv(): PagingSource<Int, DBTv>

    // the keys
    @Query("SELECT * FROM tv")
    suspend fun getKeys(): List<DBTv>

}