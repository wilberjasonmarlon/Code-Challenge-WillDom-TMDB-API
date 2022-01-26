package cu.wilb3r.codechallengetm.ui.modules.search.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResult
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import cu.wilb3r.constant.Api.IMAGESURL

class SearchViewHolder(val binding: ItemMovieBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: SearchResult) {
        binding.apply {
            binding.videoTitle.text = "${item.title}"
            val url = IMAGESURL + "${item.poster_path}"
            binding.videoImage.load(url) {
                transformations(RoundedCornersTransformation(25f))
            }
            binding.ratingBar.rating = item.vote_average?.toFloat()?.div(2)?: 0f
        }
    }
}