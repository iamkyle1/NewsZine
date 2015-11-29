package com.dylee.newszine.CustomViewGroup;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by 1000128 on 15. 11. 3..
 */
public class CustomWebview extends WebView {

    Toolbar mToolbar;
    float lastY = 0;
    float curY = 0;
    float curX = 0;

    public CustomWebview(Context context) {
        super(context);

    }

    // 소스상에서 생성할 때 쓰인다.
    // xml 을 통해 생성할 때 attribute 들이 attrs 로 넘어온다. 이 때 경유하는 constructor.
    // 대부분 defStyle 이 있는 3번째 constructor 를 통한다.
    public CustomWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    // xml 을 통해 생성하면서 style 도 함께 적용되어 있다면 타게 되는 constructor. defStyle = 0 이면 no style 을 의미한다.
    public CustomWebview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    private float xDistance, yDistance, lastX;

    public void setHeaderComponent(Toolbar toolbar) {

        mToolbar = toolbar;

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        Log.i("Custom Webview", "Custom Webview");
        float yfinalDistance = 0;
        boolean firstFlag = true;
        float inintialHeight = 0;

        if (firstFlag) {
            inintialHeight = this.getTranslationY();
            firstFlag = false;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
//                Log.i("Webview ", "DownX=" + lastX + "  DowY=" + lastY);
                break;
            case MotionEvent.ACTION_MOVE:
                curX = ev.getX();
                curY = ev.getY();
                yDistance = curY - lastY;

                lastX = curX;
                lastY = curY;

                yfinalDistance = yfinalDistance + yDistance;
//                Log.i("plsu", " yDistance   " + yDistance);

//                Log.i("Position ", "getTransY=" + this.getTranslationY() + " , YfinalDistance=" + yfinalDistance + " , finalTransY=" + (this.getTranslationY() + yfinalDistance));
//                if (mToolbar.getTranslationY() <= 0 & mToolbar.getTranslationY() >= -inintialHeight) {
//                    if (mToolbar.getTranslationY() + yfinalDistance < -inintialHeight) {
//                        mToolbar.setTranslationY(-inintialHeight);
////                        this.setTranslationY(0);
//                    } else if (mToolbar.getTranslationY() + yfinalDistance > 0) {
//                        mToolbar.setTranslationY(0);
////                        this.setTranslationY(inintialHeight);
//                    } else {
//                        mToolbar.setTranslationY(mToolbar.getTranslationY() + yfinalDistance);
//                        this.setTranslationY(this.getTranslationY() + yfinalDistance);
//                    }
////                    return true;
//                }
                break;
//                return super.onTouchEvent(ev);
            case MotionEvent.ACTION_UP:
                float UplastX = ev.getX();
                float uplastY = ev.getY();
//                Log.i("Webview ", "UPX=" + UplastX + "  UPY=" + uplastY);
                break;
        }
        return super.onTouchEvent(ev);
    }

}
