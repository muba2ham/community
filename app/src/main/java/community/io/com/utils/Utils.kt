package community.io.com.utils

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import community.io.com.R
import com.google.android.material.snackbar.Snackbar

object Utils {



    private var progressDialog: ProgressDialog? = null

    fun showSnackbarMessage(view: View?, message: String?) {
        if (view != null) {
            Snackbar.make(view, message ?: "No message", Snackbar.LENGTH_LONG).show()
        }

    }



    fun showCustomLottieAnimationDialog(context: Context, message: String? ) {

        if (progressDialog != null && progressDialog!!.isShowing()) {

            return
        }

        if (!(context as AppCompatActivity).isFinishing) {
            progressDialog = ProgressDialog(context);
            progressDialog!!.setIndeterminate(true);
            progressDialog!!.setMessage(message);
            progressDialog!!.show();
        }

//        if (dialog != null && dialog!!.isShowing()) {
//            return
//        }
//        if (!(context as AppCompatActivity).isFinishing) {
//            dialog = Dialog(context)
//            dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
//
//            dialog!!.setContentView(R.layout.layout_progress)
//            dialog!!.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            //            ivProgressBar = (CircularProgressView) dialog.findViewById(R.id.ivProgressBarTwo);
////            ivProgressBar.startAnimation();
//            dialog!!.setCancelable(isCancel)
//            val params: WindowManager.LayoutParams =
//                dialog!!.getWindow()!!.getAttributes()
//            params.height = WindowManager.LayoutParams.MATCH_PARENT
//            params.width = WindowManager.LayoutParams.MATCH_PARENT
//            dialog!!.getWindow()!!.setAttributes(params)
//            dialog!!.getWindow()!!.setDimAmount(0f)
//            dialog!!.show()
//        }
    }


    fun hideCustomLottieAnimationDialog() {
        try {
            if (progressDialog != null) {
                progressDialog!!.dismiss()
            }
        } catch (e: Exception) {
            Log.e("myException", e.message!!)
        }
    }


}