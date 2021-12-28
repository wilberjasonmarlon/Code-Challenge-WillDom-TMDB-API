package cu.wilb3r.codechallengetm.di

import androidx.paging.ExperimentalPagingApi
import cu.wilb3r.codechallengetm.data.local.db.dao.*
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.CastDataSourceImpl
import cu.wilb3r.codechallengetm.data.repository.movie.datasource.PopularMovieRemoteMediator
import cu.wilb3r.codechallengetm.data.repository.tv.datasource.PopularTvRemoteMediator
import cu.wilb3r.codechallengetm.domain.datasource.CastDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @ExperimentalPagingApi
    @Provides
    fun provideMovieRemoteMediator(
        movieDao: MovieDao,
        genreDao: GenreDao,
        moviePopularDao: MoviePopularDao,
        tmdbApiService: TMDBApiService
    ) = PopularMovieRemoteMediator(movieDao, genreDao, moviePopularDao, tmdbApiService)

    @ExperimentalPagingApi
    @Provides
    fun provideTvRemoteMediator(
        tvDao: TvDao,
        genreDao: GenreDao,
        tvPopularDao: TvPopularDao,
        tmdbApiService: TMDBApiService
    ) = PopularTvRemoteMediator(tvDao, genreDao, tvPopularDao, tmdbApiService)

    @Singleton
    @Provides
    fun provideCastDataSource(api: TMDBApiService): CastDataSource = CastDataSourceImpl(api)


}