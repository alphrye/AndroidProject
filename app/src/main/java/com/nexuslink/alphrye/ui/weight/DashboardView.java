package com.nexuslink.alphrye.ui.weight;

import android.animation.ValueAnimator;
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
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 *    author : alphrye
 *    time   : 2019/2/27
 *    desc   : 仪表盘
 */
public class DashboardView extends View {
    private static final String COLOR_PRIMARY = "#FFFFFF";
    private static final int STROKE_WIDTH_DEFAULT = 10;
    private static final int LEN_BETWEEN_CIRCLE_AND_INDICATOR = 35;
    private static final int WIDTH_DEFALUT = 1080;
    private static final int HEIGHT_DEFALUT = 1080;
    private int mDeviation;

    /**绘制刻度的画笔*/
    private Paint mScalePaint;

    /**绘制LCD数字的画笔*/
    private Paint mLCDPaint;

    /**绘制与指示器相关的画笔*/
    private Paint mIndicatorPaint;

    private int perDegree;

    private int mCenterCircleRadius;

    private float mCurValue;

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
        mCurValue = 0.0f;
        initPaints();
        setBackgroundColor(Color.BLACK);
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
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            int minLen = Math.min(widthSize, heightSize);
            setMeasuredDimension(minLen, minLen);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WIDTH_DEFALUT, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize, HEIGHT_DEFALUT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDeviation = getWidth() / 8;
        int startX = 0;
        int startY = getHeight() / 2 + mDeviation;

        drawScaleAndNum(canvas, startX, startY);

        drawSpeedIndicator(canvas, startX, startY, mCenterCircleRadius + LEN_BETWEEN_CIRCLE_AND_INDICATOR);

        drawCenterCircle(canvas, mCenterCircleRadius);

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
        int longEndY = getHeight() / 2 + mDeviation;
        int shortEndX = startX + shortLen;
        int shortEndY = getHeight() / 2 + mDeviation;
        int len = 30;
        canvas.rotate(-perDegree * 6, getWidth() / 2, getHeight() / 2 + mDeviation);
        for (int i = 0; i < 49; i++) {
            if (i % 6 == 0) {
                mScalePaint.setColor(Color.RED);
                mScalePaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
                canvas.drawLine(startX, startY, longEndX, longEndY, mScalePaint);
                canvas.save();
                String text = i / 2 + "";
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
            canvas.rotate(perDegree, getWidth() / 2, getHeight() / 2 + mDeviation);
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

        mIndicatorPaint.setShader(null);

        mIndicatorPaint.setColor(Color.BLACK);
        mIndicatorPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2 + mDeviation, radius + 25, mIndicatorPaint);

        mIndicatorPaint.setColor(Color.parseColor(COLOR_PRIMARY));
        mIndicatorPaint.setStyle(Paint.Style.STROKE);
        mIndicatorPaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2 + mDeviation, radius, mIndicatorPaint);
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
        LinearGradient mGradient = new LinearGradient(startX, startY, getWidth() / 2, getHeight() / 2 + mDeviation, new int[] {Color.parseColor("#00000000"), Color.parseColor("#FF0000")}, null,Shader.TileMode.MIRROR);
        mIndicatorPaint.setShader(mGradient);
        canvas.rotate(- perDegree * 6 + mCurValue * perDegree, getWidth() / 2, getHeight() / 2 + mDeviation);
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(getWidth() / 2 + 30 + 35 + 40, getHeight() / 2 + 25 + mDeviation);
        path.lineTo(getWidth() / 2 + 30 + 35 + 80, getHeight() / 2 + mDeviation);
        path.lineTo(getWidth() / 2 + 30 + 35 + 40, getHeight() / 2 - 25 + mDeviation);
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
        float startY = getHeight() / 2 +  getWidth() / 4 + mDeviation;

        mLCDPaint.setTextSize(120);
        canvas.drawText(speedText, startX, startY, mLCDPaint);

        mLCDPaint.setTextSize(60);
        canvas.drawText(tvKmPerHour, startX + speedTvRect.width() + len, startY, mLCDPaint);
        canvas.restore();
    }

    /**
     * 更新指示器值
     * @param value
     */
    public void updateToValue(float value) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurValue, value);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        // TODO: 2019/2/28 设置一个最快和最慢时间
        valueAnimator.setDuration((long) (value * 100));
        valueAnimator.start();
    }
}
