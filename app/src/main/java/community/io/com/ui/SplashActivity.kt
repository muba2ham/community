package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope

import community.io.com.R
import community.io.com.databinding.ActivitySplashBinding
import community.io.com.singleton.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


class SplashActivity : AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_splash)
//        loginViewModel = EventViewModel(application)
//        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this




//        when (PrefrenceHelper(this).getDarkModeEnabled()) {
//            resources.getString(R.string.text_light_theme) -> {
//                splash_image.setBackgroundResource(R.drawable.splash_logo_with_name_light)
//
//            }
//            resources.getString(R.string.text_dark_theme) -> {
//                splash_image.setBackgroundResource(R.drawable.splash_logo_with_name_dark)
//            }
//            resources.getString(R.string.text_system_default_theme) -> {
//                splash_image.setBackgroundResource(R.drawable.splash_logo_with_name_light)
//            }
//            else -> {
//                splash_image.setBackgroundResource(R.drawable.splash_logo_with_name_light)
//            }
//        }

        configureViewEvents()

    }

    override fun onResume() {
        super.onResume()

    }



    private fun configureViewEvents() {

      lifecycleScope.launch {
            var userToken = App.userPreferences.userToken.first()

            binding.splashImage.postDelayed(Runnable {

                if (userToken != null && userToken.isNotEmpty()){

                    val intent = Intent(App.getAppContext(), HomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
//                finish()
                }else{
                    val intent = Intent(App.getAppContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
//                finish()
                }


            }, 3000)



        }



    }

//    private fun gotoHomeScreen() {
//        val intent = Intent(this, HomeActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent)
//    }


}
