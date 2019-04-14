package com.nexuslink.alphrye.item;

import android.graphics.Typeface;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.TextView;

import com.nexuslink.alphrye.SimpleItem;
import com.nexuslink.alphrye.common.MyApplication;
import com.nexuslink.alphrye.cyctastic.R;
import com.nexuslink.alphrye.model.RunningTimeModel;
import com.nexuslink.alphrye.ui.fragment.NewCycleFragment;

import java.util.List;

public class RunningTimeItem extends SimpleItem<RunningTimeModel> {

    @Override
    protected void bindViewHolder(RecyclerView.ViewHolder viewHolder, int position, List<Object> payloads) {
        super.bindViewHolder(viewHolder, position, payloads);
        if (viewHolder instanceof  ViewHolder) {
            if (!payloads.isEmpty()) {

                int result = (int) payloads.get(0);
                switch (result) {
                    case NewCycleFragment.FLAG_TIME_START:
                        //开始计时
                        Log.d("Test", "bindViewHolder: ");
//                        ((ViewHolder) viewHolder).mTvTitle.setText("TEST");
                        break;
                    case NewCycleFragment.FLAG_TIME_PAUSE:
                        ((ViewHolder) viewHolder).mChronometer.stop();
                        break;
                    case NewCycleFragment.FLAG_TIME_CONTINUE:
                        ((ViewHolder) viewHolder).mChronometer.start();
                        break;
                }
                return;
            }

            String title = mModel.mTitle;
            if (TextUtils.isEmpty(title)) {
                title = "Null";
            }
            ((ViewHolder) viewHolder).mTvTitle.setText(title);
            Typeface typeface = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "lcd_num.ttf");
            ((ViewHolder) viewHolder).mChronometer.setTypeface(typeface);
            ((ViewHolder) viewHolder).mChronometer.setBase(SystemClock.elapsedRealtime());
            ((ViewHolder) viewHolder).mChronometer.start();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvTitle;
        public Chronometer mChronometer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.v_title);
            mChronometer = itemView.findViewById(R.id.v_chronometer);
        }
    }
}
