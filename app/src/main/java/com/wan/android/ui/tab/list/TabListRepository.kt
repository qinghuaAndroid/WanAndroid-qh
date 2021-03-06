package com.wan.android.ui.tab.list

import com.wan.baselib.mvvm.BaseRepository
import com.wan.baselib.mvvm.Result
import com.wan.android.bean.ArticleEntity
import com.wan.android.http.HttpHelper

/**
 * @author cy
 * Create at 2020/4/16.
 */
class TabListRepository: BaseRepository() {

    suspend fun getProjectList(pageNum: Int, id: Int):Result<ArticleEntity>{
        return safeApiCall { requestProjectList(pageNum, id) }
    }

    private suspend fun requestProjectList(pageNum: Int, id: Int):Result<ArticleEntity> {
        return executeResponse(HttpHelper.apiService.getProjectList(pageNum, id))
    }

    suspend fun getAccountList(id: Int, pageNum: Int):Result<ArticleEntity>{
        return safeApiCall { requestAccountList(id, pageNum) }
    }

    private suspend fun requestAccountList(id: Int, pageNum: Int):Result<ArticleEntity> {
        return executeResponse(HttpHelper.apiService.getAccountList(id, pageNum))
    }
}