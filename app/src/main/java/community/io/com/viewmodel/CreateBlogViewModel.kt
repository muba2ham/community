package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.BlogsResponseModel
import community.io.com.data.model.PostCommentResponseModel
import community.io.com.databinding.ActivityCreateBlogBinding
import community.io.com.singleton.App
import community.io.com.utils.Utils
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class CreateBlogViewModel(private val context: Application) :  ViewModel() {

    val directoryResult: MutableLiveData<BaseResponse<BlogsResponseModel>> = MutableLiveData()
    val commentResult: MutableLiveData<BaseResponse<PostCommentResponseModel>> = MutableLiveData()


    val searchTerm = MutableLiveData<String>()
    val page = MutableLiveData<Int>()
    val pagesize = MutableLiveData<Int>()
    val uploadImageMessage = MutableLiveData<String>()
    val lastImage =  MutableLiveData<String>()
//    val selectedImage=  MutableLiveData<String>()
    var oncreateBlogClick: (() -> Unit)? = null


    var blogTitle = MutableLiveData<String>()
    var blogDescriptions = MutableLiveData<String>()
    var isCreateButtonEnabled = MutableLiveData(false)
    var onCreaeBlogMediaClick: (() -> Unit)? = null


    fun onTitleTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        blogTitle.value = text.toString()
        checkFieldCompletion()
    }

    fun onDescriptionTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        blogDescriptions.value = text.toString()
        checkFieldCompletion()
    }

    private fun checkFieldCompletion() {
        val areFieldsValid =
            blogTitle.value.toString() != null &&  blogTitle.value.toString().isNotEmpty()&& blogTitle.value.toString().length >=5 &&
                    blogDescriptions.value.toString() != null && blogDescriptions.value.toString().isNotEmpty() && blogDescriptions.value.toString().length >=10

        Log.e("myabcdTested", "$areFieldsValid")
        isCreateButtonEnabled.value = areFieldsValid
    }



    fun onMediaSelectClicked() {
        onCreaeBlogMediaClick?.invoke()


    }
    fun onCreateClicked(){
createPost()
    }


    fun onCreateBlogClicked(){

        oncreateBlogClick!!.invoke()

    }

    fun uploadBlogImage(imageFile: String, binding: ActivityCreateBlogBinding) {
        directoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
//                if(!TextUtils.isEmpty(App.userPreferences.userId.toString())){
                var  userId : String? =  App.userPreferences.userId.first()
//                }

                Log.e("onEmailTextChanged", "$userId")

//                var targetPath = pathForImage(lastImage.value);


                var file = File(imageFile)
                val data: HashMap<String, RequestBody> = HashMap()
                data["fileName"] = RestClient.makeTextRequestBody("${file.name}")

//                val data: MutableMap<String, String> = HashMap()
//                data["fileName"] = "${file.name}"

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)
                    .uploadBlogImage(
                        RestClient.makeMultipartRequestBody(
                            imageFile,
                            "file",
                            context,
                            checkMediaType = true
                        )
                        , data
                    ).enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {

                            Log.e("uploadimageSucess2", "${t?.message}")
                            directoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {

                            if (response?.body() != null) {
                                Log.e("uploadimageSucess", "${response?.message()}")

                                Log.e("uploadimageSucess22", "${response.body().toString()}")

                                lastImage.value = imageFile
                                uploadImageMessage.value = response.body()
                                Log.e("uploadimageSucess232", "${file.extension}")

                                if(file.extension.contains("mov") || file.extension.contains("mp4")){
//                this.isVideo = true;
                                    binding.llSelectMedia.visibility = View.VISIBLE

                                }else{
//                this.isVideo = false;
                                    binding.llSelectMedia.visibility = View.GONE
                                    binding.blogCreateImage.visibility = View.VISIBLE
                                }
Utils.hideCustomLottieAnimationDialog()
//                                locationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                Log.e("uploadimageSucess1", "${response?.message()}")
                                directoryResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                Log.e("uploadimageSucess2", "${ex?.message}")
                directoryResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun getBlogs() {
        directoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                var  communityId : String? =   App.userPreferences.communityId.first()
                val obj = JsonObject()
                obj.addProperty("searchTerm", searchTerm.value.toString())
                obj.addProperty("page", page.value.toString())
                obj.addProperty("pagesize", pagesize.value.toString())
                obj.addProperty("communityId", communityId)

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

                    .getBlogs(
                        obj
//                        , data
                    ).enqueue(object : Callback<BlogsResponseModel> {
                        override fun onFailure(call: Call<BlogsResponseModel>, t: Throwable) {

                            directoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<BlogsResponseModel>,
                            response: Response<BlogsResponseModel>
                        ) {

                            if (response?.body() != null) {
                                directoryResult.value = BaseResponse.Success(response?.body())
                            } else {

                                directoryResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })


            } catch (ex: Exception) {
                directoryResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun createComment(comment: String?, parentId: String?) {

        directoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                var  userId : String? =   App.userPreferences.userId.first()
                val obj = JsonObject()
                obj.addProperty("post_title", comment)
                obj.addProperty("parent_id", parentId)
                obj.addProperty("post_by", userId)
                obj.addProperty("image", "")

//                val data: MutableMap<String, String> = HashMap()
//                data["parent_id"] = "0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

                    .createPost(
                        obj
//                        , data
                    ).enqueue(object : Callback<PostCommentResponseModel> {
                        override fun onFailure(call: Call<PostCommentResponseModel>, t: Throwable) {

                            commentResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<PostCommentResponseModel>,
                            response: Response<PostCommentResponseModel>
                        ) {

                            if (response?.body() != null) {
                                commentResult.value = BaseResponse.Success(response?.body())
                            } else {

                                commentResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })


            } catch (ex: Exception) {
                commentResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun createPost() {

        directoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                var  userId : String? =   App.userPreferences.userId.first()
                val obj = JsonObject()
                obj.addProperty("post_title", blogTitle.value.toString())
                obj.addProperty("post_description", blogDescriptions.value.toString())
                obj.addProperty("post_by", userId)
                obj.addProperty("image", lastImage.value.toString())
                obj.addProperty("parent_id", "0")

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

                    .createPost(
                        obj
//                        , data
                    ).enqueue(object : Callback<PostCommentResponseModel> {
                        override fun onFailure(call: Call<PostCommentResponseModel>, t: Throwable) {

                            commentResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<PostCommentResponseModel>,
                            response: Response<PostCommentResponseModel>
                        ) {

                            if (response?.body() != null) {
                                commentResult.value = BaseResponse.Success(response?.body())
                            } else {

                                commentResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })


            } catch (ex: Exception) {
                commentResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

}