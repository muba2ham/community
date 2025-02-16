package community.io.com.singleton

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ProcessLifecycleOwner
import community.io.com.BuildConfig
import community.io.com.data.preferences.UserPreferences
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.*
import javax.inject.Inject


@HiltAndroidApp
class App : Application() {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_data_store")

    companion object {
        lateinit var userPreferences: UserPreferences
        private lateinit var mInstance: App

        fun getAppContext(): Context = mInstance.applicationContext


    }


    override fun onCreate() {
        super.onCreate()
        mInstance = this
        userPreferences = UserPreferences(this.applicationContext.dataStore)


        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun log(priority: Int, tag: String?, message: String, t: Throwable?) =
                    super.log(priority, "ayan_$tag", message, t)

                override fun createStackElementTag(element: StackTraceElement): String =
                    String.format(
                        "(%s, Line: %s, Method: %s)",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
            })
        }
        Log.e("pppp", "ppp")


    }




}