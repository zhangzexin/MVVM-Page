package zzx.mvvm.basepage.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import zzx.mvvm.basepage.base.ILazyLoad

/**
 *@描述：绑定视图的Fragment
 *@time：2022/8/8
 *@author:zhangzexin
 */
abstract class BaseBindingFragment<D : ViewDataBinding>: BaseFragment() {

    protected var _binding: D? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setCurrentState(ILazyLoad.ON_CREATE_VIEW)
        if (getRootView() != null) {
            return getRootView()
        }
        injectDataBinding(inflater, container, savedInstanceState)
        doLazyLoad(false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected open fun injectDataBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        _binding = DataBindingUtil.inflate(inflater,layoutId,container,false)
        mContainer = container
        _binding!!.lifecycleOwner = this
        setRootView(_binding!!.root)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding!!.lifecycleOwner = null
        _binding!!.unbind()
        _binding = null
    }


}