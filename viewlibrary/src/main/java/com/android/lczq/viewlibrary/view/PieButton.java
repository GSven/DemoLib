package com.android.lczq.viewlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;
/**
*
*@author Gsven
*@Date 2020/1/7 17:37
*@Desc
*/
public class PieButton extends View {
    protected static final int DEAFUALT_PROGRESS_VIEW_WIDTH = 200;//进度条默认宽度
    protected static final int DEAFUALT_PROGRESS_VIEW_HEIGHT = 50;//进度条默认高度
    private static final int TOTAL_NUM = 100;//总份额
    private int mLeftProgress; //左边份额
    private int mRightProgress;//右边份额
    private Paint mLeftPaint;//左边按钮画笔
    private int mLeftPieColor;
    private Path mLeftPath;
    private int mLeftWidth;//左边按钮宽度

    private Paint mRightPaint;//右边按钮画笔
    private int mRightPieColor;
    private Path mRightPath;
    private int mRightWidth;//右边按钮宽度

    private Paint mTextPaint;//文字画笔
    private int mOffetAngle;//倾斜距离
    private int mOffetDistance;//左右按钮空隙的距离

    private int mHeight; //控件高度
    private int mWidth; //控件宽度
    private String mLeftText;//左边文字
    private String mRightText;//右边文字
    private int mTextSize;//文字大小
    private int mTextHeight;//文字高度
    private RectF rectF;//按钮两边的半圆区域

    private float mLeftCurrentProgress;//当前左边份额
    private float mRightCurrentProgress;//当前右边份额
    private float mLeftPerProgress;//左边每次加的份额
    private float mRightPerProgress;//右边每次加的份额
    private int mPeriod = 20;//份额每次添加的周期
    private Timer timer;
    private boolean mLeftComplete, mRightComplete;//左右两边是否达到总份额
    private IOnClickListener mOnClickListener;


    public PieButton(Context context) {
        this(context, null);
    }

    public PieButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PieButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mOffetAngle = 10;
        mOffetDistance = dp2px(getContext(), 5);

        mLeftPaint = new Paint();
        mLeftPieColor = Color.parseColor("#FB5D44");
        mLeftPaint.setColor(mLeftPieColor);
        mLeftPaint.setAntiAlias(true);
        mLeftPaint.setStyle(Paint.Style.FILL);
        mLeftPath = new Path();

        mRightPaint = new Paint();
        mRightPieColor = Color.parseColor("#5087ED");
        mRightPaint.setColor(mRightPieColor);
        mRightPaint.setAntiAlias(true);
        mRightPaint.setStyle(Paint.Style.FILL);
        mRightPath = new Path();


        mTextPaint = new TextPaint();
        mTextPaint.setColor(Color.WHITE);
        mTextSize = dp2px(getContext(), 14);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mLeftProgress = 50;
        mRightProgress = TOTAL_NUM - mLeftProgress;
        mLeftText = "是";
        mRightText = "否";
        rectF = new RectF();
        measureTextHeight();
    }

    private void measureTextHeight() {
        Rect rect = new Rect();
        mTextPaint.getTextBounds(mLeftText, 0, mLeftText.length(), rect);
        mTextHeight = rect.height();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画按钮
        drawLeft(canvas);
        drawRight(canvas);
    }

    private int mCurrentLeftWidth;//当前左边宽度

    private void drawLeft(Canvas canvas) {
        canvas.save();
        mCurrentLeftWidth = (int) ((mLeftCurrentProgress * 1.0 / mLeftProgress) * mLeftWidth);
        mLeftPath.reset();
        mLeftPath.moveTo(mHeight / 2, 0);
        mLeftPath.lineTo(mHeight / 2 + mCurrentLeftWidth + mOffetAngle, 0);
        mLeftPath.lineTo(mHeight / 2 + mCurrentLeftWidth, mHeight);
        mLeftPath.lineTo(mHeight / 2, mHeight);
        rectF.setEmpty();
        rectF.set(0, 0, mHeight, mHeight);
        mLeftPath.arcTo(rectF, 90, 180);
        mLeftPath.close();
        canvas.drawPath(mLeftPath, mLeftPaint);

        //绘制文字
        if (!TextUtils.isEmpty(mLeftText)) {
            canvas.drawText(mLeftText, (mHeight / 2 + mLeftWidth) / 2, getHeight() / 2 + mTextHeight / 2, mTextPaint);
        }
        canvas.restore();

    }

    private int mCurrentRightWidth;//当前右边宽度

    private void drawRight(Canvas canvas) {
        canvas.save();
        mCurrentRightWidth = (int) ((mRightCurrentProgress * 1.0 / mRightProgress) * mRightWidth);
        mRightPath.reset();
        mRightPath.moveTo(mWidth - mHeight / 2, 0);
        rectF.setEmpty();
        rectF.set(mWidth - mHeight, 0, mWidth, mHeight);
        mRightPath.arcTo(rectF, -90, 180);
        mRightPath.lineTo(mWidth - mHeight / 2 - mCurrentRightWidth - mOffetAngle, mHeight);
        mRightPath.lineTo(mWidth - mHeight / 2 - mCurrentRightWidth, 0);
        mRightPath.close();
        canvas.drawPath(mRightPath, mRightPaint);
        //绘制文字
        if (!TextUtils.isEmpty(mRightText)) {
            canvas.drawText(mRightText, (mWidth - mHeight / 2 - mLeftWidth - mOffetDistance + (mRightWidth + mHeight / 2) / 2),
                    getHeight() / 2 + mTextHeight / 2,
                    mTextPaint);
        }
        canvas.restore();
    }

    /**
     * 设置字体大小
     *
     * @param size
     */
    private void setTextSize(int size) {
        this.mTextSize = dp2px(getContext(), size);
        measureTextHeight();
    }

    /**
     * 设置左右两侧文字
     *
     * @param leftText
     * @param rightText
     */
    public void setTextDesc(String leftText, String rightText) {
        this.mLeftText = leftText;
        this.mRightText = rightText;
    }

    /**
     * 设置左边进度
     *
     * @param leftProgress
     */
    public void setProgress(int leftProgress) {
        if (leftProgress > TOTAL_NUM || leftProgress < 0) {
            throw new IllegalArgumentException("参数值超时范围..");
        }
        this.mLeftProgress = leftProgress;
        this.mRightProgress = (TOTAL_NUM - this.mLeftProgress);
        //更新动画
        updateProgress();
    }


    private void updateProgress() {
        mLeftPerProgress = mLeftProgress * 1.0F / TOTAL_NUM;
        mRightPerProgress = mRightProgress * 1.0F / TOTAL_NUM;

        mLeftCurrentProgress = 0;
        mRightCurrentProgress = 0;
        mLeftComplete = false;
        mRightComplete = false;

        releaseTimer();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //左边
                if (mLeftCurrentProgress < mLeftProgress) {
                    mLeftCurrentProgress += mLeftPerProgress;
                } else {
                    mLeftCurrentProgress = mLeftProgress;
                    mLeftComplete = true;
                }

                //右边
                if (mRightCurrentProgress < mRightProgress) {
                    mRightCurrentProgress += mRightPerProgress;
                } else {
                    mRightCurrentProgress = mRightProgress;
                    mRightComplete = true;
                }

                postInvalidate();
                //两边都完成了，结束timer
                if (mLeftComplete && mRightComplete) {
                    releaseTimer();
                }
            }
        }, 0, mPeriod);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        releaseTimer();
    }

    /**
     * 释放timer
     */
    private void releaseTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
        //总mwidth  = 左半圆+矩形+由半圆
        mLeftWidth = (int) ((mLeftProgress * 1.0 / (mLeftProgress + mRightProgress)) * (mWidth - mOffetDistance - mHeight));
        mRightWidth = (int) ((mRightProgress * 1.0 / (mLeftProgress + mRightProgress)) * (mWidth - mOffetDistance - mHeight));
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    protected int measureWidth(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = dp2px(getContext(), DEAFUALT_PROGRESS_VIEW_WIDTH);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    protected int measureHeight(int measureSpec) {
        int result = 0;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = dp2px(getContext(), DEAFUALT_PROGRESS_VIEW_HEIGHT);
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }


    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    private float downX;
    private float downY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Region l = setRegion(mLeftPath);
                Region r = setRegion(mRightPath);
                if (isInRegion(downX, downY, l)) {
                    //左边响应事件
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickListener(true);
                    }
                } else if (isInRegion(downX, downY, r)) {
                    //右边响应事件
                    if (mOnClickListener != null) {
                        mOnClickListener.onClickListener(false);
                    }
                }
                break;
        }
        return true;
    }

    public interface IOnClickListener {
        /**
         * @param left true-左边响应事件；
         *             false-右边响应事件
         */
        void onClickListener(boolean left);
    }


    public void setOnClickListener(IOnClickListener listener) {
        this.mOnClickListener = listener;
    }


    /**
     * 由于Path是由多个坐标点构成的，将path转化成region，
     * 可以通过region.contains(int x,int y)来判断点击坐标是否在该区域。
     * 该方法不仅适用于饼图，其他更加复杂图形同样适用，
     * 当然前提是你能构建出绘制这个图形所需要的路径。
     *
     * @param path
     */
    public Region setRegion(Path path) {
        Region re = new Region();
        RectF rectF = new RectF();
        path.computeBounds(rectF, true);
        re.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        return re;
    }

    /**
     * 判断是否在某一个区域内
     *
     * @param x
     * @param y
     * @return
     */
    public boolean isInRegion(float x, float y, Region region) {
        return region != null && region.contains((int) x, (int) y);
    }


}
