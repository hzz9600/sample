package com.example.appdemo.paging;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.appdemo.R;
import com.hans.tinypaging.ITnPagingViewController;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import me.bakumon.statuslayoutmanager.library.DefaultOnStatusChildClickListener;
import me.bakumon.statuslayoutmanager.library.StatusLayoutManager;
import retrofit2.Call;

public class WanSmartCallListLoader<ITEM> extends WanBeanExtractCallTnPaging<ITEM> implements ITnPagingViewController {
    private static final String TAG = WanSmartCallListLoader.class.getSimpleName();
    private SmartRefreshLayout view;
    private BaseQuickAdapter adapter;
    private StatusLayoutManager statusLayoutManager;

    private List<ITEM> allDatas = new ArrayList<>();

    @Override
    public WanSmartCallListLoader buildCall(CallCreator<Call> call) {
        return (WanSmartCallListLoader) super.buildCall(call);
    }

    @Override
    public WanSmartCallListLoader exception(ExceptionCallBack ex) {
        return (WanSmartCallListLoader) super.exception(ex);
    }

    @Override
    public WanSmartCallListLoader lifecycle(LifecycleOwner owner) {
        return (WanSmartCallListLoader) super.lifecycle(owner);
    }

    public void bindViewAdapter(SmartRefreshLayout view, BaseQuickAdapter adapter) {
        bindViewAdapter(view, adapter, new StatusLayoutManager.Builder(view.getLayout()));
    }

    public void bindViewAdapter(SmartRefreshLayout view, BaseQuickAdapter adapter, StatusLayoutManager.Builder builder) {
        this.view = view;
        this.adapter = adapter;

        //视图控制
        setViewController(this);
        statusLayoutManager = builder.setOnStatusChildClickListener(new DefaultOnStatusChildClickListener() {

            @Override
            public void onEmptyChildClick(View view) {
                listRefresh();
            }

            @Override
            public void onErrorChildClick(View view) {
                listRefresh();
            }
        }).build();

        //恢复上次状态
        restoreView();

        //重新绑定后必须调用，用来初始化监听
        bindInit();
    }


    @Override
    protected void registerListener() {
        view.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //主动调用加载更多
                listLoadMore();
            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //主动调用刷新
                listRefresh();
            }
        });
    }

    @Override
    protected void storeData(List<ITEM> list, int page) {
        if (isFirstPage()) {
            allDatas.clear();
        }

        allDatas.addAll(list);
        if (this.adapter != null) {
            this.adapter.setList(allDatas);
        }
    }

    @Override
    protected void onViewStatusChange(ITnPagingViewController.STATUS status) {

    }

    @Override
    public void toggleNormalView() {
        statusLayoutManager.showSuccessLayout();
    }

    @Override
    public void toggleLoadingView() {
        statusLayoutManager.showLoadingLayout();
    }

    @Override
    public void toggleErrorView(String error) {
        if (error != null) {
            TextView textView = statusLayoutManager.getErrorLayout().findViewById(R.id.tv_status_error_content);
            textView.setText(error);
        }
        statusLayoutManager.showErrorLayout();
    }

    @Override
    public void toggleEmptyView() {
        statusLayoutManager.showEmptyLayout();
    }

    @Override
    public void onFinishLoading() {
        Log.i(TAG, "onFinishLoading: ");
        view.finishRefresh();
        view.finishLoadMore();
    }

    @Override
    public void onLoadMoreError(String message) {
        // TODO: 2021/11/19 加载更多 错误
    }


    @Override
    public void onFinishSetNoMoreData(boolean noMore) {
        view.setNoMoreData(noMore);
    }
}
