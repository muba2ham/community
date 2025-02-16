package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.AdvlHomeCategoryResponseModel
import community.io.com.data.model.AdvlHomeResoinseModel
import community.io.com.data.model.LocationsResponceModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdvtListViewModel(private val context: Application) :  ViewModel() {

    val categoryResult: MutableLiveData<BaseResponse<AdvlHomeResoinseModel>> = MutableLiveData()
    val categoryResult2: MutableLiveData<BaseResponse<AdvlHomeCategoryResponseModel>> = MutableLiveData()


    val category = MutableLiveData<String>()
    val categoryId = MutableLiveData<String>()

    val searchTerm = MutableLiveData<String>()
    val page = MutableLiveData<Int>()
    val pagesize = MutableLiveData<Int>()

    var onCategoryClick: (() -> Unit)? = null
    var onViewAdsClick: (() -> Unit)? = null
    var onPostAddClick: (() -> Unit)? = null

    fun onCategoryClicked() {
        Log.e("onMartialStatusClicked", "onMartialStatusClicked")
        onCategoryClick?.invoke()
    }

    fun onViewAdsClicked(){
        onViewAdsClick?.invoke()
    }

    fun onPostAdClicked(){
        onPostAddClick?.invoke()
    }


    fun onSearchTextChanged(text: CharSequence?) {
        Log.e("onLastNameTextChanged", "$text")
        searchTerm.value = text.toString()

    }


    fun getAdvtCategory() {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("communityId", "")
                obj.addProperty("searchTerm", searchTerm.value.toString())
                obj.addProperty("page", page.value.toString())
                obj.addProperty("pagesize", pagesize.value.toString())
                obj.addProperty("parent_id", 0)

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "$0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)
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

    fun getCategoryAllList() {
        categoryResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()
                obj.addProperty("communityId", "")
                obj.addProperty("searchTerm", searchTerm.value.toString())
                obj.addProperty("page", page.value.toString())
                obj.addProperty("pagesize", pagesize.value.toString())
                obj.addProperty("parent_id", 0)

                val data: MutableMap<String, String> = HashMap()
                data["parent_id"] = "$0"


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(
                    NetworkCall::class.java)
                    .getCategoryAllList(
                        obj
                    ).enqueue(object : Callback<AdvlHomeCategoryResponseModel> {
                        override fun onFailure(call: Call<AdvlHomeCategoryResponseModel>, t: Throwable) {
                            categoryResult.value = BaseResponse.Error(t?.message)

                        }

                        override fun onResponse(
                            call: Call<AdvlHomeCategoryResponseModel>,
                            response: Response<AdvlHomeCategoryResponseModel>
                        ) {

                            if (response?.body() != null) {
                                categoryResult2.value = BaseResponse.Success(response?.body())
                            } else {

                                categoryResult2.value = BaseResponse.Error(response?.message())
                            }
                        }
                    })

            } catch (ex: Exception) {
                categoryResult2.value = BaseResponse.Error(ex.message)
            }
        }

    }
}