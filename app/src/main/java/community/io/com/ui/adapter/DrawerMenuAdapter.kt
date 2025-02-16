package community.io.com.ui.adapter

import android.content.Context
import android.content.res.TypedArray
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.databinding.ItemMenuDrawerBinding

class DrawerMenuAdapter(private val context: Context, private val drawerItems: TypedArray) :
    RecyclerView.Adapter<DrawerMenuAdapter.DrawerMenuViewHolder>() {

    var onItemClick: ((Int) -> Unit)? = null
    var selectedPosition = 0


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DrawerMenuAdapter.DrawerMenuViewHolder {
        val binding: ItemMenuDrawerBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_menu_drawer, parent, false
        )
        return DrawerMenuViewHolder(binding)
    }


    override fun getItemCount(): Int = drawerItems.length()


    override fun onBindViewHolder(holder: DrawerMenuAdapter.DrawerMenuViewHolder, position: Int) {

        val item = drawerItems.getString(position)
        holder.bind(position, item)
    }

    inner class DrawerMenuViewHolder(val viewBinding: ItemMenuDrawerBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: String?) {
            viewBinding.model  = item
            viewBinding.llMenu.setOnClickListener {
                selectedPosition = position
                onItemClick?.invoke(selectedPosition)
            }
            Log.e("notifiydataaaa", "$position")

        }
    }

}