package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.AdminContactResponseModel
import community.io.com.data.model.DirectoryResponseModel
import community.io.com.data.model.LocationsResponceModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsViewModel (private val context: Application) :  ViewModel() {


    val locationResult: MutableLiveData<BaseResponse<LocationsResponceModel>> = MutableLiveData()
    val directoryResult: MutableLiveData<BaseResponse<AdminContactResponseModel>> = MutableLiveData()
    var onLocationClicked: (() -> Unit)? = null
    val locations = MutableLiveData<String>()
    val locationsId = MutableLiveData<Int>()



    fun onLocationClicked() {
        onLocationClicked?.invoke()
        Log.e("onLocationClicked", "onLocationClicked")
    }





    fun getAdminContacts() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("communityId", locationsId.value.toString())


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

                    .getAdminContacts(
                        obj
                    ).enqueue(object : Callback<AdminContactResponseModel> {
                        override fun onFailure(call: Call<AdminContactResponseModel>, t: Throwable) {

                            directoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<AdminContactResponseModel>,
                            response: Response<AdminContactResponseModel>
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

    fun getEventLocations() {
        locationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

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

}