package community.io.com.api

//import com.google.gson.GsonBuilder
import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.webkit.MimeTypeMap
import community.io.com.BuildConfig
import community.io.com.singleton.App

import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.first
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object RestClient {

    private val MEDIA_TYPE_TEXT = MediaType.parse("multipart/form-data")
    var MEDIA_TYPE_IMAGE = MediaType.parse("placeholder/*")
//    MediaType.parse("multipart/form-data")


    fun retrofitCallBack(context: Context, serverToken1 : String  =  "" ): Retrofit {
        val trustAllCerts: Array<TrustManager> = arrayOf<TrustManager>(
            object : X509TrustManager {
                @Throws(CertificateException::class)
                override    fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                return    arrayOf()
                }

//                val acceptedIssuers: Array<X509Certificate?>?
//                    get() = arrayOf()
            }
        )


        val sslContext: SSLContext = SSLContext.getInstance("SSL")
        sslContext.init(null, trustAllCerts, SecureRandom())
        // Create an ssl socket factory with our all-trusting manager
        // Create an ssl socket factory with our all-trusting manager
        val sslSocketFactory: SSLSocketFactory = sslContext.getSocketFactory()


        val logging = HttpLoggingInterceptor()
// set your desired log level
        logging.level = HttpLoggingInterceptor.Level.BODY
//        logging.level = HttpLoggingInterceptor.Level.BASIC

        val client = OkHttpClient.Builder().readTimeout(3000, TimeUnit.SECONDS)
            .writeTimeout(300, TimeUnit.SECONDS)
            .addInterceptor(Interceptor { chain ->
                var request = chain.request()



                var   serverToken =  "Bearer "+ serverToken1

//                var  serverToken : String? =   App.userPreferences.userToken.first()
//                var serverToken = ""
//                if(!TextUtils.isEmpty(App.userPreferences.userToken.toString())){
//                    serverToken =  "Bearer "+ App.userPreferences.userToken.toString()
//                }
                request = request.newBuilder()
                    .addHeader("Authorization", serverToken)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .build()

                val response = chain.proceed(request)
//                if (response.code() == 401) {
//                if (response.code() == 999) {
//                    PrefrenceHelper(context).userLogout()
//                    val intent = Intent(context, MainActivity::class.java)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    context.startActivity(intent)

//                    exitProcess(2)
//                    return@Interceptor response
//                }
                response
            }).connectionSpecs(
                listOf(
                    ConnectionSpec.MODERN_TLS,
                    ConnectionSpec.COMPATIBLE_TLS,
                    ConnectionSpec.CLEARTEXT
                )
            )
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .sslSocketFactory(sslSocketFactory)
            .addInterceptor(logging).build()

        val gson = GsonBuilder()
            .setLenient()
            .create()

        return Retrofit.Builder()
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .baseUrl(BuildConfig.SERVICE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }


    fun makeTextRequestBody(stringData: Any): RequestBody {
        return RequestBody.create(
            MEDIA_TYPE_TEXT,
            stringData.toString()
        )

    }


    fun makeMultipartRequestBody(
        photoPath: String?,
        partName: String?,
        context: Context,
        checkMediaType : Boolean = false
    ): MultipartBody.Part? {
        try {
            val file = File(photoPath)

            var extentions = MediaType.parse("multipart/form-data")

            if (checkMediaType){
                extentions = MediaType.parse(file.getMimeType())
            }

//            var extentions = MediaType.parse(file.getMimeType())

//            val requestFile =
//                RequestBody.create(MEDIA_TYPE_IMAGE, file)

            val requestFile =
                RequestBody.create(extentions, file)

            return MultipartBody.Part.createFormData(
                partName, file.name,
//                partName, "community",
                requestFile
            )
//            return MultipartBody.Part.createFormData(
//                partName, "Community",
//                requestFile
//            )
        } catch (e: NullPointerException) {
            Log.e("MydataError", e.message!!)
            return null
        }
    }

    private fun File.getMimeType(fallback: String = "placeholder/*") : String {
        return MimeTypeMap.getFileExtensionFromUrl(toString())
            ?.run { MimeTypeMap.getSingleton().getMimeTypeFromExtension(toLowerCase()) }
            ?: fallback // You might set it to */*
    }
}


