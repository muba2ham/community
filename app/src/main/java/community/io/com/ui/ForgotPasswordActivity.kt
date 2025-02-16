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
import community.io.com.databinding.ActivityForgotPasswordBinding

import community.io.com.singleton.App
import community.io.com.utils.Utils
import community.io.com.viewmodel.ForgotPasswordViewModel
import kotlinx.coroutines.launch

class ForgotPasswordActivity : AppCompatActivity() {


    lateinit var loginViewModel : ForgotPasswordViewModel


    lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        loginViewModel = ForgotPasswordViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()


    }



    fun showLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Loading data...")
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.GONE
    }

    private  fun processLogin(data: UserResponceModel?) {
        Log.e("Success:", data?.message.toString())
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (data!!.status >= 0) {
Utils.showSnackbarMessage(binding.root, data?.message.toString() )
            onBackPressedDispatcher.onBackPressed()
        }else{

            Utils.showSnackbarMessage(binding.root, data?.message.toString() )
        }
//        showToast("Success:" + data?.message)
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())
//        showToast("Error:" + msg)
        Utils.showSnackbarMessage(binding.root, msg.toString())
    }


    fun bindViewModel(){

        loginViewModel.onLoginClick = {
            onBackPressedDispatcher.onBackPressed()
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