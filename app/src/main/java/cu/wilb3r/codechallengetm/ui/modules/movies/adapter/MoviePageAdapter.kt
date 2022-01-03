package cu.wilb3r.codechallengetm.ui.modules.movies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import coil.ImageLoader
import cu.wilb3r.codechallengetm.data.local.entities.DBMovie
import cu.wilb3r.codechallengetm.databinding.ItemListMovieBinding
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import javax.inject.Inject

class MoviePageAdapter @Inject constructor(private var imageLoader: ImageLoader) :
    PagingDataAdapter<DBMovie, MovieViewHolder>(DiffUtilCallBack()) {

    private var movieCategoryType: Int = CategoryType.POPULAR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        return when (movieCategoryType) {
            CategoryType.POPULAR -> MovieViewHolder(
                ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                imageLoader
            )
            else -> MovieViewHolder(
                ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                imageLoader
            )
        }
//        return MovieViewHolder(
//            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        )

    }

    fun setCategory(@CategoryType category: Int) {
        movieCategoryType = category
    }

    private var onItemClickListener: ((ImageView, TextView, DBMovie, Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (ImageView, TextView, DBMovie, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(
        holder: MovieViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        getItem(position)?.let { item ->
            holder.apply {
                bind(item)
                binding.root.apply {
                    when (binding) {
                        is ItemMovieBinding -> {
                            setOnClickListener {
                                onItemClickListener?.invoke(binding.videoImage, binding.videoTitle, item, position)
                            }
                        }
                        is ItemListMovieBinding -> {
                            setOnClickListener {
                                onItemClickListener?.invoke(binding.videoImage, binding.videoTitle, item, position)
                            }
                        }
                    }
                }
            }
        }
    }

}