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

class ForgotPasswordViewModel(private val context: Application) :  ViewModel(){


    var email = MutableLiveData<String>()

    var userName = MutableLiveData<String>()
    var userEmail = MutableLiveData<String>()

    val loginResult: MutableLiveData<BaseResponse<UserResponceModel>> = MutableLiveData()
    var isLoginButtonEnabled = MutableLiveData(false)

    val errorEmail = ObservableField<String>()

    var onLoginClick: (() -> Unit)? = null
    var onRegisterClick: (() -> Unit)? = null


//
    fun onUpdatePasswordClicked() {

        Log.e("myLoginButtonClicked", "myLoginButtonClicked")
        loginUser()

    }

    fun onLoginClicked() {
        onLoginClick?.invoke()


    }


    fun onRegisterClicked() {
        onRegisterClick?.invoke()


    }

    fun onEmailTextChanged(text: CharSequence?) {
        Log.e("onEmailTextChanged", "$text")
        email.value = text.toString()
        checkFieldCompletion()
    }



    private fun checkFieldCompletion() {
        val areFieldsValid =
            email.value.toString() != null &&  email.value.toString().isNotEmpty() && email.value.toString().isValidEmail()

        Log.e("myabcdTested", "$areFieldsValid")
        isLoginButtonEnabled.value = areFieldsValid
    }


    fun String.isValidEmail(): Boolean {
        return this.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }



    private fun loginUser() {

        if(!email.value.toString().isValidEmail()){
            errorEmail.set("Enter a valid email address");
        }else {
            errorEmail.set(null)

            logincommunityUser()
        }
    }

    fun logincommunityUser() {

        loginResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("email", email.value.toString())

                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .forgotUserPassword(
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


            } catch (ex: Exception) {
                loginResult.value = BaseResponse.Error(ex.message)
            }
        }
    }





}