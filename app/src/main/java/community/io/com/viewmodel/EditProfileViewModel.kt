package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.LocationsResponceModel
import community.io.com.data.model.UserModel
import community.io.com.data.model.UserResponceModel
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

class EditProfileViewModel(private val context: Application) :  ViewModel() {


    var email = MutableLiveData<String>()
     var mobile = MutableLiveData<String>()
    val state = MutableLiveData<String>()
    val city = MutableLiveData<String>()
    val yourProfession = MutableLiveData<String>()
    val martialStatus = MutableLiveData<String>()
    val numberOfChildren  = MutableLiveData<String>()
    val familyName = MutableLiveData<String>()
    val fatherName = MutableLiveData<String>()
    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val lastImage =  MutableLiveData<String>()

    val userData =  MutableLiveData<UserModel>()

    val loginResult: MutableLiveData<BaseResponse<UserResponceModel>> = MutableLiveData()
    val locationResult: MutableLiveData<BaseResponse<LocationsResponceModel>> = MutableLiveData()

    val errorFirstName = ObservableField<String>()
    val errorLastName = ObservableField<String>()
    val errorMobile = ObservableField<String>()
    val errorEmail = ObservableField<String>()


    var isRegisteredButtonEnabled = MutableLiveData(false)



    var onLoginClick: (() -> Unit)? = null
    var onStateClick: (() -> Unit)? = null
    var MartialStatusClicked: (() -> Unit)? = null
    var onImageClicked: (() -> Unit)? = null




    fun onImageClicked() {

        Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
        onImageClicked!!.invoke()

    }

    fun onRegisteredClicked() {

        Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
        registeredUser()

    }

    fun onMartialStatusClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        MartialStatusClicked?.invoke()
    }

    fun onStateClicked() {
        onStateClick?.invoke()
        Log.e("onStateClicked", "onStateClicked")
    }

//    fun onLoginClicked() {
//        onLoginClick?.invoke()
//    }

    fun changeUserProfiles(text: String) {
        Log.e("onProfileImageChange", "$text")
        lastImage .value =  text
//        userPicture.set(text)
    }

    fun onLastNameTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        lastName.value = text.toString()
    }

    fun onFirstNameTextChanged(text: CharSequence?) {
        Log.e("onFirstNameTextChanged", "$text")
        firstName.value = text.toString()
    }

    fun onFatherNameTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        fatherName.value = text.toString()
    }

    fun onFamilyNameTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        familyName.value = text.toString()
    }

    fun onMartialStatusTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        martialStatus.value = text.toString()
    }

    fun onNumberOfChildrenTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        numberOfChildren.value = text.toString()
    }

    fun onYourProfessionTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        yourProfession.value = text.toString()
    }

    fun onCityTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        city.value = text.toString()
    }


    fun onStateTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        state.value = text.toString()
    }

    fun onMobileTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        mobile.value = text.toString()
        checkFieldCompletion()
    }




    fun onEmailTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        email.value = text.toString()
        checkFieldCompletion()
    }




    private fun checkFieldCompletion() {

        val areFieldsValid =
            email.value.toString() != null &&  email.value.toString().isNotEmpty() && email.value.toString().isValidEmail()
                    &&  mobile.value.toString() != null && mobile.value.toString().isNotEmpty()


        Log.e("myabcdTested", "$areFieldsValid")
        isRegisteredButtonEnabled.value = areFieldsValid
    }


    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }


    private fun passwordValidator(password: String): Boolean {
//        val PASSWORD_PATTERN =
//            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[*!^()@&#$%+=_]).{6,})"
//        var pattern = Pattern.compile(PASSWORD_PATTERN)
//        var matcher = pattern.matcher(password)
//        return matcher.matches()

        return  password.isNotEmpty() && password.length>=6
    }


    private fun registeredUser() {

        if(!email.value.toString().isValidEmail()){
            errorEmail.set("Enter a valid email address");
        }
        else {
            errorEmail.set(null)
            updatecommunityUser()

        }
    }




    fun uploadImage() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
//                if(!TextUtils.isEmpty(App.userPreferences.userId.toString())){
                var  userId : String? =  App.userPreferences.userId.first()
//                }

                Log.e("onEmailTextChanged", "$userId")

//                var targetPath = pathForImage(lastImage.value);


                var file = File(lastImage.value)
                val data: HashMap<String, RequestBody> = HashMap()
                data["fileName"] = RestClient.makeTextRequestBody("${file.name}")

//                val data: MutableMap<String, String> = HashMap()
//                data["fileName"] = "${file.name}"

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)
                    .uploadImage(
                        RestClient.makeMultipartRequestBody(
                            lastImage.value!!,
                            "file",
                            context
                        )
                        , data
                    ).enqueue(object : Callback<String> {
                        override fun onFailure(call: Call<String>, t: Throwable) {

                            locationResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<String>,
                            response: Response<String>
                        ) {

                            if (response?.body() != null) {
                                Log.e("uploadimageSucess", "${response?.message()}")
Utils.hideCustomLottieAnimationDialog()
//                                locationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                locationResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                locationResult.value = BaseResponse.Error(ex.message)
            }
        }

    }


    fun getProfile() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
//                if(!TextUtils.isEmpty(App.userPreferences.userId.toString())){
                  var  userId : String? =  App.userPreferences.userId.first()
//                }

                Log.e("onEmailTextChanged", "$userId")

                val data: MutableMap<String, String> = HashMap()
                data["id"] = "$userId"

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)
                    .getProfile(
                        data
                    ).enqueue(object : Callback<UserResponceModel> {
                        override fun onFailure(call: Call<UserResponceModel>, t: Throwable) {

                            locationResult.value = BaseResponse.Error(t?.message)



                        }

                        override fun onResponse(
                            call: Call<UserResponceModel>,
                            response: Response<UserResponceModel>
                        ) {

                            if (response?.body() != null) {

                                userData.value=  response.body()!!.data

//                                 email.value = userData.value!!.email
//                                 mobile.value = userData.value!!.mobile
                                 state.value = userData.value!!.state
//                                 city.value = userData.value!!.city
//                                 yourProfession.value = userData.value!!.profession
                                 martialStatus.value = userData.value!!.martial_status
//                                 numberOfChildren.value  = userData.value!!.no_of_children
////                                 familyName.value = userData.value
////                                 fatherName.value = userData.value
//                                 firstName.value = userData.value!!.name
//                                 lastName.value = userData.value!!.last_name
                                lastImage.value =  userData.value!!.profile_pic
Utils.hideCustomLottieAnimationDialog()
//                                locationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                locationResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                locationResult.value = BaseResponse.Error(ex.message)
            }
        }

    }


    fun getEventLocations() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .getEventLocations(
                        obj
                    ).enqueue(object : Callback<LocationsResponceModel> {
                        override fun onFailure(call: Call<LocationsResponceModel>, t: Throwable) {

                            locationResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<LocationsResponceModel>,
                            response: Response<LocationsResponceModel>
                        ) {

                            if (response?.body() != null) {
                                locationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                locationResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                locationResult.value = BaseResponse.Error(ex.message)
            }
        }

    }

    fun updatecommunityUser() {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("name", firstName.value.toString())
                obj.addProperty("last_name", lastName.value.toString())
                obj.addProperty("middle_name", ' ')
                obj.addProperty("profession", yourProfession.value.toString())
                obj.addProperty("martial_status", martialStatus.value.toString())
                obj.addProperty("no_of_children", numberOfChildren.value.toString())
                obj.addProperty("email", email.value.toString())
                obj.addProperty("address_1", userData.value!!.address_1)
                obj.addProperty("address_1", userData.value!!.address_1)
                obj.addProperty("community_id", userData.value!!.community_id)
                obj.addProperty("mobile",  mobile.value.toString())


                var  userId : String? =  App.userPreferences.userId.first()

                Log.e("onEmailTextChanged", "$userId")

                val data: MutableMap<String, String> = HashMap()
                data["id"] = "$userId"

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .updateProfile(
                        obj, data
                    ).enqueue(object : Callback<UserResponceModel> {
                        override fun onFailure(call: Call<UserResponceModel>, t: Throwable) {

                            loginResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<UserResponceModel>,
                            response: Response<UserResponceModel>
                        ) {

                            if (response?.body() != null) {
                                loginResult.value = BaseResponse.Success(response?.body())
                            } else {

                                loginResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })


            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

}