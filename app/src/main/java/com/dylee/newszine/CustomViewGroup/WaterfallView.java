package com.dylee.newszine.CustomViewGroup;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.dylee.newszine.adapter.MagazineAdapter;
import com.dylee.newszine.logging.Qlog;

/**
 * Created by 1000128 on 15. 11. 6..
 */
public class WaterfallView extends LinearLayout {

    private static final String TAG = WaterfallView.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    private OnClickListener onClickListener = null;
    private LinearLayout m_Line1;
    private LinearLayout m_Line2;
    //    private ListView m_Line1, m_Line2;
    private Context mContext;
    MagazineAdapter magazineAdapter;
    ArrayAdapter<String> padapter;


    public WaterfallView(Context context) {

        super(context);
        mContext = context;
        // TODO Auto-generated constructor stub
        InitLine();

    }

    public WaterfallView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        InitLine();
    }

    private void InitLine() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        lp.weight = 1;

        m_Line1 = new LinearLayout(this.getContext());
        m_Line1.setOrientation(VERTICAL);
        m_Line1.setGravity(Gravity.CENTER_HORIZONTAL);
        m_Line1.setLayoutParams(lp);

        // line2
        m_Line2 = new LinearLayout(this.getContext());
        m_Line2.setOrientation(VERTICAL);
        m_Line2.setGravity(Gravity.CENTER_HORIZONTAL);
        m_Line2.setLayoutParams(lp);


//
//        m_Line1 = new ListView(mContext);
//        m_Line2 = new ListView(mContext);

        addView(m_Line1);
        addView(m_Line2);


//        setAdapter();
    }

    public ListAdapter getAdapter() {
        return magazineAdapter;
    }

    public void BindLayout() {
        int count = magazineAdapter.getCount();

        if (DEBUG) {
            Qlog.i(TAG, "Bind Layout:total Count =  " + count);
        }

        for (int i = 0; i < count; i++) {
            final View v = magazineAdapter.getView(i, null, null);

            v.setOnClickListener(this.onClickListener);
            v.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            if (i % 2 == 0) {
                m_Line1.addView(v);
//                m_Line1.invalidate();
                if (DEBUG) {
                    Qlog.i(TAG, "Bind Layout: =  " + i + v.toString());
                }
                ;
            }
            if (i % 2 == 1) {
                m_Line2.addView(v);
//                m_Line2.invalidate();
                if (DEBUG) {
                    Qlog.i(TAG, "Bind Layout:t =  " + i + " " + v.toString());
                }
            }
        }
//        m_Line1.invalidate();
//        m_Line2.invalidate();
        this.requestLayout();
        this.invalidate();

    }

    private void AddItem() {
    }

    public void setAdapter(MagazineAdapter adapter) {
        this.magazineAdapter = adapter;
    }

    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}


//final ViewTreeObserver vto = v.getViewTreeObserver();
//            vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//                @Override
//                public void onGlobalLayout() {
//
//                    if (DEBUG) {
//                        Qlog.i(TAG, " height =  " + v.getHeight());
//                    }
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
////                        vto.removeOnGlobalLayoutListener(this);
//                    } else {
////                        vto.removeGlobalOnLayoutListener(this);
//                    }
//                }
//            });
