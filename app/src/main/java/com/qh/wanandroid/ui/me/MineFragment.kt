package com.qh.wanandroid.ui.me

import android.view.View
import androidx.lifecycle.Observer
import com.example.common.constant.Const
import com.example.devlibrary.helper.LiveEventBusHelper
import com.example.devlibrary.mvvm.BaseVMFragment
import com.example.devlibrary.utils.StringUtils
import com.qh.wanandroid.R
import com.qh.wanandroid.databinding.FragmentMineBinding
import com.qh.wanandroid.ui.girl.GirlActivity
import com.qh.wanandroid.ui.login.LoginActivity
import org.jetbrains.anko.support.v4.startActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author FQH
 * Create at 2020/4/2.
 */
class MineFragment : BaseVMFragment<MineViewModel, FragmentMineBinding>() {

    private val mViewModel: MineViewModel by viewModel()

    override fun startObserve() {
        mViewModel.integralData.observe(this,
            Observer {
                mBinding.tvUserName.text = it.username
                mBinding.tvId.text = StringUtils.format("id:%s", it.userId.toString())
                mBinding.tvRanking.text = it.rank.toString()
                mBinding.tvIntegral.text = it.coinCount.toString()
            })
    }

    override fun attachLayoutRes(): Int = R.layout.fragment_mine

    override fun initData() {
        LiveEventBusHelper.observe(Const.LOGIN_SUCCESS, Boolean::class.java, this,
            Observer<Boolean> { loadData() })
    }

    override fun initView(view: View) {
        mBinding.tvUserName.onClick { startActivity<LoginActivity>() }
        mBinding.llHistory.onClick {  }//足迹
        mBinding.llRanking.onClick {  }//排名
        mBinding.rlIntegral.onClick {  }//我的积分
        mBinding.rlCollect.onClick {  }//我的收藏
        mBinding.rlArticle.onClick {  }//我的文章
        mBinding.rlWebsite.onClick {  }//网站
        mBinding.rlGirl.onClick { startActivity<GirlActivity>() }//轻松一下
        mBinding.rlSet.onClick {  }//设置
    }

    override fun loadData() {
        mViewModel.getIntegral()
    }

}