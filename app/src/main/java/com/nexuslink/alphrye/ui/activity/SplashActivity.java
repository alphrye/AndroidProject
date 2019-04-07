package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;

import java.util.Calendar;
import java.util.TimeZone;

import butterknife.BindView;

public class SplashActivity extends MyActivity {
    @BindView(R.id.tv_month)
    TextView mTvMonth;
    @BindView(R.id.tv_day)
    TextView mTvDay;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected int getTitleBarId() {
        return 0;
    }

    @Override
    protected void initView() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "lcd_num.ttf");
        mTvMonth.setTypeface(typeface);
        mTvDay.setTypeface(typeface);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 800);
    }

    @Override
    protected void initData() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8:00"));
        int monthText = calendar.get(Calendar.MONTH);
        int dayText = calendar.get(Calendar.DATE);
        mTvMonth.setText(String.valueOf(monthText));
        mTvDay.setText(String.valueOf(dayText));
    }
}
