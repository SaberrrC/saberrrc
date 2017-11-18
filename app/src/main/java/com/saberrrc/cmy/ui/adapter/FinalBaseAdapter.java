package com.saberrrc.cmy.ui.adapter;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public class FinalBaseAdapter<T> extends BaseAdapter {
    private int     itemLayout;
    private List<T> datas;

    public interface AdapterListener<T> {
        void bindView(FinalViewHolder viewHolder, T dataBean, int position);
    }

    private AdapterListener mAdapterListener;

    public FinalBaseAdapter(List<T> datas, int itemLayout, AdapterListener adapterListener) {
        this.datas = datas;
        this.itemLayout = itemLayout;
        this.mAdapterListener = adapterListener;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FinalViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(itemLayout, null, false);
            viewHolder = new FinalViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FinalViewHolder) convertView.getTag();
        }
        mAdapterListener.bindView(viewHolder, datas.get(position), position);
        return convertView;
    }

    public static class FinalViewHolder {
        private View mview;

        public FinalViewHolder(View convertView) {
            mview = convertView;
        }

        private SparseArray<View> viewList = new SparseArray<>();

        public View getViewById(int id) {
            View view = viewList.get(id);
            if (view == null) {
                view = mview.findViewById(id);
                viewList.put(id, view);
            }
            return view;
        }
    }
}