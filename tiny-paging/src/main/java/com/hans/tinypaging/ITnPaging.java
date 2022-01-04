package com.hans.tinypaging;

public interface ITnPaging {


    interface CallCreator<CALL> {
        CALL buildCall(int page, int size);
    }

    interface ExceptionCallBack {
        void onException(Throwable throwable);
    }
}
