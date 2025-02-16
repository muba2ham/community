package community.io.com.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdminContact
import community.io.com.data.model.Directory
import community.io.com.databinding.ItemAdminContactUsBinding
import community.io.com.databinding.ItemDirectroyBinding

class AdminContactAdapter(
    private val ctx: Context,
    private var locations: MutableList<AdminContact>,
) : RecyclerView.Adapter<AdminContactAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: AdminContactAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminContactAdapter.ViewHolder {
        val binding: ItemAdminContactUsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_admin_contact_us, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<AdminContact>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemAdminContactUsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: AdminContact?) {
            viewBinding.adminName.text = "${item!!.name}"
            viewBinding.tvAdminName.text = "${item!!.admin_name}"
            viewBinding.tvMobile.text = "${item!!.mobile}"
            viewBinding.tvEmail.text = "${item!!.email}"
            viewBinding.tvWebsite.text = "${item!!.website}"


            viewBinding.tvMobile2.text = "${item!!.mobile_1}"
            viewBinding.tvEmail1.text = "${item!!.email_1}"
            viewBinding.tvWebsite2.text = "${item!!.website_1}"

            viewBinding.tvWebsite.setPaintFlags(viewBinding.tvEmail.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
            viewBinding.tvWebsite2.setPaintFlags(viewBinding.tvMobile.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

            Log.e("notifiydataaaa", "$position")
        }
    }

}