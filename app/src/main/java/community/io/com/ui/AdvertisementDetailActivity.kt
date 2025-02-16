package community.io.com.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.AdvertisementDetailResponseModel
import community.io.com.databinding.ActivityAdvertisementDetailBinding
import community.io.com.utils.Utils
import community.io.com.viewmodel.AdvertisementDetailViewModel

class AdvertisementDetailActivity: AppCompatActivity()  {

    lateinit var binding: ActivityAdvertisementDetailBinding

    lateinit var loginViewModel : AdvertisementDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_advertisement_detail)
        loginViewModel = AdvertisementDetailViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
    }

    fun  bindViewModel(){
        var advt_id =  intent.getStringExtra("advt_id")


        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }


        loginViewModel.categoryResult.observe(this) {
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


        loginViewModel.getAdvtList(advt_id)


    }


    fun showLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Loading data...")
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.GONE
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())
        Utils.showSnackbarMessage(binding.root, msg.toString())
    }

    private fun  processLocationList(data: AdvertisementDetailResponseModel?) {
        Log.e("Success:", data?.message.toString())

       loginViewModel.advertisementDetail.value = data?.data?.get(0)

        Utils.showSnackbarMessage(binding.root, data?.message.toString())

    }


}