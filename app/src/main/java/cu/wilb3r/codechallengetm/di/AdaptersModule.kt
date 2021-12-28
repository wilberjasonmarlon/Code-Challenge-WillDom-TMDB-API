package cu.wilb3r.codechallengetm.di

import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.MoviePageAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object AdaptersModule {
    @Provides
    fun provideMoviePageAdapter(imageLoader: ImageLoader): MoviePageAdapter =
        MoviePageAdapter(imageLoader).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT
        }
}