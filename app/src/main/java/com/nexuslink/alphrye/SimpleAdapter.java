package com.nexuslink.alphrye;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.item.FeedItem;
import com.nexuslink.alphrye.item.RideHistoryItem;
import com.nexuslink.alphrye.item.RunningDataItem;
import com.nexuslink.alphrye.item.RunningTimeItem;
import com.nexuslink.alphrye.item.SearchTipItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<SimpleModel> mModels;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;
    private boolean mCanDrag;
    private ItemTouchHelper mItemTouchHelper;

    private Map<Integer, SimpleItem> mMap;

    private SimpleAdapter(Context mContext, RecyclerView recyclerView, List<SimpleModel> models,
                          RecyclerView.LayoutManager layoutManager,
                          OnItemClickListener itemClickListener,
                          boolean drag) {
        this.mContext = mContext;
        if (models == null) {
            models = new ArrayList<>();
        }
        this.mModels = models;
        this.mRecyclerView = recyclerView;
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(mContext);
        }
        this.mLayoutManager = layoutManager;
        this.mOnItemClickListener = itemClickListener;
        this.mCanDrag = drag;

        mMap = new HashMap<>();

        if (mRecyclerView == null) {
            return;
        }
        mRecyclerView.setAdapter(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (mCanDrag) {
            ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
                @Override
                public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                   RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
                   if (manager instanceof GridLayoutManager
                           || manager instanceof StaggeredGridLayoutManager) {
                       int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                               | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                       // TODO: 2019/4/2 了解这里的swipflags
                       return makeMovementFlags(dragFlags, 0);

                   } else if (manager instanceof LinearLayoutManager) {
                       int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
                       return makeMovementFlags(dragFlags, 0);
                   }
                    return 0;
                }

                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    int originPos = viewHolder.getAdapterPosition();
                    int targetPos = target.getAdapterPosition();
                    SimpleModel targetModel=  mModels.get(targetPos);
                    if (targetPos < originPos) {
                        for (int i = originPos; i > targetPos ; i--) {
                            if (targetModel != null && !targetModel.mImmobile) {
                                Collections.swap(mModels, i , i -1);
                            } else {
                                return false;
                            }
                        }
                    } else {
                        for (int i = originPos; i < targetPos; i++) {
                            if (targetModel != null && !targetModel.mImmobile) {
                                Collections.swap(mModels, i , i + 1);
                            } else {
                                return false;
                            }
                        }
                    }
                    notifyItemMoved(originPos, targetPos);
                    return true;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                }
            };
            mItemTouchHelper = new ItemTouchHelper(callback);
            mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mModels == null
                || mModels.isEmpty()
                || position >= mModels.size()) {
            return -1;
        }
        SimpleModel curModel = mModels.get(position);
        if (curModel == null) {
            return -1;
        }
        return curModel.mCardType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //ViewType =》Model类型 =》new 对应ViewHolder(layoutId SimpleItem)
        // TODO: 2019/3/31 使用Map优化, 这里与业务逻辑没有解耦
        if (viewType == CardConstant.TYPE_FEED) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_feed, null);
            return new FeedItem.ViewHolder(itemView);
        } else if (viewType == CardConstant.TYPE_RIDE_HISTORY) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_ride_history, null);
            return new RideHistoryItem.ViewHolder(itemView);
        } else if (viewType == CardConstant.TYPE_SEARCH_TIPS) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_search_tips, null);
            return new SearchTipItem.ViewHolder(itemView);
        } else if (viewType == CardConstant.TYPE_RUNNING_DATA) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_running_data, null);
            return new RunningDataItem.ViewHolder(itemView);
        } else if (viewType == CardConstant.TYPE_RUNNING_TIME) {
            View itemView = LayoutInflater.from(mContext).inflate(R.layout.layout_item_running_time, null);
            return new RunningTimeItem.ViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (!payloads.isEmpty()) {
            Log.d("Test", "onBindViewHolder: " + payloads.get(0));
        }
        if (mModels == null
                || mModels.isEmpty()
                || position >= mModels.size()) {
            return;
        }
        final SimpleModel curModel = mModels.get(position);
        if (curModel == null) {
            return;
        }

        SimpleItem simpleItem = curModel.createItem();
        if (simpleItem == null) {
            return;
        }
        simpleItem.mModel = curModel;
        simpleItem.setItemClickListener(mOnItemClickListener);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (!curModel.mImmobile) {
                    if (mItemTouchHelper == null) {
                        return false;
                    }
                    mItemTouchHelper.startDrag(holder);
                    return true;
                }
                return false;
            }
        });
        simpleItem.bindViewHolder(holder, position, payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public void appendData(List<SimpleModel> modelList) {
        if (modelList == null
                || modelList.isEmpty()) {
            return;
        }
        if (mModels == null){
            mModels = new ArrayList<>();
        }
        mModels.addAll(modelList);
        notifyDataSetChanged();
    }

    public void setData(List<SimpleModel> modelList) {
        if (modelList == null
                || modelList.isEmpty()) {
            return;
        }
        if (mModels == null){
            mModels = new ArrayList<>();
        }
        mModels = modelList;
        notifyDataSetChanged();
    }


    public static class Builder {
        private Context mContext;
        private RecyclerView mRecyclerView;
        private RecyclerView.LayoutManager mLayoutManager;
        private List<SimpleModel> mModels;
        private OnItemClickListener mOnItemClickListener;
        private boolean mCanDrag;

        public Builder(Context context) {
            this.mContext = context;
        }

        public Builder recyclerView (RecyclerView recyclerView) {
            this.mRecyclerView = recyclerView;
            return this;
        }

        public Builder data (List<SimpleModel> models) {
            if (models == null) {
                models = new ArrayList<>();
            }
            this.mModels = models;
            return this;
        }

        public Builder layoutManager(RecyclerView.LayoutManager layoutManager) {
            if (layoutManager == null) {
                mLayoutManager = new LinearLayoutManager(mContext);
            } else {
                this.mLayoutManager = layoutManager;
            }
            return this;
        }

        public Builder itemClickListener (OnItemClickListener itemClickListener) {
            this.mOnItemClickListener = itemClickListener;
            return this;
        }

        public Builder drag(boolean b) {
            this.mCanDrag = b;
            return this;
        }

        public SimpleAdapter build () {
            return new SimpleAdapter(mContext, mRecyclerView, mModels, mLayoutManager, mOnItemClickListener, mCanDrag);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View v, SimpleModel model, int position);
    }
}
