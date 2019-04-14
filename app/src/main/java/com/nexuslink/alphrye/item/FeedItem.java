package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nexuslink.alphrye.EquidistantDecoration;
import com.nexuslink.alphrye.SimpleAdapter;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.SimpleModel;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;
import com.nexuslink.alphrye.model.FeedPicModel;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedItem extends SimpleItem<FeedModel> {


    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        if (viewHolder instanceof ViewHolder) {
            FeedModel.FeedUserBean user = mModel.user;
            if (user != null) {
                String userName = user.user_name;
                if (TextUtils.isEmpty(userName)) {
                    userName = "Null";
                }
                ((ViewHolder) viewHolder).mTvName.setText(userName);

                String avatarUrl = user.user_avatar;

                RequestOptions options = new RequestOptions()
                        .error(R.drawable.map_logo)
                        .fallback(R.drawable.amap_bus);
                Glide.with(MyApplication.getContext()).load(avatarUrl).apply(options).into(((ViewHolder) viewHolder).mIvAvatar);
            }

            FeedModel.FeedContentBean content = mModel.content;
            if (content != null) {
                String contentText = content.text;
                if (TextUtils.isEmpty(contentText)) {
                    contentText = "Null";
                }
                ((ViewHolder) viewHolder).mTvContent.setText(contentText);
                RecyclerView recyclerView = ((ViewHolder) viewHolder).mPicRecycler;
                Log.d("Pic", "bindViewHolder: " + content.pic_urls);
                if (content.hasPic()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    List<FeedPicModel> feedPicModels = createFeedPicModels(content.pic_urls);
                    int picCount = content.getPicCount();
                    int spanCount = picCount > 3 ? 3 : picCount;
                    new SimpleAdapter.Builder(viewHolder.itemView.getContext())
                            .recyclerView(((ViewHolder) viewHolder).mPicRecycler)
                            .layoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL))
                            .data(new ArrayList<SimpleModel>(feedPicModels))
                            .itemDecoration(new EquidistantDecoration(3, 15))
                            .build();
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            //时间暂时由服务端下发
            String updateTime = mModel.update_time;
            if (TextUtils.isEmpty(updateTime)) {
                ((ViewHolder) viewHolder).mTvTime.setVisibility(View.GONE);
            } else {
                ((ViewHolder) viewHolder).mTvTime.setVisibility(View.VISIBLE);
                ((ViewHolder) viewHolder).mTvTime.setText(updateTime);
            }
        }
    }

    private List<FeedPicModel> createFeedPicModels(List<String> picUrls) {
        if (picUrls == null || picUrls.isEmpty()) {
            return new ArrayList<>();
        }
        List<FeedPicModel> feedPicModels = new ArrayList<>();
        for (String picUrl : picUrls) {
            FeedPicModel model = new FeedPicModel(picUrl);
            feedPicModels.add(model);
        }
        return feedPicModels;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;
        public TextView mTvTime;
        public TextView mTvContent;
        public CircleImageView mIvAvatar;
        public RecyclerView mPicRecycler;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
            mPicRecycler = itemView.findViewById(R.id.v_recycler);
        }
    }
}
