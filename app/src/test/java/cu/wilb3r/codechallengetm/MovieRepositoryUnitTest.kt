package cu.wilb3r.codechallengetm

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import cu.wilb3r.codechallengetm.data.local.db.dao.MovieAndMoviePopular
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.data.repository.movie.MovieRepositoryImp
import cu.wilb3r.codechallengetm.domain.repository.MovieRepository
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.Assert.assertEquals
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
@ExperimentalCoroutinesApi
class MovieRepositoryUnitTest{

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }


    @Test
    fun `testing get movie list`() = runBlockingTest {
        val movieRepository = mockk<MovieRepositoryImp>()
        val mockedResponse = flowOf<PagingData<MovieAndMoviePopular>>()
        coEvery { movieRepository.getPopularMovies() } returns mockedResponse
        val result = movieRepository.getPopularMovies()
        result.collectLatest {
            println(">> Result = $it")
        }

        assertEquals(mockedResponse, result)


    }

}