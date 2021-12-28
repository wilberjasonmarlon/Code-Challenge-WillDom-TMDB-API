package cu.wilb3r.codechallengetm.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import cu.wilb3r.codechallengetm.data.local.entities.DBGenre.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class DBGenre(
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val name: String?
) : Parcelable {
    companion object {
        const val TABLE_NAME = "genre"
    }
}