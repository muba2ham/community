package community.io.com.viewmodel

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.AdvertisementListResponseModel
import community.io.com.data.model.AdvlHomeResoinseModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvertisementViewModel(private val context: Application) :  ViewModel() {


    val categoryResult: MutableLiveData<BaseResponse<AdvertisementListResponseModel>> = MutableLiveData()

    val searchTerm = MutableLiveData<String>()
    val page = MutableLiveData<Int>()
    val pagesize = MutableLiveData<Int>()


    fun getAdvertisementList(categeroyid : String?, location_id  : String?, subcategory_id : String?, keyword: String? ) {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()

                if (!TextUtils.isEmpty(categeroyid)){
                    obj.addProperty("category_id", categeroyid)
                }else{
//                    obj.addProperty("category_id", "0")
                }
                if (!TextUtils.isEmpty(location_id)){
                    obj.addProperty("location_id", location_id)
                }
                if (!TextUtils.isEmpty(subcategory_id)){
                    obj.addProperty("subcategory_id", subcategory_id)
                }
                if (!TextUtils.isEmpty(keyword)){
                    obj.addProperty("keyword", keyword)
                }
//                obj.addProperty("category_id", categeroyid)
//                obj.addProperty("location_id", location_id)
//                obj.addProperty("subcategory_id", subcategory_id)
//                obj.addProperty("keyword", keyword)
                obj.addProperty("communityId", "")
                obj.addProperty("searchTerm", searchTerm.value.toString())
                obj.addProperty("page", page.value.toString())
                obj.addProperty("pagesize", pagesize.value.toString())



                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "$0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)
                    .getAdvtList(
                        obj
//                        , data
                    ).enqueue(object : Callback<AdvertisementListResponseModel> {
                        override fun onFailure(call: Call<AdvertisementListResponseModel>, t: Throwable) {
                            categoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<AdvertisementListResponseModel>,
                            response: Response<AdvertisementListResponseModel>
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