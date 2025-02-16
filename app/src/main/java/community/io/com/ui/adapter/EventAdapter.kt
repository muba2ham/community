package community.io.com.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import community.io.com.R
import community.io.com.data.model.AdvlModel
import community.io.com.data.model.Event
import community.io.com.databinding.ItemEventBinding
import java.text.SimpleDateFormat
import java.util.*

class EventAdapter(
    private val ctx: Context,
    private var locations: MutableList<Event>,
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    var onEventViewClick: ((positons : Int,event : Event) -> Unit)? = null


    override fun onBindViewHolder(holder: EventAdapter.ViewHolder, position: Int) {

        val item = locations.get(position)
        holder.bind(position, item)
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.ViewHolder {
        val binding: ItemEventBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_event, parent, false
        )
        return ViewHolder(binding)
    }



    override fun getItemCount(): Int {
        return if (locations == null) 0 else locations?.size!!
    }

    fun updateList(updatedList: List<Event>) {
        locations!!.clear()
        locations!!.addAll(updatedList)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val viewBinding: ItemEventBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(position: Int, item: Event?) {

            val simpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
            val date: Date =
                simpleDateFormat.parse(item!!.startTime)


            val dateFormat = SimpleDateFormat("d MMMM yyyy")

      var startdate : String     =    dateFormat.format(date)

            viewBinding.tvTitle.text = "${item!!.location}".plus("(").plus(startdate).plus(")")
            viewBinding.tvDescriptions.text = "${item!!.title}"
            viewBinding.tvDate.text = "${startdate}"
//            viewBinding.model  = item
            Log.e("notifiydataaaa", "$position")

            viewBinding.root.setOnClickListener {
                onEventViewClick!!.invoke(position, item)
            }
        }
    }

}