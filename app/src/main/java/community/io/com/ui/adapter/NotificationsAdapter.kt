package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.NotificationsModel
import community.io.com.databinding.ItemNotificationsBinding

class NotificationsAdapter(
    private val ctx: Context,
    private var notifications: MutableList<NotificationsModel>,
) : RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationsAdapter.ViewHolder {
        val binding: ItemNotificationsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_notifications, parent, false
        )
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NotificationsAdapter.ViewHolder, position: Int) {

        val item = notifications.get(position)
        holder.bind(position, item)
    }


    override fun getItemCount(): Int {
        return if (notifications == null) 0 else notifications?.size!!
    }

    fun updateList(updatedList: List<NotificationsModel>) {
        Log.e("Success:111",updatedList.size.toString())
        notifications.clear()
        notifications.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemNotificationsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: NotificationsModel) {
            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")

//            if (selectedPostion == position){
//                viewBinding.list.isChecked = true
//            }else{
//                viewBinding.list.isChecked = false
//            }
//            viewBinding.list.setOnClickListener {
////                if (selectedPostion == position){
////                    viewBinding.list.isChecked = false
////                }
//
//                Log.e("myRadioselctPostions", "$position")
//
//                selectedPostion  = position
//                notifyDataSetChanged()
////                viewBinding.notifyChange()
////                notifyDataSetChanged()
//
//            }

        }
    }

}