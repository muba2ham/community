package community.io.com.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import community.io.com.api.BaseResponse
import community.io.com.api.NetworkCall
import community.io.com.api.RestClient
import community.io.com.data.model.EventResponseMode
import community.io.com.data.model.LocationsResponceModel
import community.io.com.singleton.App
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class EventViewModel (private val context: Application) :  ViewModel() {

  var  eventResult : MutableLiveData<BaseResponse<EventResponseMode>> = MutableLiveData()
    val locationResult: MutableLiveData<BaseResponse<LocationsResponceModel>> = MutableLiveData()
    var  createEventResult : MutableLiveData<BaseResponse<EventResponseMode>> = MutableLiveData()

    val evetLocations = MutableLiveData<String>()
    val evetLocationsId= MutableLiveData<Int>()
    val eventDate = MutableLiveData<String>()
    val eventStartTime = MutableLiveData<String>()
    val eventEndTime = MutableLiveData<String>()
    val eventDescriptions = MutableLiveData<String>()

    val eventName = MutableLiveData<String>()

    var currentMonthName  = MutableLiveData<String>()

    var isEventSaveButtonEnabled = MutableLiveData(false)



    var onAddEventClick: (() -> Unit)? = null
    var onLoadEventClick: (() -> Unit)? = null



    fun onEventNameTextChanged(text: CharSequence?) {
        Log.e("onProfileImageChange", "$text")
        eventName .value =  text.toString()
//        userPicture.set(text)
        checkFieldCompletion()
    }

    fun onEventDescriptionTextChanged(text: CharSequence?) {
        Log.e("onProfileImageChange", "$text")
        eventDescriptions .value =  text.toString()
//        userPicture.set(text)
        checkFieldCompletion()
    }

    fun onEventDateChanged(text: Date) {

        val myFormat = "MMM.dd.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        val myFormat1 = "YYYY-MM-DD" // mention the format you need
        val sdf1 = SimpleDateFormat(myFormat1, Locale.US)

        Log.e("onEventDateChanged", "$text")
        eventDate .value = sdf.format(text)
        eventStartTime .value = sdf1.format(text)
        val c = Calendar.getInstance()
        c.time = text
        c.add(Calendar.DATE, 1)
       var dt = c.time
        eventEndTime .value = sdf1.format(dt)
//        userPicture.set(text)
        checkFieldCompletion()
    }



    fun onEventLocationsChanged(text: String) {
        Log.e("onProfileImageChange", "$text")
        evetLocations .value =  text
//        userPicture.set(text)
        checkFieldCompletion()
    }


    private fun checkFieldCompletion() {

        val areFieldsValid =
            eventName.value.toString() != null &&  eventName.value.toString().isNotEmpty()
                    &&  eventDescriptions.value.toString() != null && eventDescriptions.value.toString().isNotEmpty()
                    &&  eventDate.value.toString() != null && eventDate.value.toString().isNotEmpty()
                    &&  evetLocations.value.toString() != null && evetLocations.value.toString().isNotEmpty()


        Log.e("myabcdTested", "$areFieldsValid")
        isEventSaveButtonEnabled.value = areFieldsValid
    }


    fun changeMonthName(monthame : String) {

        currentMonthName.value   = monthame
            Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
//        onAddEventClick?.invoke()

    }

    fun onAddNewEventsClicked() {

        Log.e("myRegisteredButtonClicked", "myRegisteredButtonClicked")
        onAddEventClick?.invoke()

    }

    fun onLoadEventsClicked() {
        onLoadEventClick?.invoke()
        getEvents()

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


    fun getEvents() {
        eventResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {
                val obj = JsonObject()

                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .getEvents(
                        obj
                    ).enqueue(object : Callback<EventResponseMode> {
                        override fun onFailure(call: Call<EventResponseMode>, t: Throwable) {

                            eventResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<EventResponseMode>,
                            response: Response<EventResponseMode>
                        ) {

                            if (response?.body() != null) {
                                eventResult.value = BaseResponse.Success(response?.body())
                            } else {

                                eventResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                eventResult.value = BaseResponse.Error(ex.message)
            }
        }
    }

    fun createEvent() {

        createEventResult.value = BaseResponse.Loading()
        viewModelScope.launch {
            try {

                var  userId : String? =  App.userPreferences.userId.first()

                val obj = JsonObject()
                obj.addProperty("title", eventName.value.toString())
                obj.addProperty("short_desc", eventDescriptions.value.toString())
                obj.addProperty("startTime", eventStartTime.value.toString())
                obj.addProperty("endTime", eventEndTime.value.toString())
                obj.addProperty("allDay", false)
                obj.addProperty("booked_community_id", evetLocationsId.value)
                obj.addProperty("booked_by", userId)


                var  serverToken : String? =   App.userPreferences.userToken.first()

                RestClient.retrofitCallBack(context.applicationContext,  "$serverToken").create(NetworkCall::class.java)

//                RestClient.retrofitCallBack(context.applicationContext).create(NetworkCall::class.java)
                    .createEvent(
                        obj
                    ).enqueue(object : Callback<EventResponseMode> {
                        override fun onFailure(call: Call<EventResponseMode>, t: Throwable) {

                            createEventResult.value = BaseResponse.Error(t?.message)


                        }

                        override fun onResponse(
                            call: Call<EventResponseMode>,
                            response: Response<EventResponseMode>
                        ) {

                            if (response?.body() != null) {
                                createEventResult.value = BaseResponse.Success(response?.body())
                            } else {

                                createEventResult.value = BaseResponse.Error(response?.message())
                            }

                        }

                    })



            } catch (ex: Exception) {
                createEventResult.value = BaseResponse.Error(ex.message)
            }
        }

    }
}