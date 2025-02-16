package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.*
import community.io.com.databinding.ActivityBlogsListBinding
import community.io.com.ui.adapter.BlogsAdapter
import community.io.com.utils.Utils
import community.io.com.viewmodel.CreateBlogViewModel
import com.bumptech.glide.Glide

class BlogsListActivity : AppCompatActivity()  {


    lateinit var loginViewModel : CreateBlogViewModel

    lateinit var binding: ActivityBlogsListBinding
    val directory_data =  mutableListOf<BlogsModel>()

    lateinit  var adapter : BlogsAdapter
      var commentPositons : Int = -1


          companion object {

        @JvmStatic // add this line !!
        @BindingAdapter("profileImage1")
        fun  loadImage(view: ImageView, imageUrl: String) {
            Glide.with(view.context)
                .load(imageUrl)
//                .apply(RequestOptions().circleCrop())
//                .load(imageUrl).apply(RequestOptions().circleCrop())
                .into(view)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_blogs_list)
        loginViewModel = CreateBlogViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        bindViewModel()


    }

    fun callCreateCommentFunctions(positons: Int , comment: String?, parentId: String?){
        commentPositons = positons
        loginViewModel.createComment(comment, parentId)
    }

    fun  bindViewModel() {


        loginViewModel.page.value = 1
        loginViewModel.pagesize.value = 40
        loginViewModel.searchTerm.value = ""


        adapter =  BlogsAdapter(this, mutableListOf<BlogsModel>())
        binding.rcvBlogs.adapter  = adapter
        binding.rcvBlogs.layoutManager = LinearLayoutManager(this)
        binding.rcvBlogs.setHasFixedSize(true)

        adapter.onSendMessageClick ={ positons: Int , comment: String?, parentId: String? ->
           callCreateCommentFunctions(positons,comment, parentId)

        }

//        binding.rcvBlogs.setNestedScrollingEnabled(false)

        loginViewModel.commentResult.observe(this){
            when (it) {
                is BaseResponse.Loading -> {
                    showLoading()
                }

                is BaseResponse.Success -> {
                    stopLoading()

                    processCommentReponse(it.data)

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


        loginViewModel.getBlogs()


        binding.swipyrefreshlayout.setOnRefreshListener {
            binding.swipyrefreshlayout.isRefreshing = true
            loginViewModel.getBlogs()

        }
            binding.ivToolbarBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

        loginViewModel.oncreateBlogClick={
            val intent = Intent(this@BlogsListActivity, CreateBlogActivity::class.java)
            startActivity(intent)
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

    private fun  processCommentReponse(data: PostCommentResponseModel?) {
        Log.e("Success:", data?.message.toString())
        Utils.showSnackbarMessage(binding.root, data?.message.toString())
        if (commentPositons!=-1){



        }

    }

    private fun  processDirectoryList(data: BlogsResponseModel?) {
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


}