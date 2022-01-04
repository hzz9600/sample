package com.hans.tinypaging;

public interface ITnPagingViewController {

    enum STATUS {
        NORMAL,LOADING,ERROR,EMPTY
    }

    void toggleNormalView();

    void toggleLoadingView();

    void toggleErrorView(String error);

    void toggleEmptyView();


    void onFinishLoading();

    void onLoadMoreError(String error);

    void onFinishSetNoMoreData(boolean noMore);
}
