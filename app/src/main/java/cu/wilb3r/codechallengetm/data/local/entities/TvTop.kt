package cu.wilb3r.codechallengetm.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "tv_top",
    foreignKeys = [ForeignKey(
        entity = DBTv::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("tv_id")
    )]
)
data class TvTop(
    @PrimaryKey(autoGenerate = false)
    val tv_id: Int?,
    val page: Int
): Parcelable

