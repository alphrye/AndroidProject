package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.model.RideHistoryModel;


public class RideHistoryItem extends SimpleItem<RideHistoryModel> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
