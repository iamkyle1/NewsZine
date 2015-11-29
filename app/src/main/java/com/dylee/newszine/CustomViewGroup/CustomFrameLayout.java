package com.dylee.newszine.CustomViewGroup;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by 1000128 on 15. 11. 3..
 */
public class CustomFrameLayout extends FrameLayout {

    private float initialTranslationPosition;
    private boolean firstCalled = true;
    Toolbar mToolbar;
    CustomWebview mCutomWebview;
    float inintialHeight = 0;

    float lastY = 0, lastX = 0;
    float curX = 0, curY = 0;

    public CustomFrameLayout(Context context) {
        super(context);
        initValue();
    }

    public CustomFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValue();

    }

    public CustomFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValue();
    }

    public void initValue() {
        initialTranslationPosition = 192;
        mToolbar = (Toolbar) this.getChildAt(1);
        mCutomWebview = (CustomWebview) this.getChildAt(0);
//        initialTranslationPosition = this.getChildAt(0).getTranslationY();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//      return true;
        return super.dispatchTouchEvent(ev);
    }


    public void setActionBarHeight(int height) {

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        Log.i("layout", " " + left + " " + top);
//        Log.i("child",this.getChildAt(1).onLa)
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("Touch event", "View Group Touch Event");
//      return true;
        return super.onTouchEvent(event);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        float yfinalDistance = 0;


        float xDistance, yDistance;

        mToolbar = (Toolbar) this.getChildAt(1);
        mCutomWebview = (CustomWebview) this.getChildAt(0);
//        Log.i("component", " component   " + mCutomWebview + "  "+mToolbar);

        if (firstCalled) {
            inintialHeight = mCutomWebview.getTranslationY();
//            Log.i("inintialHeight", " inintialHeight   " + inintialHeight);
            firstCalled = false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = yfinalDistance=0f;
                lastX = ev.getX();
                lastY = ev.getY();

                break;
            case MotionEvent.ACTION_MOVE:
                curX = ev.getX();
                curY = ev.getY();
                yDistance = curY - lastY;
//                Log.i("CURXY ", "CUR Y= " + curY + "  Last Y=" + lastY + " yDistance=" + yDistance);

                lastX = curX;
                lastY = curY;

//                Log.i("gtY ", "getTY " + mToolbar.getTranslationY());
                if (mToolbar.getTranslationY() <= 0 & mToolbar.getTranslationY() >= -inintialHeight) {
                    if (mToolbar.getTranslationY() + yDistance >= -inintialHeight && mToolbar.getTranslationY() + yDistance <= 0) {
                        mToolbar.setTranslationY(mToolbar.getTranslationY() + yDistance);
                        mCutomWebview.setTranslationY(mCutomWebview.getTranslationY() + yDistance);
//                        mToolbar.scrollBy(1,3);
                        return false;
                    } else if (mToolbar.getTranslationY() + yDistance > 0) {
                        mToolbar.setTranslationY(Math.min(0, mToolbar.getTranslationY() + yDistance));
                        mCutomWebview.setTranslationY(Math.min(inintialHeight, mCutomWebview.getTranslationY() + yDistance));
                        return false;
                    } else if (mToolbar.getTranslationY() + yDistance < -inintialHeight) {
                        mToolbar.setTranslationY(Math.max(-inintialHeight, mToolbar.getTranslationY() + yDistance));
                        mCutomWebview.setTranslationY(Math.max(0, mCutomWebview.getTranslationY() + yDistance));
//                        mToolbar.scrollTo(700,700);
                        return false;
                    }
                }
//                return false;

//                yfinalDistance = yfinalDistance + yDistance;
//                Log.i(plsu", " yDistance   " + yDistance);
//                Log.i("Object ", "getTransY=" + this.getChildAt(1).getTranslationY());
//                this.getChildAt(1).setTranslationY(40);


//                if (mToolbar.getTranslationY() <= 0 & mToolbar.getTranslationY() >= -inintialHeight) {
//                    if (mToolbar.getTranslationY() + yDistance < -inintialHeight) {
//                        mToolbar.setTranslationY(-inintialHeight);
//                        Log.i("inside1", " yDistance   " + yDistance);
//                        return false;
////                        this.setTranslationY(0);
//                    } else if (mToolbar.getTranslationY() + yDistance > 0) {
//                        Log.i("inside2", " Position Y="+ this.getChildAt(1).getTranslationY() + "  Distance =  "+yDistance );
//                        mToolbar.setTranslationY(0);
//                        return false;
////                        this.setTranslationY(inintialHeight);
//                    } else {
//                        mToolbar.setTranslationY(mToolbar.getTranslationY() + yDistance);
////                        this.setTranslationY(this.getTranslationY() + yfinalDistance);
//                        Log.i("inside3", " yDistance   " + yDistance);
//                        return true;
//                    }
//                }
//                return true;
                break;
//                return super.onTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                float UplastX = ev.getX();
                float uplastY = ev.getY();
//                Log.i("Webview ", "UPX=" + UplastX + "  UPY=" + uplastY);
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        final float x = ev.getX();
//        final float y = ev.getY();
//
////        Log.i("onthouchevent 11", "touchX=" + x + "  TouchY=" + y);
//
//        if (firstCalled) {
//            initialTranslationPosition = this.getChildAt(0).getTranslationY();
//            firstCalled = false;
//        }
//
////        Log.i("top", " " + this.getTop() + " " + this.getChildAt(0).getTranslationY());
////        final View cwb = this.getChildAt(0);
//
////        cwb.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
////            @Override
////            public boolean onPreDraw() {
////                cwb.getViewTreeObserver().removeOnPreDrawListener(this);
//////                Log.v("TAGTAG", "Top : " + cwb.getTop());
//////                Log.v("TAGTAG", "Left : " + cwb.getLeft());
////                return false;
////            }
////        });
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                xDistance = yDistance = 0f;
//                lastX = ev.getX();
//                lastY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                final float curX = ev.getX();
//                final float curY = ev.getY();
//                xDistance += Math.abs(curX - lastX);
//                yDistance = curY - lastY;
//
//                Log.i("move last x,y ", "lastX=" + lastX + "  lastY=" + lastY );
////                Log.i("move x,y ", "X=" + curX + "  Y=" + curY + " YDistance=" + yDistance);
//
////                Log.i("yDistance", " " + yDistance + " TY" + this.getChildAt(0).getTranslationY());
////                lastX = curX;
////                lastY = curY;
//                if ((this.getChildAt(0).getTranslationY() + yDistance) > 0 &
//                        (this.getChildAt(0).getTranslationY() + yDistance) < initialTranslationPosition
//                        ) {
////                    if (Math.abs(yDistance) >= initialTranslationPosition) {
////                        yDistance = initialTranslationPosition;
////                    }
////                    this.getChildAt(0).setTranslationY(-yDistance);
////                    Log.i("inside Move", "this.getChildAt(1).TransY " + this.getChildAt(1).getTranslationY() + "   yDistance " + yDistance);
////                    this.getChildAt(1).setTranslationY(this.getChildAt(1).getTranslationY() + yDistance);
////                    return true;
//
//                }
//                return false;
//
//            case MotionEvent.ACTION_UP:
//                Log.i("UP CUrrent X,Y", "curX=" + ev.getX() + "  curY=" + ev.getY());
//
//                break;
//        }

//        return false;
//        return super.onInterceptTouchEvent(ev);
//    }

}