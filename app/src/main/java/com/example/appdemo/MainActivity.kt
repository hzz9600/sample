package com.example.appdemo

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.appdemo.beans.ArticleBean
import com.example.appdemo.beans.WanBaseBean
import com.example.appdemo.beans.WanListBean
import com.example.appdemo.paging.WanSmartCallListLoader
import com.example.appdemo.study.MyAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private val loader by lazy {
        WanSmartCallListLoader<WanBaseBean<WanListBean<ArticleBean>>>()
            .apply {
            }
    }


    private lateinit var mRefreshLayout: SmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MyAdapter()

        mRefreshLayout = findViewById(R.id.refreshLayout)
        mRecyclerView = findViewById(R.id.recyclerView)

        mRecyclerView.adapter = adapter

        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.wanandroid.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
            )
            .build()

        loader.buildCall { page, _ ->
            retrofit.create(IHttpService::class.java).getArticleList(page)
        }.exception {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
        }
            .lifecycle(this)
            .bindViewAdapter(mRefreshLayout, adapter)

        mRefreshLayout.autoRefresh()

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}