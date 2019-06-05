package com.nexuslink.alphrye.item;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.RunningDataModel;
import com.nexuslink.alphrye.ui.fragment.NewCycleFragment;

import java.util.List;

public class RunningDataItem extends SimpleItem<RunningDataModel> {

    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        super.bindViewHolder(viewHolder, position, payloads);
        if (viewHolder instanceof  ViewHolder) {
            if (!payloads.isEmpty()) {
                int payload = (int) payloads.get(0);
//                if (payload == NewCycleFragment.POSITION_ALTITUDE) {
//                    String data = mModel.mData;
//                    if (TextUtils.isEmpty(data)) {
//                        data = "Null";
//                    }
//                    ((ViewHolder) viewHolder).mTvData.setText(data);
//                }
                return;
            }
            String title = mModel.mTitle;
            if (TextUtils.isEmpty(title)) {
                title = "Null";
            }
            ((ViewHolder) viewHolder).mTvTitle.setText(title);
            String data = mModel.mData;
            if (TextUtils.isEmpty(data)) {
                data = "Null";
            }
            Typeface typeface = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "lcd_num.ttf");
            ((ViewHolder) viewHolder).mTvData.setTypeface(typeface);
            ((ViewHolder) viewHolder).mTvData.setText(data);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvData;
        public TextView mTvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvData = itemView.findViewById(R.id.v_data);
            mTvTitle = itemView.findViewById(R.id.v_title);
        }
    }
}
