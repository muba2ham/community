package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.BlogsComment
import community.io.com.data.model.BlogsModel
import community.io.com.databinding.ItemBlogCommentBinding

class BlogCommentAdapter(
    private val ctx: Context,
    private var locations: MutableList<BlogsComment>,
) : RecyclerView.Adapter<BlogCommentAdapter.ViewHolder>() {


    override fun onBindViewHolder(holder: BlogCommentAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BlogCommentAdapter.ViewHolder {
        val binding: ItemBlogCommentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_blog_comment, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

//    fun updateList(updatedList: List<BlogsModel>) {
//        locations!!.clear()
//        locations!!.addAll(updatedList)
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(val viewBinding: ItemBlogCommentBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: BlogsComment?) {
            viewBinding.model = item
            Log.e("notifiydataaaa", "$position")
        }
    }

}