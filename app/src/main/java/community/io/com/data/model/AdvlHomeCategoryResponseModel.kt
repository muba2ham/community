package community.io.com.data.model

class AdvlHomeCategoryResponseModel(
    val status : Int,
    val message : String,
    val data : MutableList<AdvlCategoryModel>
)


class AdvlCategoryModel(
    val id : String,
    val name : String,
    val parent_id : String,
    val image : String,
    val totaladvt : String,
)
