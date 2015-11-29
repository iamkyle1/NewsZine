package com.dylee.newszine.ui;

/**
 * Created by 1000128 on 15. 10. 4..
 */

//2015.11.7 에 ParallaxWebview Activity를 사용함에 따라 더이상 사용하지 않음.

//여기서는 Listview위에 addheaderview를 넣어서 toolbar를 사라지게 하는 effect를 tkdydgka.

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebBackForwardList;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dylee.newszine.R;

import java.util.ArrayList;
import java.util.List;

public class WebviewActivity extends Activity implements View.OnClickListener {
    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    public WebView webView;
    Toolbar mToolBar;
    private TypedValue mTypedValue = new TypedValue();
    ArrayList<View> mMainWebviewArray;
    private ViewAdapter mAdapter;
    ListView PrallaxListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_webview_activity);

//        list_view_for_webview
        PrallaxListView = (ListView) findViewById(R.id.list_view_for_webview);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View mainWebview = (View) inflater.inflate(R.layout.layout_webview, PrallaxListView, false);
//        webView = (WebView) mainWebview.findViewById(R.id.gnb_webview);
//
        mMainWebviewArray = new ArrayList<View>(1);
        mMainWebviewArray.add(mainWebview);
//        mMainWebviewArray.add(webView);

        mAdapter = new ViewAdapter(getApplicationContext(), R.layout.layout_webview, mMainWebviewArray);
//
        PrallaxListView.setAdapter(mAdapter);


        View FL = (View) new View(this);
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);

        int mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

        AbsListView.LayoutParams rlp = new AbsListView.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);

        rlp.height = mActionBarHeight - 2;


        FL.setLayoutParams(rlp);
        PrallaxListView.addHeaderView(FL, null, false);

        webView = (WebView) mainWebview.findViewById(R.id.gnb_webview);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        WebSettings settings = webView.getSettings();

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        ImageView exit_search = (ImageView) findViewById(R.id.exit_btn);
        exit_search.setOnClickListener(this);
        ImageView search_btn = (ImageView) findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);

        TextView title = (TextView) findViewById(R.id.sTitle);

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

//        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
//        final int mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());
//
//        AbsListView.LayoutParams rlp = new AbsListView.LayoutParams(
//                FrameLayout.LayoutParams.MATCH_PARENT,
//                FrameLayout.LayoutParams.WRAP_CONTENT);
//////        rlp.setMargins(0, 55, 0, 0);
//        rlp.height = mActionBarHeight;
//        View FL = (View) new View(this);
//        FL.setLayoutParams(rlp);


        webView.loadUrl(uris);


        PrallaxListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
//                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                int scrollY = getScrollY(PrallaxListView);
                mToolBar.setTranslationY(scrollY);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                //TODO 화면이 바닦에 닿을때 처리
//                    nPage = nPage + 1;
//                    nCurrentIndex = nPage * nCountPerSearch + 1;
//                    start_search(nCurrentIndex, nCountPerSearch);
//                }
            }
        });
//        webView.addView(FL);
//
//        webView.setOnScrollChangeListener(new WebView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                Log.i("webview New ", "X= " + scrollX + "  Y+  " + scrollY);
//                Log.i("Webview OLD", "X= " + oldScrollX + "  Y+  " + oldScrollY);
////                if (mActionBarHeight >= scrollY) {
////                    mToolBar.setTranslationY(-scrollY);
////                    webView.setTranslationY(-scrollY);
////                    mToolBar.setAlpha(-scrollY);
////                    webView.setTranslationY(100);
////                    webView.setOverScrollMode(1);
////                }
//            }
//
//        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.mid_back_key);
        fab.setOnClickListener(this);
        fab.setAlpha((float) 0.5);
        android.graphics.drawable.Drawable d = getDrawable(R.drawable.back_btn);
        fab.setBackgroundDrawable(d);
        fab.setHovered(true);

        title.setText(intent.getStringExtra("title"));

//        //Google Admob
//        AdView mAdView = (AdView) findViewById(R.id.adView2);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
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
//        WebBackForwardList list = webView.copyBackForwardList();

        switch (view.getId()) {
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
            case R.id.botton_right_up_scroll_key:
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.search_btn:
                Intent intent = new Intent(this, NewsSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    this.startActivity(intent);
                } catch (Exception e) {
//                    Log.i("  exception", " :" + e);
                }
                break;
            case R.id.exit_btn:
                finish();
                break;
        }
    }


    static class ViewAdapter extends ArrayAdapter<View> {
        private List<View> _views;
        private Context _context;

        public ViewAdapter(Context context, int resource, List<View> objects) {
            super(context, resource, objects);
            this._views = objects;
            this._context = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return _views.get(position);
        }

    }
}

