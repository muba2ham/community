package community.io.com.data.model

data class PostCommentResponseModel(
    val status : Int,
    val message : String,
    val data : PostComment
)



data class PostComment(
    val post_id : Int,
    val post_title : String,
    val post_description : String,
    val video_thumb : String,
    val video : String,
    val image : String,
    val parent_id : Int,
    val post_by : String,
    val created_on : String,
    val published : Int,


    )
