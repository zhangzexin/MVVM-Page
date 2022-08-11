package zzx.mvvm.page.extend.list.handle

import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.module.BaseLoadMoreModule
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import zzx.mvvm.page.extend.list.event.ILoadEvent
import zzx.mvvm.page.extend.list.event.ListEvent

interface ILoadListHandle<E> {

    /**
     * 是否隐藏加载更多UI
     */
    var isGoneLoadModel: Boolean

    /**
     * 最小item数，用于决定是否显示页未UI
     */
    var mMinItemCount: Int

    var mRecyclerView: RecyclerView
    var mAdapter: BaseQuickAdapter<E, BaseViewHolder>

    /**
     * 下拉更新控件
     */
    var mRefreshLayout: SwipeRefreshLayout?

    /**
     * 默认加载更多UI
     */
    var defLoadMoreView: BaseLoadMoreView

    val listDataObserver: Observer<ListEvent<E>>

    /**
     * 处理列表数据加载逻辑
     */
    fun buildSimpleListDataHandler(): Observer<ListEvent<E>> {
        return Observer<ListEvent<E>> {
            when (it.event) {
                ILoadEvent.EVENT_LOAD_INIT_OR_RETRY, ILoadEvent.EVENT_PULL_TO_REFRESH -> {
                    if (it.list != null && it.list!!.isNotEmpty()) {
                        _showContent()
                        mAdapter.setList(it.list)
                    } else {
                        _showEmpty()
                        mAdapter.setList(ArrayList())
                        mAdapter.loadMoreModuleCheckModule()?.loadMoreEnd(true)
                    }
                    mRefreshLayout?.isRefreshing = false
                    mAdapter.loadMoreModuleCheckModule()?.loadMoreComplete()
                }
                ILoadEvent.EVENT_LOAD_MORE -> {
                    if (it.list != null && it.list!!.isNotEmpty()) {
                        mAdapter.addData(it.list!!)
                        mAdapter.loadMoreModuleCheckModule()?.loadMoreComplete()
                    } else {
                        mAdapter.loadMoreModuleCheckModule()?.loadMoreEnd(checkGoneLoadModel())
                    }
                }
                else -> {

                }
            }
        }
    }

    fun _showEmpty()

    fun _showContent()

    private fun checkGoneLoadModel(): Boolean {
        return !(mAdapter.itemCount > getMinItemCount() && !isGoneLoadModel)
    }

    fun getMinItemCount(): Int {
        return mMinItemCount
    }

    fun BaseQuickAdapter<E, out BaseViewHolder>.loadMoreModuleCheckModule(): BaseLoadMoreModule? {
        if (mAdapter is LoadMoreModule) {
            return loadMoreModule
        }
        return null
    }
}