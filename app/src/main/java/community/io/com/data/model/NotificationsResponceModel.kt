package community.io.com.data.model

data class NotificationsResponceModel(


    val status : Int,
    val message : String,
    val data : MutableList<NotificationsModel>
)


data class NotificationsModel(
    val id : Int,
    val message : String,
    val created_on : String,
    val is_announcement: Int,
    val message_type : String
)