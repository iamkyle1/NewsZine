package com.dylee.newszine;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.dylee.newszine.adapter.FeedGridAdapter;
import com.dylee.newszine.object.NewsObject;

import java.util.ArrayList;

//import com.skplanet.ocb.NewsMainUIFragment.ClassObject.newsObject;
//import com.skplanet.ocb.ui.adapter.FeedAdapter;
//import com.skplanet.ocb.ui.adapter.FeedGridAdapter;
//import com.skplanet.ocb.ui.adapter.ImageDL;

/**
 * Created by 1000128 on 15. 10. 4..
 */

/**
 * A placeholder fragment containing a simple view.
 */

public class NewsListFragment extends android.support.v4.app.Fragment {

    private static final String TAG = NewsListFragment.class.getSimpleName();
    public static final boolean DEBUG = Boolean.parseBoolean("true");

    private String mURL;
    private Context mContext;
    private GridView mMainFeedListView;
    private FeedGridAdapter mFeedGridAdapter;
    GridView GV;

    public NewsListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_fragment,
                container, false);

        GV = (GridView) rootView.findViewById(R.id.grid_view);
        mFeedGridAdapter = new FeedGridAdapter(getActivity());
        GV.setAdapter(mFeedGridAdapter);
        addNewURLList();

        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void addNewURLList() {
        ArrayList<NewsObject> newsObjectList = new ArrayList<>();
        String sLandingURL[] = {"http://m.media.daum.net/media/"
                , "http://m.donga.com/"
                , "http://m.hani.co.kr/"
                , "http://mnews.joinsmsn.com/"
                , "http://m.yonhapnews.co.kr/mob2/kr/main_kr.jsp"
                , "http://m.khan.co.kr"
                , "http://m.ytn.co.kr"
                , "http://m.mt.co.kr"
                , "http://m.news.nate.com"
                , "http://m.news.naver.com/home.nhn"
                , "http://m.nocutnews.co.kr/"
                , "http://m.zdnet.co.kr/"

                , "http://m.metroseoul.co.kr"
                , "http://m.etnews.co.kr"
                , "http://www.dispatch.co.kr/"
                , "http://m.sportsseoul.com/"
                , "http://m.kr.wsj.com"
                , "http://www.newstapa.com/"
                , "http://m.koreaherald.com/"
                , "http://m.ohmynews.com"

        };

        int[] iDrawable = {
                R.drawable.news1_daum
                , R.drawable.news1_donga
                , R.drawable.news1_han
                , R.drawable.news1_ja
                , R.drawable.news1_yoenhap
                , R.drawable.news1_kh
                , R.drawable.news1_ytn
                , R.drawable.news1_money
                , R.drawable.news1_nate
                , R.drawable.news1_naver
                , R.drawable.news1_nocut
                , R.drawable.news1_zdnet
                , R.drawable.news1_metro
                , R.drawable.news1_etnews
                , R.drawable.news1_dispatch
                , R.drawable.news1_sports
                , R.drawable.news1_wsj
                , R.drawable.news1_newstapa
                , R.drawable.news1_koreaherald
                , R.drawable.news1_ohmynews
        };

        String[] sTitle = {
                "다 음 "
                , "동 아"
                , "한 겨 레 "
                , "중 앙 일 보"
                , "연 합 뉴 스"
                , "경 향 신 문"
                , "Y T N"
                , "머 니 투 데 이"
                , "네 이 트"
                , "네 이 버"
                , "노 컷 뉴 스"
                , "ZDNET"
                , "메 트 로"
                , "전 자 신 문"
                , "디 스 패 치"
                , "스 포 츠 서 울"
                , "월 스 트 리 트 저 널"
                , "뉴 스 타 파"
                , "Korea Herald"
                , "오마이 뉴스"
        };

        for (int i = 0; i < sLandingURL.length; i++) {
            newsObjectList.add((new NewsObject()).setValue(sLandingURL[i], iDrawable[i], sTitle[i]));
        }


        mFeedGridAdapter.clearAdapter();
        mFeedGridAdapter.addAll(newsObjectList);
    }

}//MainActivityFragment



