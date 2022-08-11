package zzx.mvvm.page.extend.list.livedata

class Event<T> {
    private var content: T?
    private var isInit: Boolean = false
    private var hasBeenHandled = false

    constructor(value: T?) {
        content = value
    }

    constructor(value: T?, init: Boolean) {
        content = value
        isInit = init
    }

    fun peekContent(): T? {
        return content
    }

    fun getHasBeenHandled(): Boolean {
        return hasBeenHandled
    }

    fun getIsInit(): Boolean {
        return isInit
    }

    fun setHasBeenHandled() {
        hasBeenHandled = true
    }
}