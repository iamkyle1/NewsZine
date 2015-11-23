package com.dylee.newszine.util;

/**
 * Created by 1000128 on 15. 10. 4..
 */

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dylee.newszine.logging.Qlog;

/**
 * Created by TheLittleNaruto on 21-07-2015 at 16:51
 */
public class VerticalScrollingViewpager extends ViewPager {
    private static final String TAG = VerticalScrollingViewpager.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;
    protected int[] height = {0, 0}, width = {0, 0};


    public VerticalScrollingViewpager(Context context) {
        super(context);
    }

    public VerticalScrollingViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

//        int[] height = {0, 0};

//        for (int i = 0; i < getChildCount(); i++) {
        for (int i = 0; i < 2; i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            height[i] = child.getMeasuredHeight();
            width[i] = child.getMeasuredWidth();

            if (DEBUG) {
                Qlog.i(TAG, " childCount " + getChildCount() + ",child= " + i + " , height =   " + height[i]);
            }
        }
//        if (getChildAt(0) == (GridView) findViewById(R.id.grid_view)) {
////   heightMeasureSpec = MeasureSpec.makeMeasureSpec(height , MeasureSpec.EXACTLY);
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * 10, MeasureSpec.EXACTLY);
//        }else{
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * 2, MeasureSpec.EXACTLY);
//        }
//        if (DEBUG) {
//            Qlog.i(TAG, " current Item " + this.getCurrentItem() + "height = " + height);
//        }
        if (this.getCurrentItem() == 0) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * 10, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height[0] * 10, MeasureSpec.EXACTLY);
        } else {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height * 2, MeasureSpec.EXACTLY);
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height[1], MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (DEBUG) {
            Qlog.i(TAG, " onLayout " + l + "  " + t + "  " + r + "   " + b);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (DEBUG) {
            Qlog.i(TAG, " onDraw POS = " + this.getCurrentItem());
        }
//        int heightMeasureSpec=0;
//        int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width[0], MeasureSpec.EXACTLY);
//        if (this.getCurrentItem() == 0) {
//            heightMeasureSpec= MeasureSpec.makeMeasureSpec(height[0]*10, MeasureSpec.EXACTLY);
//            if (DEBUG) {
//                Qlog.i(TAG, " onDraw POS1 height=" + height[0]);
//            }
//        } else {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height[1], MeasureSpec.EXACTLY);
//            if (DEBUG) {
//                Qlog.i(TAG, " onDraw POS2 height= " + height[1]);
//            }
//        }
//        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onDraw(canvas);

    }
}

