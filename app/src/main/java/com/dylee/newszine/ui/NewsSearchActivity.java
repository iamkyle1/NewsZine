package com.dylee.newszine.ui;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dylee.newszine.CustomViewGroup.AnimatedListView;
import com.dylee.newszine.R;
import com.dylee.newszine.adapter.NewsAdapter;
import com.dylee.newszine.object.NaverNews;
import com.dylee.newszine.parser.XMLPullNewsFeedParser;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by 1000128 on 15. 10. 25..
 */

public class NewsSearchActivity extends Activity implements View.OnClickListener {

    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    final String API_KEY = "e00dd87dbc6fa7a2fc7ac62fce87aa43";
    String ENDPOINT = "http://openapi.naver.com/search";
    private ArrayList<NaverNews> mItems = null;
    String newsUrl, query;
    public static Context mContext;
    private String feedURL;
    private AnimatedListView mListView;
    boolean lastitemVisibleFlag = false;        //화면에 리스트의 마지막 아이템이 보여지는지 체크
    private int nPage = 0;
    private int nCountPerSearch = 40;
    private int nCurrentIndex = 1;
    private EditText searchText;
    NewsAdapter news_adapter;
    InputMethodManager imm;
    AlertDialog.Builder builder;
    String search_string;
    Toolbar mToolBar;
    private TypedValue mTypedValue = new TypedValue();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_search);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mListView = (AnimatedListView) findViewById(R.id.listView);

        ImageView exit_search = (ImageView) findViewById(R.id.exit_search_btn);
        ImageView start_search = (ImageView) findViewById(R.id.start_search_btn);
        searchText = (EditText) findViewById(R.id.search_text);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View placeHolderView = (View) inflater.inflate(R.layout.view_header_placeholder, mListView, false);

        View FL = (View) new View(this);
        getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);

//        float height=getActionBar().getHeight();

        int mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());


        AbsListView.LayoutParams rlp = new AbsListView.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
////        rlp.setMargins(0, 55, 0, 0);
        rlp.height = mActionBarHeight;

        FL.setLayoutParams(rlp);
//

//
//        mListView.addHeaderView(placeHolderView, null, false);
        mListView.addHeaderView(FL, null, false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.botton_right_up_scroll_key);
        fab.setOnClickListener(this);
        fab.setAlpha((float) 0.6);
        fab.setImageResource(R.drawable.arrow_up);
        fab.setHovered(true);


//        View v = View.inflate(getApplicationContext(), R.layout.view_header_placeholder, mListView);
//        View v = getLayoutInflater().inflate(R.layout.view_header_placeholder,mListView);
//        mListView.addHeaderView(v);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
                int scrollY = getScrollY(mListView);
                mToolBar.setTranslationY(scrollY);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag) {
                    //TODO 화면이 바닦에 닿을때 처리
                    nPage = nPage + 1;
                    nCurrentIndex = nPage * nCountPerSearch + 1;
                    start_search(nCurrentIndex, nCountPerSearch);
                }
            }
        });

        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
//                    nPage = nPage+1;
//                    nCurrentIndex = nPage * nCountPerSearch + 1;
                    search_string = searchText.getText().toString().trim();
                    start_search(nCurrentIndex, nCountPerSearch);
                    imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });
        exit_search.setOnClickListener(this);
        start_search.setOnClickListener(this);
        setupAds();
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


    public void start_search(int nCurrentIndex, int nCountPerSearch) {
        /*
        key	string (필수)	이용 등록을 통해 받은 key 스트링을 입력합니다.
        target	string (필수) : news	서비스를 위해서는 무조건 지정해야 합니다.
        query	string (필수)	검색을 원하는 질의, UTF-8 인코딩 입니다.
        display	integer : 기본값 10, 최대 100	검색결과 출력건수를 지정합니다. 최대 100까지 가능합니다.
        start	integer : 기본값 1, 최대 1000	검색의 시작위치를 지정할 수 있습니다. 최대 1000까지 가능합니다.
        sort	string : date (기본값), sim	정렬 옵션입니다.
        date : 날짜순(기본값)
        sim : 유사도순
          */


        if (search_string.length() < 2) {
            Toast.makeText(getApplicationContext(), "2글자 이상을 넣어 주세요 ", Toast.LENGTH_SHORT).show();
            return;
        } else {
            feedURL = Uri.parse(ENDPOINT).buildUpon().appendQueryParameter("key", API_KEY).appendQueryParameter(
                    "target", "news").appendQueryParameter("start", String.valueOf(nCurrentIndex))
                    .
                            appendQueryParameter("display", String.valueOf(nCountPerSearch)).appendQueryParameter("sort", "date").appendQueryParameter("query", search_string).build().toString();
            new FetchltemsTask().execute(feedURL);
        }
    }

    private class FetchltemsTask extends AsyncTask<String, Void, ArrayList<NaverNews>> {
        @Override
        protected ArrayList<NaverNews> doInBackground(String... params) {
//            Log.i("intext fetch", "L is " + params[0]);
            return new XMLPullNewsFeedParser(params[0]).fetchItems();
        }

        @Override
        protected void onPostExecute(ArrayList<NaverNews> items) {
//            mItems = items;
//            Log.i("intext", "fetch size " + mItems.get(1).getOriginalLink()+" Link : "+mItems.get(1).getLink());
//            if (items.size() == 0) {
//                if (news_adapter != null) news_adapter.clear();
            setupAdapter(items);
//                 Toast.makeText(getApplicationContext(), "데이타가 없습니다. ", Toast.LENGTH_SHORT).show();
//            } else {
//                setupAdapter(items);
//            }
        }
    }

    void setupAdapter(ArrayList<NaverNews> items) {
//        if (getActivity() == null || mListView == null) {
//            Log.e("adapter Error", "error");
//            return;
//        }

//
//        for (int i = 0; i < items.size(); i++)
//            mItems.add(items.get(i));

        if (items.size() == 0 && nPage == 0) {
            if (news_adapter != null) news_adapter.clear();
            builder.setTitle("확인 대화 상자")        // 제목 설정
                    .setMessage("검색된 뉴스가 없습니다.")        // 메세지 설정
                    .setCancelable(false)        // 뒤로 버튼 클릭시 취소 가능 설정
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        // 확인 버튼 클릭시 설정
                        public void onClick(DialogInterface dialog, int whichButton) {

                        }
                    });
            AlertDialog dialog = builder.create();    // 알림창 객체 생성
            dialog.show();    // 알림창 띄우기

        } else if (items.size() != 0 & nPage == 0) {

            news_adapter = new NewsAdapter(this, R.layout.news_listview, items);
            mListView.setAdapter(news_adapter);

        } else if (items.size() != 0) {
            news_adapter.addItem(items);
        }

        mListView.setOnItemClickListener(new
                        ListViewItemClickListener()
        );

//        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                // TODO Auto-generated method stub
//            }
//
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                // TODO Auto-generated method stub
//                final ListView lw = getListView();
//
//                if (scrollState == 0)
//                    Log.i("a", "scrolling stopped...");
//
//
//                if (view.getId() == lw.getId()) {
//                    final int currentFirstVisibleItem = lw.getFirstVisiblePosition();
//                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
//                        mIsScrollingUp = false;
//                        Log.i("a", "scrolling down...");
//                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
//                        mIsScrollingUp = true;
//                        Log.i("a", "scrolling up...");
//                    }
//
//                    mLastFirstVisibleItem = currentFirstVisibleItem;
//                }
//            }
//        });

    }

    private class ListViewItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 0) {
                return;
            }
            NaverNews NNS = news_adapter.getItem(position - 1);
//            Log.i("position", "positon " + position + "Clicked ");
            URL urls = NNS.getOriginalLink();
            Intent intent = new Intent(getApplicationContext(), ParallaxWebviewActivity.class);
            intent.putExtra("uris", urls.toExternalForm());
            intent.putExtra("title", NNS.getTitle().substring(0, 18));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
//            Log.i("position ", "VALE " + "Clicked ");
        }
    }

    public void onClick(View view) {
//        WebBackForwardList list = webView.copyBackForwardList();

        switch (view.getId()) {
            case R.id.botton_right_up_scroll_key:
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
                mListView.smoothScrollToPosition(0);
                break;
            case R.id.start_search_btn:
                search_string = searchText.getText().toString().trim();
//                if (news_adapter !=null & news_adapter.getSize() > 0)
//                    news_adapter.clear();
                start_search(nCurrentIndex, nCountPerSearch);
                break;
            case R.id.exit_search_btn:
                finish();
                break;


        }
    }

    private void setupAds() {
//        Google Admob
        AdView mAdView = (AdView) findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//
//        AdView mAdView2 = (AdView) findViewById(R.id.adView2);
//        AdRequest adRequest2 = new AdRequest.Builder().build();
//        mAdView2.loadAd(adRequest2);

    }


}


/*
class SearchTextChangedListener implements TextWatcher {
    Context mContext;

    public SearchTextChangedListener(Context context) {
        mContext = context;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(s)) {
            mTopBar.setShowSearchWordClose(false);
            hideKeyWord();
            return;
        }

        try {
            mKeyword = s.toString();

            if(DEBUG)
                Qlog.e(TAG, "mKeyword : " + mKeyword);

            if (mKeyword == null || mKeyword.length() == 0) {
                mTopBar.setShowSearchWordClose(false);
                hideKeyWord();
            }
            else if (mKeyword.length() > 0) {
                mTopBar.setShowSearchWordClose(true);
                showKeyWord();
            }

            if (mHistoryDataList != null) {
                mHistoryDataList.clear();
            }
            mHistoryDataList = SearchData.getItems(mKeyword);

            getKeywordResult();
        } catch (NullPointerException e) {
            if (DEBUG) Qlog.e(TAG, e);
        } catch (Exception e) {
            if (DEBUG) Qlog.e(TAG, e);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}

*/