package com.example.myapplication

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import zzx.mvvm.basepage.viewmodel.IBaseViewModel

/**
 *@描述：
 *@time：2022/7/18
 *@author:zhangzexin
 */
class MainViewModel:ViewModel(), IBaseViewModel {
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