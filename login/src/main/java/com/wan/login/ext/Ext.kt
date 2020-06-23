package com.wan.login.ext

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.common.constant.Const
import com.example.devlibrary.ext.showToast
import com.tencent.mmkv.MMKV
import com.wan.login.R

/**
 * @author FQH
 * Create at 2020/4/13.
 */
internal fun Fragment.startActivity(clazz: Class<*>, needLogin: Boolean = false) {
    val isLogin = MMKV.defaultMMKV().decodeBool(Const.IS_LOGIN, false)
    if (needLogin && isLogin.not()) {
        showToast(resources.getString(R.string.login_tint))
        startActivity(Intent(activity, com.wan.login.LoginActivity::class.java))
    } else {
        startActivity(Intent(activity, clazz))
    }
}

internal fun Activity.startActivity(clazz: Class<*>, needLogin: Boolean = false) {
    val isLogin = MMKV.defaultMMKV().decodeBool(Const.IS_LOGIN, false)
    if (needLogin && isLogin.not()) {
        showToast(resources.getString(R.string.login_tint))
        startActivity(Intent(this, com.wan.login.LoginActivity::class.java))
    } else {
        startActivity(Intent(this, clazz))
    }
}