package zzx.mvvm.basepage.base

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData

/**
 *@描述：
 *@time：2022/8/8
 *@author:zhangzexin
 */
fun <T> LiveData<T>.observeNullable(owner: LifecycleOwner, block: (T) ->Unit) {
    this.observe(owner) {
        block(it)
    }
}