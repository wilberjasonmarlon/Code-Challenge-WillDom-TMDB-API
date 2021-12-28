package cu.wilb3r.codechallengetm.ui.modules.movies.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var onItemClickListener: ((View, DBMovie, Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (View, DBMovie, Int) -> Unit) {
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
                    setOnClickListener { view ->
                        onItemClickListener?.invoke(binding.root, item, position)
                    }
                }
            }
        }
    }

}