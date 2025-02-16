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
import community.io.com.data.model.UserResponceModel
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//import android.support.annotation.NonNull;


class LoginViewModel(private val context: Application) :  ViewModel(), Callback<UserResponceModel> {






//    val state: LiveData<State>
//        get() = _state
//
//    private val _state = MutableLiveData<State>()

    var email = MutableLiveData<String>()
    var password = MutableLiveData<String>()


//    private var userMutableLiveData: MutableLiveData<User>? = null


    val loginResult: MutableLiveData<BaseResponse<UserResponceModel>> = MutableLiveData()
    var isLoginButtonEnabled = MutableLiveData(false)


    val errorPassword = ObservableField<String>()
    val errorEmail = ObservableField<String>()

    var onForgotClick: (() -> Unit)? = null
    var onRegisterClick: (() -> Unit)? = null


//    fun getUser(): LiveData<User?>? {
//        if (userMutableLiveData == null) {
//            userMutableLiveData = MutableLiveData()
//        }
//        return userMutableLiveData
//    }
//
    fun onLoginClicked() {

Log.e("myLoginButtonClicked", "myLoginButtonClicked")
        loginUser()

    }

    fun onForgotClicked() {
        onForgotClick?.invoke()


    }


    fun onRegisterClicked() {
        onRegisterClick?.invoke()


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



    sealed class State {
        data class NextButtonEnabled(val enable: Boolean) : State()

        data class EmailValid(val valid: Boolean) : State()

        data class PasswordValid(val valid: Boolean) : State()

        data class UserResponse(val resStatus: Int, val obj: Any?, val message: String) : State()
    }

    sealed class Event {
        object ViewCreated : Event()

        data class EmailFieldChanged(val email: String) : Event()

        data class PasswordFieldChanged(val password: String) : Event()

        object ValidateUser : Event()
    }

//    fun onEventReceived(event: Event) {
//        when (event) {
//            Event.ViewCreated -> checkFieldCompletion()
//            Event.ValidateUser -> loginUser()
//            is Event.EmailFieldChanged -> onEmailFieldChanged(event.email)
//            is Event.PasswordFieldChanged -> onPasswordFieldChanged(event.password)
//        }
//    }


//    private fun onEmailFieldChanged(email: String) {
//        _state.value =
//            State.EmailValid(
//                email.isValidEmail()
//            )
//        this.email = email
//        checkFieldCompletion()
//    }

//    private fun onPasswordFieldChanged(inputPassword: String) {
//        _state.value =
//            State.PasswordValid(
//                passwordValidator(inputPassword)
//            )
//        password = inputPassword
//        checkFieldCompletion()
//    }

    private fun checkFieldCompletion() {
        val areFieldsValid =
            email.value.toString() != null &&  email.value.toString().isNotEmpty() && email.value.toString().isValidEmail() &&  password.value.toString() != null && password.value.toString().isNotEmpty() && password.value.toString().length >=5

        Log.e("myabcdTested", "$areFieldsValid")
        isLoginButtonEnabled.value = areFieldsValid
//        _state.value =
//            State.NextButtonEnabled(
//                areFieldsValid
//            )
    }

    private fun passwordValidator(password: String): Boolean {
//        val PASSWORD_PATTERN =
//            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[*!^()@&#$%+=_]).{6,})"
//        var pattern = Pattern.compile(PASSWORD_PATTERN)
//        var matcher = pattern.matcher(password)
//        return matcher.matches()

        return  password.isNotEmpty() && password.length>=5
    }

    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }



    private fun loginUser() {

        if(!email.value.toString().isValidEmail()){
            errorEmail.set("Enter a valid email address");
//            _state.value =
//                State.EmailValid(
//                    email.isValidEmail()
//                )
        }else if (!passwordValidator(password.value.toString()) ){
            errorPassword.set("Password Length should be greater than 5");
//            _state.value =
//                State.PasswordValid(
//                    passwordValidator(password)
//                )

        }else {
            errorEmail.set(null);
            errorPassword.set(null);
            logincommunityUser()
//            Utils.showCustomLottieAnimationDialog(context.baseContext, false)

//            val obj = JsonObject()
//            obj.addProperty(Const.Params.EMAIL, email)
//            obj.addProperty(Const.Params.PASS_WORD, password)
//            obj.addProperty(Const.Params.PHONE, "")
//            obj.addProperty(Const.Params.DEVICE_TYPE, Const.DeviceType.ANDROID)
//            if (!TextUtils.isEmpty(PrefrenceHelper(context).getDeviceToken()!!)) {
//                obj.addProperty(
//                    Const.Params.DEVICE_TOKEN,
//                    PrefrenceHelper(context).getDeviceToken()
//                )
//            }
//


//            RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
//                .loginUser(
//                    obj
//                ).enqueue(this)

        }
    }

    fun logincommunityUser() {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("email", email.value.toString())
                obj.addProperty("password", password.value.toString())

                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                .loginUser(
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


    override fun onResponse(call: retrofit2.Call<UserResponceModel>, response: Response<UserResponceModel>) {
        Log.e("getSuccessresponce", response.message());
//        if (response?.body() != null) {
//            val message =  Utils.errorMessageGet(response.code(), response.body()!!.message, context)
//            _state.value =
//                State.UserResponse(
//                    response.code(), response.body(),
//                    message
////                    response.body()!!.message
//                )
//        } else {
//            val message = errorResponse(response?.errorBody()?.string())
//            _state.value =
//                State.UserResponse(
//                    response?.code()!!, null,
//                    message
//                )
//        }
    }

    override fun onFailure(call: retrofit2.Call<UserResponceModel>, t: Throwable) {
        Log.e("onFailerGoes", "onFailerGoes")
//        _state.value =
//            State.UserResponse(
//                0,
//                null,
//                t?.message.toString()
//            )
    }

//    private fun errorResponse(errorJsonString: String?): String {
//        if(errorJsonString!!.contains("message")) {
//            return JsonParser().parse(errorJsonString)
//                .asJsonObject["message"]
//                .asString
//        }else{
//            return  ""
//        }
//    }



}