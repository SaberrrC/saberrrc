package com.saberrrc.cmy.common.base;

import com.saberrrc.cmy.common.net.Api;

import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class RxPresenter<T extends BaseView> implements BasePresenter<T> {

    protected Api apiService;

    public RxPresenter(Api apiService) {
        this.apiService = apiService;
    }

    protected T mView;

    protected CompositeDisposable mCompositeDisposable;

    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscrebe(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void attachView(T view) {
        WeakReference<T> tSoftReference = new WeakReference<>(view);
        this.mView = tSoftReference.get();
    }

    @Override
    public void detachView() {
        this.mView = null;
        unSubscribe();
    }
}