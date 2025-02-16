package community.io.com.ui.adapter


import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.Locations
import community.io.com.databinding.ItemLocationBinding


class LocationAdapter(
    private val ctx: Context,
    private var locations: MutableList<Locations>,
) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    var selectedPostion   = -1

        override fun onBindViewHolder(holder: LocationAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LocationAdapter.ViewHolder {
        val binding: ItemLocationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_location, parent, false
        )
        return ViewHolder(binding)
    }

//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): LocationAdapter.ViewHolder =
//        ViewHolder(
//            ItemLocationBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )


    fun getSelectedPositons(): Int {
        return selectedPostion
    }

    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<Locations>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemLocationBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: Locations?) {
            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
            if (selectedPostion == position){
                viewBinding.list.isChecked = true
            }else{
                viewBinding.list.isChecked = false
            }
            viewBinding.list.setOnClickListener {
//                if (selectedPostion == position){
//                    viewBinding.list.isChecked = false
//                }

                Log.e("myRadioselctPostions", "$position")

                selectedPostion  = position
                notifyDataSetChanged()
//                viewBinding.notifyChange()
//                notifyDataSetChanged()

            }

        }
    }

}