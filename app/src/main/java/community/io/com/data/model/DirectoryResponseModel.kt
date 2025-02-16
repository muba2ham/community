package community.io.com.data.model

data class DirectoryResponseModel (
    val status : Int,
    val message : String,
    val data : MutableList<Directory>
)

data class Directory(
    val id : Int,
    val name : String,
    val last_name : String,
    val middle_name : String,
    val profile_pic : String,
    val hamula_1 : String,

    val hamula_2 : String,
    val hamula_3 : String,
    val hamula_4 : String,

    val profession : String,
    val martial_status : String,
    val no_of_children : String,
    val email : String,
    val mobile : String,
    )