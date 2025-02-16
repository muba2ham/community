package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.UserResponceModel
import community.io.com.databinding.ActivityLoginBinding
import community.io.com.viewmodel.LoginViewModel
import community.io.com.singleton.App
import community.io.com.utils.Utils
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.observeOn
import kotlinx.coroutines.launch


class LoginActivity : AppCompatActivity() {


    lateinit var loginViewModel : LoginViewModel


    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
         loginViewModel = LoginViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
        lifecycleScope.launch {
            var userToken = App.userPreferences.userToken.first()

            if (userToken != null && userToken.isNotEmpty()){

                val intent = Intent(App.getAppContext(), HomeActivity::class.java)
                startActivity(intent)
                finish()
            }

        }

//        binding.setLoginViewModel(loginViewModel)

//        setContentView(R.layout.activity_main)

    }

    fun forgotPasswordDialog(){

        Log.e("myForgotClicked", "myForgotClicked")
    }


    fun showLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Authenticate...")
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.GONE
    }

     private  fun processLogin(data: UserResponceModel?) {
        Log.e("Success:", data?.message.toString())
         Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (data!!.access_token.length >= 0) {
            lifecycleScope.launch {
                App.userPreferences.saveToken(data.access_token)
                App.userPreferences.saveUserId(data.data.id)
                App.userPreferences.saveUserEmail(data.data.email)
                App.userPreferences.saveCommunityId(data.data.community_id)
                App.userPreferences.saveUsername(data.data.name)
                App.userPreferences.saveUserMobile(data.data.mobile)
                App.userPreferences.saveUserAddress1(data.data.address_1)

                val intent = Intent(App.getAppContext(), HomeActivity::class.java)
                startActivity(intent)
                finish()

            }

        }else{
//            Utils.showSnackbarMessage(binding.root, data?.message.toString() )
        }
//        showToast("Success:" + data?.message)
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())
//        showToast("Error:" + msg)
        Utils.showSnackbarMessage(binding.root, msg.toString())
    }


    fun bindViewModel(){

        loginViewModel.onForgotClick = {
            val intent = Intent(App.getAppContext(), ForgotPasswordActivity::class.java)
            startActivity(intent)
//            forgotPasswordDialog()
        }

        loginViewModel.onRegisterClick = {
            val intent = Intent(App.getAppContext(), RegisterActivity::class.java)
            startActivity(intent)
        }

        loginViewModel.loginResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                        processLogin(it.data)

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

    }
}