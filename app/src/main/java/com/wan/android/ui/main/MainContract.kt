package com.wan.android.ui.main

import com.wan.baselib.mvp.IModel
import com.wan.baselib.mvp.IPresenter
import com.wan.baselib.mvp.IView
import com.wan.baselib.network.HttpResult
import com.wan.android.bean.UserInfoEntity
import io.reactivex.rxjava3.core.Observable

/**
 * @author cy
 * Create at 2020/4/20.
 */
interface MainContract {

    interface View : IView {
        fun showLogoutSuccess(success: Boolean)
        fun showUserInfo(bean: UserInfoEntity)
    }

    interface Presenter : IPresenter<View> {
        fun logout()
        fun getUserInfo()
    }

    interface Model : IModel {
        fun logout(): Observable<HttpResult<Any>>
        fun getUserInfo(): Observable<HttpResult<UserInfoEntity>>
    }
}