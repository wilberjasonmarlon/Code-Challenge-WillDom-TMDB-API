package cu.wilb3r.codechallengetm.ui.modules.tv.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import coil.ImageLoader
import cu.wilb3r.codechallengetm.data.local.entities.DBTv
import cu.wilb3r.codechallengetm.databinding.ItemListMovieBinding
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import cu.wilb3r.codechallengetm.ui.modules.movies.adapter.CategoryType
import javax.inject.Inject

class TvPageAdapter @Inject constructor(
    private val imageLoader: ImageLoader
) :
    PagingDataAdapter<DBTv, TvViewHolder>(DiffUtilCallBack()) {

    private var movieCategoryType: Int = CategoryType.POPULAR

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        return when (movieCategoryType) {
            CategoryType.POPULAR, CategoryType.TRENDING -> TvViewHolder(
                ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                imageLoader
            )
            else -> TvViewHolder(
                ItemListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false),
                imageLoader
            )
        }

    }

    private var onItemClickListener: ((View, DBTv, Int) -> Unit)? = null

    fun setCategory(@CategoryType category: Int) {
        movieCategoryType = category
    }

    fun setOnItemClickListener(listener: (View, DBTv, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(
        holder: TvViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        getItem(position)?.let { item ->
            holder.apply {
                bind(item)
                binding.root.apply {
                    setOnClickListener { view ->
                        onItemClickListener?.invoke(view, item, position)
                    }
                }
            }
        }
    }

}