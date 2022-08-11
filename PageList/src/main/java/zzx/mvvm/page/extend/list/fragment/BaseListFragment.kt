package zzx.mvvm.page.extend.list.fragment

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.loadmore.BaseLoadMoreView
import com.chad.library.adapter.base.module.LoadMoreModuleConfig
import zzx.mvvm.basepage.fragment.BaseBVMFragment
import zzx.mvvm.page.extend.list.event.ILoadEvent
import zzx.mvvm.page.extend.list.event.ListEvent
import zzx.mvvm.page.extend.list.handle.ILoadListHandle
import zzx.mvvm.page.extend.list.viewmodel.BaseListViewModel

abstract class BaseListFragment<E, VM : BaseListViewModel<E>, D : ViewDataBinding> :
    BaseBVMFragment<VM, D>(),
    ILoadListHandle<E> {

    override var isGoneLoadModel: Boolean = false

    override var mMinItemCount: Int = 10

    /**
     * 默认加载更多UI
     */
    override var defLoadMoreView: BaseLoadMoreView = LoadMoreModuleConfig.defLoadMoreView

    override val listDataObserver: Observer<ListEvent<E>>
        get() = buildSimpleListDataHandler()

    override fun initView() {
        mRecyclerView.layoutManager = buildLayoutManager()
        mRecyclerView.adapter = mAdapter
        if (mStatusView != null) {
            mAdapter.setEmptyView(mStatusView as View)
        }
        mAdapter.loadMoreModuleCheckModule()?.loadMoreView = defLoadMoreView
        mAdapter.loadMoreModuleCheckModule()?.setOnLoadMoreListener {
            if (viewModel.mPageNumber > 0) {
                viewModel._loadData(ILoadEvent.EVENT_LOAD_MORE)
            } else {
                viewModel._loadData(ILoadEvent.EVENT_LOAD_INIT_OR_RETRY)
            }
        }
        mRefreshLayout?.setOnRefreshListener {
            viewModel._loadData(ILoadEvent.EVENT_PULL_TO_REFRESH)
        }
    }

    override fun _showEmpty() {
        viewModel.showEmpty()
    }

    override fun _showContent() {
        viewModel.showContent()
    }

    override fun dataObserve() {
        super.dataObserve()
        viewModel._listdata.observe(this, listDataObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        mRefreshLayout?.setOnRefreshListener(null)
        mAdapter.loadMoreModuleCheckModule()?.setOnLoadMoreListener(null)
    }

    private fun buildLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

}