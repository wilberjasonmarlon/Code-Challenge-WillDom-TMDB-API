package cu.wilb3r.codechallengetm.di

import android.app.Application
import androidx.room.Room
import cu.wilb3r.codechallengetm.data.local.db.AppDataBase
import cu.wilb3r.codechallengetm.data.local.db.dao.*
import cu.wilb3r.constant.Data
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application): AppDataBase {
        return Room.databaseBuilder(application, AppDataBase::class.java, Data.DB_NAME).build()
    }

    @Singleton
    @Provides
    fun provideTvDao(appDataBase: AppDataBase): TvDao {
        return appDataBase.tvDao()
    }

    @Singleton
    @Provides
    fun provideMovieDao(appDataBase: AppDataBase): MovieDao {
        return appDataBase.MovieDao()
    }

    @Singleton
    @Provides
    fun provideGenreDao(appDataBase: AppDataBase): GenreDao {
        return appDataBase.GenreDao()
    }

    @Singleton
    @Provides
    fun provideMoviePopularDao(appDataBase: AppDataBase): MoviePopularDao {
        return appDataBase.MoviePopularDao()
    }

    @Singleton
    @Provides
    fun provideMovieTopDao(appDataBase: AppDataBase): MovieTopDao {
        return appDataBase.MovieTopDao()
    }

    @Singleton
    @Provides
    fun provideTvPopularDao(appDataBase: AppDataBase): TvPopularDao {
        return appDataBase.TvPopularDao()
    }

    @Singleton
    @Provides
    fun provideTvTopDao(appDataBase: AppDataBase): TvTopDao {
        return appDataBase.TvTopDao()
    }

}