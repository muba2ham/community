package community.io.com.ui.dailog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.databinding.DialogAddAttachmentBinding
import community.io.com.databinding.DialogChooseImageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog



class ChooseImageDialog(
    private val mContext: Context?,
    private val cameraClickListener: View.OnClickListener,
    private val galleryClickListener: View.OnClickListener,
    private val closeClickListener : View.OnClickListener
) : BottomSheetDialog(mContext!!, R.style.CustomBottomSheetDailog)
{
    init {

        val binding1: DialogChooseImageBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.dialog_choose_image,
            null,
            false
        )
        setContentView(binding1.root)
        setCancelable(false)

        binding1.textViewGallery.setOnClickListener(galleryClickListener)
        binding1.textViewCamera.setOnClickListener(cameraClickListener)

//        textViewGallery.setOnClickListener(galleryClickListener)
//
//        textViewCamera.setOnClickListener(cameraClickListener)
//
//        imageClose.setOnClickListener(closeClickListener)

        this.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}

//class ChooseImageDialog(
//    private val mContext: Context?, private val cameraClickListener: View.OnClickListener,
//    private val galleryClickListener: View.OnClickListener
//) : Dialog(mContext!!, R.style.CustomDialog) {
//    init {
//        setContentView(R.layout.dialog_choose_image)
//
//        textViewGallery.setOnClickListener(galleryClickListener)
//
//        textViewCamera.setOnClickListener(cameraClickListener)
//
//        this.window!!.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.MATCH_PARENT
//        )
//    }
//}