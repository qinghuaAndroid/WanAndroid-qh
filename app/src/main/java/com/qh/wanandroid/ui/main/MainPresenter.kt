package com.qh.wanandroid.ui.main

import com.example.devlibrary.mvp.BasePresenter
import com.example.devlibrary.network.RxHelper
import com.example.devlibrary.network.RxObserver
import com.qh.wanandroid.bean.UserInfoEntity

/**
 * @author FQH
 * Create at 2020/4/10.
 */
class MainPresenter : BasePresenter<MainContract.Model, MainContract.View>(),
    MainContract.Presenter {

    override fun createModel(): MainContract.Model? {
        return MainModel()
    }

    override fun logout() {
        mView?.showLoading()
        val disposableObserver = mModel?.logout()?.compose(RxHelper.handleResult())
            ?.subscribeWith(object : RxObserver<Any>() {
                override fun onSuccess(t: Any?) {
                    mView?.hideLoading()
                    mView?.showLogoutSuccess(true)
                }

                override fun onFail(errorCode: Int, errorMsg: String?) {
                    mView?.hideLoading()
                    mView?.showLogoutSuccess(false)
                }
            })
        addDisposable(disposableObserver)
    }

    override fun getUserInfo() {
        mView?.showLoading()
        val disposableObserver = mModel?.getUserInfo()?.compose(RxHelper.handleResult())
            ?.subscribeWith(object : RxObserver<UserInfoEntity>() {
                override fun onSuccess(t: UserInfoEntity?) {
                    mView?.hideLoading()
                    t?.let { mView?.showUserInfo(t) }
                }

                override fun onFail(errorCode: Int, errorMsg: String?) {
                    mView?.hideLoading()
                    mView?.showError(errorMsg)
                }
            })
        addDisposable(disposableObserver)
    }
}