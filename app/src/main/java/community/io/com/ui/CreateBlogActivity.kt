package community.io.com.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.api.BaseResponse
import community.io.com.data.model.PostCommentResponseModel
import community.io.com.databinding.ActivityCreateBlogBinding
import community.io.com.utils.AddAttachmentPicker
import community.io.com.utils.Utils
import community.io.com.viewmodel.CreateBlogViewModel
import java.io.File

class CreateBlogActivity: AppCompatActivity(), AddAttachmentPicker.ImagePickerListener  {

    lateinit var loginViewModel : CreateBlogViewModel

    lateinit var binding: ActivityCreateBlogBinding

    private var imagePicker: AddAttachmentPicker? = null




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_create_blog)
        loginViewModel = CreateBlogViewModel(application)
        binding.loginViewModel = loginViewModel
        binding.lifecycleOwner = this
        this@CreateBlogActivity.let { imagePicker = AddAttachmentPicker(it, this) }
        var  baseApiUrl="http://communityapp.com/"
        var   emptyProfile= baseApiUrl+"images/uploads/profile/empty-profile.png"
        loginViewModel.lastImage.value  = emptyProfile
        bindViewModel()
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
        onBackPressedDispatcher.onBackPressed()

    }

    fun  bindViewModel(){

        binding.ivToolbarBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()

        }

        loginViewModel.onCreaeBlogMediaClick  = {
            imagePicker?.show("Image")
        }

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
    }


    override fun onImagePathReady(path: String, title: String, fileType: Int) {
        val file = File(path)
        if (!file.exists()) return
        this.let {


//            if(file.extension!= ".mov" && file.extension!=".mp4"){
//
//                return
//            }
//           else
//               if(file.extension== ".mov" || file.extension ==".mp4"){
////                this.isVideo = true;
//                binding.llSelectMedia.visibility = View.VISIBLE
//
//            }else{
////                this.isVideo = false;
//                binding.llSelectMedia.visibility = View.INVISIBLE
//                binding.blogCreateImage.visibility = View.VISIBLE
//            }

            binding.tvBlogCreateImageMessage.visibility = View.VISIBLE

            loginViewModel.uploadBlogImage(file.absolutePath, binding)

//            if((file.extension!= ".mov" && file.extension!=".mp4") && options.mediaType==1){
//                this.fileUploadMessage='Only .mp4 and .mov video supported!';
//                return false;
//            }
//            if(file.extension== ".mov" || file.extension ==".mp4"||options.mediaType==1){
//                this.isVideo = true;
//            }else{
//                this.isVideo = false;
//            }
//            loginViewModel.lastImage.value   = file.absolutePath
//            loginViewModel.uploadImage()
//            profileViewModel.image = file
            Log.e("mydtata", file.absolutePath)
//            Glide.with(this).load(file).into(imageViewProfile)

//            uploadToAmazon(file, fileType)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        imagePicker?.onRequestPermissionsResult(requestCode, grantResults)
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker?.onActivityResult(requestCode, resultCode, data)
//        data?.let { imagePicker?.onActivityResult(requestCode, resultCode, it) }
    }

    override fun onImagePickerPermissionFailed(message: String) {
        Log.e("loadfailed", message)
    }

    override fun shouldStartActivity(intent: Intent, requestCode: Int) {
        startActivityForResult(intent, requestCode)
    }


}