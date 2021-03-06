package com.wan.common.arouter

import android.content.Context
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.wan.common.constant.Const
import com.tencent.mmkv.MMKV

/**
 * @author cy
 * Create at 2020/4/22.
 */
@Interceptor(priority = 8, name = "login")
class LoginInterceptor : IInterceptor {
    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        if (postcard?.extra == Const.NEED_LOGIN) {
            val isLogin = MMKV.defaultMMKV()!!.decodeBool(Const.IS_LOGIN, false)
            if (isLogin) {
                callback?.onContinue(postcard)
            } else {
                callback?.onInterrupt(RuntimeException("please to login"))
            }
        } else {
            callback?.onContinue(postcard)
        }
    }

    override fun init(context: Context?) {

    }
}