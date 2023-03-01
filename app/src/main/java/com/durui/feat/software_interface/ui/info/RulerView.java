package com.durui.feat.software_interface.ui.info;

//https://www.jianshu.com/p/5b03ad991437/

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.OverScroller;


public class RulerView extends View {
    private final int primaryColor = Color.parseColor("#bbdefb");
    private final int primaryLightColor = Color.parseColor("#ECF7FF");
    private final int primaryDarkColor = Color.parseColor("#8aacc8");

    private int currentIndex;

    private Paint paint;
    private int moveX = 0;//当前滚动距离
    private OverScroller scroller;
    private GestureDetector gestureDetector;
    private int width, height;//控件的宽高
    private int count;
    private int left;
    private int right;
    private int top;
    private int bottom;
    private int centerX;
    private int minX;
    private int maxX;
    private int upType = 0;//初始值； 1 sroll 滚动松开  2快速滑动松开


    public RulerView(Context context) {
        this(context, null);
    }

    public RulerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    public void init(Context context) {
        paint = new Paint();
        scroller = new OverScroller(context);

        gestureDetector = new GestureDetector(context, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                upType = 1;
                moveX = getScrollX() + (int) v;
                scrollTo(moveX, 0);
                invalidate();
                return false;
            }

            @Override
            public void onLongPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                upType = 2;
                scroller.fling(getScrollX(), getScrollY(), -(int) v, 0, minX, maxX, 0, 0);

                invalidate();
                return false;
            }
        });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width = getMeasuredWidth();
        height = getMeasuredHeight();

        int leftTextDistance = -3600;
        int rightTextDistance = 4200;

        minX = leftTextDistance;
        maxX = rightTextDistance;


        left = minX - width * 2;
        right = maxX + width * 2;

        count = (right - this.left) / 60;

        top = height / 2 - 200;
        bottom = height / 2 + 200;
        centerX = width / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //todo 尺面
        super.onDraw(canvas);
        paint.setColor(primaryLightColor);
        canvas.drawRect(new Rect(left, top, right, bottom), paint);

        //todo 刻度
        paint.setColor(primaryColor);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(60);
        //canvas.drawLine(centerX + moveX, top, centerX + moveX, height / 2 - 10, paint);
        for (int i = 0; i < count / 2; i++) {

            if (i % 5 == 0) {
                canvas.drawLine(centerX + i * 60, top, centerX + i * 60, top + 200, paint);
                canvas.drawLine(centerX - i * 60, top, centerX - i * 60, top + 200, paint);
                if (160 - i >= 100 && 160 - i <= 160) {
                    canvas.drawText((160 - i) + "", centerX + i * -60, bottom - 60, paint);
                }

                if (160 + i >= 160 && 160 + i <= 230) {
                    canvas.drawText((160 + i) + "", centerX + i * 60, bottom - 60, paint);
                }
            } else {
                canvas.drawLine(centerX + i * 60, top, centerX + i * 60, top + 100, paint);
                canvas.drawLine(centerX - i * 60, top, centerX - i * 60, top + 100, paint);
            }

        }

        paint.setStrokeWidth(4);

        //todo 实时计数
        paint.setColor(primaryDarkColor);
        canvas.drawLine(centerX + moveX, top, centerX + moveX, top + 200, paint);


        paint.setTextSize(100);
        if (moveX > 0 && moveX <= 4200 && moveX / 60f - (moveX / 60) >= 0.5) {
            canvas.drawText("" + (160 + moveX / 60 + 1), centerX + moveX, top - 100, paint);
            currentIndex = 160 + moveX / 60 + 1;//todo
        } else if (moveX > 0 && moveX <= 4200 && moveX / 60f - (moveX / 60) < 0.5) {
            canvas.drawText("" + (160 + moveX / 60), centerX + moveX, top - 100, paint);
            currentIndex = 160 + moveX / 60;//todo
        } else if (moveX < 0 && moveX >= -3600 && moveX / 60f - moveX / 60 <= -0.5) {
            canvas.drawText("" + (160 + moveX / 60 - 1), centerX + moveX, top - 100, paint);
            currentIndex = 160 + moveX / 60 - 1;//todo
        } else if (moveX < 0 && moveX >= -3600 && moveX / 60f - moveX / 60 > -0.5) {
            canvas.drawText("" + (160 + moveX / 60), centerX + moveX, top - 100, paint);
            currentIndex = 160 + moveX / 60;//todo
        } else if (moveX == 0) {
            canvas.drawText("" + (160), centerX + moveX, top - 100, paint);
            currentIndex = 160;//todo
        } else if (moveX > 4200) {
            canvas.drawText("" + (230), centerX + moveX, top - 100, paint);
            currentIndex = 230;//todo
        } else if (moveX < -3600) {
            canvas.drawText("" + (100), centerX + moveX, top - 100, paint);
            currentIndex = 100;//todo
        }

    }

    private static final String TAG = "MyView";

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                isRightPosition = false;
                upType = 0;

                break;
            case MotionEvent.ACTION_UP:
                if (upType == 1) {
                    if (moveX > 4200) {
                        scroller.startScroll(getScrollX(), getScrollY(), 4200 - moveX, 0);
                    } else if (moveX < -3600) {
                        scroller.startScroll(getScrollX(), getScrollY(), -3600 - moveX, 0);
                    } else {
                        goToNumberPosition();
                    }


                    invalidate();
                }

                break;
        }

        return true;
    }

    public void goToNumberPosition() {
        if (Math.abs(moveX) / 60f - Math.abs(moveX) / 60 >= 0.5) {//向右滑动
            int dx = 60 - (Math.abs(moveX) % 60);
            if (moveX >= 0) {
                scroller.startScroll(getScrollX(), getScrollY(), dx, 0);
            } else {

                scroller.startScroll(getScrollX(), getScrollY(), -dx, 0);
            }

            invalidate();
        } else {
            int dx = -(Math.abs(moveX) % 60);
            if (moveX >= 0) {
                scroller.startScroll(getScrollX(), getScrollY(), dx, 0);
            } else {
                scroller.startScroll(getScrollX(), getScrollY(), -dx, 0);
            }
        }
    }

    private boolean isRightPosition = false;

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            moveX = scroller.getCurrX();
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        } else {
            if (upType == 2 && scroller.isFinished() && !isRightPosition) {
                isRightPosition = true;
                goToNumberPosition();
                postInvalidate();
            }
        }

    }

    public int getCurrentIndex() {
        return currentIndex;
    }
}

