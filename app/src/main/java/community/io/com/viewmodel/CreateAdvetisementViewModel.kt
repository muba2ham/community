package community.io.com.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.AdvertisementListResponseModel
import community.io.com.data.model.AdvlHomeResoinseModel
import community.io.com.data.model.CreateAdvertisementResponseModel
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class CreateAdvetisementViewModel(private val context: Application) : ViewModel() {


    val categoryResult: MutableLiveData<BaseResponse<AdvlHomeResoinseModel>> = MutableLiveData()
    val createAdvtResult: MutableLiveData<BaseResponse<CreateAdvertisementResponseModel>> = MutableLiveData()

    val category = MutableLiveData<String>()
    val categoryId = MutableLiveData<String>()

    val postingTitle = MutableLiveData<String>()
    val postingAdverTisement = MutableLiveData<String>()
    val postingDeals = MutableLiveData<String>()
    val postingMobile = MutableLiveData<String>()
    val postingEmail = MutableLiveData<String>()
    val postingAddress = MutableLiveData<String>()
    val postingWebsite = MutableLiveData<String>()
    val postingPrice = MutableLiveData<String>()


    val lastlogo = MutableLiveData<String>()
    val lastPicture1 = MutableLiveData<String>()
    val lastPicture2 = MutableLiveData<String>()
    val lastPicture3 = MutableLiveData<String>()
    val lastPicture4 = MutableLiveData<String>()

    val startDate = MutableLiveData<String>()
    val endDate = MutableLiveData<String>()

    val searchTerm = MutableLiveData<String>()
    val page = MutableLiveData<Int>()
    val pagesize = MutableLiveData<Int>()

    val selectedImage = MutableLiveData<Int>()

    var isRegisteredButtonEnabled = MutableLiveData(false)



    var onCategoryClick: (() -> Unit)? = null
    var onStartDateClick: (() -> Unit)? = null
    var onEndDateClick: (() -> Unit)? = null


    var onLogoClicked: (() -> Unit)? = null
    var onPicture1Clicked: (() -> Unit)? = null
    var onPicture3Clicked: (() -> Unit)? = null
    var onPicture2Clicked: (() -> Unit)? = null
    var onPicture4Clicked: (() -> Unit)? = null


    fun onLogoClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onLogoClicked?.invoke()
    }

    fun onPicture1Clicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onPicture1Clicked?.invoke()
    }

    fun onPicture3Clicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onPicture3Clicked?.invoke()
    }

    fun onPicture2Clicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onPicture2Clicked?.invoke()
    }

    fun onPicture4Clicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onPicture4Clicked?.invoke()
    }


    fun onCategoryClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onCategoryClick?.invoke()
    }


    fun onStartDateClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onStartDateClick?.invoke()
    }

    fun onEndDateClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onEndDateClick?.invoke()
    }

    fun onEventDateChanged(text: Date, isStartDate: Boolean) {

        val myFormat = "MMM.dd.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        val myFormat1 = "YYYY-MM-DD" // mention the format you need
        val sdf1 = SimpleDateFormat(myFormat1, Locale.US)

        Log.e("onEventDateChanged", "$text")
        if (isStartDate) {
            startDate.value = sdf.format(text)
//        eventStartTime .value = sdf1.format(text)
        } else {
            endDate.value = sdf.format(text)
        }
//        val c = Calendar.getInstance()
//        c.time = text
//        c.add(Calendar.DATE, 1)
//        var dt = c.time
//        endDate .value = sdf1.format(dt)
//        userPicture.set(text)
//        checkFieldCompletion()
    }

    fun onPostingAddressTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingAddress.value = text.toString()
        checkFieldCompletion()
    }

    fun onPostingWebsiteTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingWebsite.value = text.toString()
    }

    fun onPostingPriceTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingPrice.value = text.toString()
    }

    fun onPostingEmailTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingEmail.value = text.toString()
        checkFieldCompletion()
    }

    fun onPostingMobileTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingMobile.value = text.toString()
        checkFieldCompletion()

    }

    fun onDealChangeTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingDeals.value = text.toString()

    }


    fun onPostingTitleTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingTitle.value = text.toString()
        checkFieldCompletion()
    }

    fun onAdverTisementTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        postingAdverTisement.value = text.toString()
        checkFieldCompletion()

    }


    private fun checkFieldCompletion() {
        val areFieldsValid =
            postingTitle.value.toString() != null &&  postingTitle.value.toString().isNotEmpty()&&
                    postingAdverTisement.value.toString() != null && postingAdverTisement.value.toString().isNotEmpty()
                    && postingMobile.value.toString() != null && postingMobile.value.toString().isNotEmpty()
                    && postingEmail.value.toString() != null && postingEmail.value.toString().isNotEmpty()&& postingEmail.value.toString().isValidEmail()
                    && postingAddress.value.toString() != null && postingAddress.value.toString().isNotEmpty()


        Log.e("myabcdTested", "$areFieldsValid")
        isRegisteredButtonEnabled.value = areFieldsValid
    }

    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

    fun onRegisteredClicked() {

        Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
        createAdvt()

    }

    fun uploadImage(imageFile: String) {

        if (selectedImage.value == 0) {
            lastlogo.value = imageFile
        } else if (selectedImage.value == 1) {
            lastPicture1.value = imageFile
        } else if (selectedImage.value == 2) {
            lastPicture2.value = imageFile
        } else if (selectedImage.value == 3) {
            lastPicture3.value = imageFile
        } else if (selectedImage.value == 4) {
            lastPicture4.value = imageFile
        }


        uploadBlogImage(imageFile)

    }

    fun uploadBlogImage(imageFile: String) {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
//                if(!TextUtils.isEmpty(App.userPreferences.userId.toString())){
                var userId: String? = App.userPreferences.userId.first()
//                }

                Log.e("onEmailTextChanged", "$userId")

//                var targetPath = pathForImage(lastImage.value);


                var file = File(imageFile)
                val data: HashMap<String, RequestBody> = HashMap()
                data["fileName"] = RestClient.makeTextRequestBody("${file.name}")

                var serverToken: String? = App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext, "$serverToken")
                    .create(NetworkCall::class.java)
                    .uploadAdvertiseImage(
                        RestClient.makeMultipartRequestBody(
                            imageFile,
                            "file",
                            context,
                            checkMediaType = true
                        ), data
                    ).enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {

                            Log.e("uploadimageSucess2", "${t?.message}")
                            categoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {

                            if (response?.body() != null) {
                                Log.e("uploadimageSucess", "${response?.message()}")

                                Log.e("uploadimageSucess22", "${response.body().toString()}")

                                Utils.hideCustomLottieAnimationDialog()
//                                Utils.showSnackbarMessage()
//                                lastImage.value = imageFile
//                                uploadImageMessage.value = response.body()
//                                Log.e("uploadimageSucess232", "${file.extension}")

//                                locationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                Log.e("uploadimageSucess1", "${response?.message()}")
                                categoryResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })


            } catch (ex: Exception) {
                Log.e("uploadimageSucess2", "${ex?.message}")
                categoryResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun createAdvt(){
        createAdvtResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                var userId: String? = App.userPreferences.userId.first()
//                }

                Log.e("onEmailTextChanged", "$userId")

                val obj = JsonObject()
                obj.addProperty("title", postingTitle.value.toString())
                obj.addProperty("category_id", categoryId.value.toString())
                obj.addProperty("subcategory_id", "")
                obj.addProperty("description", postingAdverTisement.value.toString())

                obj.addProperty("email", postingEmail.value.toString())
                obj.addProperty("contact_number", postingMobile.value.toString())
                obj.addProperty("address", postingMobile.value.toString())
                obj.addProperty("deals",postingDeals.value.toString())
//
                obj.addProperty("website", postingWebsite.value.toString())
                obj.addProperty("price", postingPrice.value.toString())
                obj.addProperty("condition", "")
                obj.addProperty("is_negotiable", "")

                obj.addProperty("start_date", startDate.value.toString())
                obj.addProperty("end_date", endDate.value.toString())
                obj.addProperty("is_negotiable", "")
                obj.addProperty("brand", "")

                obj.addProperty("model", "")
                obj.addProperty("delivery", "")
                obj.addProperty("feature_photo", lastlogo.value.toString())
                obj.addProperty("gallery_1", lastPicture1.value.toString())

                obj.addProperty("gallery_2", lastPicture1.value.toString())
                obj.addProperty("gallery_3", lastPicture3.value.toString())
                obj.addProperty("gallery_4", lastPicture4.value.toString())
                obj.addProperty("agree_t_n_c", 1)
                obj.addProperty("created_by", "$userId")




                var serverToken: String? = App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext, "$serverToken").create(
                    NetworkCall::class.java
                )
                    .createAdvt(
                        obj
                    ).enqueue(object : Callback<CreateAdvertisementResponseModel> {
                        override fun onFailure(call: Call<CreateAdvertisementResponseModel>, t: Throwable) {
                            Log.e("onEmailTextChanged", "${t?.message}")
                            createAdvtResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<CreateAdvertisementResponseModel>,
                            response: Response<CreateAdvertisementResponseModel>
                        ) {

                            if (response?.body() != null) {
                                createAdvtResult.value = BaseResponse.Success(response?.body())
                            } else {
                                Log.e("onEmailTextChanged", "${response?.message()}")
                                createAdvtResult.value = BaseResponse.Error(response?.message())
                            }
                        }
                    })

            } catch (ex: Exception) {
                Log.e("onEmailTextChanged", "${ex.message}")

                createAdvtResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun getAdvtCategory() {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("communityId", "")
//                obj.addProperty("searchTerm", searchTerm.value.toString())
//                obj.addProperty("page", page.value.toString())
//                obj.addProperty("pagesize", pagesize.value.toString())
//                obj.addProperty("parent_id", 0)

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "$0"


                var serverToken: String? = App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext, "$serverToken").create(
                    NetworkCall::class.java
                )
                    .getCtegory(
                        obj, data
                    ).enqueue(object : Callback<AdvlHomeResoinseModel> {
                        override fun onFailure(call: Call<AdvlHomeResoinseModel>, t: Throwable) {
                            categoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<AdvlHomeResoinseModel>,
                            response: Response<AdvlHomeResoinseModel>
                        ) {

                            if (response?.body() != null) {
                                categoryResult.value = BaseResponse.Success(response?.body())
                            } else {

                                categoryResult.value = BaseResponse.Error(response?.message())
                            }
                        }
                    })

            } catch (ex: Exception) {
                categoryResult.value = BaseResponse.Error(ex.message)
            }
        }

    }
}