package cu.wilb3r.codechallengetm.ui.modules.tv.adapter

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.ImageLoader
import coil.load
import coil.transform.RoundedCornersTransformation
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.databinding.ItemListMovieBinding
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import cu.wilb3r.constant.Api.IMAGESURL

class TvViewHolder(val binding: ViewBinding, val imageLoader: ImageLoader) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DBTv) {
        binding.apply {
            when (binding) {
                is ItemMovieBinding -> {
                    binding.videoTitle.text = "${item.name}"
                    item.genre?.let {
                        binding.videoGenre.text = "$it".trim('[', ']')
                    }
                    val url = IMAGESURL + "${item.poster_path}"
                    binding.videoTitle.transitionName = item.name.toString()
                    binding.videoImage.transitionName = item.poster_path.toString()
                    binding.ratingBar.rating = item.vote_average?.toFloat()?.div(2)?: 0f
                    binding.videoImage.load(url, imageLoader) {
                        transformations(RoundedCornersTransformation(25f))
                    }
                    binding.videoImage.transitionName = "imgPoster"
                }
                is ItemListMovieBinding -> {
                    binding.videoTitle.text = "${item.name}"
                    binding.videoOverview.text = "${item.overview}"
                    binding.ratingBar.rating = item.vote_average?.toFloat()?.div(2)?: 0f
                    val url = IMAGESURL + "${item.poster_path}"
                    binding.videoTitle.transitionName = item.name.toString()
                    binding.videoImage.transitionName = item.poster_path.toString()
                    binding.videoImage.load(url, imageLoader) {
                        transformations(RoundedCornersTransformation(25f))
                    }
                    binding.videoImage.transitionName = "imgPoster"
                }
            }

        }
    }
}