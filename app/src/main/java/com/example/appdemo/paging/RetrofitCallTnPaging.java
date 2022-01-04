package com.example.appdemo.paging;

import android.util.Log;

import com.hans.tinypaging.LoadResult;
import com.hans.tinypaging.ViewControllerTnPaging;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public abstract class RetrofitCallTnPaging<ITEM> extends ViewControllerTnPaging<ITEM, Call> {
    private static final String TAG = RetrofitCallTnPaging.class.getSimpleName();


    @Override
    protected void doRequest(Call call, int page) {
        Log.i(TAG, "doRequest: page -> " + page);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                Log.i(TAG, "onResponse: " + response);
                LoadResult result;
                //主动调用成功获取数据
                if (response.body() == null) {
                    result = new LoadResult.Error(new HttpException(response));
                } else {
                    result = extractListFromResponse(response.body());
                }
                requestDone(result);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                //主动调用失败
                requestDone(new LoadResult.Error(t));
            }
        });
    }
}
