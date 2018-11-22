package com.buddha.imagegallary.module.upload


import android.Manifest
import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.Settings
import android.support.v4.widget.ContentLoadingProgressBar
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.buddha.imagegallary.R
import com.buddha.imagegallary.module.base.DaggerBaseFragment
import com.buddha.imagegallary.util.Config
import com.buddha.imagegallary.util.Config.StartActivityForResult.CAPTURE_ACTIVITY_REQUEST_CODE
import com.buddha.imagegallary.util.Config.StartActivityForResult.PICK_IMAGE_REQUEST
import com.buddha.imagegallary.util.CustomViewModelFactory
import com.buddha.imagegallary.util.ProgressInterface
import com.buddha.imagegallary.util.Utils
import com.buddha.imagegallary.util.rxpermissions2.Permission
import com.buddha.imagegallary.util.rxpermissions2.RxPermissions
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import com.yalantis.ucrop.UCrop
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class UploadFragment : DaggerBaseFragment(), ProgressInterface {
    private lateinit var rxPermissions: RxPermissions
    private lateinit var mCurrentPhotoPath: String
    private lateinit var uploadViewModel: UploadViewModel
    private var progressBar: ContentLoadingProgressBar? = null
    private var btnUpload: Button? = null
    private lateinit var cameraIntent: Intent
    @Inject
    lateinit var util: Utils

    @Inject
    lateinit var customViewModelFactory: CustomViewModelFactory

    override fun showProgress() {
        progressBar?.visibility = View.VISIBLE
        progressBar?.show()
    }

    override fun setProgress(progressValue: Long) {
        progressBar?.progress = progressValue.toInt()
    }

    override fun hideProgress() {
        progressBar?.visibility = View.GONE
        progressBar?.hide()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_upload
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rxPermissions = RxPermissions(this)
        rxPermissions.setLogging(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        btnUpload = rootView?.findViewById(R.id.btnUpload)
        progressBar = rootView?.findViewById(R.id.progressBar)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        uploadViewModel = ViewModelProviders.of(this, customViewModelFactory).get(UploadViewModel::class.java)
        btnUpload?.setOnClickListener {
            showPhotoSelectionDialog()
        }
    }

    private fun showPhotoSelectionDialog() {
        if (isAlive(activity)) {
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setCancelable(true)
                    .setMessage(R.string.msg_take_photo)
                    .setTitle(R.string.lbl_take_photo)
                    .setPositiveButton(R.string.btn_gallery) { _, _ -> galleryOnClick() }
                    .setNegativeButton(R.string.btn_camera) { _, _ -> cameraOnClick() }
            val alertDialog = builder.create()
            alertDialog.show()
        }
    }

    private fun galleryOnClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Config.StartActivityForResult.PICK_IMAGE_REQUEST)
    }

    private fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES)
        val image = File.createTempFile(imageFileName, ".jpg", storageDir)
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }

    private fun cameraOnClick() {
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(object : Observer<Permission> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        uploadViewModel.addSubscription(d)
                    }

                    override fun onNext(permission: Permission) {
                        if (permission.granted) {
                            cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            if (cameraIntent.resolveActivity(context?.packageManager
                                            ?: return) != null) {
                                // Create the File where the photo should go
                                var photoFile: File? = null
                                try {
                                    photoFile = createImageFile()
                                } catch (ex: IOException) {
                                    ex.printStackTrace()
                                }

                                // Continue only if the File was successfully created
                                if (photoFile != null) {
                                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                                    val builder = StrictMode.VmPolicy.Builder()
                                    StrictMode.setVmPolicy(builder.build())
                                    startActivityForResult(cameraIntent, Config.StartActivityForResult.CAPTURE_ACTIVITY_REQUEST_CODE)
                                }
                            }
                        } else {
                            if (permission.shouldShowRequestPermissionRationale) {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(activity
                                        ?: return)
                                builder.setMessage("Without this permission the app is unable to capture photo. Are you sure you want to deny this permission?")
                                builder.setPositiveButton(R.string.retry) { _, _ -> cameraOnClick() }
                                builder.setNegativeButton(R.string.sure) { dialog, _ ->
                                    dialog.dismiss()
                                }
                                builder.create().show()
                            } else {
                                val builder: AlertDialog.Builder = AlertDialog.Builder(activity
                                        ?: return)
                                builder.setMessage("To take photo for your profile, the app needs write access on your device storage")
                                builder.setPositiveButton(R.string.ok) { dialog, _ -> dialog.dismiss() }
                                builder.setNegativeButton(R.string.settings) { dialog, _ ->
                                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                    val uri = Uri.fromParts("package", activity?.packageName, null)
                                    intent.data = uri
                                    startActivityForResult(intent, 101)
                                    dialog.dismiss()
                                }
                                builder.create().show()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                    }

                })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                val destinationFileName = "cropped"
                if (requestCode == PICK_IMAGE_REQUEST && data != null) {
                    val fileUri = data.data
                    if (fileUri != null) {
                        UCrop.of(fileUri, Uri.fromFile(File(activity?.cacheDir, destinationFileName)))
                                .withAspectRatio(1f, 1f)
                                .withMaxResultSize(Config.Global.IMAGE_WIDTH, Config.Global.IMAGE_HEIGHT)
                                .start(activity ?: return)
                    }

                } else if (requestCode == CAPTURE_ACTIVITY_REQUEST_CODE) {
                    try {
                        val mImageBitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(mCurrentPhotoPath))
                        UCrop.of(uriFromBitmap(mImageBitmap), Uri.fromFile(File(activity?.cacheDir, destinationFileName)))
                                .withAspectRatio(1f, 1f)
                                .withMaxResultSize(Config.Global.IMAGE_WIDTH, Config.Global.IMAGE_HEIGHT)
                                .start(activity ?: return)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else if (requestCode == UCrop.REQUEST_CROP) {
                    val resultUri = UCrop.getOutput(data ?: return)
                    Log.d("resultUri", resultUri?.toString() ?: "")
                    if (resultUri != null) {
                        showProgress()
                        MediaManager.get()
                                .upload(resultUri)
                                .option("folder", "marsplaychallenge/")
                                .option("tags", "marsplay")
                                .callback(object : UploadCallback {
                                    override fun onSuccess(requestId: String?, resultData: MutableMap<Any?, Any?>?) {
                                        Log.d("requestId(onSuccess)", requestId ?: "")
                                        hideProgress()
                                        util.showToast("Image uploaded successfully")
                                        btnUpload?.text="Upload"
                                        btnUpload?.isEnabled=true
                                        val returnIntent = Intent()
                                        returnIntent.putExtra("result", "success")
                                        activity?.setResult(Activity.RESULT_OK, returnIntent)
                                        activity?.finish()
                                    }

                                    override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                                        Log.d("requestId(onProgress)", requestId ?: "")
                                        val progress = (bytes / totalBytes) * 100
                                        Log.d("requestId(progress)", progress.toString())
                                        //setProgress(progress)

                                    }

                                    override fun onReschedule(requestId: String?, error: ErrorInfo?) {
                                        Log.d("requestId(onReschedule)", requestId ?: "")
                                        hideProgress()
                                    }

                                    override fun onError(requestId: String?, error: ErrorInfo?) {
                                        hideProgress()
                                        Log.d("requestId(onError)", requestId ?: ""+" "
                                        +error?.description)
                                        util.showToast("Image upload failed")
                                        btnUpload?.text="Upload"
                                        btnUpload?.isEnabled=true
                                    }

                                    override fun onStart(requestId: String?) {
                                        showProgress()
                                        btnUpload?.text="Please wait..."
                                        btnUpload?.isEnabled=false
                                    }

                                }).dispatch()

                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data)
                }
            }

            UCrop.RESULT_ERROR -> {
                UCrop.getError(data ?: return)?.printStackTrace()
            }
            Activity.RESULT_CANCELED -> return
        }
    }

    private fun uriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(activity?.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }
}
