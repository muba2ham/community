package community.io.com.data.model

data class CreateAdvertisementResponseModel(
    val status : Int,
    val message : String,
    val data : CreateAdvlModel
)



class CreateAdvlModel(
    val id : String,
    val title : String,
    val category_id : String,
//    val image : String,
//    val totaladvt : String,
)