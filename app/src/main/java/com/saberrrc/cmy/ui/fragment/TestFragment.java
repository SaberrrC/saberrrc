package com.saberrrc.cmy.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.widget.Button;

import com.github.lzyzsd.jsbridge.core.CallBackFunction;
import com.github.lzyzsd.jsbridge.core.DefaultHandler;
import com.github.lzyzsd.jsbridge.type.BridgeHandler;
import com.github.lzyzsd.jsbridge.view.BridgeWebView;
import com.google.gson.Gson;
import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.common.utils.LogUtil;
import com.saberrrc.cmy.presenter.Contract.TestContract;
import com.saberrrc.cmy.presenter.TestPresenter;

public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View, View.OnClickListener {

    private BridgeWebView webView;
    private Button        button;
    ;

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_test);
        webView = view.findViewById(R.id.webView);
        button = view.findViewById(R.id.button);
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


        button.setOnClickListener(this);

        webView.setDefaultHandler(new DefaultHandler());

        webView.setWebChromeClient(new WebChromeClient() {

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType, String capture) {
                this.openFileChooser(uploadMsg);
            }

            @SuppressWarnings("unused")
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String AcceptType) {
                this.openFileChooser(uploadMsg);
            }

            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                pickFile();
            }
        });

        webView.loadUrl("file:///android_asset/demo.html");

        webView.registerHandler("submitFromWeb", new BridgeHandler() {

            @Override
            public void handler(String data, CallBackFunction function) {
                LogUtil.d("handler = submitFromWeb, data from web = " + data);
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }

        });

        User user = new User();
        Location location = new Location();
        location.address = "SDU";
        user.location = location;
        user.name = "大头鬼";

        webView.callHandler("functionInJs", new Gson().toJson(user), new CallBackFunction() {
            @Override
            public void onCallBack(String data) {

            }
        });

        webView.send("hello");
    }

    public void pickFile() {
        Intent chooserIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooserIntent.setType("image/*");
        startActivityForResult(chooserIntent, RESULT_CODE);
    }

    int RESULT_CODE = 0;

    ValueCallback<Uri> mUploadMessage;

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            webView.callHandler("functionInJs", "data from Java", new CallBackFunction() {

                @Override
                public void onCallBack(String data) {
                    // TODO Auto-generated method stub
                    LogUtil.d("reponse data from js " + data);
                }

            });
        }
    }

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

    static class Location {
        String address;
    }

    static class User {
        String   name;
        Location location;
        String   testStr;
    }

}
