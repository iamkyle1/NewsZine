package com.dylee.newszine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dylee.newszine.CustomViewGroup.WaterfallView;
import com.dylee.newszine.adapter.MagazineAdapter;
import com.dylee.newszine.logging.Qlog;
import com.dylee.newszine.object.HuffNews;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 1000128 on 15. 10. 4..
 */

public class MagazineFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    private static final String TAG = MagazineFragment.class.getSimpleName();
    private static final boolean DEBUG = com.dylee.newszine.BuildMode.ENABLE_LOG;

    CallbackManager callbackManager;
    Button customLogInButton, customLogOutButton;
    AccessTokenTracker accessTokenTracker;
    AccessToken accessToken;
    ProfileTracker profileTracker;
    Profile profile;
    private SharedPreferences sp;
    String at;

    //    FaceBook fb;
    String id = "";
    String name = "";
    String email = "";
    LoginButton loginButton;
    TextView loginGuide;
    WaterfallView mWaterfallView;
    MagazineAdapter mMagazineAdpater;

    private Handler mShowHandler;
    private static final int MESSAGE_HUFF_END = 100;
    private static final int MESSAGE_GEO_END = 200;
    private static final int MESSAGE_SISA_END = 300;


    public MagazineFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
        String APP_ID = getString(R.string.facebook_app_id);
        FacebookSdk.sdkInitialize(getContext());
        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().logOut();
//        Log.i("1st accestoken expire ", " = "+AccessToken.getCurrentAccessToken().isExpired()+"  "+ AccessToken.getCurrentAccessToken().getUserId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.magazine_fragment,
                container, false);

        loginButton = (LoginButton) rootView.findViewById(R.id.FB_login_button);
        loginGuide =(TextView)rootView.findViewById(R.id.FB_login_guide);
        loginGuide.setText("Facebook에 로그인 하시면, WIKITree등의 다양한 뉴스를 보실수 있습니다");

        loginButton.setOnClickListener(this);

        mWaterfallView = (WaterfallView) rootView.findViewById(R.id.waterfall_view);
//        mMagazineAdpater = new MagazineAdapter(this.getActivity());
        mMagazineAdpater = new MagazineAdapter(this, this.getActivity());

        mWaterfallView.setAdapter(mMagazineAdpater);
        getPages();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        accessTokenTracker.stopTracking();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMagazineAdpater.clearGlide();
//        Glide.clear();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.FB_login_button:
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "user_likes", "user_posts"));
                Log.i("accestoken =....... ", "= " + accessToken);
                loginFaceBook();
                loginButton.setVisibility(View.INVISIBLE);
                loginGuide.setVisibility(View.INVISIBLE);
                getPages();
                break;
        }

    }

    public void loginFaceBook() {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        getPages();
//
//                        GraphRequest request = GraphRequest.newMeRequest(
//                                loginResult.getAccessToken(),
//                                new GraphRequest.GraphJSONObjectCallback() {
//                                    @Override
//                                    public void onCompleted(
//                                            JSONObject object,
//                                            GraphResponse response) {
//                                        // Application code
//                                        try {
//
//                                            id = (String) response.getJSONObject().get("id");//페이스북 아이디값
//                                            name = (String) response.getJSONObject().get("name");//페이스북 이름
//                                            email = (String) response.getJSONObject().get("email");//이메일
//
////                                            Log.i("value ", "id= " + id + " name= " + name);
//                                        } catch (JSONException e) {
//                                            // TODO Auto-generated catch block
//                                            Log.i("value ", "id= " + id + " name= " + name);
//                                            e.printStackTrace();
//                                        }
                        // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다
//                                    }
//                                });

//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", "id,name,email,gender, birthday");
//                        request.setParameters(parameters);
//                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.i("Login Cancel Changed", "really fragment...?");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i("Login Error Changed", "really fragment...?");
                    }
                }

        );
    }


    public void getPages() {
        ArrayList<HuffNews> mMagazineObjectList = new ArrayList<>();

        mMagazineAdpater.clearAdapter();

        if (mShowHandler == null) {
            mShowHandler = new Handler(new HandlerCallback());
        }
        getNatGeo();
        getHuff();
        getSisaIn();

    }



    private class HandlerCallback implements Handler.Callback {
        public int End_state = 0;

        @Override
        public synchronized boolean handleMessage(Message msg) {
            switch (msg.what) {

                case MESSAGE_HUFF_END:
                    End_state = End_state + 1;
                    if (DEBUG) {
                        Qlog.i(TAG, "messgae = " + msg.what + " count = " + End_state);
                    }
                    break;
                case MESSAGE_GEO_END:
                    End_state = End_state + 1;

                    if (DEBUG) {
                        Qlog.i(TAG, "messgae = " + msg.what + " count = " + End_state);
                    }
                    break;
                case MESSAGE_SISA_END:
                    End_state = End_state + 1;
                    if (DEBUG) {
                        Qlog.i(TAG, "messgae = " + msg.what + " count = " + End_state);
                    }
                    break;
            }
            if (End_state >= 3) {
                mMagazineAdpater.ordering();
                mWaterfallView.BindLayout();

//                mWaterfallView.requestLayout();
//                mWaterfallView.invalidate();
            }
            return true;
        }
    }

    public void getHuff() {

        if (AccessToken.getCurrentAccessToken() != null) {
            if (!AccessToken.getCurrentAccessToken().isExpired()) {
//
                GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "/huffpostkorea/feed", new GraphRequest.Callback() {
                    //                GraphRequest request = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "/NatGeoKorea/feed", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        try {
                            JSONObject jsonObject = graphResponse.getJSONObject();
                            Log.i("Test ", " =" + graphResponse.getRawResponse());
                            JSONArray jArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jArray.length(); i++) {
                                HuffNews HFNS = new HuffNews();
                                JSONObject obj = jArray.getJSONObject(i);
                                try {
                                    String pictureLink = obj.getString("picture");
                                    HFNS.setPictureURL(pictureLink);
                                    Log.i("feeds picture ", "  = " + pictureLink);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String originalPageurls = obj.getString("link");
                                    Log.i("feeds Url ", "  = " + originalPageurls);
                                    HFNS.setDestinationURL(originalPageurls);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String desc = obj.getString("description");
                                    HFNS.setDescription(desc);
                                    Log.i("feeds picture ", "  = " + desc);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String message = obj.getString("message");
                                    Log.i("feeds message ", "  = " + message);
                                    HFNS.setMessage(message);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String caption = obj.getString("caption");
                                    HFNS.setCaption(caption);
                                    Log.i("feeds caption ", "  = " + caption);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String description = obj.getString("description");
                                    HFNS.setDescription(description);
                                    Log.i("feeds description ", "  = " + description);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String icon = obj.getString("icon");
                                    HFNS.setIconURL(icon);
                                    Log.i("feeds icon ", "  = " + icon);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    String updated_time = obj.getString("updated_time");
                                    HFNS.setUDate(updated_time);
                                    Log.i("feeds updated_time ", "  = " + updated_time);
                                } catch (Exception ex) {
                                    Log.i("feeds ", "created time error");
                                    ex.printStackTrace();
                                }
                                try {
                                    String created_time = obj.getString("created_time");
                                    HFNS.setCDate(created_time);
                                    Log.i("feeds created_time ", "  = " + created_time);
                                } catch (Exception ex) {
                                    Log.i("feeds ", "created time error");
                                    ex.printStackTrace();
                                }

                                if (HFNS.getDestinationURL() != null & HFNS.getPictureURL() != null)
                                    mMagazineAdpater.addItem(HFNS);
                                Log.i("String", "  =" + HFNS);
                            }


                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }

                        mShowHandler.removeMessages(MESSAGE_GEO_END);
                        mShowHandler.sendEmptyMessage(MESSAGE_GEO_END);

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,picture,link, message,caption,description,icon,updated_time,created_time");
                request.setParameters(parameters);
                request.executeAsync();

                loginButton.setVisibility(View.INVISIBLE);
                loginGuide.setVisibility(View.INVISIBLE);
            } else


                loginButton.setVisibility(View.VISIBLE);
                loginGuide.setVisibility(View.VISIBLE);
        }

    }

    public void getNatGeo() {

        if (AccessToken.getCurrentAccessToken() != null) {
            if (!AccessToken.getCurrentAccessToken().isExpired()) {

                GraphRequest request2 = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "/NatGeoKorea/feed", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        try {
                            JSONObject jsonObject = graphResponse.getJSONObject();
//                            Log.i("Test ", " =" + graphResponse.getRawResponse());
                            JSONArray jArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jArray.length(); i++) {
                                HuffNews HFNS = new HuffNews();
                                JSONObject obj = jArray.getJSONObject(i);
                                try {
                                    String pictureLink = obj.getString("picture");
                                    HFNS.setPictureURL(pictureLink);
//                                    Log.i("feeds picture ", "  = " + pictureLink);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String desc = obj.getString("description");
                                    HFNS.setDescription(desc);
//                                    Log.i("feeds picture ", "  = " + desc);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String originalPageurls = obj.getString("link");
//                                    Log.i("feeds Url ", "  = " + originalPageurls);
                                    HFNS.setDestinationURL(originalPageurls);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    String message = obj.getString("message");
//                                    Log.i("feeds message ", "  = " + message);
                                    HFNS.setMessage(message);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String caption = obj.getString("caption");
                                    HFNS.setCaption(caption);
//                                    Log.i("feeds caption ", "  = " + caption);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String description = obj.getString("description");
                                    HFNS.setDescription(description);
//                                    Log.i("feeds description ", "  = " + description);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String icon = obj.getString("icon");
                                    HFNS.setIconURL(icon);
//                                    Log.i("feeds icon ", "  = " + icon);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String updated_time = obj.getString("updated_time");
                                    HFNS.setUDate(updated_time);
//                                    Log.i("feeds updated_time ", "  = " + updated_time);
                                } catch (Exception ex) {
//                                    Log.i("feeds ", "created time error");
                                    ex.printStackTrace();
                                }
                                try {
                                    String created_time = obj.getString("created_time");
                                    HFNS.setCDate(created_time);
//                                    Log.i("feeds created_time ", "  = " + created_time);
                                } catch (Exception ex) {
//                                    Log.i("feeds ", "created time error");
                                    ex.printStackTrace();
                                }
                                if (HFNS.getDestinationURL() != null & HFNS.getPictureURL() != null)
                                    mMagazineAdpater.addItem(HFNS);
//                                Log.i("String", "  =" + HFNS);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        mShowHandler.removeMessages(MESSAGE_HUFF_END);
                        mShowHandler.sendEmptyMessage(MESSAGE_HUFF_END);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,picture,link, message,caption,description,icon,updated_time,created_time");
                request2.setParameters(parameters);
                request2.executeAsync();
                loginButton.setVisibility(View.INVISIBLE);
                loginGuide.setVisibility(View.INVISIBLE);
            } else

            {
                loginButton.setVisibility(View.VISIBLE);
                loginGuide.setVisibility(View.VISIBLE);
            }
        }
        ;
    }

    public void getSisaIn() {

        if (AccessToken.getCurrentAccessToken() != null) {
            if (!AccessToken.getCurrentAccessToken().isExpired()) {

                GraphRequest request3 = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(), "/sisain/feed", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        try {
                            JSONObject jsonObject = graphResponse.getJSONObject();
                            JSONArray jArray = jsonObject.getJSONArray("data");

                            for (int i = 0; i < jArray.length(); i++) {
                                HuffNews HFNS = new HuffNews();
                                JSONObject obj = jArray.getJSONObject(i);
                                try {
                                    String pictureLink = obj.getString("picture");
                                    if (DEBUG)
                                        Log.i("special feeds picture ", "  = " + pictureLink);
                                    HFNS.setPictureURL(pictureLink);
                                    if (DEBUG)
                                        Log.i("special feeds picture ", "  = " + pictureLink);
                                } catch (Exception ex) {
                                    if (DEBUG) Log.i("special feeds picture ", "  = no picture");
                                    ex.printStackTrace();
                                }
                                try {
                                    String desc = obj.getString("description");
                                    HFNS.setDescription(desc);
                                    if (DEBUG) Log.i("sisa feeds picture ", "  = " + desc);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String originalPageurls = obj.getString("link");
                                    if (DEBUG) Log.i("sisa feeds Url ", "  = " + originalPageurls);
                                    HFNS.setDestinationURL(originalPageurls);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                try {
                                    String message = obj.getString("message");
                                    if (DEBUG) Log.i("sisa feeds message ", "  = " + message);
                                    HFNS.setMessage(message);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String caption = obj.getString("caption");
                                    HFNS.setCaption(caption);
                                    if (DEBUG) Log.i("sisa feeds caption ", "  = " + caption);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String description = obj.getString("description");
                                    HFNS.setDescription(description);
                                    if (DEBUG)
                                        Log.i("sisa feeds description ", "  = " + description);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String icon = obj.getString("icon");
                                    HFNS.setIconURL(icon);
                                    if (DEBUG) Log.i("sisa feeds icon ", "  = " + icon);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                try {
                                    String updated_time = obj.getString("updated_time");
                                    HFNS.setUDate(updated_time);
                                    if (DEBUG)
                                        Log.i("sisa feeds  ", " update_time = " + updated_time);
                                } catch (Exception ex) {
                                    if (DEBUG) Log.i("sisa feeds ", "created time error");
                                    ex.printStackTrace();
                                }
                                try {
                                    String created_time = obj.getString("created_time");
                                    HFNS.setCDate(created_time);
                                    if (DEBUG)
                                        Log.i("sisa feeds ", "  created_time = " + created_time);
                                } catch (Exception ex) {
                                    if (DEBUG) Log.i("sisa feeds ", "created time error");
                                    ex.printStackTrace();
                                }
                                if (HFNS.getDestinationURL() != null & HFNS.getPictureURL() != null)
                                    mMagazineAdpater.addItem(HFNS);
//                                Log.i("mWaterfallView", "  =" + HFNS);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                        mShowHandler.removeMessages(MESSAGE_SISA_END);
                        mShowHandler.sendEmptyMessage(MESSAGE_SISA_END);
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,picture,link, message,caption,description,icon,updated_time,created_time");
                request3.setParameters(parameters);
                request3.executeAsync();

                loginButton.setVisibility(View.INVISIBLE);
                loginGuide.setVisibility(View.INVISIBLE);
            } else

            {
                loginButton.setVisibility(View.VISIBLE);
                loginGuide.setVisibility(View.VISIBLE);
            }
        }
        ;
    }


}


//        accessTokenTracker = new
//        AccessTokenTracker() {
//@Override
//protected void onCurrentAccessTokenChanged(
//        AccessToken oldAccessToken,
//        AccessToken currentAccessToken) {
//        // Set the access token using
//        // currentAccessToken when it's loaded or set.
//        Log.i("AccessToken Changed", "really fragment...?" + currentAccessToken.getToken());
//        accessToken = currentAccessToken;
//        customLogInButton.setVisibility(View.VISIBLE);
//        }
//        }
//
//        ;
//        // If the access token is available already assign it.
//        accessToken = AccessToken.getCurrentAccessToken();
//
//
//        profileTracker = new
//
//        ProfileTracker() {
//@Override
//protected void onCurrentProfileChanged(
//        Profile oldProfile,
//        Profile currentProfile) {
//        Log.i("Profile Changed", "really fragment...?");
//        // App code
//        }
//        }
//
//        ;
//

