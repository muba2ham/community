package community.io.com.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.LocationsResponceModel
import community.io.com.data.model.NotificationsResponceModel
import community.io.com.data.model.UserResponceModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationViewModel (private val context: Application) :  ViewModel() {


    val notificationResult: MutableLiveData<BaseResponse<NotificationsResponceModel>> = MutableLiveData()


   var page=1
   var pagesize=40


    fun onInformationClicked() {
        getNotifications()


    }

    fun getNotifications() {
        notificationResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                val obj = JsonObject()
                obj.addProperty("page", page)
                obj.addProperty("pagesize", pagesize)


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .getNotifications(
                        obj
                    ).enqueue(object : Callback<NotificationsResponceModel> {
                        override fun onFailure(call: Call<NotificationsResponceModel>, t: Throwable) {

                            notificationResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<NotificationsResponceModel>,
                            response: Response<NotificationsResponceModel>
                        ) {

                            if (response?.body() != null) {
                                notificationResult.value = BaseResponse.Success(response?.body())
                            } else {

                                notificationResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                notificationResult.value = BaseResponse.Error(ex.message)
            }
        }

    }


}