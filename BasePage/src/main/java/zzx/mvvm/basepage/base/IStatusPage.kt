package zzx.mvvm.basepage.base


/**
 * @描述：管理页面展示状态接口
 * @author：zhangzexin
 * @time:2022/7/15
 */
interface IStatusPage {
    var mPageStatus: PageStatus
    fun showLoading()
    fun showError()
    fun showEmpty()
    fun showContent()
}