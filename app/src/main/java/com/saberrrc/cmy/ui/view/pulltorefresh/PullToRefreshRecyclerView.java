package com.saberrrc.cmy.ui.view.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.saberrrc.cmy.R;

public class PullToRefreshRecyclerView extends SuperSwipeRefreshLayout {
    /**
     * 垂直方向
     */
    private final int VERTICAL   = LinearLayoutManager.VERTICAL;
    /**
     * 水平方向
     */
    private final int HORIZONTAL = LinearLayoutManager.HORIZONTAL;

    private final int LINEAR_LAYOUT_MANAGER = 0;
    private final int GRID_LAYOUT_MANAGER   = 1;
    /**
     * 内容控件
     */
    private RecyclerView            mRecyclerView;
    /**
     * 刷新布局控件
     */
    private LinearLayoutManager     mLayoutManager;
    /**
     * 刷新布局的监听
     */
    //    private OnRefreshListener mRefreshListener;
    /**
     * 内容控件滚动监听
     */
    //    private RecyclerView.OnScrollListener mScrollListener;
    /**
     * 内容适配器
     */
    private RecyclerView.Adapter    mAdapter;
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

    private ImageView          mIvFootLoad;
    private TextView           mTvFootLoadMoreTip;
    private Animation          mFootAnimation;
    private LinearInterpolator mLin;

    public PullToRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        createView(context, attrs);
        initListener();
        createHeadView();
        createFootView();
    }


    private void createView(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PullToRefreshRecyclerView);
        int layoutType = a.getInt(R.styleable.PullToRefreshRecyclerView_layoutType, LINEAR_LAYOUT_MANAGER);// 默认为线性布局管理
        int orientation = a.getInt(R.styleable.PullToRefreshRecyclerView_orientation, VERTICAL);//默认是垂直的线性布局
        int spanCount = a.getInt(R.styleable.PullToRefreshRecyclerView_spanSize, 2);//默认grid两列
        int spacingH = a.getDimensionPixelSize(R.styleable.PullToRefreshRecyclerView_spacingH, -1);//
        int spacingV = a.getDimensionPixelSize(R.styleable.PullToRefreshRecyclerView_spacingV, -1);
        boolean includeEdge = a.getBoolean(R.styleable.PullToRefreshRecyclerView_includeEdge, true);
        boolean useItemAnim = a.getBoolean(R.styleable.PullToRefreshRecyclerView_useItemAnim, false);//默认不使用动画
        a.recycle();
        mRecyclerView = new RecyclerView(context);

        // 加载颜色是循环播放的，只要没有完成刷新就会一直循环，color1>color2>color3>color4
        // swipeRfl.setColorScheme(Color.BLUE, Color.GREEN, Color.RED,
        // Color.YELLOW);

        if (layoutType == GRID_LAYOUT_MANAGER) {
            mLayoutManager = new GridLayoutManager(context, spanCount);
            mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacingH, spacingV, includeEdge));
        } else {
            mLayoutManager = new LinearLayoutManager(context);
        }
        if (!useItemAnim) {
            mRecyclerView.setItemAnimator(new NoAlphaItemAnimator());
        }
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager.setOrientation(orientation);
        mRecyclerView.setLayoutManager(mLayoutManager);

        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mRecyclerView, params);


    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerViewBackground(int resId) {
        if (mRecyclerView != null) {
            mRecyclerView.setBackgroundResource(resId);
        }
    }

    private void initListener() {
        /**
         * 监听上拉至底部滚动监听
         */
        /*mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView mRecyclerView, int dx, int dy) {
                // dy>0 表示向下滑动
                super.onScrolled(mRecyclerView, dx, dy);
                //最后显示的项
					if (hasMore && (lastVisibleItem >= totalItemCount - 1)
						&& dy > 0 && !isLoadMore) {
					isLoadMore = true;
					loadMore();
				}
                //无论水平还是垂直
                if (hasMore && dy > 0 && (mLayoutManager.findLastVisibleItemPosition() >= mLayoutManager.getItemCount() - 1)
                        && !isLoadMore) {
                    isLoadMore = true;
                    loadMore();
                }

            }
        };*/

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
                        mTvFootLoadMoreTip.setText(R.string.pull_to_refresh_load_more_release);
                    } else {
                        mTvFootLoadMoreTip.setText(R.string.pull_to_refresh_load_more_up_move);
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

    /**
     * 该方法仅对Grid类型有效
     *
     * @param spanSizeLookup
     */
    public void setSpanSizeLookup(GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        if (null != mLayoutManager && mLayoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) mLayoutManager).setSpanSizeLookup(spanSizeLookup);
        }
    }

    /***
     * 是否正在拖动
     *
     * @return
     */
    public boolean isDragging() {
        return getIsBeginDragged();
    }

    public void setOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            mLayoutManager.setOrientation(HORIZONTAL);
        } else {
            mLayoutManager.setOrientation(VERTICAL);
        }
    }

    public int getOrientation() {
        return mLayoutManager.getOrientation();
    }

    public void setLoadMoreEnable(boolean enable) {
        this.hasMore = enable;
        this.loadMoreEnable = enable;
    }

    public boolean getLoadMoreEnable() {
        return hasMore;
    }

    public void setPullRefreshEnable(boolean enable) {
        refreshEnable = enable;
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

    public RecyclerView.Adapter getAdapter() {
        return mRecyclerView.getAdapter();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null)
            mRecyclerView.setAdapter(adapter);
    }

    private void startAnimation() {
        if (mFootAnimation != null && mIvFootLoad != null) {
            mIvFootLoad.startAnimation(mFootAnimation);
        }
        if (mTvFootLoadMoreTip != null) {
            mTvFootLoadMoreTip.setText(R.string.listView_loading);
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
