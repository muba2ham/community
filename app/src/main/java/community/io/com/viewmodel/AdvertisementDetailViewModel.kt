package community.io.com.viewmodel

import android.app.Application
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.AdvertisementDetail
import community.io.com.data.model.AdvertisementDetailResponseModel
import community.io.com.data.model.AdvertisementListResponseModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertisementDetailViewModel (private val context: Application) :  ViewModel() {
    val categoryResult: MutableLiveData<BaseResponse<AdvertisementDetailResponseModel>> = MutableLiveData()

    var advertisementDetail = MutableLiveData<AdvertisementDetail>()

    fun onMobileClicked(mobileNumber : String?){
        Log.e("mobileNumber",">"+mobileNumber )

    }


    fun onEmailClicked(email : String?){
        Log.e("email",">"+email )

    }


    fun getAdvtList(advt_id : String?) {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()

                if (!TextUtils.isEmpty(advt_id)){
                    obj.addProperty("advt_id", advt_id)
                }

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "$0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)
                    .getAdvtDetails(
                        obj
//                        , data
                    ).enqueue(object : Callback<AdvertisementDetailResponseModel> {
                        override fun onFailure(call: Call<AdvertisementDetailResponseModel>, t: Throwable) {
                            categoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<AdvertisementDetailResponseModel>,
                            response: Response<AdvertisementDetailResponseModel>
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