package cu.wilb3r.codechallengetm.data.local.db.dao

import androidx.room.Dao
import androidx.room.Query
import cu.wilb3r.codechallengetm.data.local.entities.DBGenre

@Dao
interface GenreDao : BaseDao<DBGenre> {
    @Query("SELECT * FROM genre")
    suspend fun getAllGenre(): List<DBGenre>

    @Query("SELECT * FROM genre WHERE id == :id")
    suspend fun get(id: Int): List<DBGenre>
}