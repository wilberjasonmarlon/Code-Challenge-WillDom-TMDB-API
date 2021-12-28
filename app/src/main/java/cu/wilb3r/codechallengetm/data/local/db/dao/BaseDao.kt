package cu.wilb3r.codechallengetm.data.local.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @Transaction
    suspend fun insertAll(data: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: T)
}