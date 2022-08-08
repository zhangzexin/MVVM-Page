package zzx.mvvm.basepage.activity

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 *@描述：仅提供自动绑定视图
 *@time：2022/7/18
 *@author:zhangzexin
 */
abstract class BaseBindingActivity<D: ViewDataBinding>: BaseActivity() {
    lateinit var _binding: D

    override fun initContentView() {
        _binding = DataBindingUtil.setContentView(this,mContentLayoutId)
        _binding.lifecycleOwner = this
    }

    override fun onDestroy() {
        _binding.lifecycleOwner = null
        _binding.unbind()
        super.onDestroy()
    }
}