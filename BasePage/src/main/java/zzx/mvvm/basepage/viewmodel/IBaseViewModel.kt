package zzx.mvvm.basepage.viewmodel

import android.os.Looper
import androidx.annotation.NonNull
import androidx.lifecycle.*
import zzx.mvvm.basepage.base.PageStatus

/**
 *@描述：ViewModel基础接口
 *@time：2022/7/18
 *@author:zhangzexin
 */
interface IBaseViewModel : LifecycleEventObserver {
    val _stateEvent: MutableLiveData<PageStatus>
        get() = MutableLiveData<PageStatus>()
    val _finishPageEvent: MutableLiveData<Any?>
        get() = MutableLiveData<Any?>()
    val _ToastEvent: MutableLiveData<String>
        get() = MutableLiveData()

    fun finishPage() {
        _finishPageEvent.postValue(null)
    }

    fun showToast(msg: String) {
        if (isMainThread()) {
            _ToastEvent.value = msg
        } else {
            _ToastEvent.postValue(msg)
        }
    }

    fun isMainThread():Boolean {
        return Looper.getMainLooper().thread.id == Thread.currentThread().id
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when(event) {
            Lifecycle.Event.ON_ANY ->  onAny(source,event)
            Lifecycle.Event.ON_CREATE ->  onCreate()
            Lifecycle.Event.ON_START ->  onStart()
            Lifecycle.Event.ON_RESUME ->  onResume()
            Lifecycle.Event.ON_PAUSE ->  onPause()
            Lifecycle.Event.ON_STOP ->  onStop()
            Lifecycle.Event.ON_DESTROY ->  onDestroy()
        }
    }

    fun onDestroy()

    fun onStop()

    fun onPause()

    fun onResume()

    fun onStart()

    fun onCreate()

    fun onAny(source: LifecycleOwner, event: Lifecycle.Event)


}