package cu.wilb3r.codechallengetm.ui.modules.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cu.wilb3r.codechallengetm.data.remote.model.dell.SearchResult
import cu.wilb3r.codechallengetm.databinding.ItemMovieBinding
import cu.wilb3r.constant.Api
import javax.inject.Inject

class SearchAdapter @Inject constructor() :
    RecyclerView.Adapter<SearchAdapter.ViewHolder>() {


    class ViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
        override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    private var items: List<SearchResult>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    fun addItems(items: List<SearchResult>) {
        this.items = items
    }

    private var onItemClickListener: ((SearchResult, Int) -> Unit)? = null
    fun setOnItemClickListener(listener: (SearchResult, Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            videoImage.load(Api.IMAGESURL + item.poster_path)
            videoTitle.text = item.title
            videoImage.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(item, position)
                }
            }
        }
    }

}