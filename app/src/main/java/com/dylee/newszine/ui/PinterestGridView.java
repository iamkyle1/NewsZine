package com.dylee.newszine.ui;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by 1000128 on 15. 11. 6..
 */
public class PinterestGridView extends LinearLayout {

    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    private ListAdapter m_Adapter;
    private OnClickListener onClickListener = null;
    private LinearLayout m_Line1;
    private LinearLayout m_Line2;
    private LinearLayout m_Line3;
    private Context mContext;
    ArrayAdapter<String> padapter;

    public PinterestGridView(Context context) {
        super(context);
        mContext = context;
        // TODO Auto-generated constructor stub
        InitLine();
    }

    public PinterestGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        InitLine();
    }

    private void InitLine() {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lp.weight = 1;

        // line2
        m_Line1 = new LinearLayout(this.getContext());
        m_Line1.setOrientation(VERTICAL);
        m_Line1.setLayoutParams(lp);

        // line2
        m_Line2 = new LinearLayout(this.getContext());
        m_Line2.setOrientation(VERTICAL);
        m_Line2.setLayoutParams(lp);

        // line3
        m_Line3 = new LinearLayout(this.getContext());
        m_Line3.setOrientation(VERTICAL);
        m_Line3.setLayoutParams(lp);

        addView(m_Line1);
        addView(m_Line2);
        addView(m_Line3);
        setAdapter();
    }

    public ListAdapter getAdapter() {
        return m_Adapter;
    }

    private void BindLayout() {
        int count = m_Adapter.getCount();
        for (int i = 0; i < count; i++) {
            View v = m_Adapter.getView(i, null, null);
            v.setOnClickListener(this.onClickListener);
            if (i == 0 || i % 3 == 0)
                m_Line1.addView(v);

            if (i == 1 || i % 3 == 1)
                m_Line2.addView(v);

            if (i == 2 || i % 3 == 2)
                m_Line3.addView(v);
        }
//        Log.v("countTAG", "" + count);
    }

    private void AddItem() {

    }

    public void setAdapter() {

        String[] values = new String[]{"Android List View",
                "Adapter implementation",
                "Simple List View In Android",
                "Create List View Android",
                "Android Example",
                "List View Source Code",
                "List View Array Adapter",
                "Android Example List View"
        };

        padapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, values);

        this.m_Adapter = padapter;

        BindLayout();
    }

    public OnClickListener getOnclickListner() {
        return onClickListener;
    }

    public void setOnclickLinstener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}