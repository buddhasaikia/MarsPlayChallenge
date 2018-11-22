package com.buddha.imagegallary.module.details


import android.os.Bundle
import android.support.v4.widget.ContentLoadingProgressBar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.buddha.imagegallary.R
import com.buddha.imagegallary.data.model.Resource
import com.buddha.imagegallary.module.base.DaggerBaseFragment
import com.buddha.imagegallary.util.Config
import com.buddha.imagegallary.util.Utils
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import com.github.chrisbanes.photoview.PhotoView
import javax.inject.Inject

class DetailsFragment : DaggerBaseFragment() {
    @Inject
    lateinit var utils: Utils

    private var  imageView: PhotoView? = null
    private var imageLoadingProgressBar: ContentLoadingProgressBar? = null
    private var lblUserName: TextView? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_details
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        imageView = rootView?.findViewById(R.id.imageView)
        imageLoadingProgressBar = rootView?.findViewById(R.id.progressBar)
        lblUserName = rootView?.findViewById(R.id.lblUserName)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val datum: Resource? = arguments?.getParcelable(Config.Extras.DATUM)
        val url = MediaManager.get().url().type("upload").generate(datum?.publicId)
        Glide.with(activity ?: return)
                .load(url)
                .into(imageView ?: return)
    }

    companion object {
        fun newInstance(datum: Resource?): DetailsFragment? {
            val fragment = DetailsFragment()
            val bundle = Bundle()
            bundle.putParcelable(Config.Extras.DATUM, datum)
            fragment.arguments = bundle
            return fragment
        }
    }
}
