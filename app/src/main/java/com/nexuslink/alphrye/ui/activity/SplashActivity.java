package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;

import java.util.Calendar;

import butterknife.BindView;

/**
 * 闪屏页面
 * @author yuanrui
 * @date 2019/3/18
 */

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
                Intent intent = new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 1000);
    }

    @Override
    protected void initData() {
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DATE);
        String monthText = String.valueOf(month);
        String dayText = String.valueOf(day);
        mTvMonth.setText(month < 10 ? "0" + monthText : monthText);
        mTvDay.setText(day < 10 ? "0" + dayText : dayText);
    }
}
