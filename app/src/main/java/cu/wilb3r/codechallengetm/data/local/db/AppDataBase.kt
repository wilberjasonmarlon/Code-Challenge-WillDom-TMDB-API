package cu.wilb3r.codechallengetm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cu.wilb3r.codechallengetm.data.local.converter.ListIntConverter
import cu.wilb3r.codechallengetm.data.local.converter.ListStringConverter
import cu.wilb3r.codechallengetm.data.local.db.dao.*
import cu.wilb3r.codechallengetm.data.local.entities.*
import cu.wilb3r.constant.Data

@Database(
    entities = [
        DBMovie::class,
        DBTv::class,
        DBGenre::class,
        MoviePopular::class,
        MovieTop::class,
        TvPopular::class,
        TvTop::class
    ],
    version = Data.DB_VERSION,
    exportSchema = false
)
@TypeConverters(
    ListStringConverter::class,
    ListIntConverter::class
)
abstract class AppDataBase : RoomDatabase() {
    abstract fun tvDao(): TvDao
    abstract fun MovieDao(): MovieDao
    abstract fun GenreDao(): GenreDao
    abstract fun MoviePopularDao(): MoviePopularDao
    abstract fun MovieTopDao(): MovieTopDao
    abstract fun TvPopularDao(): TvPopularDao
    abstract fun TvTopDao(): TvTopDao
}