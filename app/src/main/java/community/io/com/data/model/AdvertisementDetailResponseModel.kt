package community.io.com.data.model

data class AdvertisementDetailResponseModel(
    val status : Int,
    val message : String,
    val data : MutableList<AdvertisementDetail>
)

data class AdvertisementDetail(
    val id : String,
    val title : String,
    val category_id : String,
    val feature_photo : String,
    val condition : String,
    val price : String,
    val categoryname : String,
    val is_negotiable : String,
    val deals : String,
    val address : String,
    val website : String,
    val email : String,
    val model : String,
    val brand : String,
    val locationname : String,
    val name : String,
    val last_name : String,
    val middle_name : String,
    val contact_number : String,
    val created_on : String,
    val created_by : String,
    val description : String,
    val gallery : MutableList<String>,

)