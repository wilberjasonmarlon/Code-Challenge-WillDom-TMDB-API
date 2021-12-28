package cu.wilb3r.codechallengetm.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao : BaseDao<DBMovie> {
    @Query("SELECT * FROM movie ORDER BY popularity DESC")
    fun getAllMovie(): PagingSource<Int, DBMovie>

    // the keys
    @Query("SELECT * FROM movie")
    suspend fun getKeys(): List<DBMovie>
}