package community.io.com.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.*
import community.io.com.databinding.ActivityHaninaDirectoryBinding
import community.io.com.databinding.LocationLayoutBinding
import community.io.com.singleton.App
import community.io.com.ui.adapter.DirectoryAdapter
import community.io.com.ui.adapter.EventAdapter
import community.io.com.ui.adapter.LocationAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.HaninaDirectoryViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions


class HaninaDirectoryActivity: AppCompatActivity()  {


    lateinit var loginViewModel : HaninaDirectoryViewModel

    lateinit var binding: ActivityHaninaDirectoryBinding
    val spl_data =  mutableListOf<Locations>()

    val directory_data =  mutableListOf<Directory>()

    lateinit  var adapter : DirectoryAdapter


    companion object {

        @JvmStatic // add this line !!
        @BindingAdapter("profileImage")
        fun  loadImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl).apply(RequestOptions().circleCrop())
                .into(view)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_hanina_directory)
        loginViewModel = HaninaDirectoryViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()


    }

    fun  bindViewModel(){

        loginViewModel.page .value = 1
        loginViewModel.pagesize .value = 40
        loginViewModel.locationsId.value = 0
        loginViewModel.searchTerm .value = ""
binding.swipyrefreshlayout.setOnRefreshListener {
    binding.swipyrefreshlayout.isRefreshing = true
    loginViewModel.getMemberContacts()

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



        loginViewModel.directoryResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processDirectoryList(it.data)

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }


        adapter =  DirectoryAdapter(this, mutableListOf<Directory>())
        binding.rcvEvents.adapter  = adapter
        binding.rcvEvents.layoutManager = LinearLayoutManager(this)
        binding.rcvEvents.setHasFixedSize(true)


        loginViewModel.getMemberContacts()

        loginViewModel.getEventLocations()


        loginViewModel.onLocationClicked ={
            openLocationSelectDialog()
        }
        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }


    fun openLocationSelectDialog(){
        Log.e("LocationselectionDialog:", "LocationselectionDialog:")

        val builder: AlertDialog.Builder = AlertDialog.Builder( this)

        val binding1: LocationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.location_layout,
            null,
            false
        )




        builder.setView(binding1.getRoot())
//        setContentView(binding.getRoot())
        val alert: AlertDialog = builder.create()

        var adapter =  LocationAdapter(this, spl_data)
        binding1.rcvLocations.adapter  = adapter
        binding1.rcvLocations.layoutManager = LinearLayoutManager(this)
        binding1.rcvLocations.setHasFixedSize(true)


        binding1.btnCancel.setOnClickListener {
            alert.dismiss()
        }

        binding1.btnOkay.setOnClickListener {

            Log.e("myselectedDataaname", "myselectedDataaname")
            Log.e("myselectedDataaname", "${adapter.getSelectedPositons()}")
            var item  = spl_data.get(adapter.getSelectedPositons())
            Log.e("myselectedDataaname", item.name)
            loginViewModel.locationsId.value   = item.id
//            loginViewModel.onEventLocationsChanged(item.name)
            loginViewModel.locations.value   = item.name
            loginViewModel.page .value = 1
            directory_data.clear()
            alert.dismiss()
        }


        alert.show()

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


    private fun  processDirectoryList(data: DirectoryResponseModel?) {
        Log.e("Success:", data?.message.toString())
        binding.swipyrefreshlayout.isRefreshing = false
        if (data!!.data.size >= 0) {
            directory_data.addAll(data?.data!!)

            loginViewModel.page.value = loginViewModel.page.value!!.plus(1)

            adapter.updateList(directory_data)
            Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

        }else{

        }

        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }

    private fun processLocationList(data: LocationsResponceModel?) {
        Log.e("Success:", data?.message.toString())
        if (data!!. spl_data.size >= 0) {
            spl_data.addAll(data?.spl_data!!)
            Log.e("Success:", "${data?.spl_data!!.size}")

        }else{

        }
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }


}