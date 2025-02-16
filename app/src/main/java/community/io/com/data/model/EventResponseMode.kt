package community.io.com.data.model


data class EventResponseMode(
    val status : Int,
    val message : String,
    val data : MutableList<Event>
)

data class Event(
    val name : String,
    val title : String,
    val short_desc : String,
    val startTime : String,
    val endTime : String,
    val location : String,

    val allDay : Boolean,

)