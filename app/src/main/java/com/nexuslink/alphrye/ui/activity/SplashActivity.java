package com.nexuslink.alphrye.ui.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.widget.TextView;

import com.nexuslink.alphrye.common.MyActivity;
import com.nexuslink.alphrye.cyctastic.R;

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
            }
        }, 800);
    }

    @Override
    protected void initData() {

    }
}
