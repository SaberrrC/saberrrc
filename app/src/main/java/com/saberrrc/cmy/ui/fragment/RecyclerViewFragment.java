package com.saberrrc.cmy.ui.fragment;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.saberrrc.cmy.App;
import com.saberrrc.cmy.R;
import com.saberrrc.cmy.bean.HomeBannerBean;
import com.saberrrc.cmy.bean.result.BannerResultBean;
import com.saberrrc.cmy.common.base.BaseFragment;
import com.saberrrc.cmy.common.image.GlideRoundTransform;
import com.saberrrc.cmy.common.utils.DefaultOnPermissionListener;
import com.saberrrc.cmy.presenter.Contract.RecyclerViewContract;
import com.saberrrc.cmy.presenter.RecyclerViewPresenter;
import com.saberrrc.cmy.ui.adapter.FinalRecycleAdapter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

public class RecyclerViewFragment extends BaseFragment<RecyclerViewPresenter> implements RecyclerViewContract.View, FinalRecycleAdapter.OnViewAttachListener {

    @BindView(R.id.rc_content)
    RecyclerView mRecyclerView;
    private List<Object> mDatas = new ArrayList<>();
    private FinalRecycleAdapter mFinalRecycleAdapter;
    private List<Banner> MbList = new ArrayList<>();
    private RefreshLayout mRefreshLayout;

    @Override
    public View createView() {
        View view = creatViewFromId(R.layout.layout_recyclerview);
        mRefreshLayout = (RefreshLayout)view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
        return view;
    }

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    public void initData() {
        Map<Class, Integer> map = new HashMap<>();
        map.put(HomeBannerBean.class, R.layout.layout_item_home_banner);
        if (mDatas.size() > 0) {
            mDatas.clear();
        }
        for (int i = 0; i < 20; i++) {
            mDatas.add(new HomeBannerBean());
        }
        mFinalRecycleAdapter = new FinalRecycleAdapter(mDatas, map, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFinalRecycleAdapter);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollThreshold) {
                if (scrollThreshold != 0) {
                    for (Banner banner : MbList) {
                        if (banner != null) {
                            banner.stopAutoPlay();
                        }
                    }
                } else {
                    for (Banner banner : MbList) {
                        if (banner != null) {
                            banner.startAutoPlay();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onGetBannerListSuccess(String code, BannerResultBean data, String msg, int position) {
        final List<String> newBannerList = new ArrayList<>();
        newBannerList.add("http://img5.imgtn.bdimg.com/it/u=167873737,3665636002&fm=27&gp=0.jpg");
        newBannerList.add("http://img3.imgtn.bdimg.com/it/u=549189480,3550988247&fm=27&gp=0.jpg");
        newBannerList.add("http://img4.imgtn.bdimg.com/it/u=2890397330,2141066028&fm=200&gp=0.jpg");
        newBannerList.add("http://img5.imgtn.bdimg.com/it/u=2348495697,1961865516&fm=27&gp=0.jpg");
        newBannerList.add("http://img2.imgtn.bdimg.com/it/u=995596080,3982192314&fm=27&gp=0.jpg");
        newBannerList.add("http://img1.imgtn.bdimg.com/it/u=3338076047,3542768880&fm=27&gp=0.jpg");
        newBannerList.add("http://img4.imgtn.bdimg.com/it/u=893095850,4256793114&fm=27&gp=0.jpg");
        if (MbList.get(position) != null) {
            MbList.get(position).setImages(newBannerList);
            //banner设置方法全部调用完毕时最后调用
            MbList.get(position).start();
        }
    }

    @Override
    public void onGetBannerListFailed(Throwable ex, String code, String msg, int position) {
    }

    @Override
    public void onBindViewHolder(FinalRecycleAdapter.ViewHolder holder, int position, Object itemData) {
        if (itemData instanceof HomeBannerBean) {
            if (MbList.size() >= position + 1) {
                Banner banner = MbList.get(position);
                if (banner != null) {
                    banner.startAutoPlay();
                    return;
                }
            }
            Banner banner = (Banner) holder.getViewById(R.id.bv_banner);
            MbList.add(banner);
            banner.setImageLoader(new ImageLoader() {
                @Override
                public void displayImage(Context context, final Object path, final ImageView imageView) {
                    checkPermission(new DefaultOnPermissionListener(getActivity()) {
                        @Override
                        public void onPermissionGranted() {
                            Glide.with(App.getInstance()).load(path)
                                                                        .transform(new GlideRoundTransform(getContext(),20))
//                                    .bitmapTransform(new RoundedCornersTransformation(getContext(), 30, 0, RoundedCornersTransformation.CornerType.ALL))
                                    .placeholder(R.mipmap.holder_img).error(R.mipmap.error_img).into(imageView);
                        }
                    });
                }

                @Override
                public ImageView createImageView(Context context) {
                    ImageView imageView = new ImageView(getActivity());
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    return imageView;
                }
            });
            mPresenter.getBannerList(position);
            return;
        }
    }
}
