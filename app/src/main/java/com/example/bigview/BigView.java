package com.example.bigview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class BigView extends View implements GestureDetector.OnGestureListener, View.OnTouchListener {


    private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private Rect mRect;
    private BitmapFactory.Options mOptions;
    private BitmapRegionDecoder mBitmapRegionDecoder;
    private int mImgWidth;
    private int mImgHeight;
    private float mScale;
    private int viewdWidth;
    private int viewHeight;
    private Bitmap mBitmap;

    public BigView(Context context) {
        this(context, null);
    }

    public BigView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//   创建一个矩形 展示图片
        mRect = new Rect();
        mOptions = new BitmapFactory.Options();
        mGestureDetector = new GestureDetector(this);
        mScroller = new Scroller(context);

        setOnTouchListener(this);
    }


    public void setImg(InputStream is) {
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, mOptions);
        mOptions.inJustDecodeBounds = false;
        mImgWidth = mOptions.outWidth;
        mImgHeight = mOptions.outHeight;
//        开启复用
        mOptions.inMutable = true;
//         图片由一个一个像素点组成的  一个像素点包含了（argb） 透明  红色 绿色 蓝色
//        argb 在内存 怎么保存？ 计算机 最小单位 是 位
//        ARGB_8888 代表一个像素点，  argb 每个占用8位 组成一个像素点
//        每8位是一个字节  一个像素点 占用32位 占用4字节
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
//        创建区域解码器
        try {
            mBitmapRegionDecoder = BitmapRegionDecoder.newInstance(is, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        获取 view的 宽高
        viewdWidth = getMeasuredWidth();
        viewHeight = getMeasuredHeight();
//        确定图片的加载区域
        mRect.top = 0;
        mRect.left = 0;
        mRect.right = mImgWidth;
//
        mScale = viewdWidth / (float) mImgWidth;

        mRect.bottom = (int) (viewHeight / mScale);
        Log.e("eee", mScale + "---- " + viewdWidth + "----" + viewHeight + "--" + mImgWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBitmapRegionDecoder == null) {
            return;
        }
//        内存 复用
        mOptions.inBitmap = mBitmap;
        mBitmap = mBitmapRegionDecoder.decodeRegion(mRect, mOptions);
        Matrix matrix = new Matrix();
        matrix.setScale(mScale, mScale);
        canvas.drawBitmap(mBitmap, matrix, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
//        交给 mGestureDetector 处理
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        处理滑动 惯性  如果 没停下来 强制 停下来
        if (!mScroller.isFinished()) {
            mScroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.e("eeee", distanceX + "====" + distanceY);
//        只移动 矩形
        mRect.offset(0, (int) distanceY);
        Log.e("eeee", mRect.bottom + "====" + distanceY + "---" + mImgHeight + "---" + mRect.top);
//        滑动距离 不能超过 图片大小
        if (mRect.bottom > mImgHeight) {
            mRect.bottom = mImgHeight;
            mRect.top = mImgHeight - (int) (viewHeight / mScale);
        }
        if (mRect.top < 0) {
            mRect.top = 0;
            mRect.bottom = (int) (viewHeight / mScale);
        }
//        触发 odraw
        invalidate();
        return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        mScroller.fling(0, mRect.top, 0, (int) velocityY, 0, 0, 0
                , mImgHeight - (int) (viewHeight / mScale));
        return false;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.isFinished()) {
            return;
        }

        if (mScroller.computeScrollOffset()) {
            mRect.top = mScroller.getCurrY();
            mRect.bottom=mRect.top+(int)(viewHeight / mScale);
            invalidate();

        }
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


    @Override
    public void onLongPress(MotionEvent e) {

    }


}
