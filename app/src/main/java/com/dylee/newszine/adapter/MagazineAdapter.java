package com.dylee.newszine.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dylee.newszine.R;
import com.dylee.newszine.glide.RoundedCornersTransformation;
import com.dylee.newszine.logging.Qlog;
import com.dylee.newszine.object.HuffNews;
import com.dylee.newszine.ui.ParallaxWebviewActivity;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by 1000128 on 15. 11. 8..
 */
public class MagazineAdapter extends BaseAdapter {

    private static final String TAG = MagazineAdapter.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    int LCDwidth, LCDheight;
    ImageView mPicture, mBottomPicture;
    TextView mMessage;
    TextView mCaption, mDesc;
    int gWidth = 0, gHeight = 0;
    RelativeLayout RelativeLO;
    LinearLayout LinearLO;
    FrameLayout FLO;
    List<ImageView> mMainImage=new ArrayList<ImageView>();
    List<ImageView> mBottomImage=new ArrayList<ImageView>();


    public static class MagazineViewHolder {
        public int mDisplayPosition;
        public ImageView feedPicture;

        public ImageView mBottomPicture;
        public TextView mCaption;
        public TextView feedMessage;
    }

    final Activity mActivity;
    final Fragment mFragment;
    //    final Context mContext;
    final ArrayList<HuffNews> mDataSetList = new ArrayList<>();

    boolean backPressState = false;
    final LayoutInflater mInflater;
//
//    public MagazineAdapter(Activity context) {
//        super();
//        this.mActivity = context;
//        this.mInflater = LayoutInflater.from(mActivity);
//    }

    public MagazineAdapter(Fragment context, Activity act) {
        super();
        this.mActivity = act;
        this.mFragment = context;
        this.mInflater = LayoutInflater.from(mActivity);
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

//    this.mDataSetList.


    public void ordering() {

        if (DEBUG) {
            Qlog.i(TAG, "Size =" + mDataSetList.size());

            for (int i = 0; i < mDataSetList.size(); i++) {
                Qlog.i(TAG + "Ordering", "  " + i + "  " + mDataSetList.get(i).getPictureURL());
            }
        }

        Comparator<HuffNews> comparator = new Comparator<HuffNews>() {
            @Override
            public int compare(HuffNews object1, HuffNews object2) {
//                return object1.getCDate().after(object2.getCDate()) ? 1: -1 ;
//                return (object1.getDescription().length() > object2.getDescription().length()) ? 1 : -1;

//                ava.lang.IllegalArgumentException: Comparison method violates its general contract!
                return 1;
//                return ( object1.getMessage().length() > object2.getMessage().length()) ? 1: -1 ;
            }
        };
        Collections.sort(mDataSetList, comparator);
    }


    @Override
    public View getView(final int position, View convertView,
                        ViewGroup parent) {
        MagazineViewHolder holder = null;
        int mLineNumber = 0;

        Resources res = mActivity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        float scaleDensity = dm.scaledDensity; //1DP당 픽셀값

        LCDwidth = getLcdSIzeWidth();
        LCDheight = getLcdSIzeHeight();


        if (convertView == null) {
            holder = new MagazineViewHolder();
            convertView = mInflater.inflate(
                    R.layout.each_magazine_feed, null);

            holder.feedPicture = (ImageView) convertView.findViewById(R.id.main_picture);
            holder.mBottomPicture = (ImageView) convertView.findViewById(R.id.bottom_picture);
            holder.feedMessage = (TextView) convertView.findViewById(R.id.message);
            holder.mCaption = (TextView) convertView.findViewById(R.id.source);

            HuffNews magazineFeed = (HuffNews) this.getItem(position);
            //상위 사진을 불러옴, 여기서 main picture의 width, height 결정
            // width는 향후 bottom picture의 width로 사용됨. --.일단 전역 변수로 지정함.--    int gWidth = 0, gHeight = 0;
            getMainPicture(convertView, holder, magazineFeed, position);


            //viewholder에 매핑
            convertView.setTag(holder);
        } else {
            holder = (MagazineViewHolder) convertView.getTag();
        }

        return convertView;
    }

    public void clearGlide() {

        if(DEBUG) Qlog.i(TAG, "enter clearglide()"+ mMainImage.size());
//        if (mMainImage.size()>0 ) {
//            for (ImageView img : mMainImage) {
//                Glide.clear(img);
//                if(DEBUG) Qlog.i(TAG, "clearglide():clear Main Image");
//            }
//        }
//
//        if (mMainImage.size()>0 ) {
//            for (ImageView img : mBottomImage) {
//                Glide.clear(img);
//                if(DEBUG) Qlog.i(TAG, "clearglide():clear Main Image");
//            }
//        }


    }

    public void getMainPicture(final View convertView, final MagazineViewHolder holder, final HuffNews mainFeed, final int position) {
        RoundedCornersTransformation mRctf = new RoundedCornersTransformation(mActivity, 10, 0, RoundedCornersTransformation.CornerType.TOP);
        String FeedURL = null;
        if (DEBUG) {
            Qlog.i(TAG, "mainfeed url=" + mainFeed.getPictureURL());
            Qlog.i(TAG, "mainfeed url=" + mainFeed.getPictureURL());
        }
        if (mainFeed.getPictureURL().equals("No Picture")) {
            FeedURL = new String("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-10/icons-390.jpg");

        } else {
            FeedURL = mainFeed.getPictureURL();
        }

        if (DEBUG) {
            if (FeedURL.equals("http://icons.iconarchive.com/icons/custom-icon-design/flatastic-10/icons-390.jpg")) {
                Qlog.i(TAG, "fed URL =" + FeedURL + " " + mainFeed.getMessage());
                Qlog.i(TAG, "fed URL2  =" + mainFeed.getDestinationURL());
            }
        }
        try {
            if (!mFragment.isDetached()) {
//                Glide.clear(holder.feedPicture);

//                Glide.clear(holder.feedPicture);
                mMainImage.add(holder.feedPicture);

//                if(DEBUG) {Qlog.i(TAG," image Size"+ mMainImage.size());}

                Glide.with(mActivity).load(FeedURL).asBitmap().error(R.drawable.while_img).format(DecodeFormat.PREFER_ARGB_8888).placeholder(R.drawable.loading_icon).transform(mRctf).into(new BitmapImageViewTarget(holder.feedPicture) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
                        super.onResourceReady(bitmap, anim);
//                        mMainImage.remove(holder.feedPicture);
                        String msg;
                        String mTitle = " ";
//                        Log.i("Size of rectangle", " width = " + bitmap.getWidth() + "   height = " + bitmap.getWidth());

                        float mRatio = (getLcdSIzeWidth() / 2) / bitmap.getWidth();
                        int mWidth = (int) (bitmap.getWidth() * mRatio);
                        int mHeight = (int) (bitmap.getHeight() * mRatio);

                        gWidth = mWidth;
                        gHeight = mHeight;

                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(gWidth, gHeight);
                        lp.topMargin = 40;
                        lp.leftMargin = 20;
                        holder.feedPicture.setLayoutParams(lp);

                        if (DEBUG & mActivity == null) Qlog.i(TAG, "Activity is null");

                        getBottomPicture(convertView, holder, mainFeed, position, gWidth);

                        if (mainFeed.getMessage() != null) {
                            msg = mainFeed.getMessage().trim();
                            if (msg.length() > 18) {
                                mTitle = msg.substring(0, 17).concat("...");
                            } else mTitle = msg;
                        } else {
                            mTitle = new String("...");
                        }

                        final String titleMSg = mTitle;

                        holder.feedPicture.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String urls = mainFeed.getDestinationURLASString();
                                        Intent intent = new Intent(mActivity, ParallaxWebviewActivity.class);
                                        intent.putExtra("uris", urls);
                                        intent.putExtra("title", titleMSg);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        try {
                                            mActivity.startActivity(intent);
                                        } catch (Exception e) {
//                                            Log.i("  exception", " :" + e);
                                        }
                                    }
                                });
                    }

                    //
                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        holder.feedPicture.setScaleX(0);
                    }
                });
            }
        } catch (Exception e) {
            if (DEBUG) {
                Qlog.i(TAG, "Exception occurred at getMainPicture()");
                e.printStackTrace();
            }
        }
    }

    public void getBottomPicture(View convertView, final MagazineViewHolder holder, final HuffNews mainFeed, int position, final int Width) {
        RoundedCornersTransformation mBot = new RoundedCornersTransformation(mActivity, 10, 0, RoundedCornersTransformation.CornerType.BOTTOM);
        try {

            if (!mFragment.isDetached()) {
                mBottomImage.add(holder.mBottomPicture);
                Glide.with(mActivity).load(R.drawable.while_img).asBitmap().transform(mBot).into(new BitmapImageViewTarget(holder.mBottomPicture) {
                    @Override
                    public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                        String msg = " ";
                        String mTitle = " ";
                        String mContents;
                        super.onResourceReady(bitmap, anim);
                        mBottomImage.remove(holder.mBottomPicture);

                        int mLineNumber, mHeight = 100;

                        Resources res = mActivity.getResources();
                        DisplayMetrics dm = res.getDisplayMetrics();
                        float scaleDensity = dm.scaledDensity; //1DP당 픽셀값

                        //Reltive layout mBottomPicture에서 Cation과 mMessage에 따라 Height를 결정
                        if (mainFeed.getMessage() != null) {
                            mContents = mainFeed.getMessage();
                            msg = mainFeed.getMessage().trim();
                            mLineNumber = (int) (msg.length() / 20 + 2);
                            if (msg.length() > 18) {
                                mTitle = msg.substring(0, 17).concat("...");
                            } else mTitle = msg;
                        } else {
                            mContents = msg;
                            mTitle = new String("....");
                            mLineNumber = 1;
                        }

                        final String titleMSg = mTitle;

                        RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(Width, (int) (16 * scaleDensity + 2));
                        lp1.leftMargin = 30;
                        lp1.topMargin = 20;
                        lp1.addRule(RelativeLayout.BELOW, R.id.main_picture);
                        holder.mCaption.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10);
                        holder.mCaption.setTypeface(null, Typeface.BOLD);

                        if (mainFeed.getCaption() != null)
                            holder.mCaption.setText(mainFeed.getCaption());
                        else holder.mCaption.setText(" Unknown or National Geographics");

                        holder.mCaption.setLayoutParams(lp1);

                        mHeight = (int) ((mLineNumber + 1) * 17 * (scaleDensity + 2));

                        RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(Width, mHeight);
                        lp2.leftMargin = 30;
                        lp2.topMargin = 20;
                        lp2.addRule(RelativeLayout.BELOW, R.id.source);

                        holder.feedMessage.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13);
                        holder.feedMessage.setTypeface(null, Typeface.BOLD);
                        holder.feedMessage.setText(mContents);
                        holder.feedMessage.setLineSpacing(3.0f, 1.1f);


                        holder.feedMessage.setLayoutParams(lp2);
//                        Log.i("height", " = " + holder.feedMessage.getHeight());
//                        Log.i("height", " = " + holder.mCaption.getHeight());


                        ViewTreeObserver vto = holder.feedMessage.getViewTreeObserver();
                        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @Override

                            public void onGlobalLayout() {
                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Width, holder.feedMessage.getHeight());
                                lp.addRule(RelativeLayout.BELOW, R.id.main_picture);
                                lp.leftMargin = 20;
                                holder.mBottomPicture.setScaleType(ImageView.ScaleType.FIT_XY);
                                holder.mBottomPicture.setLayoutParams(lp);
//                                Log.i("OnGlolbalLayout", "height=" + holder.feedMessage.getHeight());
                                ViewTreeObserver obs = holder.feedMessage.getViewTreeObserver();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    obs.removeOnGlobalLayoutListener(this);
                                } else {
                                    obs.removeGlobalOnLayoutListener(this);
                                }
                            }
                        });

//
//                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(Width, mHeight);
//                lp.addRule(RelativeLayout.BELOW, R.id.main_picture);
//                lp.leftMargin = 20;
//                holder.mBottomPicture.setScaleType(ImageView.ScaleType.FIT_XY);
//                holder.mBottomPicture.setLayoutParams(lp);


                        holder.mBottomPicture.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        final String urls = mainFeed.getDestinationURLASString();
                                        Intent intent = new Intent(mActivity, ParallaxWebviewActivity.class);
                                        intent.putExtra("uris", urls);
                                        intent.putExtra("title", titleMSg);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        try {
                                            mActivity.startActivity(intent);
                                        } catch (Exception e) {
//                                            Log.i("  exception", " :" + e);
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        holder.mBottomPicture.setScaleX(0);
                    }
                });
            }
        } catch (Exception e) {
            if (DEBUG) {
                Qlog.i(TAG, "Exception occurred at getBottomPicture()");
                e.printStackTrace();
            }
            ;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public synchronized void addItem(HuffNews i) {
        mDataSetList.add(i);
//        Log.i("Any Item=", " " + i);
        this.notifyDataSetChanged();
    }

    public void addAll(List<HuffNews> items) {
        mDataSetList.addAll(items);
    }


    public void clearAdapter() {
        mDataSetList.clear();
        this.notifyDataSetChanged();
    }

    private void showView(View view, boolean flag) {
        view.setVisibility((flag) ? (View.VISIBLE) : (View.GONE));
    }

    private void makeupSingleFeedView(View convertView, final MagazineViewHolder holder, final HuffNews mainFeed, int position) {
        URL imageURL = null;
        URLConnection conn = null;
        InputStream is = null;
        final String sDestUrl;

        Log.i("String", "position=" + position + " url=" + mainFeed.getPictureURL());
    }

    public int getLcdSIzeWidth() {
        return ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    }

    public int getLcdSIzeHeight() {
        return ((WindowManager) mActivity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
    }


}

/*
final float GESTURE_THRESHOLD_DIP = 16.0f;

Log.i("metrix2=", "=============================");
        Log.i("metrix2=", "metrix =" + dm);

        Log.i("metrix2 Screen", " width= " + width + "  height=" + height);
        Log.i("metrix2 Screen itSelf ", "width=" + width * scaleDensity + "  height=" + height * scaleDensity);
        */


//        try {
//            new DownloadImageTask(holder.feedPicture)
//                    .execute(mainFeed.getPictureURL().toString());
//
//        }catch(Exception e){
//            Log.i("   ", "   ");
//        }


//private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
//        ImageView bmImage;
//
//        public DownloadImageTask(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls) {
//            String urldisplay = urls[0];
//            Log.i("result urls:", "  " + urldisplay);
//            Bitmap mIcon11 = null;
//            try {
//                InputStream in = new java.net.URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//
//        protected void onPostExecute(Bitmap result) {
//            Log.i("result bitmap:", " height= " + result.getHeight() + " width:  " + result.getWidth());
//
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                    result.getWidth()*4,
//                    result.getHeight()*4);
//            bmImage.setScaleType(ImageView.ScaleType.FIT_XY);
//            bmImage.setLayoutParams(lp);
//
//            bmImage.setImageBitmap(result);
//        }
//    }
//
//