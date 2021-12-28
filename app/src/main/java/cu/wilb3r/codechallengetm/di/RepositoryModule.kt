package cu.wilb3r.codechallengetm.di

import androidx.paging.ExperimentalPagingApi
import cu.wilb3r.codechallengetm.data.local.db.dao.*
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.data.repository.CommonRepositoryImp
import cu.wilb3r.codechallengetm.data.repository.GenreRepositoryImp
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.PopularMovieRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.movie.MovieRepositoryImp
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.TopMovieRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.tv.datasource.PopularTvRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.tv.TvRepositoryImp
import cu.wilb3r.codechallengetm.data.repository.tv.datasource.TopTvRemoteMediator
import cu.wilb3r.codechallengetm.domain.datasource.CastDataSource
import cu.wilb3r.codechallengetm.domain.repository.CommonRepository
import cu.wilb3r.codechallengetm.domain.repository.GenreRepository
import cu.wilb3r.codechallengetm.domain.repository.MovieRepository
import cu.wilb3r.codechallengetm.domain.repository.TvRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @ExperimentalPagingApi
    @Provides
    fun provideMovieRepository(
        moviePopularDao: MoviePopularDao,
        movieTopDao: MovieTopDao,
        pMediator: PopularMovieRemoteMediator,
        topMediator: TopMovieRemoteMediator,
        castDataSource: CastDataSource
    ): MovieRepository = MovieRepositoryImp(moviePopularDao, movieTopDao, pMediator, topMediator, castDataSource)

    @ExperimentalPagingApi
    @Provides
    fun provideTvRepository(
        tvPopularDao: TvPopularDao,
        tvTopDao: TvTopDao,
        popularRemoteMediator: PopularTvRemoteMediator,
        topRemoteMediator: TopTvRemoteMediator,
        castDataSource: CastDataSource
    ): TvRepository = TvRepositoryImp(tvPopularDao, tvTopDao, popularRemoteMediator, topRemoteMediator, castDataSource)

    @Provides
    fun provideGenreRepository(
        api: TMDBApiService
    ): GenreRepository = GenreRepositoryImp(api)

    @Provides
    fun provideCommonRepository(api: TMDBApiService): CommonRepository = CommonRepositoryImp(api)

}