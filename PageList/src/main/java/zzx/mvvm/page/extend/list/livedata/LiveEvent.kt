package zzx.mvvm.page.extend.list.livedata

import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class LiveEvent<T> {
    private var mData: MutableLiveData<Event<T>>

    constructor(value: T) {
        mData = MutableLiveData<Event<T>>()
        mData.value = Event<T>(value, true)
    }

    constructor() {
        mData = MutableLiveData()
    }

    fun postValue(value: T) {
        mData.postValue(Event(value, false))
    }

    fun setValue(value: T) {
        mData.value = Event(value, false)
    }

    fun postCall() {
        mData.postValue(Event(null))
    }

    fun call() {
        mData.value = Event(null)
    }

    fun getValue(): T? {
        return mData.value?.peekContent()
    }

    @MainThread
    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<T>) {
        mData.observe(owner) observe2@ {
            if (it != null) {
                if (it.getIsInit()) {
                    return@observe2
                }
                observer.onChanged(it.peekContent())
            }
        }
    }

    fun observeOnce(onwner: LifecycleOwner, observer: Observer<T>) {
        mData.observe(onwner) observe2@ {
            if (it != null) {
                if (it.getIsInit()) {
                    return@observe2
                }

                if (it.getHasBeenHandled()) {
                    return@observe2
                }
                it.setHasBeenHandled()
                observer.onChanged(it.peekContent())
            }
        }
    }

}