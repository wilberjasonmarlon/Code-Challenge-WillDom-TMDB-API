package cu.wilb3r.codechallengetm.data.local.entities


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "movie_popular",
    foreignKeys = [ForeignKey(
        entity = DBMovie::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("movie_id")
    )]
)
data class MoviePopular(
    @PrimaryKey(autoGenerate = false)
    val movie_id: Int?,
    val page: Int
) : Parcelable

