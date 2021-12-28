package cu.wilb3r.codechallengetm.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cu.wilb3r.codechallengetm.data.local.converter.ListIntConverter
import cu.wilb3r.codechallengetm.data.local.converter.ListStringConverter
import cu.wilb3r.codechallengetm.data.local.entities.DBTv.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class DBTv(
    val backdrop_path: String?,
    val first_air_date: String?,
    @TypeConverters(ListIntConverter::class)
    val genre_ids: List<Int>?,
    @TypeConverters(ListStringConverter::class)
    var genre: List<String>?,
    @PrimaryKey(autoGenerate = false)
    val id: Int?,
    val name: String?,
    @TypeConverters(ListStringConverter::class)
    val origin_country: List<String>?,
    val original_language: String?,
    val original_name: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,
    val vote_average: Double?,
    val vote_count: Int?
) : Parcelable {
    companion object {
        const val TABLE_NAME = "tv"
        private val LOADING = DBTv(
            "loading",
            "loading",
            null,
            listOf("loading"),
            null,
            "loading",
            null,
            "loading",
            "loading",
            "loading",
            0.0,
            "loading",
            0.0,
            0
        )
        val LOADING_LIST = arrayListOf(
            LOADING, LOADING, LOADING, LOADING, LOADING, LOADING,
            LOADING, LOADING, LOADING, LOADING, LOADING, LOADING,
            LOADING, LOADING, LOADING, LOADING, LOADING, LOADING,
            LOADING, LOADING, LOADING, LOADING, LOADING, LOADING,
            LOADING, LOADING, LOADING, LOADING, LOADING, LOADING,
        )
    }
}
