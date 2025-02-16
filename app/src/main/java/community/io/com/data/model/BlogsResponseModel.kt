package community.io.com.data.model

data class BlogsResponseModel(
    val status : Int,
    val message : String,
    val data : MutableList<BlogsModel>
)

data class BlogsModel(
    val post_id : String,
    val post_title : String,
    val post_description : String,
    val name : String,
    val middle_name : String,
    val last_name : String,

    val parent_id : String,
    val video_thumb : String,
    val video : String,
    val image : String,
    val comments : MutableList<BlogsComment>,
)

data class BlogsComment(
    val post_id : String,
    val post_title : String,
    val name : String,
    val middle_name : String,
    val last_name : String,
    val parent_id : String,
    val video_thumb : String,
    val video : String,
    val image : String,

)
