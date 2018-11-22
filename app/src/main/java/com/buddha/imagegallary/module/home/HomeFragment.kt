package com.buddha.imagegallary.module.home

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.buddha.imagegallary.R
import com.buddha.imagegallary.data.model.CloudinaryImageList
import com.buddha.imagegallary.di.scope.PerActivity
import com.buddha.imagegallary.module.base.DaggerBaseFragment
import com.buddha.imagegallary.util.CustomViewModelFactory
import com.buddha.imagegallary.util.ErrorMessageFactory
import com.buddha.imagegallary.util.Utils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import retrofit2.Response
import javax.inject.Inject
import com.buddha.imagegallary.util.SpacesItemDecoration
import android.R.attr.spacing




@PerActivity
class HomeFragment : DaggerBaseFragment(), SwipeRefreshLayout.OnRefreshListener {

    private var isLoading = false
    private var isLastPage = false
    private var OFFSET = 0
    private var PAGE_SIZE = 100
    private var TOTAL_PRODUCT = 0

    @Inject
    lateinit var viewModelFactory: CustomViewModelFactory
    @Inject
    lateinit var errorMessageFactory: ErrorMessageFactory
    @Inject
    lateinit var utils: Utils
    private var homeViewModel: HomeViewModel? = null

    private var imageView: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    private var mList: CloudinaryImageList? = CloudinaryImageList()

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    private var adapterPhoto: AdapterPhoto? = null

    private lateinit var layoutManager: GridLayoutManager

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        imageView = rootView?.findViewById(R.id.imageView)
        recyclerView = rootView?.findViewById(R.id.recyclerView)
        layoutManager = GridLayoutManager(activity, 2)
        val onSpanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (adapterPhoto?.getItemViewType(position) === adapterPhoto?.VIEW_ITEM) 1 else 2
            }
        }
        layoutManager.spanSizeLookup = onSpanSizeLookup
        recyclerView?.layoutManager = layoutManager

        val spacingInPixels = resources.getDimensionPixelSize(R.dimen.spacing)
        recyclerView?.addItemDecoration(SpacesItemDecoration(spacingInPixels))

        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout?.setOnRefreshListener(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapterPhoto = AdapterPhoto(activity)
        recyclerView?.adapter = adapterPhoto

        recyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    val visibleItemCount = layoutManager.getChildCount()
                    val totalItemCount = layoutManager.getItemCount()
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                    if (!isLoading && !isLastPage && OFFSET <= TOTAL_PRODUCT) {
                        if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                            OFFSET += PAGE_SIZE
                            loadItems(true)
                        }
                    }
                }
            }
        })

        homeViewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        if (swipeRefreshLayout?.isRefreshing == false)
            onRefresh()
    }

    companion object {

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onRefresh() {
        loadItems(false)
    }

    private fun loadItems(isLoadMore: Boolean) {
        swipeRefreshLayout?.isRefreshing = true
        homeViewModel?.fetchImageList()
                ?.subscribe(object : Observer<Response<CloudinaryImageList>> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        homeViewModel?.addSubscription(d)
                    }

                    override fun onNext(t: Response<CloudinaryImageList>) {
                        if (t.isSuccessful) {
                            mList = t.body()
                            if (isLoadMore) {
                                adapterPhoto?.removeLoadingFooter()
                                adapterPhoto?.addMoreItems(mList?.resources)
                                isLastPage = adapterPhoto?.itemCount == TOTAL_PRODUCT
                                if (!isLastPage)
                                    adapterPhoto?.addLoadingFooter()
                            } else {
                                TOTAL_PRODUCT = mList?.resources?.size ?: 0

                                adapterPhoto?.addItems(mList?.resources)
                                isLastPage = adapterPhoto?.itemCount == TOTAL_PRODUCT
                                if (!isLastPage)
                                    adapterPhoto?.addLoadingFooter()
                            }
                            isLoading = false
                            swipeRefreshLayout?.isRefreshing = false
                        } else {
                            utils.showErrorDialog("Oops! Unable to retrieve images. Keep patience, we are looking into it")
                            swipeRefreshLayout?.isRefreshing = false
                        }
                    }

                    override fun onError(e: Throwable) {
                        errorMessageFactory.getError(e)
                        swipeRefreshLayout?.isRefreshing = false
                    }

                })
    }
}
