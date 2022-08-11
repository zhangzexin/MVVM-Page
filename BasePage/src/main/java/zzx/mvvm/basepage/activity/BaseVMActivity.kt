package zzx.mvvm.basepage.activity


import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.CreationExtras
import zzx.mvvm.basepage.viewmodel.IBaseViewModel
import zzx.mvvm.basepage.base.PageStatus
import zzx.mvvm.basepage.base.observeNullable
import java.lang.reflect.ParameterizedType

/**
 *@描述：默认创建对应ViewModel,视图
 *@time：2022/7/18
 *@author:zhangzexin
 */
abstract class BaseVMActivity<VM: ViewModel,D : ViewDataBinding>: BaseBindingActivity<D>() {

    protected lateinit var viewModel:VM

    override fun initContentView() {
        super.initContentView()
        injectViewModel()
        initBaseObserver()
    }

    private fun initBaseObserver() {
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

            iBaseViewModel._ToastEvent.observe(this) {
                if (!it.isNullOrEmpty()) {
                    iBaseViewModel._ToastEvent.value = null
                    Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
                }
            }

            iBaseViewModel._finishPageEvent.observeNullable(this) {
                finishPage(it)
            }
        }
    }

    private fun finishPage(any: Any?) {
        ActivityCompat.finishAfterTransition(this)
    }

    private fun injectViewModel() {
        createViewModel()
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
            defaultViewModelProviderFactory
        }
        val extrasProducer: () -> CreationExtras = { CreationExtras.Empty }

        val storeProducer: () -> ViewModelStore = { viewModelStore }

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
}