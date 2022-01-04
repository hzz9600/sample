package com.example.appdemo

import com.example.appdemo.beans.ArticleBean
import com.example.appdemo.beans.WanBaseBean
import com.example.appdemo.beans.WanListBean
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface IHttpService {
    @GET("/article/list/{page}/json")
    fun getArticleList(@Path("page") page: Int): Call<WanBaseBean<WanListBean<ArticleBean>>>
}
