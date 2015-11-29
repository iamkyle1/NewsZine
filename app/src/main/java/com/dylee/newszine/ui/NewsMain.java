package com.dylee.newszine.ui;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.dylee.newszine.BuildMode;
import com.dylee.newszine.MagazineFragment;
import com.dylee.newszine.NewsListFragment;
import com.dylee.newszine.R;
import com.dylee.newszine.logging.LogAppender;
import com.dylee.newszine.logging.LogLevel;
import com.dylee.newszine.logging.Qlog;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class NewsMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    TabLayout bigMenu;
    ListView LV;
    ImageView mSearchImage, mPlaceImage;

    ImageView mCloseBtn;

    //Main Viewpager를 위한 Static 변수
    public final static int NEWSLIST_FRAGMENT = 0;
    public final static int MAGAZINE_FRAGMENT = 1;
    private int mCurrentFragmentIndex;


    private AnimationDrawable mPlaceAnimation;
    private int mPlaceAnimationRepeatCount;
    private int mPlaceAnimationDuration;
    private int mPlaceAnimationFrame;
    private Handler mPlaceHandler;
    private ViewPager mViewpager;

    private static final int MESSAGE_PLACE_ANIMATION_START = 1001;
    private static final int MESSAGE_PLACE_ANIMATION_STOP = 1002;
    private static final int MESSAGE_PLACE_ANIMATION_END = 1003;
    private CallbackManager callbackManager;


    AlarmMoneyRecever ALMreceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            Qlog.initialize(BuildMode.enable(BuildMode.BUILD_ENABLE_LOG) ?
                    (LogLevel.VERBOSE) : (LogLevel.NONE), LogAppender.LOGCAT);

        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_news_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setMonetizationAlert();
        setupToolbar();


        setupTablayout();
        setupFab();
        setSearchAnimation();
        setupAds();
        setPlaceAnimation();
        setupViewPager();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.drawer_btn);
        ab.setTitle("뉴스모아 : 종합뉴스 매거진");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View v = inflater.inflate(R.layout.nav_header_news_main, null, false);
//http://stackoverflow.com/questions/33364276/getting-error-in-existing-code-after-updating-support-repository-to-23-1-0/33365230#33365230


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.inflateHeaderView(R.layout.nav_header_news_main);

        ImageView mCloseBtn = (ImageView) header.findViewById(R.id.close_btn1);
        mCloseBtn.setOnClickListener(this);
        //Drawer의 x 버튼눌렀을 떄의 반응
//        mCloseBtn = (ImageView) v.findViewById(R.id.close_btn1);
        //////// 외 오류가 나는지 모르겠슴........
//        navigationView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//            }
//        });
    }


    public void setMonetizationAlert() {

        // Android 2.3 이상에서 사용 가능한 방식.
        long installed = 0;
        long now = System.currentTimeMillis();
        long mDaydiff;


        if (DEBUG) {
            Qlog.i(TAG, "setMonetizationAlert Started");
        }
        boolean s = DEBUG;
//        Log.i(TAG, "DEBUG " + DEBUG + " " + TAG);

        try {
            PackageManager packageManager = this.getPackageManager();
            installed = packageManager.getPackageInfo("com.dylee.newszine", 0)
                    .firstInstallTime;
            if (DEBUG) {
                Qlog.i(TAG, "installed date=" + getData(installed));
            }

        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block

            if (DEBUG) {
                Qlog.i(TAG, "installed date error=");
            }
            installed = System.currentTimeMillis();
            e.printStackTrace();
        }

        String storedAdDate = getAppPreferences4Ad(this, "adDate");
//        storedAdDate = "2015/10/19 17:27:18";
        String storedAdCount = getAppPreferences4Ad(this, "AD_NOAD");

        if (storedAdDate != "") {
            //long type date의 차이를 구하면 milisecond가 된다, 이걸 1000 / (60 * 60 * 24) 나누어 주면 일수가 나온다.
            mDaydiff = (now - getDateFromStr(storedAdDate).getTime()) / 1000 / (60 * 60 * 24);

            //설치 한지 2주가 되거나, 광고가 뜬지 2주가 지나면..... 광고를 한번 보여 준다.
            if (mDaydiff > 7) {
                if (DEBUG) {
                    Qlog.e(TAG, "AD Needed before mDayDiff = " + mDaydiff);
                }
                // AD 를 보여 주고, 상태를  NO AD로 바꾸어 준다.
                setAppPreferences4Ad(this, "adDate", getData(now));
                setADAlarm();
            }
        } else {
            mDaydiff = (now - installed) / 1000 / (60 * 60 * 24);
            if (mDaydiff > 7) {
                if (DEBUG) {
                    Qlog.e(TAG, "AD Needed before mDayDiff = " + mDaydiff);
                }
                setAppPreferences4Ad(this, "adDate", getData(now));
                setADAlarm();
            }
        }
    }

    private void setADAlarm() {
        IntentFilter filler =
                new IntentFilter("com.dylee.AlarmMoneyRecever"); //BroadcastReceiver의 action 값

        if (DEBUG) {
            Qlog.e(TAG, "setADAlarm()");
        }
        //BroadcastReceiv er 클래스
        ALMreceiver = new AlarmMoneyRecever();
        registerReceiver(ALMreceiver, filler);

        Intent Intent = new Intent("com.dylee.AlarmMoneyRecever");
        PendingIntent pIntent = PendingIntent.getBroadcast(this, 0, Intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //앱이 뜬 후 60초 후에 보여준다.
        long second = 1 * 60;
        long intervalTime = 1 * 60;

        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + second, intervalTime, pIntent);

    }

    public Date getDateFromStr(String strDate) {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//            String strDate = "2014-01-29 13:30";
        try {
            return dateFormat.parse(strDate);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public class AlarmMoneyRecever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub


            if (DEBUG) {
                Qlog.i(TAG, "hiAlarm");
            }

            Intent i = new Intent(NewsMain.this, AnimationActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_in);
            try {
                unregisterReceiver(ALMreceiver);
            } catch (IllegalArgumentException e) {
            }
        }
    }


    private static String getData(long datetime) {
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetime);
        String strDate = formatter.format(calendar.getTime());

        return strDate;
    }


    public int getLcdSIzeWidth() {
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public int getLcdSIzeHeight() {
        return ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            if (ALMreceiver != null) unregisterReceiver(ALMreceiver);
        } catch (Exception e) {
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }


    private void setupTablayout() {
        bigMenu = (TabLayout) findViewById(R.id.bigMenu);
        bigMenu.setTabGravity(TabLayout.GRAVITY_FILL);
        bigMenu.addTab(bigMenu.newTab().setText("뉴  스"));
        bigMenu.addTab(bigMenu.newTab().setText("메 거 진"));


        bigMenu.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        bigMenu.setOnClickListener(this);
    }

    private void setupViewPager() {
        mViewpager = new ViewPager(this);
        mViewpager = (ViewPager) findViewById(R.id.viewPager);

        mCurrentFragmentIndex = NEWSLIST_FRAGMENT;
        setCurrentInflateItem(mCurrentFragmentIndex);

        FragmentManager fm = getSupportFragmentManager();
        mViewpager.setOffscreenPageLimit(1);

        mViewpager.setAdapter(
                new FragmentPagerAdapter(fm) {
                    @Override
                    public int getCount() {
                        return 2;
                    }

                    @Override
                    public Fragment getItem(int pos) {
                        Fragment newFragment = null;
                        switch (pos) {
                            case NEWSLIST_FRAGMENT:
                                newFragment = new NewsListFragment();
                                break;
                            case MAGAZINE_FRAGMENT:

                                newFragment = new MagazineFragment();
//                                Bundle bundle = new Bundle();
//                                bundle.pu
//                                bundle.putInt(key, value);
//                                fragment.setArguments(bundle);
                                break;
                        }
                        return newFragment;
                    }

                    @Override
                    public Object instantiateItem(ViewGroup container, int position) {
                        return super.instantiateItem(container, position);
                    }
                });

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
            }

            public void onPageSelected(int pos) {
                bigMenu.setScrollPosition(pos, pos, true);

                resizePager(pos);
                if (DEBUG) {
                    Qlog.i(TAG, " current POS " + pos);
                }

//                mViewpager.notify
                mViewpager.requestLayout();
                mViewpager.invalidate();
//                mViewpager.invalidate();
//                setCurrentInflateItem(pos);
            }

            public void resizePager(int pos) {
                if (DEBUG) {
                    Qlog.i(TAG, " resizePager start " + pos);
                }
//                View view = mViewpager.findViewWithTag(pos);
////                if (view == null) return;
////                view.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////                int width = view.getMeasuredWidth();
////                int height = view.getMeasuredHeight();
////                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
////
//                if (DEBUG) {
////                    Qlog.i(TAG, " resizePager width " + width + " height =" + height);
//                    Qlog.i(TAG, " resizePager width " + " height =" );
//                }
//
//                int height=View.MeasureSpec.makeMeasureSpec(38517, View.MeasureSpec.EXACTLY);
//                int width=View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY);
//
//                mViewpager.measure(0,height);
////                mViewpager.setLayoutParams(params);
            }
        });
    }


//    android:layout_width="match_parent"
//    android:layout_height="wrap_content"/>


    private void setCurrentInflateItem(int type) {
        if (DEBUG) {
            Qlog.i(TAG, " Inflate Item " + type);
        }
        if (type == 0) {
            mViewpager.setCurrentItem(0);

        } else if (type == 1) {
            mViewpager.setCurrentItem(1);
        }
    }


    //툴발에서 검색 버튼 셋팅
    private void setSearchAnimation() {
        mSearchImage = (ImageView) findViewById(R.id.gnb_topbar_search_btn);
        mSearchImage.setOnClickListener(this);
    }

    //위치 버튼 셋팅
    private void setPlaceAnimation() {
//        mPlaceImage = (ImageView) findViewById(R.id.gnb_topbar_place_btn);
//        mPlaceImage.setOnClickListener(this);
//
//        mPlaceAnimation = (AnimationDrawable) getResources().getDrawable(R.anim.anim_place_show);
//        mPlaceAnimationRepeatCount = getResources().getInteger(R.integer.gnb_place_anim_repeat_count);
//        mPlaceImage.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                final int action = event.getAction();
//                if (action == MotionEvent.ACTION_DOWN) {
////                    stopPlaceAnimation();
//                }
//                return false;
//            }
//        });
//
//        placeAnimationInit();
//        startPlaceAnimation();
    }

    //위치 버튼의 Animation
    private void placeAnimationInit() {

//        if (mPlaceHandler == null) {
//            mPlaceHandler = new Handler(new HandlerCallback());
//        }
//        if (mPlaceAnimation != null) {
//            mPlaceAnimationFrame = mPlaceAnimation.getNumberOfFrames();
//            int duration = 0;
//            for (int i = 0; i < mPlaceAnimationFrame; i++) {
//                duration += mPlaceAnimation.getDuration(i);
//            }
//            mPlaceAnimationDuration = duration * mPlaceAnimationRepeatCount;
//        }
    }


    private class HandlerCallback implements Handler.Callback {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_PLACE_ANIMATION_START:
                    sendPlaceAnimationStop();
                    sendPlaceAnimationStart();
                    break;
                case MESSAGE_PLACE_ANIMATION_STOP:
                    sendPlaceAnimationStop();
                    break;
                case MESSAGE_PLACE_ANIMATION_END:
                    sendPlaceAnimationEnd();
                    break;
            }
            return true;
        }

    }

    public void startPlaceAnimation() {
        if (mPlaceHandler != null) {
            mPlaceHandler.removeMessages(MESSAGE_PLACE_ANIMATION_START);
            mPlaceHandler.sendEmptyMessage(MESSAGE_PLACE_ANIMATION_START);
        }
    }

    public void stopPlaceAnimation() {
        if (mPlaceHandler != null) {
            mPlaceHandler.removeMessages(MESSAGE_PLACE_ANIMATION_STOP);
            mPlaceHandler.sendEmptyMessage(MESSAGE_PLACE_ANIMATION_STOP);
        }
    }

    private void sendPlaceAnimationStart() {
        if (mPlaceAnimation != null && mPlaceImage != null) {
            mPlaceImage.setImageDrawable(mPlaceAnimation);
            mPlaceAnimation.setOneShot(false);
            mPlaceAnimation.start();
            mPlaceHandler.removeMessages(MESSAGE_PLACE_ANIMATION_END);
            mPlaceHandler.sendEmptyMessageDelayed(MESSAGE_PLACE_ANIMATION_END, mPlaceAnimationDuration);
        }
    }

    private void sendPlaceAnimationEnd() {
        if (mPlaceAnimation != null) {
            mPlaceAnimation.setOneShot(true);
        }
        checkPlaceAnimationEnd();
    }

    private void checkPlaceAnimationEnd() {
        if (mPlaceAnimation != null) {
            int duration = mPlaceAnimation.getDuration(0);
            mPlaceHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Drawable currentframe = mPlaceAnimation.getCurrent();
                    Drawable lastFrame = mPlaceAnimation.getFrame(mPlaceAnimationFrame - 1);
                    if (!mPlaceAnimation.isRunning() || (currentframe == lastFrame)) {
                        mPlaceImage.setImageResource(R.drawable.gnb_place_button);
                    } else {
                        checkPlaceAnimationEnd();
                    }
                }
            }, duration * 2);
        }
    }

    private void sendPlaceAnimationStop() {
        if (mPlaceAnimation != null && mPlaceImage != null) {
            mPlaceAnimation.setOneShot(true);
            mPlaceAnimation.stop();
            mPlaceImage.setImageResource(R.drawable.gnb_place_button);
        }
    }


    private void setupFab() {
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(this);
    }

    private void setupAds() {
//        Google Admob
        AdView mAdView = (AdView) findViewById(R.id.adView);

//        AdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded();
//            {
//                setAppPreferences4Ad(this, "adDate", getData(now));
//            }
//        });

        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.news_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.urge_donation) {

            Intent i = new Intent(NewsMain.this, AnimationActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_in);

        } else if (id == R.id.facebook_share) {

            ShareDialog shareDialog = new ShareDialog(this);
            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result loginResult) {
                    // App code
                    Log.i("Callback Suceewded", "really ...?");
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    // App code
                }

            });

            Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.drawable.news_icon);

//            Uri uri = Uri.parse("R.drawable.news_icon");
//            Uri imageUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                    getResources().getResourcePackageName(R.drawable.news_icon) + '/' +
//                    getResources().getResourceTypeName(R.drawable.news_icon) + '/' +
//                    getResources().getResourceEntryName(R.drawable.news_icon));

            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("뉴스 모아 : 종합 뉴스 매거진 ")
                        .setContentDescription(
                                "다음/네이버.. 16여개 사이트 뉴스 모음과 Huff post/네셔날 지오그래픽스를 한손에")
                        .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.dylee.newszine"))
                        .setImageUrl(Uri.parse("http://icons.iconarchive.com/icons/designcontest/ecommerce-business/72/news-icon.png"))
                        .build();
                shareDialog.show(linkContent);
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.close_btn1:
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                break;
            case R.id.gnb_topbar_search_btn:
                Intent intent = new Intent(this, NewsSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("uris", "http://www.daum.net");
                try {
                    this.startActivity(intent);
                } catch (Exception e) {
                    Log.i("  exception", " :" + e);
                }
                break;
        }

    }


    public static void setAppPreferences4Ad(Activity context, String key, String value) {
        SharedPreferences pref = null;
        pref = context.getSharedPreferences("Ad", 0);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.putString(key, value);

        prefEditor.commit();
    }

    // app 쉐어드 프레퍼런스에서 값을 읽어옴
    public static String getAppPreferences4Ad(Activity context, String key) {
        String returnValue = null;

        SharedPreferences pref = null;
        pref = context.getSharedPreferences("Ad", 0);

        returnValue = pref.getString(key, "");

        return returnValue;
    }

}


//
//Resources res = getResources();
//DisplayMetrics dm = res.getDisplayMetrics();
//float scaleDensity = dm.scaledDensity; //1DP당 픽셀값
//
//int width = getLcdSIzeWidth();
//int height = getLcdSIzeHeight();
//
//final float GESTURE_THRESHOLD_DIP = 16.0f;


//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GESTURE_THRESHOLD_DIP, dm);
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, GESTURE_THRESHOLD_DIP, dm);

/*
        public static float applyDimension(int unit, float value,
        DisplayMetrics metrics)
        {
            switch (unit) {
                case COMPLEX_UNIT_PX:
                    return value;
                case COMPLEX_UNIT_DIP:
                    return value * metrics.density;
                case COMPLEX_UNIT_SP:
                    return value * metrics.scaledDensity;
                case COMPLEX_UNIT_PT:
                    return value * metrics.xdpi * (1.0f/72);
                case COMPLEX_UNIT_IN:
                    return value * metrics.xdpi;
                case COMPLEX_UNIT_MM:
                    return value * metrics.xdpi * (1.0f/25.4f);
            }
            return 0;
        }
 */