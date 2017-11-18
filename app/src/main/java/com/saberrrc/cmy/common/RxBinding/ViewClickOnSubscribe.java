package com.saberrrc.cmy.common.RxBinding;

import android.os.Looper;
import android.view.View;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;

final class ViewClickOnSubscribe implements ObservableOnSubscribe<Void> {
    private final View view;

    ViewClickOnSubscribe(View view) {
        this.view = view;
    }


    @Override
    public void subscribe(@NonNull final ObservableEmitter<Void> e) throws Exception {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw new IllegalStateException(
                    "Must be called from the main thread. Was: " + Thread.currentThread());
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (e.isDisposed()) {
                    e.onNext(null);
                }
            }
        });
    }
}