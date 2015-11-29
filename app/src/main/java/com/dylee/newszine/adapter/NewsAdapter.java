package com.dylee.newszine.adapter;

/**
 * Created by 1000128 on 15. 10. 25..
 */

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.dylee.newszine.R;
import com.dylee.newszine.logging.Qlog;
import com.dylee.newszine.object.NaverNews;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 1000128 on 15. 9. 7..
 */
public class NewsAdapter extends ArrayAdapter<NaverNews> {
    private static final String TAG = NewsAdapter.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    Context context;
    int layoutResourceId;
    ArrayList<NaverNews> data = null;
    int oldPosition = 0;
    List<Long> previousItemIds = new ArrayList<Long>();
    boolean mInitialized = false;


    public NewsAdapter(Context context, int layoutResourceId, ArrayList<NaverNews> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
//        Log.i("getviewget size", " " + data.size());

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View Viewrow = convertView;
        NewsItemHolder holder;


        int top = (parent == null) ? 0 : parent.getTop();


        if (Viewrow == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            Viewrow = inflater.inflate(layoutResourceId, parent, false);

            holder = new NewsItemHolder();
            holder.title = (TextView) Viewrow.findViewById(R.id.title);
            holder.description = (TextView) Viewrow.findViewById(R.id.desc);
            holder.tDate = (TextView) Viewrow.findViewById(R.id.date);
            Viewrow.setTag(holder);
        } else {
            holder = (NewsItemHolder) Viewrow.getTag();
        }

        NaverNews gt = data.get(position);
        holder.title.setText(gt.getTitle());
//        Log.i("getviewget get title", " " + position + " " + gt.getTitle());
        holder.description.setText(gt.getDescription());
        holder.tDate.setText(gt.getDate());

        DisplayMetrics metrics = new DisplayMetrics();
        DisplayMetrics metrics_ = metrics;

        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getMetrics(metrics);

//        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Animation animation = null;
        int oldTop = 0;

//        if (DEBUG) Qlog.i(TAG, "old Top = " + oldTop + " Top =" + top+"   "+parent.toString());

        if (oldPosition < position & mInitialized) {
            long itemId = getItemId(position);

//        if (!previousItemIds.contains(itemId)) {
            if (DEBUG) Qlog.i(TAG, "position =" + position);

            switch (2) {
                case 1:
                    animation = new TranslateAnimation(metrics_.widthPixels / 2, 0,
                            0, 0);
                    break;
                case 2:
                    animation = new TranslateAnimation(0, 0, metrics_.heightPixels / 7,
                            0);
                    break;

                case 3:
                    animation = new ScaleAnimation((float) 1.0, (float) 1.0,
                            (float) 0, (float) 1.0);
                    break;

                case 4:
                    animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
                    break;
                case 5:
//                    animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_in);
                    break;
                case 6:
                    animation = AnimationUtils.loadAnimation(context, R.anim.hyperspace_out);
                    break;
                case 7:
                    animation = AnimationUtils.loadAnimation(context, R.anim.wave);
                    break;
                case 8:
                    animation = AnimationUtils.loadAnimation(context, R.anim.push_left_in);
                    break;
                case 9:
                    animation = AnimationUtils.loadAnimation(context, R.anim.push_left_out);
                    break;
                case 10:
                    animation = AnimationUtils.loadAnimation(context, R.anim.push_up_in);
                    break;
                case 11:
                    animation = AnimationUtils.loadAnimation(context, R.anim.push_up_out);
                    break;
                case 12:
                    animation = AnimationUtils.loadAnimation(context, R.anim.shake);
                    break;
            }

            animation.setDuration(300);
            Viewrow.startAnimation(animation);
            animation = null;
        }

//        previousItemIds.add(itemId);
        oldPosition = position;
        mInitialized = true;
        return Viewrow;
    }

//    @Override SimpleCursorAdapter일때만 작동
//    public void changeCursor(Cursor cursor) {
//        Cursor oldCursor = cursor;
//        if (oldCursor != null) {
//            // store previous item IDs to check against for newness with the new cursor
//            final int idColumnIndex = oldCursor.getColumnIndexOrThrow(DatabaseContract._ID);
//            previousItemIds.clear();
//            for (oldCursor.moveToFirst(); !oldCursor.isAfterLast(); oldCursor.moveToNext()) {
//                previousItemIds.add(oldCursor.getLong(idColumnIndex));
//            }
//        }
//
//        super.changeCursor(cursor);
//    }

    public int getSize() {
        return data.size();
    }

    public void addItem(ArrayList<NaverNews> NW) {
        for (int i = 0; i < NW.size(); i++)
            data.add(NW.get(i));
        this.notifyDataSetChanged();
    }

    public void clear() {
        if (data.size() > 0) data.clear();
        this.notifyDataSetChanged();
    }

    public void dagachange() {

    }

    static class NewsItemHolder {
        TextView title;
        TextView description;
        TextView tDate;
        private URL link;
        private URL originalLink;

//        ;/        new SimpleDateFormatmpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
//static SimpleDateFormat FORMATTER_KR =
//        new SimpleDateFormat("yyyy년 MM월 dd일 HH시mm분ss초, E");
//private String title;
//private URL link;
//private URL originalLink;
//private String description;
//private Date date;
    }
}

//static SimpleDateFormat F