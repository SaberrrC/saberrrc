package com.saberrrc.cmy.common.RxBinding;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.view.View;

import io.reactivex.Observable;

public class RxView {
    @CheckResult
    @NonNull
    public static Observable<Void> clicks(@NonNull View view) {
        return Observable.create(new ViewClickOnSubscribe(view));
    }

}
