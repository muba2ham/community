package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.DataModel

import community.io.com.databinding.ItemHomeMenuBinding

class HomeMenuAdapter(
    private val ctx: Context,
    private var locations: MutableList<DataModel>,
) : RecyclerView.Adapter<HomeMenuAdapter.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    var selectedPosition = 0

    override fun onBindViewHolder(holder: HomeMenuAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HomeMenuAdapter.ViewHolder {
        val binding: ItemHomeMenuBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_home_menu, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }


    inner class ViewHolder(val viewBinding: ItemHomeMenuBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: DataModel?) {
            viewBinding.tvTitle.text = "${item!!.title}"
            viewBinding.menuImage.setImageResource(item.image)

            viewBinding.llMenu.setOnClickListener {
                Log.e("itemClicked", "itemClicked")
                selectedPosition = position
                onItemClick?.invoke(selectedPosition)
            }
//            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
        }
    }

}