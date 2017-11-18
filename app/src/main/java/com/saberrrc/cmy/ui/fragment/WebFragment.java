package com.saberrrc.cmy.ui.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.saberrrc.cmy.R;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.common.constants.Constant;
import com.saberrrc.cmy.presenter.Contract.WebContract;
import com.saberrrc.cmy.presenter.WebPresenter;

import butterknife.BindView;

public class WebFragment extends BaseFragment<WebPresenter> implements WebContract.View {

    @BindView(R.id.web_view)
    WebView mWebView;
    private String mUrl;

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_web);
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        mUrl = arguments.getString(Constant.WebView.URL);
        initWebView();
    }

    private void initWebView() {
        mWebView.setHorizontalScrollBarEnabled(false);//水平不显示
        mWebView.setVerticalScrollBarEnabled(false); //垂直不显示
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);//设置是当前html界面自适应屏幕
        mWebView.getSettings().setSupportZoom(true); //设置支持缩放
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setAllowFileAccess(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.getSettings().setDefaultTextEncodingName("utf-8");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }
        });
        if (!TextUtils.isEmpty(mUrl)) {
            mWebView.loadUrl(mUrl);
        }
    }
}