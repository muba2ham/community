package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.LocationsResponceModel
import community.io.com.data.model.UserResponceModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordViewModel(private val context: Application) :  ViewModel() {

    val locationResult: MutableLiveData<BaseResponse<UserResponceModel>> = MutableLiveData()

    var oldPassword = MutableLiveData<String>()
    var newPassword = MutableLiveData<String>()
    val confirmPassword = MutableLiveData<String>()

//    val errorpasswordNotMatch = ObservableField<String>()
val errorpasswordNotMatch = MutableLiveData<String>()

    var isRegisteredButtonEnabled = MutableLiveData(false)


    fun onOldPasswordTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        oldPassword.value = text.toString()

        checkFieldCompletion()
    }

    fun onNewPasswordTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        newPassword.value = text.toString()

        checkFieldCompletion()
    }

    fun onConfirmPasswordTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        confirmPassword.value = text.toString()

        checkFieldCompletion()
    }




    private fun checkFieldCompletion() {

        val areFieldsValid =
            oldPassword.value.toString() != null &&  oldPassword.value.toString().isNotEmpty()
                    &&  newPassword.value.toString() != null && newPassword.value.toString().isNotEmpty()
                    &&  confirmPassword.value.toString() != null && confirmPassword.value.toString().isNotEmpty()



        if ( newPassword.value.toString() != null && newPassword.value.toString().isNotEmpty()
            &&  confirmPassword.value.toString() != null && confirmPassword.value.toString().isNotEmpty()
        )  {

            if (newPassword.value.toString().equals(newPassword.value.toString())){
                errorpasswordNotMatch.value  = ""
            }else{
                errorpasswordNotMatch.value  = "Password mismatch"

            }


        }

        Log.e("myabcdTested", "$areFieldsValid")
        isRegisteredButtonEnabled.value = areFieldsValid
    }


    fun onUpdatePasswordClicked() {

        Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
 updatecommunityUser()

    }

    fun updatecommunityUser() {

        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("oldpassword", oldPassword.value.toString())
                obj.addProperty("newpassword", newPassword.value.toString())
                obj.addProperty("confpassword", confirmPassword.value.toString())

                var  userId : String? =  App.userPreferences.userId.first()

                Log.e("onEmailTextChanged", "$userId")

                val data: MutableMap<String, String> = HashMap()
                data["id"] = "$userId"

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .changePassword(
                        obj, data
                    ).enqueue(object : Callback<UserResponceModel> {
                        override fun onFailure(call: Call<UserResponceModel>, t: Throwable) {

                            locationResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<UserResponceModel>,
                            response: Response<UserResponceModel>
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
}