package community.io.com.data.model

data class UserResponceModel(
    val status : Int,
    val access_token : String,
    val message : String,
    val data : UserModel
)


data class UserModel(
    val id : String,
    val name : String,
    val middle_name : String,
    val  last_name : String,
    val profile_pic : String,
    val hamula_1:  String,
    val hamula_2 : String,
    val hamula_3:  String,
    val hamula_4 : String,
    val profession : String,
    val martial_status :String,
    val no_of_children : String,
    val email : String,
    val password : String,
    val email_verification : String,
    val email_verified : Int,
    val address_1 : String,
    val address_2 : String,
    val community_id : String,

    val state : String,
    val city : String,
    val country :String,
    val zip_code : String,
    val mobile : String,

     val inactive : Int,
    val created_on : String,
    val device_id : String,
    val  updated_on : String,

    val is_android: Int,
    val profile_pic_name : String
    )