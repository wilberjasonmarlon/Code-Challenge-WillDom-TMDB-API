package cu.wilb3r.codechallengetm.ui.modules.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import cu.wilb3r.codechallengetm.R
import cu.wilb3r.codechallengetm.data.remote.model.MediaResult
import cu.wilb3r.codechallengetm.databinding.ItemVideoBinding
import cu.wilb3r.constant.Api
import javax.inject.Inject

class VideoAdapter @Inject constructor() :
    RecyclerView.Adapter<VideoAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemVideoBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<MediaResult>() {
        override fun areItemsTheSame(oldItem: MediaResult, newItem: MediaResult): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MediaResult, newItem: MediaResult): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    private var items: List<MediaResult>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    fun addItems(items: List<MediaResult>) {
        this.items = items
    }

    private var onItemClickListener: ((MediaResult, String) -> Unit)? = null
    fun setOnItemClickListener(listener: (MediaResult, String) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            val url = Api.YOUTUBE + item.key
            video.load(R.drawable.ic_spash)
            video.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(item, url)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}