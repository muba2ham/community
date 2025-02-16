package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R

import community.io.com.databinding.ItemMartialStatusBinding

class MartialStatusAdapter(
    private val ctx: Context,
    private var locations: MutableList<String>,
) : RecyclerView.Adapter<MartialStatusAdapter.ViewHolder>() {
    var selectedPostion   = -1

    override fun onBindViewHolder(holder: MartialStatusAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MartialStatusAdapter.ViewHolder {
        val binding: ItemMartialStatusBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_martial_status, parent, false
        )
        return ViewHolder(binding)
    }



    fun getSelectedPositons(): Int {
        return selectedPostion
    }

    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }



    inner class ViewHolder(val viewBinding: ItemMartialStatusBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: String?) {
            viewBinding.model  = item
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