package community.io.com.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.Directory
import community.io.com.databinding.ItemDirectroyBinding


class DirectoryAdapter(
    private val ctx: Context,
    private var locations: MutableList<Directory>,
) : RecyclerView.Adapter<DirectoryAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: DirectoryAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DirectoryAdapter.ViewHolder {
        val binding: ItemDirectroyBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_directroy, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<Directory>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemDirectroyBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: Directory?) {
            viewBinding.tvName.text = "${item!!.name}"
//            viewBinding.tvFatherName.text = "${item!!.short_desc}"
//            viewBinding.tvFamilyName.text = "${item!!.startTime}"

            viewBinding.tvFatherName.text = ""
            viewBinding.tvFamilyName.text = ""


            viewBinding.tvMaritalStatus.text = "${item!!.martial_status}"
            viewBinding.tvNoofChild.text = "${item!!.no_of_children}"
            viewBinding.tvProfession.text = "${item!!.profession}"

            viewBinding.tvAddresh.text = "${item!!.hamula_1}"
            viewBinding.tvMobile.text = "${item!!.mobile}"
            viewBinding.tvEmail.text = "${item!!.email}"



            viewBinding.tvEmail.setPaintFlags(viewBinding.tvEmail.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            viewBinding.tvMobile.setPaintFlags(viewBinding.tvMobile.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")
        }
    }

}