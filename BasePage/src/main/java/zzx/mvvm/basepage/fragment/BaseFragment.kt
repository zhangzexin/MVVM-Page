package zzx.mvvm.basepage.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import zzx.mvvm.basepage.base.ILazyLoad
import zzx.mvvm.basepage.base.IStatusPage
import zzx.mvvm.basepage.base.PageStatus

/**
 *@描述：带懒加载功能的Fragment
 *@time：2022/8/8
 *@author:zhangzexin
 */
abstract class BaseFragment : Fragment(), ILazyLoad {

    var mContainer: ViewGroup? = null

    @get:LayoutRes
    protected abstract var layoutId: Int

    /**
     * 如果有状态页，设置状态页
     */
    protected abstract var mStatusView : IStatusPage?

    /**
     * 缓存视图，如果视图已经创建，则不再初始化视图
     */
    private var rootView: View? = null

    /**
     * 是否开启懒加载,默认开始
     */
    private var lazyLoadEnable = true

    /**
     * 当前状态
     */
    private var currentState = ILazyLoad.ON_IDLE

    /**
     * 是否已经执行懒加载
     */
    private var hasLazyLoad = false

    /**
     * 当前Fragment是否对用户可见
     */
    private var isVisibleToUser = false

    /**
     * 是否调用了setUserVisibleHint方法.
     * 处理show+add+hide模式下,默认可见Fragment不调用onHiddenChanged方法，进而不执行懒加载方法的问题.
     */
    private var isCallUserVisibleHint = false

    /**
     * 是否开启懒加载,调用此方法建议在getLazyInitState()所返回的状态之前
     */
    protected fun enableLazyLoad(enable: Boolean) {
        this.lazyLoadEnable = enable
    }

    /**
     * 是否显示时重新加载数据
     */
    var isVisibleLoadData = false

    /**
     * 懒加载的调用时机
     */
    protected fun getLazyLoadState() = ILazyLoad.ON_RESUME

    protected fun setCurrentState(state: Int) {
        this.currentState = state
    }

    /**
     * 是否是在setUserVisibleHint中调用
     */
    protected fun doLazyLoad(callInUserVisibleHint: Boolean) {
        if (!callInUserVisibleHint) {
            if (!isCallUserVisibleHint) isVisibleToUser = !isHidden
        }
        if (lazyLoadEnable && !hasLazyLoad && isVisibleToUser && currentState >= getLazyLoadState()) {
            hasLazyLoad = true
            lazyLoad()
        } else if (lazyLoadEnable && isVisibleLoadData && isVisibleToUser && currentState >= getLazyLoadState()) {
            isVisibleLoadData = false
            onFragmentVisibleLoadData()
        }
    }

    open fun onFragmentVisibleLoadData() {
        loadData()
    }

    override fun lazyLoad() {
        initialize()
    }

    /**
     * view初始化
     */
    abstract fun initView()

    /**
     * 数据请求，子类重载
     */
    abstract fun loadData()

    /**
     * 数据监听方法，子类重载
     */
    abstract fun dataObserve()

    fun getContainer(): ViewGroup {
        return mContainer!!
    }

    protected fun getRootView(): View? {
        return rootView
    }

    protected fun setRootView(view: View) {
        this.rootView = view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        setCurrentState(ILazyLoad.ON_ATTACH)
        doLazyLoad(false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doLazyLoad(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setCurrentState(ILazyLoad.ON_CREATE_VIEW)
        if (getRootView() != null) {
            return getRootView()
        }
        setRootView(createView(inflater, container))
        doLazyLoad(false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected fun initialize() {
        initView()
        dataObserve()
        loadData()
    }

    private fun createView(inflater: LayoutInflater, container: ViewGroup?): View {
        return inflater.inflate(layoutId,container,false)
    }

//    @LayoutRes
//    abstract fun getLayoutId(): Int

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setCurrentState(ILazyLoad.ON_ACTIVITY_CREATED)
        doLazyLoad(false)
    }

    override fun onStart() {
        super.onStart()
        setCurrentState(ILazyLoad.ON_START)
        doLazyLoad(false)
    }

    override fun onResume() {
        super.onResume()
        setCurrentState(ILazyLoad.ON_RESUME)
        doLazyLoad(false)
    }

    /**
     * 主要用于add+show+hide模式下,Fragment的隐藏于显示调用的方法
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        isVisibleToUser = !hidden
        doLazyLoad(false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        this.isVisibleToUser = isVisibleToUser
        isCallUserVisibleHint = true
        doLazyLoad(true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hasLazyLoad = false
        isVisibleToUser = false
        isCallUserVisibleHint = false
    }


    fun showEmpty() {
        mStatusView?.showEmpty()
        mStatusView?.mPageStatus = PageStatus.empty
    }

    fun showContent() {
        mStatusView?.showContent()
        mStatusView?.mPageStatus = PageStatus.normal
    }

    fun showError() {
        mStatusView?.showError()
        mStatusView?.mPageStatus = PageStatus.error
    }

    fun showLoading() {
        mStatusView?.showLoading()
        mStatusView?.mPageStatus = PageStatus.loading
    }


}