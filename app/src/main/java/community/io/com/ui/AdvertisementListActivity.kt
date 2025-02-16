package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.*
import community.io.com.databinding.ActivityAdvertisementListBinding
import community.io.com.ui.adapter.AdvertisementListAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.AdvertisementViewModel

class AdvertisementListActivity: AppCompatActivity()  {


    lateinit var binding: ActivityAdvertisementListBinding

    lateinit var loginViewModel : AdvertisementViewModel

    lateinit  var adapter : AdvertisementListAdapter

    var adverisement_list_data =  mutableListOf<AdvertisementModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_advertisement_list)
        loginViewModel = AdvertisementViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()
    }


    fun  bindViewModel(){

      var keyword =  intent.getStringExtra("keyword")
        var location_id =intent.getStringExtra("location_id")
        var category_id = intent.getStringExtra("category_id")
        var subcategory_id = intent.getStringExtra("subcategory_id")


//        intent.putExtra("keyword", loginViewModel.searchTerm.value.toString())
//        intent.putExtra("location_id","")
//        intent.putExtra("category_id",loginViewModel.categoryId.value.toString())
//        intent.putExtra("subcategory_id","")
//
        loginViewModel.page .value = 1
        loginViewModel.pagesize .value = 40
//        loginViewModel.locationsId.value = 0
        loginViewModel.searchTerm .value = ""
        binding.swipyrefreshlayout.setOnRefreshListener {
            binding.swipyrefreshlayout.isRefreshing = true
            loginViewModel.getAdvertisementList(category_id, location_id, subcategory_id, keyword)

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


        adapter =  AdvertisementListAdapter(this, mutableListOf<AdvertisementModel>())
        binding.rcvEvents.adapter  = adapter
        binding.rcvEvents.layoutManager = LinearLayoutManager(this)
        binding.rcvEvents.setHasFixedSize(true)


        adapter.onSendMessageClick = { i: Int, advlModel: AdvertisementModel? ->

            val intent =
                Intent(this@AdvertisementListActivity, AdvertisementDetailActivity::class.java)
            if (advlModel != null) {
                intent.putExtra("advt_id", advlModel.id)
            }

            startActivity(intent)
        }
        loginViewModel.getAdvertisementList(category_id, location_id, subcategory_id, keyword)

//        loginViewModel.getAdvertisementList("", "", "", "")


//        loginViewModel.onLocationClicked ={
//            openLocationSelectDialog()
//        }
        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
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

    private fun  processLocationList(data: AdvertisementListResponseModel?) {
        Log.e("Success:", data?.message.toString())
        Log.e("Success:", data?.message.toString())
        binding.swipyrefreshlayout.isRefreshing = false
        if (data!!.data.size >= 0) {
            adverisement_list_data.addAll(data?.data!!)

            loginViewModel.page.value = loginViewModel.page.value!!.plus(1)

            adapter.updateList(adverisement_list_data)
            Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

        }else{

        }

        Utils.showSnackbarMessage(binding.root, data?.message.toString())
    }

}