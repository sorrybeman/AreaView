package com.llg.areasetview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;

/*
 * create by lilugen on 2018/9/14
 */
public class AreaSetView extends View {

    //边上的格子数
    private int countX = 20;
    private int countY = 20;

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

    public void setXYCount(int x, int y) {
        this.countX = x;
        this.countY = y;
    }

    public void setSelectRect(HashSet<Point> selectRect) {
        this.selectPoint = selectRect;
    }

    public HashSet<Point> getSelectRect() {
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
        mSelectPaint.setColor(getResources().getColor(R.color.colorPrimary));
        mSelectPaint.setStyle(Paint.Style.FILL);

        mUnSelectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mUnSelectPaint.setColor(getResources().getColor(R.color.transparent));
        mUnSelectPaint.setStyle(Paint.Style.FILL);

        mGestureDetector = new GestureDetector(context, new SimpleGestureDetector());
        mGestureDetector.setIsLongpressEnabled(false);

        selectPoint.add(new Point(1,2));
        selectPoint.add(new Point(2,4));
        selectPoint.add(new Point(4,16));
        selectPoint.add(new Point(16,8));
        selectPoint.add(new Point(19,10));
        selectPoint.add(new Point(8,2));


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeigth = MeasureSpec.getSize(heightMeasureSpec);
        gridWidth = measureWidth / countX;
        gridHeight = measureHeigth / countY;

        setMeasuredDimension(gridWidth * countX, gridHeight * countY);
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void drawGridLine(Canvas canvas) {
        for (int i = 1; i < countX; i++) {
            canvas.drawLine(gridWidth * i, 0, gridWidth * i, height, mLinePaint);
        }
        for (int j = 1; j < countY; j++) {
            canvas.drawLine(0, gridHeight * j, width, gridHeight * j, mLinePaint);
        }
    }

    /**
     * 绘制已经被选中的区域
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
