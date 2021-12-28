package cu.wilb3r.codechallengetm.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import cu.wilb3r.codechallengetm.data.local.converter.ListIntConverter
import cu.wilb3r.codechallengetm.data.local.converter.ListStringConverter
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie.Companion.TABLE_NAME
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = TABLE_NAME)
data class DBMovie(
    val adult: Boolean?,
    val backdrop_path: String?,
    @TypeConverters(ListIntConverter::class)
    val genre_ids: List<Int>?,//
    @TypeConverters(ListStringConverter::class)
    var genre: List<String>?,
    @PrimaryKey(autoGenerate = false)
    val id: Int?,//
    val original_language: String?,
    val original_title: String?,
    val overview: String?,
    val popularity: Double?,
    val poster_path: String?,//
    val release_date: String?,
    val title: String?,//
    val video: Boolean?,
    val vote_average: Double?,
    val vote_count: Int?
) : Parcelable {
    companion object {
        const val TABLE_NAME = "movie"
        private val LOADING = DBMovie(
            false,
            "loading",
            null,
            listOf("loading"),
            null,
            null,
            "loading",
            "loading",
            0.0,
            "loading",
            "loading",
            "loading",
            false,
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