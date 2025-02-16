package community.io.com.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.*
import community.io.com.databinding.ActivityContactUsBinding
import community.io.com.databinding.ActivityHaninaDirectoryBinding
import community.io.com.databinding.LocationLayoutBinding
import community.io.com.ui.adapter.AdminContactAdapter
import community.io.com.ui.adapter.LocationAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.ContactUsViewModel

class AdminContactActivity: AppCompatActivity()  {


    lateinit var loginViewModel : ContactUsViewModel

    lateinit var binding: ActivityContactUsBinding
    lateinit  var adapter : AdminContactAdapter

    val spl_data =  mutableListOf<Locations>()
//    val directory_data =  mutableListOf<AdminContact>()

    val directory_data =  mutableListOf<AdminContact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_contact_us)
        loginViewModel = ContactUsViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
    }


    fun  bindViewModel(){
        loginViewModel.locationsId.value = 0

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


        adapter =  AdminContactAdapter(this, mutableListOf<AdminContact>())
        binding.rcvEvents.adapter  = adapter
        binding.rcvEvents.layoutManager = LinearLayoutManager(this)
        binding.rcvEvents.setHasFixedSize(true)


        loginViewModel.getAdminContacts()

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
            loginViewModel.locations.value   = item.name

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



    private fun  processDirectoryList(data: AdminContactResponseModel?) {
        Log.e("Success:", data?.message.toString())
        if (data!!.data.size >= 0) {
            directory_data.addAll(data?.data!!)
            adapter.updateList(directory_data)
            Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

        }else{
//Utils.showSnackbarMessage(binding.root, data?.message.toString())
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
    }
}