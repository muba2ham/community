package community.io.com.ui.dailog


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import community.io.com.R
import community.io.com.databinding.DialogAddAttachmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialog



class AddAtachmentDialog(
    private val mContext: Context?,
    private val cameraClickListener: View.OnClickListener,
    private val galleryClickListener: View.OnClickListener,
//    private val documentClickListener: View.OnClickListener,
//    private val contactClickListener: View.OnClickListener,
    private val closeClickListener: View.OnClickListener,
//    private val locationClickListener: View.OnClickListener
) : BottomSheetDialog(mContext!!, R.style.CustomBottomSheetDailog) {
    init {


        val binding1: DialogAddAttachmentBinding = DataBindingUtil.inflate(
            LayoutInflater.from(mContext),
            R.layout.dialog_add_attachment,
            null,
            false
        )



        setContentView(binding1.getRoot())
        setCancelable(false)

        binding1.textViewCamera.setOnClickListener(cameraClickListener)
        binding1.textViewGallery.setOnClickListener(galleryClickListener)
        binding1.textViewDocument.setOnClickListener(closeClickListener)

//        textViewGallery.setOnClickListener(galleryClickListener)
//
//        textViewCamera.setOnClickListener(cameraClickListener)
//
//        textViewDocument.setOnClickListener(documentClickListener)
//
//        textViewContact.setOnClickListener(contactClickListener)
//
//        imageClose.setOnClickListener(closeClickListener)
//
//        textViewLocation.setOnClickListener (locationClickListener )

        this.window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}