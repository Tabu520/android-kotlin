package com.avenue.taipt.newsappjava.utils;

import java.util.List;

public class Resource<T> {

    private String message;
    private List<T> data = null;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
