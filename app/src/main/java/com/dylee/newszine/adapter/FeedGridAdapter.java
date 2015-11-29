package com.dylee.newszine.adapter;

/**
 * Created by 1000128 on 15. 10. 4..
 */

import android.app.Activity;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dylee.newszine.R;
import com.dylee.newszine.object.NewsObject;
import com.dylee.newszine.ui.ParallaxWebviewActivity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

//import com.skplanet.ocb.soi.gpb.MainFeedsProtos;

public final class FeedGridAdapter extends BaseAdapter {
    private static final String TAG = FeedGridAdapter.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;


    public static class MainFeedItemViewHolder {
        public ImageView feedTotalImage;
    }

    public static class ViewHolder {
        int mDisplayPosition;
        // single feed layout
        public MainFeedItemViewHolder single;

        // rolling feed
        // 피드의 포지션 값
        public ViewHolder() {
            single = new MainFeedItemViewHolder();
        }
    }

    final Activity mActivity;
    final ArrayList<NewsObject> mDataSetList = new ArrayList<>();

    boolean backPressState = false;
    final LayoutInflater mInflater;

    public FeedGridAdapter(Activity context) {
        super();
        this.mActivity = context;
        this.mInflater = LayoutInflater.from(mActivity);
//        setOnGridClickListener(this);
    }

    @Override
    public int getCount() {
        if (this.mDataSetList != null) {
//            Log.i("Size = ", " +  itemCount" + this.mDataSetList.size());
            return this.mDataSetList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (this.mDataSetList != null) {
            if (mDataSetList.size() > position)
                return this.mDataSetList
                        .get(position);
        }
        return null;
    }

    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            convertView = this.mInflater.inflate(
                    R.layout.newslist_mainfeed, null);
            holder = new ViewHolder();

            holder.single.feedTotalImage = (ImageView) convertView
                    .findViewById(R.id.eachFeed);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mDisplayPosition = position;
        NewsObject mainFeed = (NewsObject) getItem(position);
        // single feed

        makeupSingleFeedView(convertView, holder, mainFeed, position);
        convertView.setDrawingCacheEnabled(true);


        convertView.setDrawingCacheEnabled(true);
        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics metrics_ = metrics;

        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Animation animation = null;

//        switch (2) {
//            case 1:
//                animation = new TranslateAnimation(metrics_.widthPixels / 2, 0,
//                        0, 0);
//                break;
//
//            case 2:
//                animation = new TranslateAnimation(0, 0, metrics_.heightPixels/6,
//                        0);
//                break;
//
//            case 3:
//                animation = new ScaleAnimation((float) 1.0, (float) 1.0,
//                        (float) 0, (float) 1.0);
//                break;
//
//            case 4:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.fade_in);
//                break;
//            case 5:
////                    animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);
//                break;
//            case 6:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.hyperspace_out);
//                break;
//            case 7:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.wave);
//                break;
//            case 8:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_in);
//                break;
//            case 9:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.push_left_out);
//                break;
//            case 10:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.push_up_in);
//                break;
//            case 11:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.push_up_out);
//                break;
//            case 12:
//                animation = AnimationUtils.loadAnimation(mActivity, R.anim.shake);
//                break;
//        }
//
//        animation.setDuration(1000);
//        convertView.startAnimation(animation);
//        animation = null;

        return convertView;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(NewsObject i) {
        mDataSetList.add(i);
    }

    public void addAll(List<NewsObject> items) {
        mDataSetList.addAll(items);
    }


    public void clearAdapter() {
        mDataSetList.clear();
    }

    private void showView(View view, boolean flag) {
        view.setVisibility((flag) ? (View.VISIBLE) : (View.GONE));
    }


    private void makeupSingleFeedView(View convertView, final ViewHolder holder, NewsObject mainFeed, int positon) {
        URL imageURL = null;
        URLConnection conn = null;
        InputStream is = null;
        final String sDestUrl;

        holder.single.feedTotalImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        Glide.with(mActivity).load(mainFeed.getImgResource()).asBitmap().format(DecodeFormat.PREFER_ARGB_8888).diskCacheStrategy(DiskCacheStrategy.ALL).override(680,300).into(holder.single.feedTotalImage);

        sDestUrl = mainFeed.getURL();

        final String title = mainFeed.getTitle();

        holder.single.feedTotalImage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String urls = sDestUrl;
                        Intent intent = new Intent(mActivity, ParallaxWebviewActivity.class);
                        intent.putExtra("uris", urls);
                        intent.putExtra("title", title);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        try {
                            mActivity.startActivity(intent);
//                        Log.i("VALE", "VALE " + "Clicked ");}
                        } catch (Exception e) {
//                            Log.i("  exception", " :" + e);
                        }
                    }
                });
    }

}//FeedAdapter Class

