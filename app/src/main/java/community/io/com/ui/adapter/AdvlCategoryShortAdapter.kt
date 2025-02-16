package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdvlModel
import community.io.com.data.model.Locations
import community.io.com.databinding.ItemCategoryBinding
import community.io.com.databinding.ItemLocationBinding

class AdvlCategoryShortAdapter(
    private val ctx: Context,
    private var locations: MutableList<AdvlModel>,
) : RecyclerView.Adapter<AdvlCategoryShortAdapter.ViewHolder>() {
    var selectedPostion   = -1

    override fun onBindViewHolder(holder: AdvlCategoryShortAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdvlCategoryShortAdapter.ViewHolder {
        val binding: ItemCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category, parent, false
        )
        return ViewHolder(binding)
    }


    fun getSelectedPositons(): Int {
        return selectedPostion
    }

    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<AdvlModel>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: AdvlModel?) {
            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
            if (selectedPostion == position){
                viewBinding.list.isChecked = true
            }else{
                viewBinding.list.isChecked = false
            }
            viewBinding.list.setOnClickListener {

                Log.e("myRadioselctPostions", "$position")

                selectedPostion  = position
                notifyDataSetChanged()

            }

        }
    }

}