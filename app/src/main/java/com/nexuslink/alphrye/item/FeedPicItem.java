package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedPicModel;

import java.util.List;

public class FeedPicItem extends SimpleItem<FeedPicModel> {

    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        super.bindViewHolder(viewHolder, position, payloads);
        if (viewHolder instanceof ViewHolder) {
            RequestOptions options = new RequestOptions()
                    .error(R.drawable.map_logo)
                    .fallback(R.drawable.amap_bus)
                    .placeholder(R.drawable.amap_car);
            Glide.with(MyApplication.getContext()).load(mModel.pic_url).apply(options).into(((ViewHolder) viewHolder).mPic);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView mPic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mPic = itemView.findViewById(R.id.v_pic);
        }
    }
}
