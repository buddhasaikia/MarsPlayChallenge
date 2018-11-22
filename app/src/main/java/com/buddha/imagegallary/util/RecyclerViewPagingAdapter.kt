package com.buddha.imagegallary.util

interface RecyclerViewPagingAdapter<T> {
    fun addLoadingFooter()

    fun removeLoadingFooter()

    fun addItem(item: T?)

    fun getItem(position: Int?): T?

    fun addItems(list: List<T>?)

    fun addMoreItems(list: List<T>?)
}
