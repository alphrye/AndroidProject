package com.nexuslink.alphrye.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
/**
 *    author : alphrye
 *    time   : 2019/2/27
 *    desc   : 仪表盘
 */
public class DashboardView extends View {

    private Paint mPaint;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        int longLen = 45;
        int shortLen = 30;
        int startX = 0;
        int startY = getHeight() / 2;
        int longEndX = startX + longLen;
        int longEndY = getHeight() / 2;
        int shortEndX = startX + shortLen;
        int shortEndY = getHeight() / 2;
        int perDegree = 180 / 36;
        int len = 30;
        canvas.rotate(-perDegree * 6, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 49; i++) {
            if (i % 6 == 0) {
                mPaint.setColor(Color.RED);
                canvas.drawLine(startX, startY, longEndX, longEndY, mPaint);
                canvas.save();
                String text = i + "";
                Rect rect =  new Rect();
                mPaint.setTextSize(50);
                mPaint.getTextBounds(text, 0, text.length(), rect);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.BLUE);
                canvas.rotate(- perDegree * (i - 6), longEndX + len + rect.width() / 2,  longEndY);
                canvas.drawText(text, longEndX + len,  longEndY + rect.height() / 2, mPaint);
                canvas.restore();
            } else {
                mPaint.setColor(Color.BLUE);
                mPaint.setStrokeWidth(5);
                canvas.drawLine(startX, startY, shortEndX, shortEndY, mPaint);
            }
            canvas.rotate(perDegree, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();

        canvas.save();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(150);
        float radius = (float) (getWidth() * 1.0 / 4);
        // TODO: 2019/2/27 适配Android 5.0以下
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(getWidth() / 2 - radius, getHeight() / 2 - radius, getWidth() / 2 + radius, getHeight() / 2 + radius, 30, -240, false, mPaint);
        }
        canvas.restore();

        canvas.save();
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.RED);
        canvas.rotate(-perDegree * 6, getWidth() / 2, getHeight() / 2);
        canvas.drawLine(startX, startY, getWidth() / 2, getHeight() / 2, mPaint);
        canvas.restore();

        canvas.save();
        String speedText = "0";
        Rect speedRect = new Rect();
        mPaint.setTextSize(120);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(speedText, 0, speedText.length(), speedRect);
        canvas.drawText(speedText, getWidth() / 2 - speedRect.width() / 2, getHeight() / 2 +  getWidth() / 4, mPaint);
        canvas.restore();

        canvas.save();
        String tvKmPerHour = "km/h";
        Rect tvKmPerHourRect = new Rect();
        mPaint.setTextSize(80);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(tvKmPerHour, 0, tvKmPerHour.length(), tvKmPerHourRect);
        canvas.drawText(tvKmPerHour, getWidth() / 2 - tvKmPerHourRect.width() / 2, getHeight() / 2 +  getWidth() / 4 + speedRect.height() / 2 + tvKmPerHourRect.height() / 2 + 20, mPaint);
        canvas.restore();
    }
}
