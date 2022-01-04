package com.hans.tinypaging;

import com.hans.tinypaging.ITnPagingViewController.STATUS;

/**
 * 抽象视图控制的分页器
 *
 * @param <ITEM>   每项数据实体
 * @param <SOURCE> 请求类型
 */
public abstract class ViewControllerTnPaging<ITEM, SOURCE> extends AbstractTinyPaging<ITEM, SOURCE> {

    private ITnPagingViewController viewController;
    private STATUS status = STATUS.NORMAL;

    protected abstract void registerListener();

    /**
     * 每次绑定组件时必须调用
     */
    protected void bindInit() {
        registerListener();
    }

    public void setViewController(ITnPagingViewController controller) {
        this.viewController = controller;
    }

    protected void onViewStatusChange(STATUS status) {

    }

    protected void restoreView() {
        changeViewLayoutByStatus(this.status, null);
    }


    private void changeViewStatus(STATUS st, String text) {
        if (st != status) {
            changeViewLayoutByStatus(st, text);
            onViewStatusChange(st);
        }
        this.status = st;
    }

    private void changeViewLayoutByStatus(STATUS st, String text) {
        if (this.viewController != null) {
            switch (st) {
                case NORMAL:
                    viewController.toggleNormalView();
                    break;
                case EMPTY:
                    viewController.toggleEmptyView();
                    break;
                case LOADING:
                    viewController.toggleLoadingView();
                    break;
                case ERROR:
                    viewController.toggleErrorView(text);
                    break;
            }
        }
    }

    @Override
    protected void handleRequestBefore() {
        if (isFirstPage() && (status == STATUS.ERROR || status == STATUS.EMPTY)) {
            changeViewStatus(STATUS.LOADING, null);
        }
    }

    @Override
    protected void handleResultAfter(LoadResult.Page<ITEM> page) {
        //只在第一页时才切换试图，列表有数据时应该只控制刷新组件停止加载行为
        if (isFirstPage() && page.isEmpty()) {
            changeViewStatus(STATUS.EMPTY, null);
        } else {
            changeViewStatus(STATUS.NORMAL, null);
        }


        if (viewController != null) {
            viewController.onFinishLoading();
            if (page.isEmpty()) {
                viewController.onFinishSetNoMoreData(true);
            }
        }
    }

    @Override
    protected void handErrorAfter(LoadResult.Error error) {
        if (isFirstPage()) {
            changeViewStatus(STATUS.ERROR, error.getMessage());
        }

        if (viewController != null) {
            if (isFirstPage()) {

            } else {
                viewController.onLoadMoreError(error.getMessage());
            }
            viewController.onFinishLoading();
        }

    }
}
