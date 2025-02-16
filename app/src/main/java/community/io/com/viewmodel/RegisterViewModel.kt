package community.io.com.viewmodel

import android.app.Application
import android.provider.Settings.Secure
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.LocationsResponceModel
import community.io.com.data.model.UserResponceModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterViewModel(private val context: Application) :  ViewModel() {


    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()
    var confirmPassword = MutableLiveData<String>()
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
    val userPicture =  MutableLiveData<String>()

    val loginResult: MutableLiveData<BaseResponse<UserResponceModel>> = MutableLiveData()
    val locationResult: MutableLiveData<BaseResponse<LocationsResponceModel>> = MutableLiveData()

    val errorFirstName = ObservableField<String>()
    val errorLastName = ObservableField<String>()
    val errorMobile = ObservableField<String>()
    val errorEmail = ObservableField<String>()
    val errorPassword = ObservableField<String>()
    val errorConfirmPassword = ObservableField<String>()



    var isRegisteredButtonEnabled = MutableLiveData(false)



    var onLoginClick: (() -> Unit)? = null
    var onStateClick: (() -> Unit)? = null
var MartialStatusClicked: (() -> Unit)? = null


    fun onAlreadyRegisteredClicked() {
        onLoginClick?.invoke()
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

    fun onLoginClicked() {
        onLoginClick?.invoke()
    }

    fun changeUserProfiles(text: String) {
        Log.e("onProfileImageChange", "$text")
        userPicture .value =  text
//        userPicture.set(text)
    }

    fun onLastNameTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        lastName.value = text.toString()
    }

    fun onFirstNameTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
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


    fun onPasswordConfirmTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        confirmPassword.value = text.toString()
        checkFieldCompletion()
    }

    fun onEmailTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        email.value = text.toString()
        checkFieldCompletion()
    }


    fun onPasswordTextChanged(text: CharSequence?) {
        password.value = text.toString()
        Log.e("onPasswordTextChanged", "$text")
        checkFieldCompletion()
    }

    private fun checkFieldCompletion() {

        val areFieldsValid =
            email.value.toString() != null &&  email.value.toString().isNotEmpty() && email.value.toString().isValidEmail()
                    &&  mobile.value.toString() != null && mobile.value.toString().isNotEmpty()

                    &&  password.value.toString() != null && password.value.toString().isNotEmpty() && password.value.toString().length >=6
                    &&  confirmPassword.value.toString() != null && confirmPassword.value.toString().isNotEmpty() && confirmPassword.value.toString().length >=6
                    &&  password.value.toString().equals(confirmPassword.value.toString())

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
        }else if (!passwordValidator(password.value.toString()) ){
            errorPassword.set("Password Length should be greater than 5");

        }else {
            errorEmail.set(null);
            errorPassword.set(null);
            registercommunityUser()

        }
    }


    fun getEventLocations() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()


                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
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

    fun registercommunityUser() {


       var androidId =
            Secure.getString(context.getContentResolver(), Secure.ANDROID_ID)

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
                obj.addProperty("address_1", ' ')
                obj.addProperty("community_id", ' ')
                obj.addProperty("mobile",  mobile.value.toString())
                obj.addProperty("password", password.value.toString())
                obj.addProperty("confirm_password", confirmPassword.value.toString())
                obj.addProperty("device_id", androidId)
                obj.addProperty("is_android", 1)


                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .registerUser(
                        obj
                    ).enqueue(object : Callback<UserResponceModel> {
                        override fun onFailure(call: Call<UserResponceModel>, t: Throwable) {

                            loginResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<UserResponceModel>,
                            response: Response<UserResponceModel>
                        ) {

                            if (response?.body() != null) {
//                            var msg = ""
//                            if (!TextUtils.isEmpty(response?.body()!!.message)) {
////                            msg = response.body()!!.message
//                                msg =  Utils.errorMessageGet(response.code(), response.body()!!.message, context)
//                            }
//                            _state.value =
//                                State.ChatRecordsStatusResponse(
//                                    response.code(), response.body(),
//                                    msg
//                                )

                                loginResult.value = BaseResponse.Success(response?.body())
                            } else {

                                loginResult.value = BaseResponse.Error(response?.message())
//                            val message = errorResponse(response?.errorBody()?.string())
//                        val message =  Utils.errorMessageGet(response?.code()!!, response?.errorBody()?.string(), context)
//                            _state.value =
//                                State.ChatRecordsStatusResponse(
//                                    response?.code()!!, null,
//                                    message
//                                )
                            }

                        }

                    })


//                val loginRequest = LoginRequest(
//                    password = password.value.toString(),
//                    email = email.value.toString()
//                )
//                val response = userRepo.loginUser(loginRequest = loginRequest)
//                if (response?.code() == 200) {
//                    loginResult.value = BaseResponse.Success(response.body())
//                } else {
//                    loginResult.value = BaseResponse.Error(response?.message())
//                }

            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

}