package com.llg.areasetview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

/*
 * create by lilugen on 2018/9/14
 */
public class AreaSetView extends View {

    //边上的格子数
    private int countX;
    private int countY;

    private int width;
    private int height;
    //网格的宽高
    private int gridWidth;
    private int gridHeight;

    private Paint mLinePaint;
    private Paint mSelectPaint;
    private Paint mUnSelectPaint;

    private boolean isRemove;//根据选取的格子，判断是否移除操作
    private boolean isFinishSelect = true;//是否完成选取格子的操作

    private GestureDetector mGestureDetector;

    private HashSet<Point> selectPoint = new HashSet<>();

    private HashSet<Point> currentPoint = new HashSet<>();

    /**
     * 设置总区域 x,y方向的数目
     *
     * @param x 横向的区域块数
     * @param y 纵向的区域块数
     */
    public void setXYCount(int x, int y) {
        this.countX = x;
        this.countY = y;
    }

    /**
     * 设置选中的区域位置
     *
     * @param selectRect 选中的区域位置
     */
    public void setSelectArea(HashSet<Point> selectRect) {
        this.selectPoint = selectRect;
    }

    /**
     * 获取用户操作后的数据
     *
     * @return 用户选择的区域数据
     */
    public HashSet<Point> getSelectArea() {
        return selectPoint;
    }

    public AreaSetView(Context context) {
        super(context);
        init(context);
    }

    public AreaSetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AreaSetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(0);

        mSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectPaint.setColor(getResources().getColor(R.color.area_color));
        mSelectPaint.setStyle(Paint.Style.FILL);

        mUnSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnSelectPaint.setColor(getResources().getColor(R.color.transparent));
        mUnSelectPaint.setStyle(Paint.Style.FILL);

        mGestureDetector = new GestureDetector(context, new SimpleGestureDetector());
        mGestureDetector.setIsLongpressEnabled(false);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpec) {
        int result = 800;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }

    private int measureHeight(int measureSpec) {
        int result = 500;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.UNSPECIFIED:
                result = specSize;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.max(result, specSize);
                break;
            case MeasureSpec.EXACTLY:
                result = specSize;
                break;
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制已经被选中的格子
        drawSelectArea(canvas);
        if (!isFinishSelect) {
            //绘制正在选中的区域
            drawCurrentArea(canvas);
            isFinishSelect = true;
        }
        currentPoint.clear();
        //绘制网格
        drawGridLine(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth() - getPaddingLeft() - getPaddingRight();
        height = getHeight() - getPaddingTop() - getPaddingBottom();
        gridWidth = (int) (width / countX);
        gridHeight = (int) (height / countY);

        //表示View中可以绘制区域的四个点，它们的坐标是相对坐标
        mLeft = getPaddingLeft();
        mTop = getPaddingTop();
    }

    private int mLeft;
    private int mTop;

    private void drawGridLine(Canvas canvas) {
        for (int i = 1; i < countX; i++) {
            canvas.drawLine(mLeft+gridWidth * i, mTop, mLeft+gridWidth * i, height, mLinePaint);
        }
        for (int j = 1; j < countY; j++) {
            canvas.drawLine(mLeft, gridHeight * j+mTop, width, gridHeight * j+mTop, mLinePaint);
        }
    }

    /**
     * 绘制已经被选中的区域
     *
     * @param canvas
     */
    private void drawSelectArea(Canvas canvas) {
        for (Point point : selectPoint) {
            if (!isFinishSelect) {
                if (currentPoint.contains(point)) {
                    continue;
                }
            }
            canvas.drawRect(point.x * gridWidth,
                    point.y * gridHeight,
                    point.x * gridWidth + gridWidth,
                    point.y * gridHeight + gridHeight, mSelectPaint);
        }
    }

    /**
     * 绘制正在选取的区域
     *
     * @param canvas
     */
    private void drawCurrentArea(Canvas canvas) {
        Paint paint = isRemove ? mUnSelectPaint : mSelectPaint;
        for (Point point : currentPoint) {
            canvas.drawRect(point.x * gridWidth,
                    point.y * gridHeight,
                    point.x * gridWidth + gridWidth,
                    point.y * gridHeight + gridHeight, paint);
        }
    }

    //清除所有格子
    public void clearAll() {
        selectPoint.clear();
        invalidate();
    }

    //选中所有格子
    public void selectAll() {
        selectPoint.clear();
        for (int i = 0; i < countX; i++) {
            for (int j = 0; j < countY; j++) {
                Point point = new Point(i, j);
                selectPoint.add(point);
            }
        }
        invalidate();
    }

    public boolean isEmpty() {
        return selectPoint.isEmpty();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point point = new Point((int) event.getX() / gridWidth, (int) event.getY() / gridHeight);
                isRemove = selectPoint.contains(point);
                break;
            case MotionEvent.ACTION_UP:
                if (isRemove) {
                    selectPoint.removeAll(tempPoint);
                } else {
                    selectPoint.addAll(tempPoint);
                }
                tempPoint.clear();
                isFinishSelect = true;
                invalidate();
                break;
            default:
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }


    private HashSet<Point> tempPoint = new HashSet<>();

    /**
     * 处理用户动作
     */
    class SimpleGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            int x1 = (int) (e1.getX() / gridWidth);
            int y1 = (int) (e1.getY() / gridHeight);
            int x2 = (int) (e2.getX() / gridWidth);
            int y2 = (int) (e2.getY() / gridHeight);

            int dx = x2 - x1;
            int dy = y2 - y1;

            isFinishSelect = false;
            if (dy > 0) {
                for (int i = y1; i <= y2; i++) {
                    if (dx > 0) {
                        for (int j = x1; j <= x2; j++) {
                            Point point = new Point(j, i);
                            currentPoint.add(point);
                        }
                    } else {
                        for (int j = x1; j >= x2; j--) {
                            Point point = new Point(j, i);
                            currentPoint.add(point);
                        }
                    }
                }
            } else {
                for (int i = y1; i >= y2; i--) {
                    if (dx > 0) {
                        for (int j = x1; j <= x2; j++) {
                            Point point = new Point(j, i);
                            currentPoint.add(point);
                        }
                    } else {
                        for (int j = x1; j >= x2; j--) {
                            Point point = new Point(j, i);
                            currentPoint.add(point);
                        }
                    }
                }
            }
            //临时存起来
            tempPoint.clear();
            tempPoint.addAll(currentPoint);
            invalidate();
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            int curPointX = (int) (e.getX() / gridWidth);
            int curPointY = (int) (e.getY() / gridHeight);
            Point point = new Point(curPointX, curPointY);
            if (selectPoint.contains(point)) {
                selectPoint.remove(point);
            } else {
                selectPoint.add(point);
            }
            invalidate();
            return true;
        }
    }

}
