package community.io.com.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.UserResponceModel
import community.io.com.databinding.ActivityChangePasswordBinding
import community.io.com.utils.Utils
import community.io.com.viewmodel.ChangePasswordViewModel


class ChangePasswordActivity : AppCompatActivity() {

    lateinit var loginViewModel: ChangePasswordViewModel

    lateinit var binding: ActivityChangePasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_change_password)
        loginViewModel = ChangePasswordViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this

        bindViewModel()

    }


    fun bindViewModel() {

        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loginViewModel.locationResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processLocationList(it.data)

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

    private fun processLocationList(data: UserResponceModel?) {
        Log.e("Success:", data?.message.toString())
        if (data!!.status == 0) {
            onBackPressedDispatcher.onBackPressed()

        } else {

        }
    }

    fun showLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Loading data...")
//        binding.prgbar.visibility = View.GONE
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())

    }

    override fun isFinishing(): Boolean {
        println("Finishing")
        return super.isFinishing()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destroyed & Finished")
    }


}