package community.io.com.api


import community.io.com.data.model.*
//import com.community.user.model.*
//import com.community.user.utils.Const
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetworkCall {




    @POST("api/api-login")
    fun loginUser(@Body obj: JsonObject): Call<UserResponceModel>


    @POST("api/api-create/")
    fun registerUser(@Body obj: JsonObject): Call<UserResponceModel>

    @POST("api/api-forget-password")
    fun forgotUserPassword(@Body obj: JsonObject): Call<UserResponceModel>

    @POST("api/api-change-password/?")
    fun changePassword(@Body obj: JsonObject, @QueryMap  param : Map<String, String>): Call<UserResponceModel>




    @POST("api/api-get-event-locations")
    fun getEventLocations(@Body obj: JsonObject): Call<LocationsResponceModel>


    @POST("api/api-get-profile/")
    fun getProfile(@QueryMap  param : Map<String, String>): Call<UserResponceModel>
//    fun getProfile(@Query("id") userEmail: String): Call<UserResponceModel>

    @POST("api/api-update-profile/?")
    fun updateProfile(@Body obj: JsonObject, @QueryMap  param : Map<String, String>): Call<UserResponceModel>


    @Multipart
    @POST("api/upload-profile-image")
    fun uploadImage(
        @Part obj: MultipartBody.Part?, @PartMap partMap: Map<String,
                @JvmSuppressWildcards RequestBody>
    ): Call<String>


//    @Multipart
//    @POST("api/upload-profile-image")
//    fun uploadImage(
//        @Part obj: MultipartBody.Part?
//    ): Call<UserResponceModel>


    @POST("api/api-get-notifications")
    fun getNotifications(@Body obj: JsonObject): Call<NotificationsResponceModel>


    @POST("api/api-get-event")
    fun getEvents(@Body obj: JsonObject): Call<EventResponseMode>

    @POST("api/api-create-event")
    fun createEvent(@Body obj: JsonObject): Call<EventResponseMode>


    @POST("api/api-get-member-contacts")
    fun getMemberContacts(@Body obj: JsonObject): Call<DirectoryResponseModel>


    @POST("api/api-get-event-locations")
    fun getAdminContacts(@Body obj: JsonObject): Call<AdminContactResponseModel>


    @POST("api/api-get-posts/")
    fun getBlogs(@Body obj: JsonObject): Call<BlogsResponseModel>

    @POST("api/api-create-post/")
    fun createPost(@Body obj: JsonObject): Call<PostCommentResponseModel>


    @Multipart
    @POST("api/upload-blog-media")
    fun uploadBlogImage(
        @Part obj: MultipartBody.Part?, @PartMap partMap: Map<String,
                @JvmSuppressWildcards RequestBody>
    ): Call<String>


    @POST("api/get-category/?")
    fun getCtegory(@Body obj: JsonObject, @QueryMap  param : Map<String, String>): Call<AdvlHomeResoinseModel>

    @POST("api/get-category/?")
    fun getCategoryAllList(@Body obj: JsonObject): Call<AdvlHomeCategoryResponseModel>


    @POST("api/get-advt")
    fun getAdvtList(@Body obj: JsonObject): Call<AdvertisementListResponseModel>

    @POST("api/get-advt-details")
    fun getAdvtDetails(@Body obj: JsonObject): Call<AdvertisementDetailResponseModel>


    @Multipart
    @POST("api/upload-advt-image")
    fun uploadAdvertiseImage(
        @Part obj: MultipartBody.Part?, @PartMap partMap: Map<String,
                @JvmSuppressWildcards RequestBody>
    ): Call<String>


    @POST("api/create-advt/")
    fun createAdvt(@Body obj: JsonObject): Call<CreateAdvertisementResponseModel>

//    @POST("api/get-category/?")
//    fun getBlogs(@Body obj: JsonObject, @QueryMap  param : Map<String, String>): Call<AdminContactResponseModel>




}