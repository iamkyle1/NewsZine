package com.dylee.newszine.CustomViewGroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;

import com.dylee.newszine.logging.Qlog;
import com.dylee.newszine.ui.NewsMain;

/**
 * Created by 1000128 on 15. 11. 23..
 */
public class AnimatedListView extends android.widget.ListView {
    private static final String TAG = AnimatedListView.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;


    ListAdapter newsListAdapter;

    private OnScrollListener onScrollListener;
    private OnDetectScrollListener onDetectScrollListener;

    public AnimatedListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public AnimatedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    AnimatedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        this.newsListAdapter = adapter;
        setListeners();

        super.setAdapter(adapter);

        this.setOnDetectScrollListener(new OnDetectScrollListener() {
            @Override
            public void onUpScrolling() {
                if (DEBUG) Qlog.i(TAG, "on scroll up");
        /* do something */
            }

            @Override
            public void onDownScrolling() {
        /* do something */
                if (DEBUG) Qlog.i(TAG, "on scroll down");
            }
        });
    }

    @SuppressWarnings("UnusedParameters")
    private void onCreate(Context context, AttributeSet attrs, Integer defStyle) {
//        setListeners();

    }

    private void setListeners() {
        super.setOnScrollListener(new OnScrollListener() {

            private int oldTop;
            private int oldFirstVisibleItem;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (onScrollListener != null) {
//                    if(DEBUG) Qlog.i(TAG, "on scroll state");
                    onScrollListener.onScrollStateChanged(view, scrollState);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (onScrollListener != null) {
//                    if(DEBUG) Qlog.i(TAG, "on scroll ");
                    onScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
                }

                if (onDetectScrollListener != null) {
                    onDetectedListScroll(view, firstVisibleItem);
                }
            }

            private void onDetectedListScroll(AbsListView absListView, int firstVisibleItem) {
                View view = absListView.getChildAt(0);
                int top = (view == null) ? 0 : view.getTop();

                if (firstVisibleItem == oldFirstVisibleItem) {
                    if (top > oldTop) {
//                        if(DEBUG) Qlog.i(TAG, "on scroll up in ");
                        onDetectScrollListener.onUpScrolling();
                    } else if (top < oldTop) {
                        onDetectScrollListener.onDownScrolling();
                    }
                } else {
                    if (firstVisibleItem < oldFirstVisibleItem) {
//                        if(DEBUG) Qlog.i(TAG, "on scroll down in  ");
                        onDetectScrollListener.onUpScrolling();
                    } else {
                        onDetectScrollListener.onDownScrolling();
                    }
                }

                oldTop = top;
                oldFirstVisibleItem = firstVisibleItem;
            }
        });
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public void setOnDetectScrollListener(OnDetectScrollListener onDetectScrollListener) {
        this.onDetectScrollListener = onDetectScrollListener;
    }

    public interface OnDetectScrollListener {

        void onUpScrolling();

        void onDownScrolling();
    }
}
