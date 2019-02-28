package com.nexuslink.alphrye.ui.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Shader;
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

    private Paint mPaint;

    private Rect mSpeedTvRect;

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
        initPaint();
    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mSpeedTvRect = new Rect();
        perDegree = 180 / 36;
        mCenterCircleRadius = 35;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        mPaint.setShader(null);
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

        drawTextUnit(canvas);
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
        mPaint.setShader(null);
        canvas.rotate(-perDegree * 6, getWidth() / 2, getHeight() / 2);
        for (int i = 0; i < 49; i++) {
            if (i % 6 == 0) {
                mPaint.setColor(Color.RED);
                mPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
                canvas.drawLine(startX, startY, longEndX, longEndY, mPaint);
                canvas.save();
                String text = i + "";
                Rect rect =  new Rect();
                mPaint.setTextSize(50);
                mPaint.getTextBounds(text, 0, text.length(), rect);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(Color.parseColor(COLOR_PRIMARY));
                mPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
                canvas.rotate(- perDegree * (i - 6), longEndX + len + rect.width() / 2,  longEndY);
                canvas.drawText(text, longEndX + len,  longEndY + rect.height() / 2, mPaint);
                canvas.restore();
            } else {
                mPaint.setColor(Color.parseColor(COLOR_PRIMARY));
                mPaint.setStrokeWidth(5);
                canvas.drawLine(startX, startY, shortEndX, shortEndY, mPaint);
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
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        mPaint.setShader(null);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius, mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(20);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, radius + 30, mPaint);
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
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);
        LinearGradient mGradient = new LinearGradient(startX, startY, getWidth() / 2, getHeight() / 2, new int[] {Color.parseColor("#00000000"), Color.parseColor("#FF0000")}, null,Shader.TileMode.MIRROR);
        mPaint.setShader(mGradient);
        canvas.rotate(- perDegree * 6, getWidth() / 2, getHeight() / 2);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(getWidth() / 2 - 30 - 35, getHeight() / 2 + 20);
        path.lineTo(getWidth() / 2 - 30 - 35, getHeight() / 2 - 20);
        path.close();
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }

    /**
     * 绘制速度
     * @param canvas
     */
    private void drawSpeedText(Canvas canvas) {
        canvas.save();
        String speedText = "0";
        mSpeedTvRect = new Rect();
        mPaint.setTextSize(120);
        mPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShader(null);
        mPaint.getTextBounds(speedText, 0, speedText.length(), mSpeedTvRect);
        canvas.drawText(speedText, getWidth() / 2 - mSpeedTvRect.width() / 2, getHeight() / 2 +  getWidth() / 4, mPaint);
        canvas.restore();
    }

    /**
     * 绘制单位
     * @param canvas
     */
    private void drawTextUnit(Canvas canvas) {
        canvas.save();
        String tvKmPerHour = "km/h";
        Rect tvKmPerHourRect = new Rect();
        mPaint.setTextSize(80);
        mPaint.setShader(null);
        mPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.getTextBounds(tvKmPerHour, 0, tvKmPerHour.length(), tvKmPerHourRect);
        canvas.drawText(tvKmPerHour, getWidth() / 2 - tvKmPerHourRect.width() / 2, getHeight() / 2 +  getWidth() / 4 + mSpeedTvRect.height() / 2 + tvKmPerHourRect.height() / 2 + 20, mPaint);
        canvas.restore();
    }
}
