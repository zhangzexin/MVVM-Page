package zzx.mvvm.page.extend.list.viewmodel

import zzx.mvvm.page.extend.list.event.ILoadEvent
import zzx.mvvm.page.extend.list.event.ListEvent
import zzx.mvvm.page.extend.list.livedata.LiveEvent


abstract class BaseListViewModel<E> : BaseViewModel() {
    var mPageNumber: Int = 0
    val _listdata: LiveEvent<ListEvent<E>> = LiveEvent()

    abstract fun reFreshPage()

    fun _loadData(loadEvent: ILoadEvent?) {
        when (loadEvent) {
            ILoadEvent.EVENT_LOAD_INIT_OR_RETRY -> {
                mPageNumber = 1
                doloadData(mPageNumber, loadEvent)
            }
            ILoadEvent.EVENT_PULL_TO_REFRESH -> {
                mPageNumber = 1
                doloadData(mPageNumber, loadEvent)
            }
            ILoadEvent.EVENT_LOAD_MORE -> {
                mPageNumber++
                doloadData(mPageNumber, loadEvent)
            }
            else -> {

            }
        }
    }

    override fun showError() {
        mPageNumber = 1
        super.showError()
    }

    abstract fun doloadData(mPageNumber: Int, loadEvent: ILoadEvent)

}