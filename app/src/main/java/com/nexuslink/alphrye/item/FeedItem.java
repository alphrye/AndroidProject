package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.FeedModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedItem extends SimpleItem<FeedModel> {


    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolder) {
            FeedModel.FeedUserBean user = mModel.user;
            if (user != null) {
                String userName = user.user_name;
                if (TextUtils.isEmpty(userName)) {
                    userName = "Null";
                }
                ((ViewHolder) viewHolder).mTvName.setText(userName);

                String avatarUrl = user.user_avatar;
                // TODO: 2019/4/7
            }

            FeedModel.FeedContentBean content = mModel.content;
            if (content != null) {
                String contentText = content.text;
                if (TextUtils.isEmpty(contentText)) {
                    contentText = "Null";
                }
                ((ViewHolder) viewHolder).mTvContent.setText(contentText);
            }

            // TODO: 2019/4/7 时间
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvName;
        public TextView mTvTime;
        public TextView mTvContent;
        public CircleImageView mIvAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvName = itemView.findViewById(R.id.tv_name);
            mTvTime = itemView.findViewById(R.id.tv_time);
            mTvContent = itemView.findViewById(R.id.tv_content);
            mIvAvatar = itemView.findViewById(R.id.iv_avatar);
        }
    }
}
