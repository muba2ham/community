package community.io.com.ui


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.Locations
import community.io.com.data.model.LocationsResponceModel
import community.io.com.data.model.UserResponceModel
import community.io.com.databinding.ActivityRegisterBinding
import community.io.com.databinding.LocationLayoutBinding
import community.io.com.singleton.App
import community.io.com.ui.adapter.LocationAdapter
import community.io.com.ui.adapter.MartialStatusAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.RegisterViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {

    lateinit var loginViewModel : RegisterViewModel

    val spl_data =  mutableListOf<Locations>()


    lateinit var binding: ActivityRegisterBinding

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
            DataBindingUtil.setContentView(this, R.layout.activity_register)
        loginViewModel = RegisterViewModel(application)
        binding.loginViewModel = loginViewModel

        var  baseApiUrl="http://communityapp.com/"
        var   emptyProfile= baseApiUrl+"images/uploads/profile/empty-profile.png"
        loginViewModel.changeUserProfiles(emptyProfile)

        binding.imageUrl  = emptyProfile


        binding.lifecycleOwner = this

        bindViewModel()


    }

    fun forgotPasswordDialog(){

        Log.e("myForgotClicked", "myForgotClicked")
    }


    fun showLoading() {
        Utils.showCustomLottieAnimationDialog(this, "Loading data...")
//        binding.prgbar.visibility = View.VISIBLE
    }

    fun stopLoading() {
        Utils.hideCustomLottieAnimationDialog()
//        binding.prgbar.visibility = View.GONE
    }


    private fun processLocationList(data: LocationsResponceModel?) {
        Log.e("Success:", data?.message.toString())
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (data!!. spl_data.size >= 0) {
            spl_data.addAll(data?.spl_data!!)
            Log.e("Success:", "${data?.spl_data!!.size}")

        }else{

        }
    }

    private  fun processLogin(data: UserResponceModel?) {
        Log.e("Success:", data?.message.toString())
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

//        if (data!!.access_token.length >= 0) {
//            lifecycleScope.launch {
//                App.userPreferences.saveToken(data.access_token)
//                App.userPreferences.saveUserId(data.data.id)
//                App.userPreferences.saveUserEmail(data.data.email)
//                App.userPreferences.saveCommunityId(data.data.community_id)
//                App.userPreferences.saveUsername(data.data.name)
//                App.userPreferences.saveUserMobile(data.data.mobile)
//                App.userPreferences.saveUserAddress1(data.data.address_1)
//


//            }



//        }else{
//            Utils.showSnackbarMessage(binding.root, data?.message.toString() )
//        }
    }

    fun processError(msg: String?) {
        Log.e("Error:", msg.toString())
        Utils.showSnackbarMessage(binding.root, msg.toString())
    }


    fun openMaritalStateSelectDialog(){
        Log.e("LocationselectionDialog:", "LocationselectionDialog:")

        var myStateName = mutableListOf<String>()
        myStateName.add("Married")
        myStateName.add("Unmarried")

        val builder: AlertDialog.Builder = AlertDialog.Builder( this)

        val binding: LocationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.location_layout,
            null,
            false
        )


        builder.setView(binding.getRoot())
//        setContentView(binding.getRoot())
        val alert: AlertDialog = builder.create()

        var adapter =  MartialStatusAdapter(this, myStateName)
        binding.rcvLocations.adapter  = adapter
        binding.rcvLocations.layoutManager = LinearLayoutManager(this)
        binding.rcvLocations.setHasFixedSize(true)


        binding.btnCancel.setOnClickListener {
            Log.e("myselectedDataaname11", "myselectedDataaname11")
            alert.dismiss()
        }

        binding.btnOkay.setOnClickListener {

            Log.e("myselectedDataaname", "myselectedDataaname")
//            Log.e("myselectedDataaname", "${adapter.getSelectedPositons()}")
           var item  = myStateName.get(adapter.getSelectedPositons())
//            Log.e("myselectedDataaname", item.name)
//            loginViewModel.onStateTextChanged(item.name)
               loginViewModel.martialStatus.value   = item
                alert.dismiss()
        }


        alert.show()

    }



    fun openLocationSelectDialog(){
        Log.e("LocationselectionDialog:", "LocationselectionDialog:")

        var myStateName = mutableListOf<String>()

        val builder: AlertDialog.Builder = AlertDialog.Builder( this)

        val binding: LocationLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this),
            R.layout.location_layout,
            null,
            false
        )


        builder.setView(binding.getRoot())
//        setContentView(binding.getRoot())
        val alert: AlertDialog = builder.create()

         var adapter =  LocationAdapter(this, spl_data)
        binding.rcvLocations.adapter  = adapter
        binding.rcvLocations.layoutManager = LinearLayoutManager(this)
        binding.rcvLocations.setHasFixedSize(true)


        binding.btnCancel.setOnClickListener {
            alert.dismiss()
        }

        binding.btnOkay.setOnClickListener {

            Log.e("myselectedDataaname", "myselectedDataaname")
            Log.e("myselectedDataaname", "${adapter.getSelectedPositons()}")
            var item  = spl_data.get(adapter.getSelectedPositons())
            Log.e("myselectedDataaname", item.name)
//            loginViewModel.onStateTextChanged(item.name)
            loginViewModel.state.value   = item.name
            alert.dismiss()
        }


        alert.show()

//        val listview: Listview = dialog.findViewById(R.id.listview) as ListView
//
//        val adapter = CustomAdapter(context, your_list)
//        listview.setadapter(adapter)
//        listView.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
//            //Do something
//        })

    }



    fun bindViewModel(){


//        binding.setImageUrl("https://androidwave.com/wp-content/uploads/2019/01/profile_pic.jpg");




        loginViewModel.MartialStatusClicked ={
            openMaritalStateSelectDialog()
        }

        loginViewModel.onStateClick ={
            openLocationSelectDialog()
        }

            loginViewModel.onLoginClick = {
            onBackPressedDispatcher.onBackPressed()
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

        loginViewModel.getEventLocations()

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