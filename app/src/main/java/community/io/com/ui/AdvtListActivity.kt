package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.*
import community.io.com.databinding.ActivityAdvtListBinding
import community.io.com.databinding.LocationLayoutBinding
import community.io.com.ui.adapter.AdvlCategoryShortAdapter
import community.io.com.ui.adapter.CategoryHomeAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.AdvtListViewModel

class AdvtListActivity : AppCompatActivity()  {


    lateinit var loginViewModel : AdvtListViewModel

    lateinit var binding: ActivityAdvtListBinding

    lateinit  var adapter : CategoryHomeAdapter

    var category_data =  mutableListOf<AdvlModel>()
    var category_data2 =  mutableListOf<AdvlCategoryModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_advt_list)
        loginViewModel = AdvtListViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()


    }

    fun bindViewModel(){

        loginViewModel.page .value = 1
        loginViewModel.pagesize .value = 40
//        loginViewModel.locationsId.value = 0
        loginViewModel.searchTerm .value = ""
        loginViewModel.categoryId.value = ""

        loginViewModel.onCategoryClick = {
            opeCategorySelectDialog()
        }
        loginViewModel.onPostAddClick = {
            val intent = Intent(this@AdvtListActivity, CreateAdvertisement::class.java)
            startActivity(intent)

        }

        loginViewModel.onViewAdsClick = {
            val intent = Intent(this@AdvtListActivity, AdvertisementListActivity::class.java)
            intent.putExtra("keyword", loginViewModel.searchTerm.value.toString())
            intent.putExtra("location_id","")
            intent.putExtra("category_id",loginViewModel.categoryId.value.toString())
            intent.putExtra("subcategory_id","")
            startActivity(intent)
        }




         adapter =  CategoryHomeAdapter(this, mutableListOf<AdvlModel>())
        binding.rcvAdsList.adapter  = adapter
        binding.rcvAdsList.layoutManager = GridLayoutManager(this,2)
        binding.rcvAdsList.setHasFixedSize(true)

        adapter.onSendMessageClick ={ i: Int, advlModel: AdvlModel? ->

            val intent = Intent(this@AdvtListActivity, AdvertisementListActivity::class.java)
            intent.putExtra("keyword", "")
            intent.putExtra("location_id","")

            if (advlModel != null) {
                intent.putExtra("category_id", advlModel.id)

            }else{
                intent.putExtra("category_id", "")
            }

            intent.putExtra("subcategory_id","")
            startActivity(intent)
        }


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


        loginViewModel.categoryResult2.observe(this) {
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processDirectoryList2(it.data)

                }

                is BaseResponse.Error -> {
                    processError(it.msg)
                }
                else -> {
                    stopLoading()
                }
            }
        }

        loginViewModel.getAdvtCategory()
        loginViewModel.getCategoryAllList()

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


    private fun processDirectoryList2(data: AdvlHomeCategoryResponseModel?) {
        Log.e("Success:", data?.message.toString())

        if (data!!.status== 0)
            if (data!!.data.size >= 0) {
                category_data2.clear()
                category_data2.addAll(data?.data!!)
//                adapter.updateList(category_data2)
                Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

            }else{

            }
    }

    private fun  processDirectoryList(data: AdvlHomeResoinseModel?) {
        Log.e("Success:", data?.message.toString())

        if (data!!.status== 0)
        if (data!!.data.size >= 0) {
            category_data.clear()
            category_data.addAll(data?.data!!)
            adapter.updateList(category_data)
            Log.e("Success:", "${data?.data!!.size}")

//            binding.swipyrefreshlayout.isRefreshing = true

        }else{

        }
    }
}