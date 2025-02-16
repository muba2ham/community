package community.io.com.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.Directory
import community.io.com.data.model.Locations
import community.io.com.data.model.NotificationsModel
import community.io.com.data.model.NotificationsResponceModel
import community.io.com.databinding.ActivityNotificationsBinding
import community.io.com.ui.adapter.NotificationsAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.NotificationViewModel

class NotificationActivity : AppCompatActivity() {

    lateinit var loginViewModel: NotificationViewModel

    val spl_data =  mutableListOf<NotificationsModel>()
  lateinit  var adapter : NotificationsAdapter

    lateinit var binding: ActivityNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_notifications)
        loginViewModel = NotificationViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
    }


    fun bindViewModel() {


        //        ivToolbarBack.background = resources.getDrawable(R.drawable.ic_baseline_dehaze_24)

        binding.ivToolbarBack.setOnClickListener {

            onBackPressedDispatcher.onBackPressed()
        }


         adapter =  NotificationsAdapter(this, mutableListOf<NotificationsModel>())
        binding.rcvNotifications.adapter  = adapter
        binding.rcvNotifications.layoutManager = LinearLayoutManager(this)
        binding.rcvNotifications.setHasFixedSize(true)

        loginViewModel.notificationResult.observe(this) {
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




        loginViewModel.getNotifications()

    }


    private fun processLocationList(data: NotificationsResponceModel?) {
        Log.e("Success:", data?.message.toString())
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (data!!.status==0){
            spl_data.addAll(data?.data!!)
            adapter.updateList(spl_data)
            adapter.notifyDataSetChanged()
        }else{
            Log.e("Success:", "Data not found!")
        }
//        if (data!!. spl_data.size >= 0) {
//            spl_data.addAll(data?.spl_data!!)
//            Log.e("Success:", "${data?.spl_data!!.size}")
//
//        }else{
//
//        }
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



    override fun isFinishing(): Boolean {
        println("Finishing")
        return super.isFinishing()
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Destroyed & Finished")
    }

}