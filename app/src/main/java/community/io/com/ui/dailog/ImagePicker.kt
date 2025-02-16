package community.io.com.ui.dailog

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
//import com.community.user.R
//import com.community.user.component.dialog.ChooseImageDialog
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.os.ext.SdkExtensions.getExtensionVersion
import androidx.annotation.RequiresApi
import community.io.com.utils.ImageCompressionHelper


/**
 * Created by Kukadiya Anil on 08/02/2023.
 */

class ImagePicker(
    private val context: Context,
    private val listener: ImagePickerListener
) {

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val REQUEST_CODE_CAMERA = 2
        const val REQUEST_CODE_GALLERY_PERMISSION = 3
        const val REQUEST_CODE_CAMERA_PERMISSION = 3
        const val TAG = "ImagePicker"
        private const val ANDROID_R_REQUIRED_EXTENSION_VERSION = 2


    }

    private var title = ""

    private var filePath: String? = null

    private lateinit var imageDialog: ChooseImageDialog

    private val isGalleryPermissionGranted: Boolean
        get() {
            val lowerThanM = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            var isReadPermissionGranted =false
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                isReadPermissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED

            } else {
                 isReadPermissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
            return lowerThanM || isReadPermissionGranted
        }

    private val isCameraPermissionGranted: Boolean
        get() {
            val lowerThanM = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            val isCameraGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
//            val isWritePermissionGranted = ContextCompat.checkSelfPermission(
//                context,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ) == PackageManager.PERMISSION_GRANTED

//            return lowerThanM || isCameraGranted && isWritePermissionGranted
            return lowerThanM || isCameraGranted
        }

    fun show(title: String) {
        this.title = title
        showImagePickerDialog(title)
    }

    private fun showImagePickerDialog(dialogTitle: String) {
//        val adapter = ArrayAdapter(
//            context,
//            android.R.layout.simple_list_item_1,
//            arrayOf(
//                context.getString(R.string.image_picker_take_picture),
//                context.getString(R.string.image_picker_photo_gallery)
//            )
//        )
//
//        AlertDialog.Builder(context).apply {
//            setTitle(dialogTitle)
//            setAdapter(adapter) { _, which ->
//                if (which == 0) {
//                    openCamera()
//                } else {
//                    openGallery()
//                }
//            }
//            setNegativeButton(context.getString(R.string.image_picker_cancel), null)
//            show()
//        }
        imageDialog = ChooseImageDialog(context, View.OnClickListener {
            openCamera()
            imageDialog.dismiss()
        }, View.OnClickListener {
            openGallery()
            imageDialog.dismiss()
        }, View.OnClickListener {
            imageDialog.dismiss()
        })
        imageDialog.show()
    }

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun openGallery() {
        if (!isGalleryPermissionGranted) {
            requestGalleryPermission()
            return
        }

        val intent = Intent()
        intent.type = "image/*"
//        if (isPhotoPickerAvailable()){
                intent.action = Intent.ACTION_PICK
//            }else {
//            intent.action = Intent.ACTION_OPEN_DOCUMENT
//        }

        listener.shouldStartActivity(
            Intent.createChooser(intent, "Choose Image"),
            REQUEST_CODE_GALLERY
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun isPhotoPickerAvailable(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> true
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
                getExtensionVersion(Build.VERSION_CODES.R) >= ANDROID_R_REQUIRED_EXTENSION_VERSION
            }
            else -> false
        }
    }
    private fun openCamera() {
        if (!isCameraPermissionGranted) {
            requestCameraPermission()
            return
        }

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(context.packageManager) == null) return

        val photoFile = createImageFile() ?: return

        filePath = photoFile.absolutePath
        val photoURI =
            FileProvider.getUriForFile(
//                context,
//                context.packageName + ".provider",
                context,
                context.packageName,
                photoFile
            )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)

        listener.shouldStartActivity(intent, REQUEST_CODE_CAMERA)
    }


    private fun createImageFile(): File? {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        val mediaStorageDir =
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return null

        return File(mediaStorageDir.path + File.separator + imageFileName)
    }

    private fun requestGalleryPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
//                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )

            } else {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
    ,

            REQUEST_CODE_GALLERY_PERMISSION
        )
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES
                )

            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            },

//            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CODE_CAMERA_PERMISSION
        )
    }

    fun onRequestPermissionsResult(requestCode: Int, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                listener.onImagePickerPermissionFailed("This feature requires camera access.")
            }
        } else if (requestCode == REQUEST_CODE_GALLERY_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery()
            } else {
                listener.onImagePickerPermissionFailed("This feature requires access to a photo gallery.")
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CAMERA) {
            onImageCaptured()
        } else if (requestCode == REQUEST_CODE_GALLERY) {
            onImagePicked(data)
        }
    }

    private fun onImageCaptured() {
//        filePath?.let { listener.onImagePathReady(it, title) }

        ImageCompressionHelper(context).setImageCompressionListener(object :
            ImageCompressionHelper.ImageCompressionListener {
            override fun onImageCompression(compressionImagePath: String) {
                filePath = compressionImagePath
                filePath?.let { listener.onImagePathReady(it, title) }

            }
        }).execute(filePath)
    }

    private fun onImagePicked(data: Intent?) {
        filePath = getPath(data)

        ImageCompressionHelper(context).setImageCompressionListener(object :
            ImageCompressionHelper.ImageCompressionListener {
            override fun onImageCompression(compressionImagePath: String) {
                filePath = compressionImagePath
                filePath?.let { listener.onImagePathReady(it, title) }

            }
        }).execute(filePath)
//        filePath?.let { listener.onImagePathReady(it, title) }
    }

    private fun getPath(data: Intent?): String? {
        var path: String? = null
        val selectedImage = data?.data
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor =
            context.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            path = cursor.getString(columnIndex)
            cursor.close()
        }
        return path
    }

    interface ImagePickerListener {

        fun onImagePathReady(path: String, title: String)

        fun onImagePickerPermissionFailed(message: String)

        fun shouldStartActivity(intent: Intent, requestCode: Int)
    }
}

