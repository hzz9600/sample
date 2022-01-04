package com.hans.tinypaging;

import java.util.Collections;
import java.util.List;

public class LoadResult<T> {

    public static class Page<T> extends LoadResult<T>{
        private final List<T> data;
        private final boolean hasNextPage;

        public static <T> Page<T> empty (){
            return new Page<T>(Collections.EMPTY_LIST, false);
        }

        public Page(List<T> data, boolean hasNextPage) {
            this.data = data;
            this.hasNextPage = hasNextPage;
        }

        public boolean isEmpty() {
            return this.data == null || this.data.size() == 0;
        }

        public List<T> getData() {
            return data;
        }

        public boolean hasNextPage() {
            return hasNextPage;
        }
    }

    public static class Error extends LoadResult{
        private final Throwable throwable;
        private final String message;

        public Error(Throwable throwable) {
            this(throwable, throwable.getMessage());
        }

        public Error(Throwable throwable, String message) {
            this.throwable = throwable;
            this.message = message;
        }

        public Throwable getThrowable() {
            return throwable;
        }

        public String getMessage() {
            return message;
        }
    }
}
