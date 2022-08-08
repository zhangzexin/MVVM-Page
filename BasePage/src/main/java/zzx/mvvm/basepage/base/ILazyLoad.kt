package zzx.mvvm.basepage.base

/**
 *@描述：Fragment懒加载接口
 *@time：2022/8/8
 *@author:zhangzexin
 */
interface ILazyLoad {
    companion object {
        const val ON_ATTACH = 1
        const val ON_CREATE = 2
        const val ON_CREATE_VIEW = 3
        const val ON_ACTIVITY_CREATED = 4
        const val ON_START = 5
        const val ON_RESUME = 6
        const val ON_IDLE = 7
    }

    /**
     * 延迟加载
     */
    fun lazyLoad()
}