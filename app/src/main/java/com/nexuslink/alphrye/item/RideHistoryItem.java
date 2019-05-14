package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.amap.api.track.query.entity.Track;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.RideHistoryModel;

import java.util.List;


public class RideHistoryItem extends SimpleItem<RideHistoryModel> {

    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        super.bindViewHolder(viewHolder, position, payloads);
        if (viewHolder instanceof ViewHolder) {
           Track track = mModel.mTrack;
           if (track == null) {
               return;
           }
           long trackId = track.getTrid();
           double distance = track.getDistance();
           long time = track.getTime();

           ((ViewHolder) viewHolder).mTvDistance.setText("总里程：" + distance + "m");
           ((ViewHolder) viewHolder).mTvTrackId.setText("轨迹编号：" + trackId);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTvDistance;

        public TextView mTvTrackId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvDistance = itemView.findViewById(R.id.distance);
            mTvTrackId = itemView.findViewById(R.id.trackId);
        }
    }
}
