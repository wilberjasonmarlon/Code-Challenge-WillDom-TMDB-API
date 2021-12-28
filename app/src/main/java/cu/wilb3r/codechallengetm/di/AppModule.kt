package cu.wilb3r.codechallengetm.di

import android.app.Activity
import android.app.Application
import android.view.inputmethod.InputMethodManager
import coil.ImageLoader
import coil.util.CoilUtils
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import cu.wilb3r.codechallengetm.BuildConfig
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.data.remote.TMDBApiService
import cu.wilb3r.codechallengetm.ui.modules.movies.detail.DetailFragment
import cu.wilb3r.constant.Api
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCoroutineContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
        .setPrettyPrinting()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(): TMDBApiService {
        return Retrofit.Builder()
            .baseUrl(Api.BASEURL)
            .client(provideOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(provideGson()))
            .build().create(TMDBApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideInputMethodManager(application: Application) =
        application.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    @Provides
    @Singleton
    fun provideCoil(application: Application): ImageLoader {
        return ImageLoader.Builder(application)
            .crossfade(true)
            .placeholder(R.drawable.poster_foreground)
            .error(R.drawable.poster_foreground)
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(application))
                    .build()
            }
            .build()
    }

    @Provides
    fun provideDetailDialogFragment(): DetailFragment = DetailFragment()
}