package community.io.com.data.model

data class AdvertisementListResponseModel(
    val status : Int,
    val message : String,
    val data : MutableList<AdvertisementModel>
)


class AdvertisementModel(
    val id : String,
    val title : String,
    val category_id : String,
    val feature_photo : String,
    val gallery_1 : String,
    val condition : String,
    val created_on : String,
    val price : String,
    val categoryname : String,
    val locationname : String,
    val is_negotiable : String,
    val model : String,
    val brand : String,
    val description : String,
)