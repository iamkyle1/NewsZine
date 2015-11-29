package com.dylee.newszine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.dylee.newszine.R;

public class ParallaxWebviewActivity extends Activity implements View.OnClickListener {

    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    public com.dylee.newszine.CustomViewGroup.CustomWebview webView;
    Toolbar mToolBar;
    private TypedValue mTypedValue = new TypedValue();
    View mTrickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.parallax_webview_layout);
//
        mToolBar = (Toolbar) findViewById(R.id.toolbar_in_parallax);
        webView = (com.dylee.newszine.CustomViewGroup.CustomWebview) findViewById(R.id.webview_in_parallax);
        TextView title = (TextView) findViewById(R.id.sTitle);
        ImageView exit_search = (ImageView) findViewById(R.id.exit_btn);
        exit_search.setOnClickListener(this);
        ImageView search_btn = (ImageView) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);

//        webView.setHeaderComponent(mToolBar);

        //웹뷰를 밑으로 내림
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
        int mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

//        mToolBar.setTranslationY(500);
        webView.setTranslationY(mActionBarHeight);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH)
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
//                    PrallaxListView.smoothScrollToPosition(0);
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    String urlString = webView.getUrl().toString();
                }
            });

        Intent intent = getIntent();
        String uris = intent.getStringExtra("uris");

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.loadUrl(uris);


//        webView.setOnScrollChangeListener(new WebView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
////                Log.i("webview New ", "X= " + scrollX + "  Y+  " + scrollY);
////                Log.i("Webview OLD", "X= " + oldScrollX + "  Y+  " + oldScrollY);
//////
//            }
////
//        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mid_back_key);
        fab.setOnClickListener(this);
        fab.setAlpha((float) 0.4);

//        android.graphics.drawable.Drawable d = this.getDrawable(R.drawable.back_btn);
        fab.setImageResource(R.drawable.arrow_left_bold);
        fab.setHovered(true);

        title.setText(intent.getStringExtra("title"));
    }

    public int getScrollY(AbsListView view) {
        View c = view.getChildAt(0);
        if (c == null) {
            return 0;
        }
        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();
        int height = mToolBar.getHeight();

//        Log.d("Get top 1= ", Integer.toString(c.getTop()));
//        Log.d("Get top position = ", " " + view.getFirstVisiblePosition());
        int headerHeight = 0;
        if (firstVisiblePosition >= 1) {
            return -height;
        }

//        Log.d("Get top total = ", " " + top);
        return top;
    }


    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public void onBackPressed() {
        // 누적된 history를 저장할 변수
        WebBackForwardList list = webView.copyBackForwardList();

        if (list.getCurrentIndex() <= 0 && !webView.canGoBack()) {
            // 처음 들어온 페이지이거나, history가 없는경우
            super.onBackPressed();
        } else {
            // history가 있는 경우
            // 현재 페이지로 부터 history 수 만큼 뒷 페이지로 이동
            webView.goBackOrForward(-(list.getCurrentIndex()));
            // history 삭제
            webView.clearHistory();
        }
    }


    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.search_btn:
                Intent intent = new Intent(this, NewsSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    this.startActivity(intent);
                } catch (Exception e) {
//                    Log.i("  exception", " :" + e);
                }
                break;
            case R.id.mid_back_key:
                WebBackForwardList list = webView.copyBackForwardList();
                if (list.getCurrentIndex() <= 0 && !webView.canGoBack()) {
                    // 처음 들어온 페이지이거나, history가 없는경우
                    super.onBackPressed();
//                webView.goBackOrForward(1);
                } else {
                    // history가 있는 경우
                    // 현재 페이지로 부터 history 수 만큼 뒷 페이지로 이동
                    webView.goBackOrForward(-(list.getCurrentIndex()));
                    // history 삭제
                    webView.clearHistory();
                }
                break;
            case R.id.exit_btn:
                finish();
                break;
        }
    }


}