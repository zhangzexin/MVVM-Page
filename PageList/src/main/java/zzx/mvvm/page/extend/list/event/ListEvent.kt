package zzx.mvvm.page.extend.list.event

class ListEvent<E> {
    var list: List<E>? = null
    var event: ILoadEvent = ILoadEvent.EVENT_DEFAULT
}