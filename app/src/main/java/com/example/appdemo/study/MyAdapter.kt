package com.example.appdemo.study

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.appdemo.beans.ArticleBean

class MyAdapter: BaseQuickAdapter<ArticleBean, BaseViewHolder>(android.R.layout.simple_list_item_2) {

    override fun convert(holder: BaseViewHolder, item: ArticleBean) {
        holder.setText(android.R.id.text1,item.title)
        holder.setText(android.R.id.text2," ==> ${item.niceDate}")

    }


}