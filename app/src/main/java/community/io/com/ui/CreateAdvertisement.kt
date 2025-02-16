package community.io.com.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.AdvlHomeResoinseModel
import community.io.com.data.model.AdvlModel
import community.io.com.data.model.CreateAdvertisementResponseModel
import community.io.com.databinding.ActivityCreateAdvetisementBinding
import community.io.com.databinding.LocationLayoutBinding
import community.io.com.ui.adapter.AdvlCategoryShortAdapter
import community.io.com.ui.adapter.CategoryHomeAdapter
import community.io.com.utils.AddAttachmentPicker
import community.io.com.utils.Utils
import community.io.com.viewmodel.CreateAdvetisementViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class CreateAdvertisement : AppCompatActivity(), AddAttachmentPicker.ImagePickerListener {

    lateinit var loginViewModel : CreateAdvetisementViewModel

    lateinit var binding: ActivityCreateAdvetisementBinding

    private var imagePicker: AddAttachmentPicker? = null

    lateinit  var adapter : CategoryHomeAdapter

    var category_data =  mutableListOf<AdvlModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_advetisement)
        loginViewModel = CreateAdvetisementViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        this@CreateAdvertisement.let { imagePicker = AddAttachmentPicker(it, this) }
        var  baseApiUrl="http://communityapp.com/"
        var   emptyProfile= baseApiUrl+"images/advt/default.png"
        loginViewModel.lastlogo.value  = emptyProfile
        loginViewModel.lastPicture1.value  = emptyProfile
        loginViewModel.lastPicture2.value  = emptyProfile
        loginViewModel.lastPicture3.value  = emptyProfile
        loginViewModel.lastPicture4.value  = emptyProfile
        bindViewModel()
    }


    fun  bindViewModel(){

        loginViewModel.onCategoryClick = {
            opeCategorySelectDialog()
        }

        loginViewModel.onStartDateClick = {
            openDatePickerDialog(true)
        }

        loginViewModel.onEndDateClick = {
            openDatePickerDialog(false)
        }

        loginViewModel.onLogoClicked = {
            loginViewModel.selectedImage.value  = 0
            imagePicker?.show("Image")
        }

        loginViewModel.onPicture1Clicked = {
            loginViewModel.selectedImage.value  = 1
            imagePicker?.show("Image")
        }

        loginViewModel.onPicture3Clicked = {
            loginViewModel.selectedImage.value  = 2
            imagePicker?.show("Image")
        }

        loginViewModel.onPicture2Clicked = {
            loginViewModel.selectedImage.value  = 3
            imagePicker?.show("Image")
        }

        loginViewModel.onPicture4Clicked = {
            loginViewModel.selectedImage.value  = 4
            imagePicker?.show("Image")
        }


        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        loginViewModel.getAdvtCategory()




        val c = Calendar.getInstance()
//        var dt = c.time

        val myFormat = "MMM.dd.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)


           loginViewModel.startDate.value = sdf.format(c.time)

        loginViewModel.endDate.value = sdf.format(c.time)


        loginViewModel.categoryResult.observe(this) {
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


        loginViewModel.createAdvtResult.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processAdvtList(it.data)

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

    fun openDatePickerDialog(isStartDate : Boolean){
        var cal = Calendar.getInstance()

//        loginViewModel.onEventDateChanged(sdf.format(System.currentTimeMillis()))

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            loginViewModel.onEventDateChanged(cal.time, isStartDate)


//            loginViewModel.onEventDateChanged(sdf.format(cal.time))

        }
        DatePickerDialog(this@CreateAdvertisement, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)).show()

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

    fun opeCategorySelectDialog(){
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

        var adapter =  AdvlCategoryShortAdapter(this, category_data)
        binding1.rcvLocations.adapter  = adapter
        binding1.rcvLocations.layoutManager = LinearLayoutManager(this)
        binding1.rcvLocations.setHasFixedSize(true)


        binding1.btnCancel.setOnClickListener {
            alert.dismiss()
        }

        binding1.btnOkay.setOnClickListener {

            Log.e("myselectedDataaname", "myselectedDataaname")
            Log.e("myselectedDataaname", "${adapter.getSelectedPositons()}")
            var item  = category_data.get(adapter.getSelectedPositons())
            Log.e("myselectedDataaname", item.name)
            loginViewModel.categoryId.value   = item.id
            loginViewModel.category.value   = item.name

            alert.dismiss()
        }


        alert.show()

    }


    private fun processAdvtList(data: CreateAdvertisementResponseModel?) {
Log.e("responseParse", "responseParse")
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (data!!.status== 0){
            onBackPressedDispatcher.onBackPressed()
        }


    }
    private fun  processDirectoryList(data: AdvlHomeResoinseModel?) {
        Log.e("Success:", data?.message.toString())

        if (data!!.status== 0)
            if (data!!.data.size >= 0) {
                category_data.clear()
                category_data.addAll(data?.data!!)
//                adapter.updateList(category_data)
                Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

            }else{

            }

        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker?.onActivityResult(requestCode, resultCode, data)
//        data?.let { imagePicker?.onActivityResult(requestCode, resultCode, it) }
    }


    override fun onImagePathReady(path: String, title: String, fileType: Int) {
        val file = File(path)
        if (!file.exists()) return

        this.let {
            loginViewModel.uploadImage(file.absolutePath)
        }

    }

    override fun onImagePickerPermissionFailed(message: String) {
        Log.e("loadfailed", message)
    }


    override fun shouldStartActivity(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }

}