package com.saberrrc.cmy.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.presenter.Contract.TestContract;
import com.saberrrc.cmy.presenter.TestPresenter;

public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View {

    ;

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_test);
        //        Glide.with(App.getInstance()).load("http://img5.imgtn.bdimg.com/it/u=2348495697,1961865516&fm=27&gp=0.jpg")
        //                                                    .transform(new GlideRoundTransform(getContext(),20))
        //                .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 0, RoundedCornersTransformation.CornerType.ALL))
        //                .placeholder(R.mipmap.holder_img).error(R.mipmap.error_img).into(mIvPic);
        return view;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initData() {

    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == RESULT_CODE) {
            if (null == mUploadMessage) {
                return;
            }
            Uri result = intent == null || resultCode != 0 ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        }
    }
}
