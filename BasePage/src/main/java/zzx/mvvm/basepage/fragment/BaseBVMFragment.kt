package zzx.mvvm.basepage.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import zzx.mvvm.basepage.base.ILazyLoad
import zzx.mvvm.basepage.base.PageStatus
import zzx.mvvm.basepage.viewmodel.IBaseViewModel
import java.lang.reflect.ParameterizedType

/**
 *@描述：
 *@time：2022/8/8
 *@author:zhangzexin
 */
abstract class BaseBVMFragment<VM: ViewModel,D: ViewDataBinding>: BaseBindingFragment<D>() {
    protected lateinit var viewModel:VM

    /**
     * 是否创建的是一个Activity的ViewModel,默认为true
     */
    protected var isCreateActivityViewModel = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setCurrentState(ILazyLoad.ON_CREATE_VIEW)
        if (getRootView() != null) {
            return getRootView()
        }
        injectViewModel()
        injectDataBinding(inflater, container, savedInstanceState)
        doLazyLoad(false)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun injectViewModel() {
        createViewModel()
        if (viewModel is IBaseViewModel) {
            lifecycle.addObserver(viewModel as IBaseViewModel)
        }
        if (_binding is ViewDataBinding) {
            (_binding as ViewDataBinding).lifecycleOwner = this
        }
    }

    //根据泛型创建对应的主ViewModel，ViewModel一定是全局性的
    @Suppress("UNCHECKED_CAST")
    private fun createViewModel() {
        val parameterizedType = javaClass.genericSuperclass as? ParameterizedType
        var viewModelClass: Class<VM>? = null
        parameterizedType?.actualTypeArguments?.let {
            it.forEach { type ->
                if (type != null && type is Class<*>) {
                    val clazz = type as Class<VM>
                    if (ViewModel::class.javaObjectType.isAssignableFrom(clazz)) {
                        viewModelClass = clazz
                        return@forEach
                    }
                }
            }
        }

        val factoryProducer: () -> ViewModelProvider.Factory = {
            if (isCreateActivityViewModel) requireActivity().defaultViewModelProviderFactory else defaultViewModelProviderFactory
        }
        val extrasProducer: () -> CreationExtras = { CreationExtras.Empty }

        val storeProducer: () -> ViewModelStore = { if (isCreateActivityViewModel) requireActivity().viewModelStore else viewModelStore}

        var cached: VM? = null
        if (cached != null) {
            viewModel = cached
        }
        if (cached == null && viewModelClass != null) {
            val factory = factoryProducer()
            val store = storeProducer()
            viewModel = viewModelClass?.let {
                ViewModelProvider(
                    store,
                    factory,
                    extrasProducer()
                ).get(viewModelClass!!).also {
                    cached = it
                }
            }!!
        } else {
            viewModel
        }
    }

    override fun dataObserve() {
        if (viewModel is IBaseViewModel) {
            val iBaseViewModel = viewModel as IBaseViewModel
            iBaseViewModel._stateEvent.observe(this) {
                when(it) {
                    PageStatus.loading -> showLoading()
                    PageStatus.error -> showError()
                    PageStatus.normal -> showContent()
                    PageStatus.empty -> showEmpty()
                    else -> {}
                }
            }

            if (!isCreateActivityViewModel) {
                iBaseViewModel._ToastEvent.observe(this) {
                    if (!it.isNullOrEmpty()) {
                        iBaseViewModel._ToastEvent.value = null
                        Toast.makeText(activity,it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        if (viewModel is IBaseViewModel) {
            lifecycle.removeObserver(viewModel as IBaseViewModel)
        }
        super.onDestroy()
    }
}