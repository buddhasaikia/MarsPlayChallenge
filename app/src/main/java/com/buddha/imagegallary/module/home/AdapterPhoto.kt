package com.buddha.imagegallary.module.home

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import com.buddha.imagegallary.R
import com.buddha.imagegallary.data.model.Resource
import com.buddha.imagegallary.module.details.DetailsActivity
import com.buddha.imagegallary.util.RecyclerViewPagingAdapter
import com.bumptech.glide.Glide
import com.cloudinary.android.MediaManager
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.app.ShareCompat
import android.content.Intent



class AdapterPhoto(private var mContext: Context?) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(), RecyclerViewPagingAdapter<Resource> {

    val VIEW_ITEM = 1
    val VIEW_PROGRESS = 0
    private var isLoadingAdded = false
    private var mList: ArrayList<Resource> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_ITEM -> {
                val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_photo, viewGroup, false)
                PhotoViewHolder(view)
            }
            VIEW_PROGRESS -> {
                val view =
                        LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item_progressbar, viewGroup, false)
                ProgressViewHolder(view)
            }
            else -> throw ClassCastException("Unsupported view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = mList[position]
        if (holder is PhotoViewHolder) {
            val url = MediaManager.get().url().type("upload").generate(item.publicId)
            Log.d("fetch url", url ?: "")
            Glide.with(mContext ?: return)
                    .load(url)
                    .thumbnail(.1f)
                    .into(holder.imageView)

        } else if (holder is ProgressViewHolder) {
            holder.progressBar.isIndeterminate = true
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == mList.size - 1 && isLoadingAdded) VIEW_PROGRESS else VIEW_ITEM
    }

    override fun getItemCount(): Int {
        return mList.size
    }


    override fun addLoadingFooter() {
        isLoadingAdded = true
        addItem(Resource())
    }

    override fun removeLoadingFooter() {
        isLoadingAdded = false

        val position = mList.size - 1
        val result = getItem(position)

        if (result != null) {
            mList.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    override fun addItem(item: Resource?) {
        mList.add(item ?: return)
        notifyItemInserted(mList.size - 1);

    }

    override fun getItem(position: Int?): Resource? {
        return mList[position ?: return null]
    }

    override fun addItems(list: List<Resource>?) {
        mList.clear()
        mList.addAll(list ?: return)
        notifyDataSetChanged()
    }

    override fun addMoreItems(list: List<Resource>?) {
        mList.addAll(list ?: return)
        notifyItemRangeInserted(mList.size - 1, list.size);
    }

    inner class ProgressViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    private inner class PhotoViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val imageView: ImageView = itemView.findViewById(R.id.imageView)
        internal val btnShare:ImageView=itemView.findViewById(R.id.btnShare)
        init {
            itemView.setOnClickListener {
                DetailsActivity.start(mContext, mList[adapterPosition])
            }
            btnShare.setOnClickListener {
                val url = MediaManager.get().url().type("upload").generate(mList[adapterPosition].publicId)
                share(url)
            }
        }

        private fun share(url: String?) {
            val mimeType = "text/plain"
            val title = "Share photo url"

            val shareIntent = ShareCompat.IntentBuilder.from(mContext as? Activity)
                    .setChooserTitle(title)
                    .setType(mimeType)
                    .setText(url)
                    .intent
            if (shareIntent.resolveActivity(mContext?.packageManager?:return) != null) {
                mContext?.startActivity(shareIntent)
            }
        }

    }


}
