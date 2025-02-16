package community.io.com.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdminContact
import community.io.com.data.model.BlogsModel
import community.io.com.databinding.ItemAdminContactUsBinding
import community.io.com.databinding.ItemBlogsBinding
import community.io.com.utils.Utils

class BlogsAdapter(
    private val ctx: Context,
    private var locations: MutableList<BlogsModel>,
) : RecyclerView.Adapter<BlogsAdapter.ViewHolder>() {

    var onSendMessageClick: ((positons : Int,comment : String?, parentid:String?) -> Unit)? = null

    var onattachDocumentClick: ((positons : Int,parentid:String?) -> Unit)? = null
    var clickedPositons : Int  = -1

    override fun onBindViewHolder(holder: BlogsAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlogsAdapter.ViewHolder {
        val binding: ItemBlogsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_blogs, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<BlogsModel>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemBlogsBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: BlogsModel?) {
            viewBinding.model = item
           var adapter =  BlogCommentAdapter(viewBinding.root.context, item!!.comments)
            viewBinding.rcvBlogsComment.adapter  = adapter
            viewBinding.rcvBlogsComment.layoutManager = LinearLayoutManager(viewBinding.root.context)
            viewBinding.rcvBlogsComment.setHasFixedSize(true)

            viewBinding.sendComment.setOnClickListener {
                if (!TextUtils.isEmpty(viewBinding.etCooment.text.trim().toString())) {

                    onSendMessageClick?.invoke(position,
                        viewBinding.etCooment.text.trim().toString(),
                        item.parent_id
                    )
                }
            }


            viewBinding.attachDocuments.setOnClickListener {
                onattachDocumentClick?.invoke(
                    position,
                    item.parent_id
                )
            }
//            viewBinding.adminName.text = "${item!!.name}"
//            viewBinding.tvAdminName.text = "${item!!.admin_name}"
//            viewBinding.tvMobile.text = "${item!!.mobile}"
//            viewBinding.tvEmail.text = "${item!!.email}"
//            viewBinding.tvWebsite.text = "${item!!.website}"
//
//
//            viewBinding.tvMobile2.text = "${item!!.mobile_1}"
//            viewBinding.tvEmail1.text = "${item!!.email_1}"
//            viewBinding.tvWebsite2.text = "${item!!.website_1}"
//
//            viewBinding.tvWebsite.setPaintFlags(viewBinding.tvEmail.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
//            viewBinding.tvWebsite2.setPaintFlags(viewBinding.tvMobile.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

            Log.e("notifiydataaaa", "$position")
        }
    }

}