package community.io.com.data.model

data class LocationsResponceModel(
    val status : Int,
    val message : String,
    val spl_data : MutableList<Locations>
)

data class Locations(
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
