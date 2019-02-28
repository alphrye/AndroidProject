package com.nexuslink.alphrye.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

/**
 *    author : alphrye
 *    time   : 2019/2/27
 *    desc   : 仪表盘
 */
public class DashboardView extends View {
    private static final String COLOR_PRIMARY = "#FFFFFF";
    private static final int STROKE_WIDTH_DEFAULT = 10;
    private static final int LEN_BETWEEN_CIRCLE_AND_INDICATOR = 35;

    /**绘制刻度的画笔*/
    private Paint mScalePaint;

    /**绘制LCD数字的画笔*/
    private Paint mLCDPaint;

    /**绘制与指示器相关的画笔*/
    private Paint mIndicatorPaint;

    private int perDegree;

    private int mCenterCircleRadius;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        perDegree = 180 / 36;
        mCenterCircleRadius = 35;
        initPaints();
    }

    /**
     * 初始化画笔
     */
    private void initPaints() {
        mScalePaint = new Paint();
        mScalePaint.setAntiAlias(true);
        mScalePaint.setStyle(Paint.Style.FILL);

        mLCDPaint = new Paint();
        mLCDPaint.setAntiAlias(true);
        mLCDPaint.setStyle(Paint.Style.FILL);
        mLCDPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "lcd_num.ttf");
        mLCDPaint.setTypeface(typeface);

        mIndicatorPaint = new Paint();
        mIndicatorPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int startX = 0;
        int startY = getHeight() / 2;

        drawScaleAndNum(canvas, startX, startY);

        drawCenterCircle(canvas, mCenterCircleRadius);

        drawSpeedIndicator(canvas, startX, startY, mCenterCircleRadius + LEN_BETWEEN_CIRCLE_AND_INDICATOR);

        drawSpeedText(canvas);
    }

    /**
     * 绘制刻度与数字
     * @param canvas
     * @param startX
     * @param startY
     */
    private void drawScaleAndNum(Canvas canvas, int startX, int startY) {
        canvas.save();
        int longLen = 45;
        int shortLen = 30;
        int longEndX = startX + longLen;
        int longEndY = getHeight() / 2;
        int shortEndX = startX + shortLen;
        int shortEndY = getHeight() / 2;
        int len = 30;
        canvas.rotate(-perDegree * 6, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 49; i++) {
            if (i % 6 == 0) {
                mScalePaint.setColor(Color.RED);
                mScalePaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
                canvas.drawLine(startX, startY, longEndX, longEndY, mScalePaint);
                canvas.save();
                String text = i + "";
                Rect rect =  new Rect();
                mLCDPaint.setTextSize(70);
                mLCDPaint.getTextBounds(text, 0, text.length(), rect);
                canvas.rotate(- perDegree * (i - 6), longEndX + len + rect.width() / 2,  longEndY);
                canvas.drawText(text, longEndX + len,  longEndY + rect.height() / 2, mLCDPaint);
                canvas.restore();
            } else {
                mScalePaint.setColor(Color.parseColor(COLOR_PRIMARY));
                mScalePaint.setStrokeWidth(5);
                canvas.drawLine(startX, startY, shortEndX, shortEndY, mScalePaint);
            }
            canvas.rotate(perDegree, getWidth() / 2, getHeight() / 2);
        }
        canvas.restore();
    }

    /**
     * 绘制中心圆圈
     * @param canvas
     * @param radius
     */
    private void drawCenterCircle(Canvas canvas, float radius) {
        canvas.save();

        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setShader(null);

        mIndicatorPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        mIndicatorPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mIndicatorPaint);

        mIndicatorPaint.setColor(Color.RED);
        mIndicatorPaint.setStrokeWidth(20);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius + 30, mIndicatorPaint);
        canvas.restore();
    }

    /**
     * 绘制指示器
     * @param canvas
     * @param startX
     * @param startY
     * @param radius
     */
    private void drawSpeedIndicator(Canvas canvas, int startX, int startY, float radius) {
        canvas.save();
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        mIndicatorPaint.setColor(Color.RED);
        LinearGradient mGradient = new LinearGradient(startX, startY, getWidth() / 2, getHeight() / 2, new int[] {Color.parseColor("#00000000"), Color.parseColor("#FF0000")}, null,Shader.TileMode.MIRROR);
        mIndicatorPaint.setShader(mGradient);
        canvas.rotate(- perDegree * 6, getWidth() / 2, getHeight() / 2);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(getWidth() / 2 - 30 - 35, getHeight() / 2 + 20);
        path.lineTo(getWidth() / 2 - 30 - 35, getHeight() / 2 - 20);
        path.close();
        canvas.drawPath(path, mIndicatorPaint);
        canvas.restore();
    }

    /**
     * 绘制速度
     * @param canvas
     */
    private void drawSpeedText(Canvas canvas) {
        canvas.save();
        String speedText = "0";
        Rect speedTvRect = new Rect();
        mLCDPaint.setTextSize(120);
        mLCDPaint.getTextBounds(speedText, 0, speedText.length(), speedTvRect);

        String tvKmPerHour = "km/h";
        Rect tvKmPerHourRect = new Rect();
        mLCDPaint.setTextSize(60);
        mLCDPaint.getTextBounds(tvKmPerHour, 0, tvKmPerHour.length(), tvKmPerHourRect);
        int len = 20;
        float startX = getWidth() / 2 - (speedTvRect.width() + tvKmPerHourRect.width() + len)  / 2;
        float startY = getHeight() / 2 +  getWidth() / 4;

        mLCDPaint.setTextSize(120);
        canvas.drawText(speedText, startX, startY, mLCDPaint);

        mLCDPaint.setTextSize(60);
        canvas.drawText(tvKmPerHour, startX + speedTvRect.width() + len, startY, mLCDPaint);
        canvas.restore();
    }
}
