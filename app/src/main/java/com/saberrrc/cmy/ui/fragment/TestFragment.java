package com.saberrrc.cmy.ui.fragment;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.saberrrc.cmy.App;
import com.saberrrc.cmy.R;
import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.common.image.GlideRoundTransform;
import com.saberrrc.cmy.presenter.Contract.TestContract;
import com.saberrrc.cmy.presenter.TestPresenter;

public class TestFragment extends BaseFragment<TestPresenter> implements TestContract.View {

    private ImageView mIvPic;

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_test);
        mIvPic = view.findViewById(R.id.iv_pic);
        Glide.with(App.getInstance()).load("http://img5.imgtn.bdimg.com/it/u=2348495697,1961865516&fm=27&gp=0.jpg")
                                                    .transform(new GlideRoundTransform(getContext(),20))
//                .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 0, RoundedCornersTransformation.CornerType.ALL))
                .placeholder(R.mipmap.holder_img).error(R.mipmap.error_img).into(mIvPic);
        return view;
    }


    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initData() {
        mPresenter.getBannerList();
    }

    @Override
    public void onGetBannerListSuccess(String code, BannerResultBean data, String msg) {

    }

    @Override
    public void onGetBannerListFailed(Throwable ex, String code, String msg) {

    }
}
