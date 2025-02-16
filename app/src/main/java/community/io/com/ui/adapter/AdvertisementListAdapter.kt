package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdvertisementModel
import community.io.com.data.model.AdvlModel
import community.io.com.databinding.ItemAdvertisementListBinding


class AdvertisementListAdapter(
    private val ctx: Context,
    private var locations: MutableList<AdvertisementModel>,
) : RecyclerView.Adapter<AdvertisementListAdapter.ViewHolder>() {


    var onSendMessageClick: ((positons : Int,comment : AdvertisementModel?) -> Unit)? = null

    override fun onBindViewHolder(holder: AdvertisementListAdapter.ViewHolder, position: Int) {
        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvertisementListAdapter.ViewHolder {
        val binding: ItemAdvertisementListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_advertisement_list, parent, false
        )
        return ViewHolder(binding)
    }




    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<AdvertisementModel>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemAdvertisementListBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: AdvertisementModel?) {
            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
            viewBinding.tvTitle.text  = item!!.title.plus(" (").plus(item!!.categoryname).plus(") ")



            viewBinding.root.setOnClickListener {
                onSendMessageClick?.invoke(position, item)
            }
        }
    }

}