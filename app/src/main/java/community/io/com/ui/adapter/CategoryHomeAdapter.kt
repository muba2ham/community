package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdvlCategoryModel
import community.io.com.data.model.AdvlModel

import community.io.com.databinding.ItemCategoryHomeBinding

class CategoryHomeAdapter(
    private val ctx: Context,
    private var locations: MutableList<AdvlModel>,
) : RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder>() {



    var onSendMessageClick: ((positons : Int,comment : AdvlModel?) -> Unit)? = null

    override fun onBindViewHolder(holder: CategoryHomeAdapter.ViewHolder, position: Int) {
        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategoryHomeAdapter.ViewHolder {
        val binding: ItemCategoryHomeBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_category_home, parent, false
        )
        return ViewHolder(binding)
    }




    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<AdvlModel>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemCategoryHomeBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: AdvlModel?) {
            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
            viewBinding.tvCategoryName.text  = item!!.name.plus(" (").plus(item!!.totaladvt).plus(") ")

            viewBinding.root.setOnClickListener {
                onSendMessageClick!!.invoke(position, item)
            }
//            viewBinding.list.setOnClickListener {
//
//                Log.e("myRadioselctPostions", "$position")
//
//                selectedPostion  = position
//
//
//            }

        }
    }

}