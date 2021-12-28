package cu.wilb3r.codechallengetm.ui.modules.movies.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.databinding.ItemListMovieBinding
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import cu.wilb3r.constant.Api.IMAGESURL
import javax.inject.Inject

class MovieViewHolder @Inject constructor(
    val binding: ViewBinding,
    val imageLoader: ImageLoader
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DBMovie) {
        binding.apply {
            when (binding) {
                is ItemMovieBinding -> {
                    binding.videoTitle.text = "${item.title}"
                    item.genre?.let {
                        binding.videoGenre.text = "$it".trim('[', ']')
                    }
                    val url = IMAGESURL + "${item.poster_path}"
                    binding.videoImage.transitionName = item.poster_path.toString()
                    binding.videoImage.load(url, imageLoader) {
                        transformations(RoundedCornersTransformation(25f))
                    }
                }
                is ItemListMovieBinding -> {
                    binding.videoTitle.text = "${item.title}"
                    binding.videoOverview.text = "${item.overview}"
                    item.vote_average?.let {
                        binding.ratingBar.text = it.toString()
                    }
                    val url = IMAGESURL + "${item.poster_path}"
                    binding.videoOverview.transitionName = item.overview.toString()
                    binding.videoImage.transitionName = item.poster_path.toString()
                    binding.videoImage.load(url, imageLoader) {
                        transformations(RoundedCornersTransformation(25f))
                    }
                }
            }
        }
    }
}