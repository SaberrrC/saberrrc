package com.saberrrc.cmy.ui.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.saberrrc.cmy.App;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class LoadMutiItemRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int ITEM_TYPE_LOAD_FAILED_VIEW = Integer.MAX_VALUE - 1;
    public static final int ITEM_TYPE_NO_MORE_VIEW     = Integer.MAX_VALUE - 2;
    public static final int ITEM_TYPE_LOAD_MORE_VIEW   = Integer.MAX_VALUE - 3;
    public static final int ITEM_TYPE_NO_VIEW          = Integer.MAX_VALUE - 4;//不展示footer view

    private static final int dp_20 = dip2px(20);
    private static final int dp_30 = dip2px(30);
    private static final int dp_35 = dip2px(35);
    private static final int dp_40 = dip2px(40);
    private static final int dp_45 = dip2px(45);
    private static final int dp_50 = dip2px(50);

    private RecyclerView.Adapter mInnerAdapter;

    private View mLoadMoreView;
    private View mLoadMoreFailedView;
    private View mNoMoreView;

    private int mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
    private LoadMoreScrollListener mLoadMoreScrollListener;


    private boolean isLoadError      = false;//标记是否加载出错
    private boolean isHaveStatesView = true;

    public LoadMutiItemRecyclerView(FinalRecycleAdapter adapter) {
        this.mInnerAdapter = adapter;
        mLoadMoreScrollListener = new LoadMoreScrollListener() {
            @Override
            public void loadMore() {
                if (mOnLoadListener != null && isHaveStatesView) {
                    if (!isLoadError) {
                        showLoadMore();
                        mOnLoadListener.onLoadMore();
                    }
                }
            }
        };
    }

    public void showLoadMore() {
        mCurrentItemType = ITEM_TYPE_LOAD_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        notifyItemChanged(getItemCount());
    }

    public void showLoadError() {
        mCurrentItemType = ITEM_TYPE_LOAD_FAILED_VIEW;
        isLoadError = true;
        isHaveStatesView = true;
        notifyItemChanged(getItemCount());
    }

    public void showLoadComplete() {
        mCurrentItemType = ITEM_TYPE_NO_MORE_VIEW;
        isLoadError = false;
        isHaveStatesView = true;
        notifyItemChanged(getItemCount());
    }

    public void disableLoadMore() {
        mCurrentItemType = ITEM_TYPE_NO_VIEW;
        isHaveStatesView = false;
        notifyDataSetChanged();
    }

    public void setLoadMoreViewHolder(View v) {
        mLoadMoreView = v;
    }

    public void setLoadFailedViewHolder(View v) {
        mLoadMoreFailedView = v;
    }

    public void setNoMoreViewHolder(View v) {
        mNoMoreView = v;
    }

    //region Get ViewHolder
    private ViewHolder getLoadMoreViewHolder() {
        if (mLoadMoreView == null) {
    /*        mLoadMoreView = LayoutInflater.from(App.getInstance()).inflate(R.layout.activity_test1, null, false);
            ((RelativeLayout) mLoadMoreView).setGravity(Gravity.CENTER);*/
            mLoadMoreView = new TextView(App.getInstance());
            mLoadMoreView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            mLoadMoreView.setPadding(20, 20, 20, 20);
            ((TextView) mLoadMoreView).setText("正在加载中...");
            ((TextView) mLoadMoreView).setGravity(Gravity.CENTER);
        }
        return LoadMutiItemRecyclerView.ViewHolder.createViewHolder(mLoadMoreView);
    }

    private ViewHolder getLoadFailedViewHolder() {
        if (mLoadMoreFailedView == null) {
            mLoadMoreFailedView = new TextView(App.getInstance());
            mLoadMoreFailedView.setPadding(dp_20, dp_20, dp_20, dp_20);
            mLoadMoreFailedView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            ((TextView) mLoadMoreFailedView).setText("加载失败，点击重试");
            ((TextView) mLoadMoreFailedView).setGravity(Gravity.CENTER);
        }
        return ViewHolder.createViewHolder(mLoadMoreFailedView);
    }

    private ViewHolder getNoMoreViewHolder() {
        if (mNoMoreView == null) {
            mNoMoreView = new TextView(App.getInstance());
            mNoMoreView.setPadding(dp_20, dp_20, dp_20, dp_20);
            mNoMoreView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
            ((TextView) mNoMoreView).setText("没有更多数据");
            ((TextView) mNoMoreView).setGravity(Gravity.CENTER);
        }
        return ViewHolder.createViewHolder(mNoMoreView);
    }
    //endregion

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1 && isHaveStatesView) {
            return mCurrentItemType;
        }
        return mInnerAdapter.getItemViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NO_MORE_VIEW) {
            return getNoMoreViewHolder();
        } else if (viewType == ITEM_TYPE_LOAD_MORE_VIEW) {
            return getLoadMoreViewHolder();
        } else if (viewType == ITEM_TYPE_LOAD_FAILED_VIEW) {
            return getLoadFailedViewHolder();
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == ITEM_TYPE_LOAD_FAILED_VIEW) {
            mLoadMoreFailedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnLoadListener != null) {
                        mOnLoadListener.onRetry();
                        showLoadMore();
                    }
                }
            });
            return;
        }
        if (!isFooterType(holder.getItemViewType()))
            mInnerAdapter.onBindViewHolder(holder, position);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            @Override
            public int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position) {
                if (position == getItemCount() - 1 && isHaveStatesView) {
                    return layoutManager.getSpanCount();
                }
                if (oldLookup != null && isHaveStatesView) {
                    return oldLookup.getSpanSize(position);
                }
                return 1;
            }
        });
        recyclerView.addOnScrollListener(mLoadMoreScrollListener);
    }


    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewAttachedToWindow(holder);

        if (holder.getLayoutPosition() == getItemCount() - 1 && isHaveStatesView) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mInnerAdapter.getItemCount() + (isHaveStatesView ? 1 : 0);
    }

    public boolean isFooterType(int type) {

        return type == ITEM_TYPE_NO_VIEW ||
                type == ITEM_TYPE_LOAD_FAILED_VIEW ||
                type == ITEM_TYPE_NO_MORE_VIEW ||
                type == ITEM_TYPE_LOAD_MORE_VIEW;
    }
    //region 加载监听

    public interface OnLoadListener {
        void onRetry();

        void onLoadMore();
    }

    private OnLoadListener mOnLoadListener;

    public LoadMutiItemRecyclerView setOnLoadListener(OnLoadListener onLoadListener) {
        mOnLoadListener = onLoadListener;
        return this;
    }

    public abstract class LoadMoreScrollListener extends RecyclerView.OnScrollListener {


        private int previousTotal;
        private boolean isLoading = true;
        private LinearLayoutManager        lm;
        private StaggeredGridLayoutManager sm;
        private int[]                      lastPositions;
        private int                        totalItemCount;
        private int                        lastVisibleItemPosition;

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (recyclerView.getLayoutManager() instanceof LinearLayoutManager)
                lm = (LinearLayoutManager) recyclerView.getLayoutManager();
            else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                sm = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                lastPositions = sm.findLastVisibleItemPositions(null);
            }
            int visibleItemCount = recyclerView.getChildCount();
            if (lm != null) {
                totalItemCount = lm.getItemCount();
                lastVisibleItemPosition = lm.findLastVisibleItemPosition();
            } else if (sm != null) {
                totalItemCount = sm.getItemCount();
                lastVisibleItemPosition = lastPositions[0];
            }

            if (isLoading) {
                if (totalItemCount > previousTotal) {//加载更多结束
                    isLoading = false;
                    previousTotal = totalItemCount;
                } else if (totalItemCount < previousTotal) {//用户刷新结束
                    previousTotal = totalItemCount;
                    isLoading = false;
                } else {//有可能是在第一页刷新也可能是加载完毕
                    // TODO: 2017-04-08
                }
            }
            if (!isLoading && visibleItemCount > 0 && totalItemCount - 1 == lastVisibleItemPosition && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_IDLE) {
                loadMore();
            }
        }

        public abstract void loadMore();
    }

    public static int dip2px(float dpValue) {
        final float scale = App.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static class FinalRecycleAdapter extends RecyclerView.Adapter<ViewHolder> {
        private List<? extends Object>                   mDatas;
        private FinalRecycleAdapter.OnViewAttachListener mOnViewAttachListener;
        private              Map<Class, Integer> mClassIntegerHashMap = new HashMap<>();
        private static final int                 LOADMORE             = 0;
        private              boolean             needLoadMore         = false;
        private              int                 loadLayout           = -1;
        private List<String> strings;

        /**
         * 获取map
         *
         * @return
         */
        public Map<Class, Integer> getClassIntegerHashMap() {
            return mClassIntegerHashMap;
        }

        /**
         * 是否需要加载更多 默认不需要
         *
         * @param needLoadMore 是否需要上拉加载更多
         * @param loadLayout   上拉加载更多的布局，不要虚上拉加载，传入0
         */
        public void setNeedLoadMore(boolean needLoadMore, int loadLayout) {
            this.needLoadMore = needLoadMore;
            this.loadLayout = loadLayout;
        }

        /**
         * @param datas               数据
         * @param classIntegerHashMap Class键 数据类型 对应 条目类型，Integer值对应条目布局id
         */
        public FinalRecycleAdapter(List<? extends Object> datas, Map<Class, Integer> classIntegerHashMap, OnViewAttachListener onViewAttachListener) {
            mClassIntegerHashMap = classIntegerHashMap;
            mDatas = datas;
            mOnViewAttachListener = onViewAttachListener;
        }

        public static Map<Class, Integer> getMap() {
            return new HashMap<>();
        }


        @Override
        public int getItemViewType(int position) {
            if (needLoadMore && position == getItemCount() - 1) {
                return LOADMORE;
            }
            Class key = mDatas.get(position).getClass();
            if (mClassIntegerHashMap.containsKey(key)) {
                return mClassIntegerHashMap.get(key);
            } else {
                throw new RuntimeException("未添加进Map！");
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == LOADMORE) {
                if (viewType == -1) {
                    throw new RuntimeException("请传入加载更多的布局");
                }
                View view = LayoutInflater.from(parent.getContext()).inflate(loadLayout, parent, false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
            } else {
                View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
            }
        }

        public void addItemType(Class clzz, int layoutID) {
            mClassIntegerHashMap.put(clzz, layoutID);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (mDatas.size() == 0) {
                throw new RuntimeException("获取完数据请 notifyDataSetChanged()");
            }
            if (needLoadMore) {
                mOnViewAttachListener.onBindViewHolder(holder, position, new Object());
            } else {
                mOnViewAttachListener.onBindViewHolder(holder, position, mDatas.get(position));
            }
        }

        @Override
        public int getItemCount() {
            if (needLoadMore) {
                return mDatas.size() + 1;
            }
            return mDatas.size();
        }

        public void notifyDataSetChangedNew(List<Object> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        public void setStringList(List<String> strings) {
            this.strings = strings;

        }

        public interface OnViewAttachListener {
            void onBindViewHolder(ViewHolder holder, int position, Object itemData);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mShowItems = new SparseArray<>();
        private View rootView;

        ViewHolder(View view) {
            super(view);
            rootView = view;
        }

        public View getRootView() {
            return rootView;
        }

        public static LoadMutiItemRecyclerView.ViewHolder createViewHolder(View itemView) {
            LoadMutiItemRecyclerView.ViewHolder holder = new LoadMutiItemRecyclerView.ViewHolder(itemView);
            return holder;
        }

        public View getViewById(int id) {
            View view = mShowItems.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mShowItems.put(id, view);
            }
            return view;
        }
    }

    public static class WrapperUtils {
        public interface SpanSizeCallback {
            int getSpanSize(GridLayoutManager layoutManager, GridLayoutManager.SpanSizeLookup oldLookup, int position);
        }

        public static void onAttachedToRecyclerView(RecyclerView.Adapter innerAdapter, RecyclerView recyclerView, final SpanSizeCallback callback) {
            innerAdapter.onAttachedToRecyclerView(recyclerView);

            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof GridLayoutManager) {
                final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
                final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

                gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return callback.getSpanSize(gridLayoutManager, spanSizeLookup, position);
                    }
                });
                gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
            }
        }

        public static void setFullSpan(RecyclerView.ViewHolder holder) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();

            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {

                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;

                p.setFullSpan(true);
            }
        }
    }
}