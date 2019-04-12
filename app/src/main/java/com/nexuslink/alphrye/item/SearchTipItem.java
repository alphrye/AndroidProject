package com.nexuslink.alphrye.item;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.SearchTipModel;

import java.util.List;

public class SearchTipItem extends SimpleItem<SearchTipModel> {

    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        super.bindViewHolder(viewHolder, position, payloads);
        if (viewHolder instanceof  ViewHolder) {
            Tip tip = mModel.mTip;
            if (tip != null) {
                String tipName = tip.getName();
                if (!TextUtils.isEmpty(tipName)) {
                    ((ViewHolder) viewHolder).mResultText.setText(tipName);
                    ((ViewHolder) viewHolder).itemView.setOnClickListener(getItemClickListener());
                }
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mResultText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mResultText = itemView.findViewById(R.id.tv_result);
        }
    }
}
