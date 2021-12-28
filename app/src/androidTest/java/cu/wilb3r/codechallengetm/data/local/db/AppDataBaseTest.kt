package cu.wilb3r.codechallengetm.data.local.db

import android.content.Context
import androidx.paging.PagingSource
import androidx.room.Database
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import junit.framework.TestCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class AppDataBaseTest : TestCase() {

    private lateinit var db: AppDataBase

    @Before
    override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java).build()
    }


    @After
    fun closeDb() {
        db.close()
    }


    @Test
    @Throws(InterruptedException::class)
    fun testTvDao() = runBlocking{
        val tv = DBTv(null, null, null, null, 5656, null,
        null,null, "Algo: La pelicula", "Video sobre algo", 99.9999,
            null,9.9,99999 )
        db.tvDao().insert(tv)
        val allTvs = db.tvDao().getKeys()
        println(">> allTvs: $allTvs")
        assertEquals(allTvs[0], tv)
    }

    fun testMovieDao() = runBlocking{

        val data = DBMovie(null, null, null, null, 5656, null,
            null,null, 66.66, "url", "asddas",
            "Este es el titulo", video = true , 9.9,23432 )
        db.MovieDao().insert(data)
        val result = db.MovieDao().getKeys()
        assertEquals(result[0], data)
    }

    fun testGenreDao() {}

    fun testMoviePopularDao() {}

    fun testMovieTopDao() {}

    fun testTvPopularDao() {}

    fun testTvTopDao() {}
}