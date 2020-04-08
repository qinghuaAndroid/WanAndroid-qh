package com.qh.wanandroid.ui.system.list

import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.devlibrary.ext.showToast
import com.example.devlibrary.mvvm.BaseVMFragment
import com.qh.wanandroid.R
import com.qh.wanandroid.adapter.SystemListAdapter
import com.qh.wanandroid.databinding.FragmentSystemListBinding
import com.qh.wanandroid.listener.OnLabelClickListener
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * @author FQH
 * Create at 2020/4/8.
 */
class SystemListFragment : BaseVMFragment<SystemListViewModel, FragmentSystemListBinding>() {

    private val mViewModel by viewModel<SystemListViewModel>()

    private val systemListAdapter by lazy { SystemListAdapter(null) }

    override fun attachLayoutRes(): Int = R.layout.fragment_system_list

    override fun initData() {

    }

    override fun initView(view: View) {
        mBinding.rvSystem.layoutManager = LinearLayoutManager(context)
        mBinding.rvSystem.adapter = systemListAdapter
        systemListAdapter.setOnLabelClickListener(object : OnLabelClickListener {
            override fun onLabelClick(helper: BaseViewHolder, i: Int, j: Int) {

            }
        })
    }

    override fun loadData() {
        mViewModel.getSystemList()
    }

    override fun startObserve() {
        mViewModel.systemListData.observe(this,
            Observer {
                systemListAdapter.setList(it)
            })
        mViewModel.errorData.observe(this, Observer {
            showToast(it)
        })
    }
}