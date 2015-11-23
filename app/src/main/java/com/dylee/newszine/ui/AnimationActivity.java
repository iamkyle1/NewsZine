package com.dylee.newszine.ui;

import android.R.style;
import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.dylee.newszine.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AnimationActivity extends Activity {

    private ImageView imageView;
    private static final String TAG = NewsMain.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anim);

        Button dialog = (Button) findViewById(R.id.dialogButtonOK);
        dialog.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        setupAds();
    }

    private void setupAds() {
//        Google Admob
        AdView mAdView = (AdView) findViewById(R.id.donation_adview);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            ;

            public void onAdFailedToLoad(int errorCode) {
            }

            ;

            public void onAdOpened() {
            }

            ;

            public void onAdClosed() {
            }

            ;

            public void onAdLeftApplication() {
            }

            ;
        });

//
    }


    protected void onApplyThemeResource(Resources.Theme theme, int resid, boolean first) {
        super.onApplyThemeResource(theme, resid, first);

        // no background panel is shown
//        theme.applyStyle(style.Theme_Panel, true);
        theme.applyStyle(style.ThemeOverlay, true);
//        Theme_Panel
    }

}