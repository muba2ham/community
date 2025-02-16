package community.io.com.data.model

class AdvlHomeResoinseModel(
    val status : Int,
    val message : String,
    val data : MutableList<AdvlModel>
)


class AdvlModel(
    val id : String,
    val name : String,
    val parent_id : String,
    val image : String,
    val totaladvt : String,
)
