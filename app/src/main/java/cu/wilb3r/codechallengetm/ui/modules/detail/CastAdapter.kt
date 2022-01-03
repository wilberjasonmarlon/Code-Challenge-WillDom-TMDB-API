package cu.wilb3r.codechallengetm.ui.modules.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import cu.wilb3r.codechallengetm.data.remote.model.Cast
import cu.wilb3r.codechallengetm.databinding.ItemCastBinding
import cu.wilb3r.codechallengetm.domain.model.Media
import cu.wilb3r.constant.Api
import javax.inject.Inject

class CastAdapter @Inject constructor() :
    RecyclerView.Adapter<CastAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemCastBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    private var items: List<Cast>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    fun addItems(items: List<Cast>) {
        this.items = items
    }

    private var onItemClickListener: ((Media) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCastBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            castName.text = item.name
            val url = Api.IMAGESURL + item.profile_path
            face.load(url) {
                transformations(CircleCropTransformation())
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}