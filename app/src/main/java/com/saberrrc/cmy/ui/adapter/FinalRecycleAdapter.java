package com.saberrrc.cmy.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalRecycleAdapter extends RecyclerView.Adapter<FinalRecycleAdapter.ViewHolder> {
    private List<? extends Object> mDatas;
    private OnViewAttachListener   mOnViewAttachListener;
    private             Map<Class, Integer> mClassIntegerHashMap = new HashMap<>();
    public static final int                 REFRESH              = 0;
    public static final int                 LOAD                 = 1;

    /**
     * 获取map
     *
     * @return
     */
    public Map<Class, Integer> getClassIntegerHashMap() {
        return mClassIntegerHashMap;
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
        Class key = mDatas.get(position).getClass();
        if (mClassIntegerHashMap.containsKey(key)) {
            return mClassIntegerHashMap.get(key);
        } else {
            throw new RuntimeException("未添加进Map！");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (mDatas.size() == 0) {
            throw new RuntimeException("获取完数据请 notifyDataSetChanged()");
        }
        mOnViewAttachListener.onBindViewHolder(holder, position, mDatas.get(position));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public interface OnViewAttachListener {
        void onBindViewHolder(ViewHolder holder, int position, Object itemData);
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

        public View getViewById(int id) {
            View view = mShowItems.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mShowItems.put(id, view);
            }
            return view;
        }
    }
}