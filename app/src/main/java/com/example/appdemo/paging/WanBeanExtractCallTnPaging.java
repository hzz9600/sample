package com.example.appdemo.paging;

import com.example.appdemo.beans.WanBaseBean;
import com.example.appdemo.beans.WanListBean;
import com.hans.tinypaging.LoadResult;

import java.util.List;

/**
 * 实现默认业务拆包逻辑，其它业务控件可继承此类
 *
 * @param <ITEM>
 */
public abstract class WanBeanExtractCallTnPaging<ITEM> extends RetrofitCallTnPaging<ITEM> {

    @Override
    protected LoadResult<ITEM> extractListFromResponse(Object response) {
        boolean hasNext = true;
        List<ITEM> datas = null;

        if (response instanceof WanBaseBean) {
            WanBaseBean bean = (WanBaseBean) response;

            if (bean.data instanceof WanListBean) {
                WanListBean data = (WanListBean) bean.data;
                int current = data.offset;
                int total = data.total;

                datas = data.datas;//装载列表数据
                if (datas == null || current >= total) {
                    hasNext = false;
                }
            }
        }

        //如果上一步骤解包未装载数据，默认会返回一个空结果，加载更多将结束
        LoadResult.Page<ITEM> result = new LoadResult.Page<>(datas, hasNext);
        return result;
    }
}
