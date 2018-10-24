package com.nfrc.mytextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zhangl on 2018/10/24.
 */

public class ProgressBar extends View {


    private int mInnerBackGround = Color.RED;
    private int mOuterBackGround = Color.BLACK;

    private int mRoundWidth = 10;
    private int progressTextSize = 15;
    private int progressTextColor = Color.RED;


    private Paint mInnerPain = new Paint();
    private Paint mOuterPain = new Paint();

    private Paint mTextPain = new Paint();


    private int mMax = 100;

    private int mProgress = 50;

    public ProgressBar(Context context) {
        this(context,null);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.ProgressBar);
        mInnerBackGround = array.getColor(R.styleable.ProgressBar_innerBackground,mInnerBackGround);
        mOuterBackGround = array.getColor(R.styleable.ProgressBar_outerBackground,mOuterBackGround);
        progressTextColor = array.getColor(R.styleable.ProgressBar_progressTextColor,progressTextColor);
        mRoundWidth = (int) array.getDimension(R.styleable.ProgressBar_roundWidth,dip2px(mRoundWidth));
        progressTextSize = array.getDimensionPixelSize(R.styleable.ProgressBar_progressTextSize, (int) sp2px(progressTextSize));
        array.recycle();

        mInnerPain.setAntiAlias(true);
        mInnerPain.setColor(mInnerBackGround);
        mInnerPain.setStrokeWidth(mRoundWidth);
        mInnerPain.setStyle(Paint.Style.STROKE);

        mOuterPain.setAntiAlias(true);
        mOuterPain.setColor(mOuterBackGround);
        mOuterPain.setStrokeWidth(mRoundWidth);
        mOuterPain.setStyle(Paint.Style.STROKE);

        mTextPain.setAntiAlias(true);
        mTextPain.setColor(progressTextColor);
        mTextPain.setStrokeWidth(mRoundWidth);
        mTextPain.setTextSize(progressTextSize);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(Math.min(width,height),Math.min(width,height));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //画内圆
        int center = getWidth()/2;
        canvas.drawCircle(center,center,center - mRoundWidth/2,mInnerPain);

        //画外圆,  圆弧

        RectF rect = new RectF(mRoundWidth / 2,mRoundWidth / 2,getWidth() - mRoundWidth/2 ,getWidth() - mRoundWidth/2) ;
        if (mProgress == 0){
            return;
        }

        float percent =(float)mProgress / mMax;
        canvas.drawArc(rect,0, 360 * percent,false,mOuterPain);
        //画文字
        String text = percent * 100 +"%";
        Rect textBounds = new Rect();
        mTextPain.getTextBounds(text,0,text.length(),textBounds);
        int x = getWidth()/2 - textBounds.width()/2;
        Paint.FontMetricsInt fontMetricsInt = mTextPain.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        int baseLine = getWidth()/2 + dy;
        canvas.drawText(text,x,baseLine,mTextPain);
    }



    public synchronized void setMax(int max){
        if (max < 0){
            return;
        }

        this.mMax = max;
    }

    public synchronized void setProgeress(int progeress){
        if (progeress < 0){
            return;
        }

        this.mProgress = progeress;
        invalidate();
    }


    private float dip2px(int dip){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dip,getResources().getDisplayMetrics());
    }
    private float sp2px(int dip){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,dip,getResources().getDisplayMetrics());
    }

}
