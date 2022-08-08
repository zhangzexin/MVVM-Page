package zzx.mvvm.basepage.activity

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import zzx.mvvm.basepage.base.IStatusPage
import zzx.mvvm.basepage.base.PageStatus

/**
 *@描述：基础页
 *@time：2022/7/15
 *@author:zhangzexin
 */
abstract class BaseActivity:AppCompatActivity() {

    protected abstract var mContentLayoutId: Int
    protected abstract var mStatusView : IStatusPage?

    var mBackPressedHandler = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initCreate(savedInstanceState)
    }

    open fun initCreate(savedInstanceState: Bundle?) {
        transparenStatusBar(this)
        initContentView()
        initialize(savedInstanceState)
    }

    open fun initialize(savedInstanceState: Bundle?) {
        initView()
        dataObserver()
        onLoadDate()
    }

    private fun onLoadDate() {
        showLoading()
        loadDate()
    }

//    abstract fun getStatusPageView():IStatusPage

    abstract fun loadDate()

    abstract fun dataObserver()

    abstract fun initView()

    override fun onNewIntent(intent: Intent?) {
        onSaveNewIntent(intent)
        super.onNewIntent(intent)
    }

    open fun onSaveNewIntent(intent: Intent?) {
        setIntent(intent)
    }

    open fun initContentView() {
        setContentView(mContentLayoutId)
    }

    /**
     * 初始化flag状态栏,默认是为沉侵式方案，如有需求自行重写
     */
    open fun transparenStatusBar(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= 21) {
            val window: Window = activity.window // 添加Flag，把状态栏设置为可绘制
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT // view不根据系统窗口来调整自己的布局
            val mContentView = window.findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
            val mChildView = mContentView.getChildAt(0)
            if (mChildView != null) {
                ViewCompat.setFitsSystemWindows(mChildView, false)
                ViewCompat.requestApplyInsets(mChildView)
            }
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

    override fun onBackPressed() {
       if (!mBackPressedHandler) {
            onSuperBackPressed()
       }
    }

    fun onSuperBackPressed() {
        super.onBackPressed()
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