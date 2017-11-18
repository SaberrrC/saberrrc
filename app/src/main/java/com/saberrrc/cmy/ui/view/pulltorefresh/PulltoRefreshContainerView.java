package com.saberrrc.cmy.ui.view.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.saberrrc.cmy.R;

public class PulltoRefreshContainerView extends SuperSwipeRefreshLayout {
    /**
     * 刷新加载监听事件
     */
    private RefreshLoadMoreListener mRefreshLoadMoreListener;

    private RefreshDistanceListener mRefreshDistanceListener;
    /**
     * 是否可以加载更多
     */
    private boolean hasMore    = true;
    /**
     * 是否正在刷新
     */
    private boolean isRefresh  = false;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadMore = false;

    /***
     * 头部布局
     */
    private ImageView         mIvPullRefreshArrow;
    //    private TextView mTvPullRefreshTip;
    private AnimationDrawable mAnimationDrawable;

    public void setSeekbar(View seekbar) {
        setExceptionview(seekbar);
    }

    private ImageView          mIvFootLoad;
    private TextView           mTvFootLoadMoreTip;
    private Animation          mFootAnimation;
    private LinearInterpolator mLin;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    public PulltoRefreshContainerView(Context context) {
        this(context, null);
    }

    public PulltoRefreshContainerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createView(context, attrs);
        initListener();
        createHeadView();
        createFootView();
    }


    private void createView(Context context, AttributeSet attrs) {


    }


    private void initListener() {

        //此页面默认无法上拉加载更多
        setPullLoadMoreEnable(false);
        /**
         * 监听上拉至底部滚动监听
         */

        //下拉至顶部刷新监听
        setOnPullRefreshListener(new OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                //                mTvPullRefreshTip.setText("正在刷新...");
                //                mIvPullRefreshArrow.setVisibility(GONE);
                try {
                    mIvPullRefreshArrow.setImageResource(R.drawable.anim_pulltorefresh_refresh);
                    mAnimationDrawable = (AnimationDrawable) mIvPullRefreshArrow.getDrawable();
                    if (mAnimationDrawable != null) {
                        mAnimationDrawable.start();
                    }
                } catch (OutOfMemoryError ex) {
                    ex.printStackTrace();
                }
                if (!isRefresh) {
                    isRefresh = true;
                    refresh();
                }
            }

            @Override
            public void onPullDistance(int distance) {
                float percent = distance / getDistanceToTriggerSync();
                if (percent > 1) {
                    percent = 1f;
                }
                ViewCompat.setScaleX(mIvPullRefreshArrow, percent);
                ViewCompat.setScaleY(mIvPullRefreshArrow, percent);
                if (mRefreshDistanceListener != null) {
                    mRefreshDistanceListener.onPullDistance(distance);
                }
            }

            @Override
            public void onPullEnable(boolean enable) {
                //                mTvPullRefreshTip.setText(enable ? "松开刷新" : "下拉刷新");
                //                mIvPullRefreshArrow.setVisibility(View.VISIBLE);
                //                mIvPullRefreshArrow.setRotation(enable ? 180 : 0);
            }
        });

        setOnPushLoadMoreListener(new OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (hasMore && !isLoadMore) {
                    isLoadMore = true;
                    startAnimation();
                    loadMore();
                }
            }

            @Override
            public void onPushDistance(int distance) {
                mIvFootLoad.setRotation(distance * 2);
                if (mRefreshDistanceListener != null) {
                    mRefreshDistanceListener.onPushDistance(distance);
                }
            }

            @Override
            public void onPushEnable(boolean enable) {
                if (mTvFootLoadMoreTip != null) {
                    if (enable) {
                        mTvFootLoadMoreTip.setText("pull");
                    } else {
                        mTvFootLoadMoreTip.setText("to");
                    }
                }
            }
        });

        //        mRecyclerView.setOnScrollListener(mScrollListener);

    }

    private void createHeadView() {
        setHeaderViewBackgroundColor(0x00000000);
        View headView = LayoutInflater.from(mContext).inflate(R.layout.pullrefresh_head, this, false);
        setHeaderView(headView);
        mIvPullRefreshArrow = (ImageView) headView.findViewById(R.id.iv_pullrefresh_head);
        //        mTvPullRefreshTip = UIUtils.findViewById(headView,R.id.tv_pullrefresh_tip);
    }

    private void createFootView() {
        mLin = new LinearInterpolator();
        mFootAnimation = AnimationUtils.loadAnimation(mContext, R.anim.load_more_progress_anim);
        mFootAnimation.setInterpolator(mLin);
        View footView = LayoutInflater.from(mContext).inflate(R.layout.pullrefresh_load, this, false);
        mIvFootLoad = (ImageView) footView.findViewById(R.id.infoOperating);
        mTvFootLoadMoreTip = (TextView) footView.findViewById(R.id.tv_load_more_tip);
        setFooterView(footView);
    }


    /***
     * 是否正在拖动
     *
     * @return
     */
    public boolean isDragging() {
        return getIsBeginDragged();
    }


    public void setPullLoadMoreEnable(boolean enable) {
        this.hasMore = enable;
        this.loadMoreEnable = enable;
    }

    public boolean getPullLoadMoreEnable() {
        return hasMore;
    }

    public void setPullRefreshEnable(boolean enable) {
        refreshEnable = enable;
        //        setEnabled(enable);
    }

    public boolean getPullRefreshEnable() {
        return isEnabled();
    }

    public void loadMore() {
        if (mRefreshLoadMoreListener != null && hasMore) {
            mRefreshLoadMoreListener.onLoadMore();
        }
    }

    @Override
    protected void stopRefreshAnim() {
        stopRefresh();
    }

    @Override
    protected void stopLoadMoreAnim() {
        setLoadMoreCompleted();
    }

    /**
     * 加载更多完毕,为防止频繁网络请求,isLoadMore为false才可再次请求更多数据
     */
    public void setLoadMoreCompleted() {
        isLoadMore = false;
        setLoadMore(false);
        endAnimation();
    }

    public void setLoadMoreCompleted(boolean toBottom) {
        isLoadMore = false;
        if (toBottom) {
            setLoadMore(false);
        } else {
            setFooterViewContainerGone();
        }
        endAnimation();
    }

    public void stopRefresh() {
        isRefresh = false;
        setRefreshing(false);
        if (mAnimationDrawable != null) {
            mAnimationDrawable.stop();
        }
        mIvPullRefreshArrow.setImageResource(R.mipmap.preloader_2_00000);
    }

    public void setRefreshLoadMoreListener(RefreshLoadMoreListener listener) {
        mRefreshLoadMoreListener = listener;
    }

    public void setRefreshDistanceListener(RefreshDistanceListener refreshDistanceListener) {
        this.mRefreshDistanceListener = refreshDistanceListener;
    }

    public void refresh() {
        if (mRefreshLoadMoreListener != null) {
            mRefreshLoadMoreListener.onRefresh();
        }
    }

    private void startAnimation() {
        if (mFootAnimation != null && mIvFootLoad != null) {
            mIvFootLoad.startAnimation(mFootAnimation);
        }
        if (mTvFootLoadMoreTip != null) {
            mTvFootLoadMoreTip.setText("loading");
        }
    }

    private void endAnimation() {
        if (mFootAnimation != null) {
            mIvFootLoad.clearAnimation();
        }
    }

    public interface RefreshLoadMoreListener {
        void onRefresh();

        void onLoadMore();
    }

    public interface RefreshDistanceListener {
        void onPullDistance(int distance);

        void onPushDistance(int distance);
    }
}
