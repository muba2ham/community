package community.io.com.data.model

data class AdminContactResponseModel(
    val status : Int,
    val message : String,
    val data : MutableList<AdminContact>
)

data class AdminContact(
    val id : Int,
    val name : String,
    val admin_name : String,
    val email : String,
    val email_1 : String,
    val mobile : String,

    val mobile_1 : String,
    val website : String,
    val website_1 : String,

    val address_1 : String,
    val latitude : String,
    val longitude : String,
    val inactive : Int,
)