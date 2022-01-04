package com.hans.tinypaging;

import android.util.Log;

import androidx.annotation.CallSuper;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.util.List;

/**
 * 基本分页器
 *
 * @param <ITEM>   每项数据实体
 * @param <SOURCE> 请求类型
 */
public abstract class AbstractTinyPaging<ITEM, SOURCE> implements ITnPaging {

    private static final String TAG = AbstractTinyPaging.class.getSimpleName();

    private int defaultPage;
    private int pageSize;

    private int curPage;
    private int nextPage;


    private CallCreator<SOURCE> call;
    private ExceptionCallBack exceptionCall;
    private LifecycleOwner lifecycleOwner;

    public AbstractTinyPaging() {
        setPaging(ListLoaderConfig.DEFAULT_PAGE_START, ListLoaderConfig.DEFAULT_PAGE_SIZE);
    }

    public void setPaging(int defaultPage, int pageSize) {
        this.defaultPage = defaultPage;
        this.pageSize = pageSize;
        curPage = nextPage = defaultPage;
    }

    public int getCurPage() {
        return curPage;
    }

    public int getNextPage() {
        return nextPage;
    }

    public int getDefaultPage() {
        return defaultPage;
    }

    public AbstractTinyPaging buildCall(CallCreator<SOURCE> call) {
        this.call = call;
        return this;
    }

    public AbstractTinyPaging exception(ExceptionCallBack ex) {
        this.exceptionCall = ex;
        return this;
    }

    public AbstractTinyPaging lifecycle(LifecycleOwner owner) {
        this.lifecycleOwner = owner;
        return this;
    }

    private boolean isSafeLifecycleState() {
        //如果不绑定生命周期对象,将始终安全
        if (lifecycleOwner == null)
            return true;
        return lifecycleOwner.getLifecycle().getCurrentState() != Lifecycle.State.DESTROYED;
    }

    protected abstract LoadResult<ITEM> extractListFromResponse(Object response);

    protected abstract void doRequest(SOURCE call, int page);

    protected void listLoadMore() {
        nextPage++;
        execRequest();
    }

    public void listRefresh() {
        nextPage = defaultPage;
        execRequest();
    }

    public final boolean isFirstPage() {
        return nextPage == defaultPage;
    }

    private void execRequest() {
        if (call == null) {
            throw new IllegalArgumentException("必须配置请求回调");
        }
        handleRequestBefore();
        doRequest(call.buildCall(nextPage, pageSize), nextPage);
    }

    protected abstract void storeData(List<ITEM> list, int page);

    protected final void requestDone(LoadResult<ITEM> response) {
        if (!isSafeLifecycleState()) {
            //生命周期不安全,不往下回调
            Log.e(TAG, "requestDone -> lifecycle state is not safe!");
            return;
        }

        if (response instanceof LoadResult.Error) {
            //处理错误
            handleError((LoadResult.Error) response);
        } else if (response instanceof LoadResult.Page) {
            //处理成功数据
            handleResult((LoadResult.Page<ITEM>) response);
        } else {
            throw new IllegalArgumentException("UnKnow LoadResult Type!");
        }

    }

    /**
     * 正常返回处理
     */
    @CallSuper
    private void handleResult(LoadResult.Page<ITEM> page) {
        //处理数据
        boolean isEmpty = page.isEmpty();

        if (!isEmpty) {
            try {
                //保存数据,并回调
                storeData(page.getData(), nextPage);
                curPage = nextPage;//只要上面不报错才增加页数
            } catch (Exception e) {
                //捕获处理数据或者数据返回不规范业务处理时的报错，防止应用闪退
                handleError(new LoadResult.Error(e));
                return;
            }
        }
        handleResultAfter(page);
    }

    /**
     * 错误异常处理
     */
    @CallSuper
    protected void handleError(LoadResult.Error error) {
        if (!isFirstPage()) {
            //不是第一次加载了，重置nextPage为curPage,用于下次重复拉起这次请求,同时回调到列表中的"加载失败"提示
            nextPage = curPage;
        }
        handErrorAfter(error);
        if (exceptionCall != null) {
            exceptionCall.onException(error.getThrowable());
        }
    }

    protected void handleRequestBefore() {

    }

    protected void handleResultAfter(LoadResult.Page<ITEM> page) {

    }

    protected void handErrorAfter(LoadResult.Error error) {

    }
}
