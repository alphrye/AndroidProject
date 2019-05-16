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
import android.text.TextUtils;
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
    private static final int WIDTH_DEFAULT = 800;
    private static final int HEIGHT_DEFAULT = 1000;
    /**圆心向下偏移量*/
    private int mDeviation;

    /**绘制刻度的画笔*/
    private Paint mScalePaint;

    /**绘制LCD数字的画笔*/
    private Paint mLCDPaint;

    /**绘制与指示器相关的画笔*/
    private Paint mIndicatorPaint;

    private float perDegree;

    private int mCenterCircleRadius;

    /**当前速度*/
    private float mCurSpeed;

    /**长刻度长度*/
    private int mScaleLongLen;

    /**短刻度长度*/
    private int mScaleShortLen;

    private int mLenToScaleStart;

    /**底部空余角度*/
    private int mA;

    private int numScaleLong;

    private int numScaleShortPer;

    private int mNumScaleTotal;

    private int mValueLongPer;

    private String mSpeedText;

    public DashboardView(Context context) {
        this(context, null);
    }

    public DashboardView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public DashboardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mCenterCircleRadius = 35;
        mCurSpeed = 0.0f;
        mScaleLongLen = 45;
        mScaleShortLen = 30;
        mLenToScaleStart = 30;
        mA = 120;
        numScaleLong = 9;
        numScaleShortPer = 2;
        mNumScaleTotal = (numScaleLong - 1) * numScaleShortPer + numScaleLong;
        perDegree = ((360.0f - mA) / (mNumScaleTotal - 1));
        mValueLongPer = 3;
        initPaints();
        setBackgroundColor(Color.parseColor("#24252B"));
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
        // TODO: 2019/3/7 控制视图是一个正方形
        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            int minLen = Math.min(widthSize, heightSize);
            setMeasuredDimension(minLen, minLen);
        } else if (widthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(WIDTH_DEFAULT, heightSize);
        } else if (heightMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSize, HEIGHT_DEFAULT);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDeviation = (int) ((getHeight() / 2 * Math.cos((mA / 2.0) * Math.PI / 180.0)) / 2);
        //(startX, startY)为左边刻度的起始绘制坐标
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
        int longEndX = startX + mScaleLongLen;
        int longEndY = getHeight() / 2 + mDeviation;
        int shortEndX = startX + mScaleShortLen;
        int shortEndY = getHeight() / 2 + mDeviation;
        canvas.rotate(-(90 - mA / 2), getWidth() / 2, getHeight() / 2 + mDeviation);
        for (int i = 0; i < mNumScaleTotal; i++) {
            if (i % (numScaleShortPer + 1)== 0) {
                mScalePaint.setColor(Color.parseColor("#1DA1F2"));
                mScalePaint.setStrokeWidth(STROKE_WIDTH_DEFAULT);
                canvas.drawLine(startX, startY, longEndX, longEndY, mScalePaint);
                canvas.save();
                String text = i / (numScaleShortPer + 1) * mValueLongPer + "";
                Rect rect =  new Rect();
                mLCDPaint.setTextSize(70);
                mLCDPaint.getTextBounds(text, 0, text.length(), rect);
                canvas.rotate(- perDegree * (i - (numScaleShortPer  + 1)), longEndX + mLenToScaleStart + rect.width() / 2,  longEndY);
                canvas.drawText(text, longEndX + mLenToScaleStart,  longEndY + rect.height() / 2, mLCDPaint);
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

        mIndicatorPaint.setColor(Color.parseColor("#252831"));
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
        LinearGradient mGradient = new LinearGradient(startX, startY, getWidth() / 2, getHeight() / 2 + mDeviation, new int[] {Color.parseColor("#00000000"), Color.parseColor("#1DA1F2")}, null,Shader.TileMode.MIRROR);
        mIndicatorPaint.setShader(mGradient);
        canvas.rotate(- perDegree * (numScaleShortPer + 1) + mCurSpeed * perDegree, getWidth() / 2, getHeight() / 2 + mDeviation);
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
        if (TextUtils.isEmpty(mSpeedText)) {
            mSpeedText = "0";
        }
        Rect speedTvRect = new Rect();
        mLCDPaint.setTextSize(120);
        mLCDPaint.getTextBounds(mSpeedText, 0, mSpeedText.length(), speedTvRect);

        String tvKmPerHour = "km/h";
        Rect tvKmPerHourRect = new Rect();
        mLCDPaint.setTextSize(60);
        mLCDPaint.getTextBounds(tvKmPerHour, 0, tvKmPerHour.length(), tvKmPerHourRect);
        int len = 20;
        float startX = getWidth() / 2 - (speedTvRect.width() + tvKmPerHourRect.width() + len)  / 2;
        float startY = getHeight() / 2 +  getWidth() / 4 + mDeviation;

        mLCDPaint.setTextSize(120);
        canvas.drawText(mSpeedText, startX, startY, mLCDPaint);

        mLCDPaint.setTextSize(60);
        canvas.drawText(tvKmPerHour, startX + speedTvRect.width() + len, startY, mLCDPaint);
        canvas.restore();
    }

    /**
     * 更新当前的速度值
     * @param speed
     */
    public void updateSpeed(float speed) {
        //速度没有变换、无效不用更新
        if (mCurSpeed == speed
                || speed < 0) {
            return;
        }
        //相对相对精准的当前速度(速度转化，采用km/h)
        float accurateSpeed = speed * 3.6f;
        int generalSpeed = Math.round(accurateSpeed);
        mSpeedText = String.valueOf(generalSpeed);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurSpeed, accurateSpeed);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurSpeed = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration((long) (Math.abs(accurateSpeed - mCurSpeed ) * 100));
        valueAnimator.start();
        invalidate();
    }
}
