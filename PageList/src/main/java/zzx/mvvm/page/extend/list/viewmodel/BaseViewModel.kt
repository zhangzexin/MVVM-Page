package zzx.mvvm.page.extend.list.viewmodel

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import zzx.mvvm.basepage.base.PageStatus
import zzx.mvvm.basepage.viewmodel.IBaseViewModel

abstract class BaseViewModel : ViewModel(), IBaseViewModel {

    open fun showLoading() {
        _stateEvent.postValue(PageStatus.loading)
    }

    open fun showError() {
        _stateEvent.postValue(PageStatus.error)
    }

    open fun showEmpty() {
        _stateEvent.postValue(PageStatus.empty)
    }

    open fun showContent() {
        _stateEvent.postValue(PageStatus.normal)
    }

    override fun onDestroy() {

    }

    override fun onStop() {
    }

    override fun onPause() {
    }

    override fun onResume() {
    }

    override fun onStart() {
    }

    override fun onCreate() {
    }

    override fun onAny(source: LifecycleOwner, event: Lifecycle.Event) {
    }
}