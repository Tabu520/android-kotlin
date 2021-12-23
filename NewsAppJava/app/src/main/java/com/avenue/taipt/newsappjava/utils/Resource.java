package com.avenue.taipt.newsappjava.utils;

import java.util.List;

public class Resource<T> {

    protected String message;
    protected T data = null;

    public Resource() {

    }

    public Resource(T data, String message) {
        this.data = data;
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class Success<T> extends Resource<T> {

        public Success(T data) {
            super(data, null);
        }
    }

    public static class Error<T> extends Resource<T> {
        public Error(T data, String message) {
            super(data, message);
        }
    }

    public static class Loading<T> extends Resource<T> {
        public Loading() {
            super();
        }
    }

//    class Success<T>(data: T) : Resource<T>(data)
//    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
//    class Loading<T> : Resource<T>()

}
