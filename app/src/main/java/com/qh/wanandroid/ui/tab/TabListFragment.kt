package com.qh.wanandroid.ui.tab

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.devlibrary.mvp.BaseMvpFragment
import com.example.devlibrary.widget.LoadMoreView
import com.qh.wanandroid.R
import com.qh.wanandroid.adapter.ArticleAdapter
import com.qh.wanandroid.bean.ArticleEntity
import com.qh.wanandroid.constant.Const
import com.qh.wanandroid.databinding.FragmentArticleListBinding

/**
 * @author FQH
 * Create at 2020/4/2.
 */
class TabListFragment :
    BaseMvpFragment<TabListContract.View, TabListContract.Presenter, FragmentArticleListBinding>(),
    TabListContract.View {

    private val articleList: MutableList<ArticleEntity.DatasBean> = mutableListOf()
    private val articleAdapter by lazy { ArticleAdapter(articleList) }
    private var isRefresh = false
    private var pageNum = 0
    private var projectId: Int = 0
    private var name: String? = null
    private var type: Int? = null

    override fun attachLayoutRes(): Int = R.layout.fragment_article_list

    override fun initData() {
        type = arguments?.getInt(Const.TYPE)
        projectId = arguments?.getInt(Const.ID) ?: 0
        name = arguments?.getString(Const.NAME) ?: ""
    }

    override fun initView(view: View) {
        super.initView(view)
        initRecyclerView()
        swipeRefresh.setOnRefreshListener { loadData() }
    }

    private fun initRecyclerView() {
        articleAdapter.loadMoreModule.setOnLoadMoreListener { loadMore() }
        articleAdapter.loadMoreModule.loadMoreView = LoadMoreView()
        articleAdapter.loadMoreModule.isAutoLoadMore = true
        //当自动加载开启，同时数据不满一屏时，是否继续执行自动加载更多(默认为true)
        articleAdapter.loadMoreModule.isEnableLoadMoreIfNotFullPage = false
        rvTabList.layoutManager = LinearLayoutManager(context)
        rvTabList.adapter = articleAdapter
    }

    override fun loadData() {
        // 这里的作用是防止下拉刷新的时候还可以上拉加载
        articleAdapter.loadMoreModule.isEnableLoadMore = false
        // 下拉刷新，需要重置页数
        pageNum = 0
        isRefresh = true
        type?.let { mPresenter?.loadData(it, projectId, pageNum) }
    }

    private fun loadMore() {
        ++pageNum
        isRefresh = false
        type?.let { mPresenter?.loadData(it, projectId, pageNum) }
    }

    override fun createPresenter(): TabListContract.Presenter {
        return TabListPresenter()
    }

    override fun showList(list: MutableList<ArticleEntity.DatasBean>) {
        swipeRefresh.isRefreshing = false
        articleAdapter.loadMoreModule.isEnableLoadMore = true
        if (isRefresh) {
            articleAdapter.setList(list)
        } else {
            articleAdapter.addData(list)
        }
        if (list.size < com.example.common.constant.Const.PAGE_SIZE) {
            //如果不够一页,显示没有更多数据布局
            articleAdapter.loadMoreModule.loadMoreEnd()
        } else {
            articleAdapter.loadMoreModule.loadMoreComplete()
        }
    }

    override fun showError(errorMsg: String) {
        super.showError(errorMsg)
        swipeRefresh.isRefreshing = false
        articleAdapter.loadMoreModule.isEnableLoadMore = (true)
        articleAdapter.loadMoreModule.loadMoreFail()
    }

    override fun collectSuccess() {

    }

    override fun unCollectSuccess() {

    }
}