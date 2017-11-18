package com.saberrrc.cmy.common.base;

public interface BasePresenter<T extends BaseView> {

    void attachView(T view);

    void detachView();
}
