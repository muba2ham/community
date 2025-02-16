package community.io.com.utils

import android.Manifest

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import community.io.com.ui.CreateAdvertisement
import community.io.com.ui.dailog.AddAtachmentDialog
import community.io.com.ui.dailog.ImagePicker
//import com.shat.imagetofile.RealPathUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AddAttachmentPicker(
    private val context: Context,
    private val listener: ImagePickerListener
) {

    companion object {
        const val REQUEST_CODE_GALLERY = 1
        const val REQUEST_CODE_CAMERA = 2
        const val REQUEST_CODE_DOCUMENT = 3
        const val REQUEST_CODE_CONTACT = 4
        const val REQUEST_CODE_GALLERY_PERMISSION = 3
        const val REQUEST_CODE_CAMERA_PERMISSION = 3
        const val REQUEST_LOCATION_PERMISSION = 5
        const val TAG = "ImagePicker"
    }

    private var title = ""

    private var filePath: String? = null

    private lateinit var imageDialog: AddAtachmentDialog


    private val isLocationPermissionGrant: Boolean
        get() {
            val lowerThanM = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            val isReadPermissionGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED

            return lowerThanM || isReadPermissionGranted
        }



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

    val isCameraPermissionGranted: Boolean
        get() {
            val lowerThanM = Build.VERSION.SDK_INT < Build.VERSION_CODES.M
            var isCameraGranted = false
            var isWritePermissionGranted = false
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {

                isCameraGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
//                var isImages = ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.READ_MEDIA_IMAGES
//                ) == PackageManager.PERMISSION_GRANTED
//
//                var isVideo = ContextCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.READ_MEDIA_VIDEO
//                ) == PackageManager.PERMISSION_GRANTED
//
//
//                isWritePermissionGranted = isImages && isVideo
                return isCameraGranted
            } else {
                isCameraGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
                isWritePermissionGranted = ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
            return lowerThanM || isCameraGranted && isWritePermissionGranted
//            return lowerThanM || isCameraGranted
        }

    fun show(title: String) {
        this.title = title

        showImagePickerDialog(title)
    }

    private fun showImagePickerDialog(dialogTitle: String) {
        imageDialog = AddAtachmentDialog(context,
            View.OnClickListener {
                openCamera()
                imageDialog.dismiss()
            },
            View.OnClickListener {
                openGallery()
                imageDialog.dismiss()
            },
//            View.OnClickListener {
//                openDocument()
//                imageDialog.dismiss()
//            },
//            View.OnClickListener {
//                openContact()
//                imageDialog.dismiss()
//            },
            View.OnClickListener {
                imageDialog.dismiss()
            },

//            View.OnClickListener {
//                openCurrentLocation()
//                imageDialog.dismiss()
//            }
        )

        imageDialog.show()
    }


//    private fun openDocument() {
//        if (!isGalleryPermissionGranted) {
//            requestGalleryPermission()
//            return
//        }
////        val mimeTypes = arrayOf(
////            "application/msword",
////            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
////            "application/vnd.ms-powerpoint",
////            "application/vnd.openxmlformats-officedocument.presentationml.presentation",  // .ppt & .pptx
////            "application/vnd.ms-excel",
////            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
////            "text/plain",
////            "application/pdf",
////            "application/zip",
////            "application/vnd.android.package-archive"
////        )
////
////
////        val intent = Intent()
//////        intent.type = "*/*"
////        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
//////        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
//////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////
////        intent.action = Intent.ACTION_PICK
////        listener.shouldStartActivity(
////            Intent.createChooser(intent, context.getString(R.string.choose_image)),
////            REQUEST_CODE_DOCUMENT
////        )
////
//
////        val intent = Intent(Intent.ACTION_GET_CONTENT)
////        intent.type = "*/*"
////        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
////        intent.addCategory(Intent.CATEGORY_OPENABLE)
////        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
////        intent.action = Intent.ACTION_PICK
//
//
//        val intent = Intent()
//        intent.type = "*/*"
////        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
////        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
////        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//
//        intent.action = Intent.ACTION_PICK
//        listener.shouldStartActivity(
//            Intent.createChooser(intent, context.getString(R.string.choose_image)),
//            REQUEST_CODE_DOCUMENT
//        )
//
////        val uri: Uri = Uri.parse("content://contacts")
////        val intent = Intent(Intent.ACTION_PICK)
////        intent.type = Intent.ACTION_OPEN_DOCUMENT
////        startActivityForResult(intent, REQUEST_CODE)
//    }


//    private fun openContact() {
//        val uri: Uri = Uri.parse("content://contacts")
//        val intent = Intent(Intent.ACTION_PICK, uri)
//        intent.type = Phone.CONTENT_TYPE
//        listener.shouldStartActivity(
//            Intent.createChooser(intent, context.getString(R.string.choose_image)),
//            REQUEST_CODE_CONTACT
//        )
////        startActivityForResult(intent, REQUEST_CODE)
//    }

    private fun openGallery() {
        if (!isGalleryPermissionGranted) {
            requestGalleryPermission()
            return
        }

        val intent = Intent()
        intent.type = "image/*  video/*"
        if (Build.VERSION.SDK_INT <= 19) {


        } else {
//            intent.type = "image/*  video/*"
            val mimeTypes = arrayOf("image/* , video/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
        }
        intent.action = Intent.ACTION_PICK

        listener.shouldStartActivity(
            Intent.createChooser(intent, "Choose image"),
            REQUEST_CODE_GALLERY
        )
    }

//    private fun openCurrentLocation() {
//
//        if (!isLocationPermissionGrant) {
//            requestLocationPermission()
//            return
//        }
//
//
//        openCurrentLocationDialog()
//        Log.e("locationDialogOpen", "locationDialogOpen")
//    }


//    private fun openCurrentLocationDialog() {
//
//        currentLocationDialog = CurrentLocationDialog(context,
//            View.OnClickListener {
//
//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    listener.onImagePickerPermissionFailed("This feature requires location access.")
//                } else {
//
//                    fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
//
////                    fusedLocationClient!!.lastLocation
////                        .addOnSuccessListener { location: Location? ->
////                            // Got last known location. In some rare situations this can be null.
////
////                            Log.e("userLocation", Gson().toJson(location))
////                            if (location!=null) {
////                                listener.onLocationReady(location!!)
////                            }else{
////                                listener.onImagePickerPermissionFailed(context.resources.getString(R.string.text_location_not_found_try_again))
////                            }
////                        }
//
//
//                    fusedLocationClient!!.lastLocation
//                        .addOnSuccessListener { location: Location? ->
//                            // Got last known location. In some rare situations this can be null.
//
//                            Log.e("userLocation", Gson().toJson(location))
//                            if (location != null) {
//                                listener.onLocationReady(location!!)
//                            } else {
//                                buildLocationCallback()
////                                listener.onImagePickerPermissionFailed(context.resources.getString(R.string.text_location_not_found_try_again))
//                            }
//                        }
//                }
//
//
//                currentLocationDialog.dismiss()
//            },
//            View.OnClickListener {
//                currentLocationDialog.dismiss()
//            }
//
//        )
//
//        currentLocationDialog.show()
//
//    }


//    private fun buildLocationCallback() {
//        var locationCallback = object : LocationCallback() {
//            override fun onLocationResult(locationResult: LocationResult) {
//                if (locationResult == null) {
//                    return
//                }
//                for (location in locationResult.locations) {
//                    // Update UI with location data
////                    currentLocation = location
//                    Log.e("TAG", "onLocationResult: " + location.getLatitude())
//                }
//            }
//        }
//        var locationRequest = LocationRequest.create();
//        locationRequest.setInterval(10000);
//        locationRequest.setFastestInterval(5000);
//        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return
//        }
//        fusedLocationClient.requestLocationUpdates(
//            locationRequest,
//            locationCallback,
//            Looper.myLooper()
//        );
//    }

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
                    Manifest.permission.READ_MEDIA_VIDEO,
//                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )

            } else {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            }
            ,

            ImagePicker.REQUEST_CODE_GALLERY_PERMISSION
        )
    }
//    private fun requestGalleryPermission() {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ),
//            REQUEST_CODE_GALLERY_PERMISSION
//        )
//    }

//    private fun requestLocationPermission() {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ),
//            REQUEST_LOCATION_PERMISSION
//        )
//    }

//    private fun requestCameraPermission() {
//        ActivityCompat.requestPermissions(
//            context as Activity,
//            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            REQUEST_CODE_CAMERA_PERMISSION
//        )
//    }


    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            context as Activity,
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.S) {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO
                )

            } else {
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            },

//            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            arrayOf(Manifest.permission.CAMERA),
            ImagePicker.REQUEST_CODE_CAMERA_PERMISSION
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
//        else if (requestCode == REQUEST_LOCATION_PERMISSION) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                openCamera()
//                openCurrentLocation()
//            } else {
//                listener.onImagePickerPermissionFailed("This feature requires location access.")
//            }
//        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) return
        if (requestCode == REQUEST_CODE_CAMERA) {
            onImageCaptured()
        } else
//            if (requestCode == REQUEST_CODE_CONTACT) {
//                val uri = data?.data
//                val projection = arrayOf(Phone.NUMBER, Phone.DISPLAY_NAME)
//
//                val cursor = context.contentResolver.query(
//                    uri!!, projection,
//                    null, null, null
//                )
//                cursor!!.moveToFirst()
//
//                val numberColumnIndex: Int = cursor.getColumnIndex(Phone.NUMBER)
//                val number: String = cursor.getString(numberColumnIndex)
//
//                val nameColumnIndex: Int = cursor.getColumnIndex(Phone.DISPLAY_NAME)
//                val name: String = cursor.getString(nameColumnIndex)
//
//                listener.onContactReady(number, name)
//
//            } else
                if (requestCode == REQUEST_CODE_GALLERY) {

                val selectedUri = data?.data
                val columns = arrayOf(
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.MIME_TYPE,
                    MediaStore.Video.Media.DATA,
                    MediaStore.Video.Media.MIME_TYPE,
                )


                val cursor = context.contentResolver.query(selectedUri!!, columns, null, null, null)
                cursor!!.moveToFirst()

//            val pathColumnIndex: Int = cursor.getColumnIndex(columns[0])
                val mimeTypeColumnIndex: Int = cursor.getColumnIndex(columns[1])

//            val contentPath: String = cursor.getString(pathColumnIndex)
                var mimeType: String? = null
                try {
                    mimeType = cursor.getString(mimeTypeColumnIndex)
                } catch (e: Exception) {
                    Log.e("myfilepickerException", e.message.toString())
                }


                cursor.close()

                if (!TextUtils.isEmpty(mimeType) && mimeType!!.startsWith("image")) {
                    //It's an image
                    onImagePicked(data)
                } else if (!TextUtils.isEmpty(mimeType) && mimeType!!.startsWith("video")) {
                    //It's a video
                    filePath = getPath(data)
                    filePath?.let {
                        listener.onImagePathReady(
                            it,
                            title,
                            3
                        )
                    }
                } else {
                    filePath = getPath(data)
                    val file = File(filePath)
                    if (!file.exists()) {
                        filePath?.let { listener.onImagePathReady(it, title, -1) }
                    } else {

                        this.let {
                            Log.e("mydtata", file.absolutePath)

                            val filedumy = Uri.fromFile(file)
                            val fileExt = MimeTypeMap.getFileExtensionFromUrl(filedumy.toString())
                            Log.e("filetextextention", fileExt)

                            if (fileExt != null) {
                                var mimeType =
                                    MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExt)!!
                                Log.e("myFileType", mimeType)
                                if (!TextUtils.isEmpty(mimeType) && mimeType.startsWith("image")) {
                                    //It's an image
                                    onImagePicked(data)
                                } else {
                                    filePath?.let {
                                        listener.onImagePathReady(
                                            it,
                                            title,
                                           2
                                        )
                                    }
                                }
                            } else {
                                filePath?.let { listener.onImagePathReady(it, title, -1) }
                            }

                        }

                    }
                }

            }
//            else if (requestCode == REQUEST_CODE_DOCUMENT) {
//
//                Log.e("myFileChoosed", data?.data.toString())
//                filePath = data?.data?.let { RealPathUtil.getRealPath(context, it) }
//
//                filePath?.let {
//                    listener.onImagePathReady(
//                        it,
//                        title,
//                        4
//                    )
//                }
//            }
    }

    private fun onImageCaptured() {
//        filePath?.let { listener.onImagePathReady(it, title) }

        ImageCompressionHelper(context).setImageCompressionListener(object :
            ImageCompressionHelper.ImageCompressionListener {
            override fun onImageCompression(compressionImagePath: String) {
                filePath = compressionImagePath
                filePath?.let { listener.onImagePathReady(it, title, 2) }

            }
        }).execute(filePath)
    }

    private fun onImagePicked(data: Intent?) {
        filePath = getPath(data)

        ImageCompressionHelper(context).setImageCompressionListener(object :
            ImageCompressionHelper.ImageCompressionListener {
            override fun onImageCompression(compressionImagePath: String) {
                filePath = compressionImagePath
                filePath?.let { listener.onImagePathReady(it, title, 2) }

            }
        }).execute(filePath)
//        filePath?.let { listener.onImagePathReady(it, title) }
    }


//    fun isExternalStorageDocument(uri: Uri): Boolean {
//        return "com.android.externalstorage.documents" == uri.authority
//    }
//
//    fun isDownloadsDocument(uri: Uri): Boolean {
//        return "com.android.providers.downloads.documents" == uri.authority
//    }
//
//    fun isMediaDocument(uri: Uri): Boolean {
//        return "com.android.providers.media.documents" == uri.authority
//    }
//
//    fun isGooglePhotosUri(uri: Uri): Boolean {
//        return "com.google.android.apps.photos.content" == uri.authority
//    }


//    private fun makeFileCopyInCacheDir(contentUri: Uri): String? {
//        try {
//            val filePathColumn = arrayOf(
//                //Base File
//                MediaStore.Files.FileColumns._ID,
//                MediaStore.Files.FileColumns.TITLE,
//                MediaStore.Files.FileColumns.DATA,
//                MediaStore.Files.FileColumns.SIZE,
//                MediaStore.Files.FileColumns.DATE_ADDED,
//                MediaStore.Files.FileColumns.DISPLAY_NAME,
//                //Normal File
//                MediaStore.MediaColumns.DATA,
//                MediaStore.MediaColumns.MIME_TYPE,
//                MediaStore.MediaColumns.DISPLAY_NAME
//            )
//            //val contentUri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", File(mediaUrl))
//            val returnCursor = contentUri.let {
//                context.contentResolver.query(
//                    it,
//                    filePathColumn,
//                    null,
//                    null,
//                    null
//                )
//            }
//            if (returnCursor != null) {
//                returnCursor.moveToFirst()
//                val nameIndex = returnCursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
//                val name = returnCursor.getString(nameIndex)
//                val file = File(Environment.getDownloadCacheDirectory(), name)
//                val inputStream = context.contentResolver.openInputStream(contentUri)
//                val outputStream = FileOutputStream(file)
//                var read = 0
//                val maxBufferSize = 1 * 1024 * 1024
//                val bytesAvailable = inputStream!!.available()
//
//                //int bufferSize = 1024;
//                val bufferSize = Math.min(bytesAvailable, maxBufferSize)
//                val buffers = ByteArray(bufferSize)
//                while (inputStream.read(buffers).also { read = it } != -1) {
//                    outputStream.write(buffers, 0, read)
//                }
//                inputStream.close()
//                outputStream.close()
//                Log.e("File Path", "Path " + file.path)
//                Log.e("File Size", "Size " + file.length())
//                return file.absolutePath
//            }
//        } catch (ex: Exception) {
//            Log.e("Exception", ex.message!!)
//        }
//        return contentUri.let { RealPathUtil.getRealPath(context, it).toString() }
//    }
//
//    @SuppressLint("NewApi")
//    fun getRealPathFromURI_API19(context: Context?, uri: Uri): String? {
//        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
//
//        // DocumentProvider
//        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
//            // ExternalStorageProvider
//            if (isExternalStorageDocument(uri)) {
//                val docId = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":".toRegex()).toTypedArray()
//                val type = split[0]
//                if ("primary".equals(type, ignoreCase = true)) {
//                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
//                }
//
//                // TODO handle non-primary volumes
//            } else if (isDownloadsDocument(uri)) {
//                val id = DocumentsContract.getDocumentId(uri)
//                val contentUri = ContentUris.withAppendedId(
//                    Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id)
//                )
//                return getDataColumn(context!!, contentUri, null, null)
//            } else if (isMediaDocument(uri)) {
//                val docId = DocumentsContract.getDocumentId(uri)
//                val split = docId.split(":".toRegex()).toTypedArray()
//                val type = split[0]
//                var contentUri: Uri? = null
//                if ("image" == type) {
//                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//                } else if ("video" == type) {
//                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
//                } else if ("audio" == type) {
//                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
//                }
//                val selection = "_id=?"
//                val selectionArgs = arrayOf(
//                    split[1]
//                )
//                return getDataColumn(context!!, contentUri, selection, selectionArgs!!)
//            }
//        } else if ("content".equals(uri.scheme, ignoreCase = true)) {
//
//            // Return the remote address
//            return if (isGooglePhotosUri(uri)) uri.lastPathSegment else getDataColumn(
//                context!!,
//                uri,
//                null,
//                null
//            )
//        } else if ("file".equals(uri.scheme, ignoreCase = true)) {
//            return uri.path
//        }
//        return null
//    }

//    fun getDataColumn(
//        context: Context, uri: Uri?, selection: String?,
//        selectionArgs: Array<String>?
//    ): String? {
//        var cursor: Cursor? = null
//        val column = "_data"
//        val projection = arrayOf(
//            column
//        )
//        try {
//            cursor = context.contentResolver.query(
//                uri!!, projection, selection, selectionArgs,
//                null
//            )
//            if (cursor != null && cursor.moveToFirst()) {
//                val index: Int = cursor.getColumnIndexOrThrow(column)
//                return cursor.getString(index)
//            }
//        } finally {
//            if (cursor != null) cursor.close()
//        }
//        return null
//    }


    private fun getPath(data: Intent?): String? {
        var path: String? = null
        val selectedImage = data?.data
        val filePathColumn = arrayOf(
            MediaStore.MediaColumns.DATA,
            MediaStore.Images.Media.DATA,
            MediaStore.Files.FileColumns.DATA
        )
        val cursor =
            context.contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
        if (cursor != null) {
            cursor.moveToFirst()
            val columnIndex = cursor.getColumnIndex(filePathColumn[0])
            val columnIndexFile = cursor.getColumnIndex(filePathColumn[1])
            path = cursor.getString(columnIndex)

            if (TextUtils.isEmpty(path)) {
                path = cursor.getString(columnIndexFile)
            }


            cursor.close()
        }
        return path
    }

    interface ImagePickerListener {

        fun onImagePathReady(path: String, title: String, fileType: Int)

//        fun onContactReady(contactNumber: String, title: String)

        fun onImagePickerPermissionFailed(message: String)

        fun shouldStartActivity(intent: Intent, requestCode: Int)

//        fun onLocationReady(location: Location)
    }
}

