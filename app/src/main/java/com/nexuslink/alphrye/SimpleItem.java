package com.nexuslink.alphrye;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 *
 * @author Yuanrui
 * @param <T> 对应Model类型
 */
public abstract class SimpleItem<T extends SimpleModel> implements View.OnClickListener{
    public T mModel;
    protected int mPos;

    protected SimpleAdapter.OnItemClickListener mOnItemClickListener;

    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        this.mPos = position;
    }

    public void setItemClickListener(SimpleAdapter.OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    protected View.OnClickListener getItemClickListener() {
        return this;
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener == null) {
            return;
        }
        mOnItemClickListener.onItemClick(v, mModel, mPos);
    }
}
