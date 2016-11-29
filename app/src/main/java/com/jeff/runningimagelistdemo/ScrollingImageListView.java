package com.jeff.runningimagelistdemo;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import static java.lang.Math.abs;

/**
 * 说明：attrs.xml
 * 作者： 张武
 * 日期： 2016/11/25.
 * email:jeff_zw@qq.com
 **/

public class ScrollingImageListView extends View {

    //速度(根据视效调整)
    private int speed;

    //是否只有一张图
    private boolean isSingleImg;

    //是否乱序
    private boolean isOutOfOder;

    //是否往右滚动
    private boolean isLeftToRight;

    //单张图时 图片
    private int singleImgSrcID;

    //多张图的src list
    private List<Integer> imgSrcList;

    //多张图的bitmap
    private List<Bitmap> bitmapList;

    //主View高度(取最高的图片高度)
    private int mViewHeight;

    //滚动状态
    private State mRunningmState = State.STOP;

    //是否在滚动
    private boolean isRunning = false;
    //停止滚动之前的状态
    private State oldState = State.RIGHT_RUNNING;

    //图片偏移量
    private int offset = 0;

    //所有图平铺后的右边位置
    private int right = 0;

    //所有图平铺后的左边位置
    private int left = getMeasuredWidth();

    //已经画出的图（在bitmapList中的序号）
    private List<Bitmap> picShowList;
    private int currentDrawPicId = 0;

    public ScrollingImageListView(Context context) {
        super(context);
    }

    public ScrollingImageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public ScrollingImageListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mViewHeight > 0 ? mViewHeight : heightMeasureSpec);
    }


    /**
     * 画图
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        drawHistory(canvas);

        if (isLeftToRight) {
            while (left > 0) {
                Bitmap b = getDrawBitmap();
                picShowList.add(b);
                canvas.drawBitmap(b, left - b.getWidth(), 0, null);
                left -= b.getWidth();
            }
            if (isRunning) {
                left = offset;
                right = offset;
                offset += speed;
                postInvalidate();
            }
        } else {
            while (right < getMeasuredWidth()) {
                Bitmap b = getDrawBitmap();
                picShowList.add(b);
                canvas.drawBitmap(b, right, 0, null);
                right += b.getWidth();

            }
            if (isRunning) {
                left = offset;
                right = offset;
                offset -= speed;
                postInvalidate();
            }
        }

    }

    /**
     * 画之前已经显示出的图
     */
    private void drawHistory(Canvas canvas) {

        if (picShowList == null)
            return;

        if (isLeftToRight) {
            left = offset;
            for (Bitmap b : picShowList) {
                canvas.drawBitmap(b, left - b.getWidth(), 0, null);
                left -= b.getWidth();
                if (left < 0)
                    return;
            }
        } else {
            right = offset;
            for (Bitmap b : picShowList) {
                canvas.drawBitmap(b, right, 0, null);
                right += b.getWidth();
                if (right > getMeasuredWidth())
                    return;
            }
        }
    }


    /**
     * 获取待绘入的bitmap
     */
    private List<Integer> picId = new ArrayList<>();

    private Bitmap getDrawBitmap() {

        if (isOutOfOder) {
            //乱序
            //  如果都出现过，重置
            if (picId.size() == bitmapList.size())
                picId.clear();
            do {
                Random mRandom = new Random();
                //生成[0,n)集合中的整数,注意不包括n
                currentDrawPicId = mRandom.nextInt(bitmapList.size());
            } while (picId.contains(currentDrawPicId));
            picId.add(currentDrawPicId);
            return bitmapList.get(currentDrawPicId);
        } else {
            if (currentDrawPicId >= bitmapList.size())
                currentDrawPicId = 0;
            return bitmapList.get(currentDrawPicId++);
        }
    }

    /**
     * 资源图片文件转Bitmap
     *
     * @param srcList
     * @return
     */
    private List<Bitmap> changenSrcToBitMap(List<Integer> srcList) {
        List<Bitmap> bitMapList = new ArrayList<Bitmap>();
        Resources res = getResources();
        if (srcList == null)
            return null;
        for (int i : srcList) {
            Bitmap bitmap = BitmapFactory.decodeResource(res, i);
            bitMapList.add(bitmap);
        }
        return bitMapList;
    }

    /**
     * 初始化view
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {

        //获取xml中传入的参数
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollingImageListView);
        speed = (int) abs(a.getDimension(R.styleable.ScrollingImageListView_speed, 15));
        isSingleImg = a.getBoolean(R.styleable.ScrollingImageListView_isSingleImg, false);
        isOutOfOder = a.getBoolean(R.styleable.ScrollingImageListView_isOutOfOrder, false);
        isLeftToRight = a.getBoolean(R.styleable.ScrollingImageListView_isLeftToRight, false);
        singleImgSrcID = a.getResourceId(R.styleable.ScrollingImageListView_singleImgSrc, R.mipmap.ic_launcher);
        a.recycle();
        picShowList = new ArrayList<>();

    }

    public void setImgSrcList(List<Integer> imgSrcList) {

        this.imgSrcList = imgSrcList;

        //将待显示的图片转为bitmap
        bitmapList = changenSrcToBitMap(imgSrcList);

        //用最高得图的高度作为空间高度
        mViewHeight = 0;
        if (bitmapList == null)
            return;
        for (Bitmap bitmap : bitmapList) {
            if (bitmap.getHeight() > mViewHeight)
                mViewHeight = bitmap.getHeight();
        }
    }


    /**
     * 改变运行方向
     *
     * @param state
     */
    public void changeDirection(State state) {
        if (state == mRunningmState)
            return;
        stopRun();
        switch (state) {
            case STOP:
                return;
            case LEFT_RUNNING:
                setLeftToRight(false);
                break;
            case RIGHT_RUNNING:
                setLeftToRight(true);
                break;
        }
        mRunningmState = state;
        startRun();
    }


    public void startRun() {

        if (isRunning)
            return;
        else {
            if (oldState != mRunningmState) {
                left = getMeasuredWidth();
                right = 0;
                offset = 0;
            }
            if (isLeftToRight)
                mRunningmState = State.RIGHT_RUNNING;
            else
                mRunningmState = State.LEFT_RUNNING;
            isRunning = true;
            postInvalidate();
        }
    }

    public void stopRun() {

        if (isRunning) {
            isRunning = false;
            oldState = mRunningmState;
            mRunningmState = State.STOP;
            postInvalidate();
        }
    }

    public enum State {
        LEFT_RUNNING,
        RIGHT_RUNNING,
        STOP
    }


    /**
     * set  &&  get
     *
     */

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isSingleImg() {
        return isSingleImg;
    }

    public void setSingleImg(boolean singleImg) {
        isSingleImg = singleImg;
    }

    public boolean isOutOfOder() {
        return isOutOfOder;
    }

    public void setOutOfOder(boolean outOfOder) {
        isOutOfOder = outOfOder;
    }

    public boolean isLeftToRight() {
        return isLeftToRight;
    }

    public void setLeftToRight(boolean leftToRight) {
        isLeftToRight = leftToRight;
    }

    public int getSingleImgSrcID() {
        return singleImgSrcID;
    }

    public void setSingleImgSrcID(int singleImgSrcID) {
        this.singleImgSrcID = singleImgSrcID;
    }

    public List<Integer> getImgSrcList() {
        return imgSrcList;
    }

    public State getmRunningmState() {
        return mRunningmState;
    }

}
