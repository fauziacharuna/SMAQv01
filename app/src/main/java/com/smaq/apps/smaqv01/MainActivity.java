package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.smaq.apps.smaqv01.Important.NewsActivity;
import com.smaq.apps.smaqv01.Important.Promo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity {

    SharedPreferences installationState;

    private List<Trade> tradeList = new ArrayList<>();
    private List<Trade> tradeRecommendedList = new ArrayList<>();

    private List<Promo> promoList = new ArrayList<>();
    private List<Promo> promoRecommendedList = new ArrayList<>();

    private TradeNormalAdapter tAdapter;
    private TradeRecommendedAdapter trAdapter;

    private PromoNormalAdapter pAdapter;
    private PromoRecommendedAdapter prAdapter;

    private RecyclerView recyclerTradeNormal, recyclerTradeRecommended, recyclerPromoNormal, recyclerPromoRecommended;

    final String welcomeState = "welcomeScreenShown";
    final String rootURL = Constants.ROOT_URL;
    final String getDataURL = rootURL+"apanel/get_data.php";
    final String tradeUrl = Constants.ROOT_URL+"apanel/get_sale_data.php";
    final String promoUrl = Constants.ROOT_URL+"apanel/get_promo_data.php";
    final String tradePostUrl = Constants.ROOT_URL+"apanel/add_sale_data.php";
    final String promoPostUrl = Constants.ROOT_URL+"apanel/add_promo_data.php";
//ilham
    public static String tradeDetailTitle, tradeDetailPrice, tradeDetailDescription;

    ImageView cover;
    de.hdodenhof.circleimageview.CircleImageView pp;
    Toolbar myToolbar;

    public boolean pictureChanged;
    private boolean tradeShowYourPost;
    private boolean tradeLoaded, promoLoaded;

    private ImageLoader imgLoader;// =  new ImageLoader(this);

    private ProgressDialog loadingProfile = null;
    private ProgressDialog loadingApp;

    //private ProfileActivity profileActivity = new ProfileActivity();

    private String TAG = MainActivity.class.getSimpleName();

    ImageButton mainProfileEditButton, mainLearningButton, mainNewsButton, mainAccountSettingButton, nongkrongButton, challengeButton, mvpButton, mainMenuMain, mainMenuNongkrongButton, mainMenuTradeButton, mainMenuChallengeButton, mainMenuPromotionButton;
    ImageView teamButton;
    private boolean postItShown = false;
    private boolean itemDetailShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //loadingApp = ProgressDialog.show(this,"Loading...","Please wait...",false,false);
        MainActivity.this.loadingProfile = ProgressDialog.show(MainActivity.this, "Working..", "Downloading Data...", true, false);

        Thread mThread = new Thread() {
            @Override
            public void run() {

                try
                {
                    sleep(1500);
                    cover = (ImageView) findViewById(R.id.profileImageBig);
                    pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);

                    pictureChanged = false;
                    tradeShowYourPost = false;
                    tradeLoaded = false;
                    promoLoaded = false;

                    addListenerOnButton();

                    if(getInstallationState()) {
                        imgLoader =  new ImageLoader(MainActivity.this);
                        UserProfile userPictures = new UserProfile();
                        userPictures.getUserPictures(MainActivity.this);

                        String profilePictureParameter = "&getpp=1&txtUserID=" + userPictures.getUserID(MainActivity.this);
                        String coverPictureParameter = "&getcp=1&txtUserID=" + userPictures.getUserID(MainActivity.this);

                        String profilePictureUrl = getData(profilePictureParameter);
                        String coverPictureUrl = getData(coverPictureParameter);

                        /**
                         setCoverPicture(rootURL+coverPictureUrl);
                         //Log.e(TAG, "userSavedCoverPictures: " + userPictures.userSavedCoverPictures);
                         setProfilePicture(rootURL+profilePictureUrl);
                         //Log.e(TAG, "userSavedProfilePictures: " + userPictures.userSavedProfilePictures);
                         */

                        //MainActivity.this.loadingProfile = ProgressDialog.show(MainActivity.this, "Working..", "Downloading Data...", true, false);
                        new MainActivity.loadingProfileData().execute(rootURL + profilePictureUrl, rootURL + coverPictureUrl);

                        //loadingApp.dismiss();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
        };
        mThread.start();

        //GetSaleData();
        //GetPromoData();
        //tAdapter.notifyDataSetChanged();
        //trAdapter.notifyDataSetChanged();

        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,?> keys = prefs.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }
        */

    }

    @Override
    protected void onResume()
    {
        super.onResume();

        ImageLoader imageLoader = new ImageLoader(this);

        if (Constants.pictureChanged)
        {
            Log.e(TAG, "pictureChanged: ");
            imageLoader.clearCache();

            UserProfile userPictures = new UserProfile();
            userPictures.getUserPictures(this);

            String profilePictureParameter = "&getpp=1&txtUserID="+userPictures.getUserID(this);
            String coverPictureParameter = "&getcp=1&txtUserID="+userPictures.getUserID(this);

            String profilePictureUrl = getData(profilePictureParameter);
            String coverPictureUrl = getData(coverPictureParameter);

            setCoverPicture(rootURL+coverPictureUrl);
            //Log.e(TAG, "userSavedCoverPictures: " + userPictures.userSavedCoverPictures);
            setProfilePicture(rootURL+profilePictureUrl);
            //Log.e(TAG, "userSavedProfilePictures: " + userPictures.userSavedProfilePictures);
        }
        else
        {
            Log.e(TAG, "resume: ");
        }

    }

    @Override
    public void onBackPressed() {
        final ConstraintLayout tradePostItContainer = (ConstraintLayout) findViewById(R.id.tradePostItContainer);
        final ConstraintLayout promoPostItContainer = (ConstraintLayout) findViewById(R.id.promoPostItContainer);
        final ConstraintLayout tradeDetailOverlay = (ConstraintLayout) findViewById(R.id.tradeItemDetailsOverlay);
        final ConstraintLayout promoDetailOverlay = (ConstraintLayout) findViewById(R.id.promoItemDetailsOverlay);

        if(postItShown)
        {
            tradePostItContainer.setVisibility(View.INVISIBLE);
            promoPostItContainer.setVisibility(View.INVISIBLE);
            postItShown = false;
        }
        else if(itemDetailShown)
        {
            tradeDetailOverlay.setVisibility(View.INVISIBLE);
            promoDetailOverlay.setVisibility(View.INVISIBLE);
            itemDetailShown = false;
        }
        else
        {
            super.onBackPressed();
        }
    }

    private void addListenerOnButton() {
        mainProfileEditButton = (ImageButton) findViewById(R.id.mainProfileEditButton);
        mainLearningButton = (ImageButton) findViewById(R.id.mainLearningButton);
        mainNewsButton = (ImageButton) findViewById(R.id.mainNewsButton);
        mainAccountSettingButton = (ImageButton) findViewById(R.id.mainProfileSettingButton);
        mainMenuNongkrongButton = (ImageButton) findViewById(R.id.mainMenuButtonNongkrong);
        mainMenuTradeButton = (ImageButton) findViewById(R.id.mainMenuButtonTrade);
        mainMenuChallengeButton = (ImageButton) findViewById(R.id.mainMenuButtonTrophy);
        mainMenuPromotionButton = (ImageButton) findViewById(R.id.mainMenuButtonPromotion);
        mainMenuMain = (ImageButton) findViewById(R.id.mainMenuButtonSchool);
        nongkrongButton = (ImageButton) findViewById(R.id.bgMainNongkrongButton);
        teamButton = (ImageView) findViewById(R.id.bgMainTeamButton);
        challengeButton = (ImageButton) findViewById(R.id.bgMainChallengeButton);
        mvpButton = (ImageButton) findViewById(R.id.bgMainMvpButton);

//*************************
//TOOLBAR BUTTON FUNCTION
//*************************
        mainProfileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mainAccountSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, AccountSettingsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

//*************************
//MAIN MENU BUTTON FUNCTION
//*************************
        final ConstraintLayout schoolPageContainer = (ConstraintLayout) findViewById(R.id.schoolPageContainer);
        final ConstraintLayout tradePostFunctionContainer = (ConstraintLayout) findViewById(R.id.tradePostFunctionContainer);
        final ConstraintLayout promoPostFunctionContainer = (ConstraintLayout) findViewById(R.id.promoPostFunctionContainer);

        //final ConstraintLayout tradeNormal = (ConstraintLayout) findViewById(R.id.trade_normal);
        //final ConstraintLayout promoNormal = (ConstraintLayout) findViewById(R.id.promotion_normal);

        final LinearLayout tradeNormal = (LinearLayout) findViewById(R.id.trade_normal);
        final LinearLayout promoNormal = (LinearLayout) findViewById(R.id.promotion_normal);

        final LinearLayout hangoutPageContainer = (LinearLayout) findViewById(R.id.hangoutPageContainer);
        final LinearLayout competitionPageContainer = (LinearLayout) findViewById(R.id.competitionPageContainer);

        //final ScrollView tradeNormalScroll = (ScrollView) findViewById(R.id.trade_normal_scroll);
        //final ScrollView promotionNormalScroll = (ScrollView) findViewById(R.id.promotion_normal_scroll);

        //final HorizontalScrollView tradeRecommended = (HorizontalScrollView) findViewById(R.id.trade_recommended);
        //final HorizontalScrollView promoRecommended = (HorizontalScrollView) findViewById(R.id.promotion_recommended);

        final LinearLayout tradeRecommended = (LinearLayout) findViewById(R.id.trade_recommended);
        final LinearLayout promoRecommended = (LinearLayout) findViewById(R.id.promotion_recommended);

        final FloatingActionButton tradeAddPost = (FloatingActionButton) findViewById(R.id.btFloatPostTrade);
        final FloatingActionButton promoAddPost = (FloatingActionButton) findViewById(R.id.btFloatPostPromotion);

        final ImageView selectorSchool = (ImageView) findViewById(R.id.menuSelectorSchool);
        final ImageView selectorHangout = (ImageView) findViewById(R.id.menuSelectorNongkrong);
        final ImageView selectorTrade = (ImageView) findViewById(R.id.menuSelectorTrade);
        final ImageView selectorTrophy = (ImageView) findViewById(R.id.menuSelectorTrophy);
        final ImageView selectorPromo = (ImageView) findViewById(R.id.menuSelectorPromotion);

        mainMenuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                schoolPageContainer.setVisibility(View.VISIBLE);

                hangoutPageContainer.setVisibility(View.INVISIBLE);

                tradeRecommended.setVisibility(View.INVISIBLE);
                tradeNormal.setVisibility(View.INVISIBLE);
                tradePostFunctionContainer.setVisibility(View.INVISIBLE);
                tradeAddPost.setVisibility(View.INVISIBLE);

                promoRecommended.setVisibility(View.INVISIBLE);
                promoNormal.setVisibility(View.INVISIBLE);
                promoPostFunctionContainer.setVisibility(View.INVISIBLE);
                promoAddPost.setVisibility(View.INVISIBLE);

                competitionPageContainer.setVisibility(View.INVISIBLE);

                selectorSchool.setVisibility(View.VISIBLE);
                selectorHangout.setVisibility(View.INVISIBLE);
                selectorTrade.setVisibility(View.INVISIBLE);
                selectorTrophy.setVisibility(View.INVISIBLE);
                selectorPromo.setVisibility(View.INVISIBLE);
            }
        });

        mainMenuNongkrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                schoolPageContainer.setVisibility(View.INVISIBLE);

                hangoutPageContainer.setVisibility(View.VISIBLE);

                tradeRecommended.setVisibility(View.INVISIBLE);
                tradeNormal.setVisibility(View.INVISIBLE);
                tradePostFunctionContainer.setVisibility(View.INVISIBLE);
                tradeAddPost.setVisibility(View.INVISIBLE);

                promoRecommended.setVisibility(View.INVISIBLE);
                promoNormal.setVisibility(View.INVISIBLE);
                promoPostFunctionContainer.setVisibility(View.INVISIBLE);
                promoAddPost.setVisibility(View.INVISIBLE);

                competitionPageContainer.setVisibility(View.INVISIBLE);

                selectorSchool.setVisibility(View.INVISIBLE);
                selectorHangout.setVisibility(View.VISIBLE);
                selectorTrade.setVisibility(View.INVISIBLE);
                selectorTrophy.setVisibility(View.INVISIBLE);
                selectorPromo.setVisibility(View.INVISIBLE);
            }
        });

        mainMenuTradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(tradeLoaded == false)
                {
                    LoadSaleSection();
                }

                schoolPageContainer.setVisibility(View.INVISIBLE);

                hangoutPageContainer.setVisibility(View.INVISIBLE);

                tradeRecommended.setVisibility(View.VISIBLE);
                tradeNormal.setVisibility(View.VISIBLE);
                tradePostFunctionContainer.setVisibility(View.VISIBLE);
                tradeAddPost.setVisibility(View.VISIBLE);

                promoRecommended.setVisibility(View.INVISIBLE);
                promoNormal.setVisibility(View.INVISIBLE);
                promoPostFunctionContainer.setVisibility(View.INVISIBLE);
                promoAddPost.setVisibility(View.INVISIBLE);

                competitionPageContainer.setVisibility(View.INVISIBLE);

                selectorSchool.setVisibility(View.INVISIBLE);
                selectorHangout.setVisibility(View.INVISIBLE);
                selectorTrade.setVisibility(View.VISIBLE);
                selectorTrophy.setVisibility(View.INVISIBLE);
                selectorPromo.setVisibility(View.INVISIBLE);

                //OverScrollDecoratorHelper.setUpOverScroll(tradeRecommended);
                //OverScrollDecoratorHelper.setUpOverScroll(tradeNormalScroll);
            }
        });

        mainMenuChallengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                schoolPageContainer.setVisibility(View.INVISIBLE);

                hangoutPageContainer.setVisibility(View.INVISIBLE);

                tradeRecommended.setVisibility(View.INVISIBLE);
                tradeNormal.setVisibility(View.INVISIBLE);
                tradePostFunctionContainer.setVisibility(View.INVISIBLE);
                tradeAddPost.setVisibility(View.INVISIBLE);

                promoRecommended.setVisibility(View.INVISIBLE);
                promoNormal.setVisibility(View.INVISIBLE);
                promoPostFunctionContainer.setVisibility(View.INVISIBLE);
                promoAddPost.setVisibility(View.INVISIBLE);

                competitionPageContainer.setVisibility(View.VISIBLE);

                selectorSchool.setVisibility(View.INVISIBLE);
                selectorHangout.setVisibility(View.INVISIBLE);
                selectorTrade.setVisibility(View.INVISIBLE);
                selectorTrophy.setVisibility(View.VISIBLE);
                selectorPromo.setVisibility(View.INVISIBLE);
            }
        });

        mainMenuPromotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(promoLoaded == false)
                {
                    LoadPromoSection();
                }

                schoolPageContainer.setVisibility(View.INVISIBLE);

                hangoutPageContainer.setVisibility(View.INVISIBLE);

                tradeRecommended.setVisibility(View.INVISIBLE);
                tradeNormal.setVisibility(View.INVISIBLE);
                tradePostFunctionContainer.setVisibility(View.INVISIBLE);
                tradeAddPost.setVisibility(View.INVISIBLE);

                promoRecommended.setVisibility(View.VISIBLE);
                promoNormal.setVisibility(View.VISIBLE);
                promoPostFunctionContainer.setVisibility(View.VISIBLE);
                promoAddPost.setVisibility(View.VISIBLE);

                competitionPageContainer.setVisibility(View.INVISIBLE);

                selectorSchool.setVisibility(View.INVISIBLE);
                selectorHangout.setVisibility(View.INVISIBLE);
                selectorTrade.setVisibility(View.INVISIBLE);
                selectorTrophy.setVisibility(View.INVISIBLE);
                selectorPromo.setVisibility(View.VISIBLE);

                //OverScrollDecoratorHelper.setUpOverScroll(promoRecommended);
                //OverScrollDecoratorHelper.setUpOverScroll(promotionNormalScroll);
            }
        });

//*************************
//SCHOOL PAGE BUTTON FUNCTION
//*************************
        mainLearningButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, LearningActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mainNewsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, NewsActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

//*************************
//HANGOUT PAGE BUTTON FUNCTION
//*************************
        nongkrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, NongkrongActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        teamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, FindTeamActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

//*************************
//CHALLENGE PAGE BUTTON FUNCTION
//*************************
        challengeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, ChallengeActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        mvpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, MvpActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

//*************************
//TRADE BUTTON FUNCTION
//*************************
        recyclerTradeNormal = (RecyclerView) findViewById(R.id.trade_normal_recycler);
        recyclerTradeRecommended = (RecyclerView) findViewById(R.id.trade_recommended_recycler);

        final ConstraintLayout tradeDetailOverlay = (ConstraintLayout) findViewById(R.id.tradeItemDetailsOverlay);
        final ImageView tradeDetailImage = (ImageView) findViewById(R.id.imageTradeDetail);
        final TextView textTradeDetailTitle = (TextView) findViewById(R.id.textTradeDetailTitle);
        final TextView textTradeDetailPrice = (TextView) findViewById(R.id.textTradeDetailPrice);
        final TextView textTradeDetailDescription = (TextView) findViewById(R.id.textTradeDetailDescription);

        recyclerTradeNormal.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                //Log.e(TAG, "tradeNormal: onInterceptTouchEvent");
                Constants.TRADE_DETAIL_OVERLAY = tradeDetailOverlay;
                Constants.TEXT_TRADE_DETAIL_TITLE = textTradeDetailTitle;
                Constants.TEXT_TRADE_DETAIL_PRICE = textTradeDetailPrice;
                Constants.TEXT_TRADE_DETAIL_DESCRIPTION = textTradeDetailDescription;
                Constants.TRADE_DETAIL_IMAGE = tradeDetailImage;
                itemDetailShown = true;
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e(TAG, "tradeNormal: onTouchEvent");
                //tradeDetailOverlay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerTradeRecommended.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Constants.TRADE_DETAIL_OVERLAY = tradeDetailOverlay;
                Constants.TEXT_TRADE_DETAIL_TITLE = textTradeDetailTitle;
                Constants.TEXT_TRADE_DETAIL_PRICE = textTradeDetailPrice;
                Constants.TEXT_TRADE_DETAIL_DESCRIPTION = textTradeDetailDescription;
                Constants.TRADE_DETAIL_IMAGE = tradeDetailImage;
                itemDetailShown = true;
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e(TAG, "tradeNormal: onTouchEvent");

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        tradeDetailOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tradeDetailOverlay.setVisibility(View.INVISIBLE);
            }
        });

//*************************
//PROMO BUTTON FUNCTION
//*************************
        recyclerPromoNormal = (RecyclerView) findViewById(R.id.promotion_normal_recycler);
        recyclerPromoRecommended = (RecyclerView) findViewById(R.id.promotion_recommended_recycler);

        final ConstraintLayout promoDetailOverlay = (ConstraintLayout) findViewById(R.id.promoItemDetailsOverlay);
        final ImageView promoDetailImage = (ImageView) findViewById(R.id.imagePromoDetail);
        final TextView textPromoDetailTitle = (TextView) findViewById(R.id.textPromoDetailTitle);
        final TextView textPromoDetailDate = (TextView) findViewById(R.id.textPromoDetailDate);
        final TextView textPromoDetailDescription = (TextView) findViewById(R.id.textPromoDetailDescription);

        recyclerPromoNormal.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                //Log.e(TAG, "tradeNormal: onInterceptTouchEvent");
                Constants.PROMO_DETAIL_OVERLAY = promoDetailOverlay;
                Constants.TEXT_PROMO_DETAIL_TITLE = textPromoDetailTitle;
                Constants.TEXT_PROMO_DETAIL_DATE = textPromoDetailDate;
                Constants.TEXT_PROMO_DETAIL_DESCRIPTION = textPromoDetailDescription;
                Constants.PROMO_DETAIL_IMAGE = promoDetailImage;
                itemDetailShown = true;
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e(TAG, "tradeNormal: onTouchEvent");
                //tradeDetailOverlay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        recyclerPromoRecommended.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                Constants.PROMO_DETAIL_OVERLAY = promoDetailOverlay;
                Constants.TEXT_PROMO_DETAIL_TITLE = textPromoDetailTitle;
                Constants.TEXT_PROMO_DETAIL_DATE = textPromoDetailDate;
                Constants.TEXT_PROMO_DETAIL_DESCRIPTION = textPromoDetailDescription;
                Constants.PROMO_DETAIL_IMAGE = promoDetailImage;
                itemDetailShown = true;
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                Log.e(TAG, "tradeNormal: onTouchEvent");

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        promoDetailOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoDetailOverlay.setVisibility(View.INVISIBLE);
                itemDetailShown = false;
            }
        });

//*************************
//TRADE POST IT BUTTON FUNCTION
//*************************
        final ImageButton tradePostIt = (ImageButton) findViewById(R.id.btFloatPostTrade);
        final ImageButton tradePostItButtonCancel = (ImageButton) findViewById(R.id.tradePostItButtonCancel);
        final ImageButton tradePostItButtonOk = (ImageButton) findViewById(R.id.tradePostItButtonOk);
        final ImageButton tradePostItButtonCamera = (ImageButton) findViewById(R.id.tradePostItCamera);

        final EditText tradePostItTitle = (EditText) findViewById(R.id.tradePostItTitle);
        final EditText tradePostItDescription = (EditText) findViewById(R.id.tradePostItContent);
        final EditText tradePostItPrice = (EditText) findViewById(R.id.tradePostItPrice);

        final ConstraintLayout tradePostItContainer = (ConstraintLayout) findViewById(R.id.tradePostItContainer);

        tradePostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tradePostItTitle.setText("");
                tradePostItDescription.setText("");
                tradePostItPrice.setText("");
                tradePostItContainer.setVisibility(View.VISIBLE);
                postItShown = true;
            }
        });

        tradePostItButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tradePostItContainer.setVisibility(View.INVISIBLE);
                postItShown = false;
            }
        });

        tradePostItButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tradePostItTitle.getText().toString().equals("")
                        || tradePostItDescription.getText().toString().equals("")
                        || tradePostItPrice.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Data kurang lengkap", Toast.LENGTH_LONG).show();
                    return;
                }

                loadingApp = ProgressDialog.show(MainActivity.this,"Loading...","Please wait...",false,false);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        PostTradeData();
                        loadingApp.dismiss();
                    }
                };
                mThread.start();

                tradePostItContainer.setVisibility(View.INVISIBLE);
            }
        });

        tradePostItButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(tradePostItButtonCamera);
            }
        });

        tradePostItContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//*************************
//PROMOTION POST IT BUTTON FUNCTION
//*************************
        final ImageButton promoPostIt = (ImageButton) findViewById(R.id.btFloatPostPromotion);
        final ImageButton promoPostItButtonCancel = (ImageButton) findViewById(R.id.promoPostItButtonCancel);
        final ImageButton promoPostItButtonOk = (ImageButton) findViewById(R.id.promoPostItButtonOk);
        final ImageButton promoPostItButtonCamera = (ImageButton) findViewById(R.id.promoPostItCamera);

        final EditText promoPostItTitle = (EditText) findViewById(R.id.promoPostItTitle);
        final EditText promoPostItDescription = (EditText) findViewById(R.id.promoPostItContent);
        final EditText promoPostItPrice = (EditText) findViewById(R.id.promoPostItPrice);

        final ConstraintLayout promoPostItContainer = (ConstraintLayout) findViewById(R.id.promoPostItContainer);

        promoPostIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoPostItContainer.setVisibility(View.VISIBLE);
                postItShown = true;
            }
        });

        promoPostItButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promoPostItContainer.setVisibility(View.INVISIBLE);
                postItShown = false;
            }
        });

        promoPostItButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(promoPostItTitle.getText().toString().equals("")
                        || promoPostItDescription.getText().toString().equals("")
                        || promoPostItPrice.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Data kurang lengkap", Toast.LENGTH_LONG).show();
                    return;
                }

                loadingApp = ProgressDialog.show(MainActivity.this,"Loading...","Please wait...",false,false);

                Thread mThread = new Thread() {
                    @Override
                    public void run() {
                        PostPromoData();
                        loadingApp.dismiss();
                    }
                };
                mThread.start();

                promoPostItContainer.setVisibility(View.INVISIBLE);
            }
        });

        promoPostItButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser(promoPostItButtonCamera);
            }
        });

        promoPostItContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

//*************************
//TRADE FILTER BUTTON FUNCTION
//*************************
        final TextView textTradeYourPost = (TextView) findViewById(R.id.textTradeYourPost);

        textTradeYourPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tradeShowYourPost == false)
                {
                    recyclerTradeNormal.setAdapter(null);
                    tradeList.clear();

                    tAdapter = new TradeNormalAdapter(tradeList);
                    recyclerTradeNormal.setItemAnimator(new DefaultItemAnimator());
                    recyclerTradeNormal.setAdapter(tAdapter);

                    recyclerTradeRecommended.setAdapter(null);
                    tradeRecommendedList.clear();

                    trAdapter = new TradeRecommendedAdapter(tradeRecommendedList);
                    recyclerTradeRecommended.setItemAnimator(new DefaultItemAnimator());
                    recyclerTradeRecommended.setAdapter(trAdapter);

                    loadingApp = ProgressDialog.show(MainActivity.this,"Loading...","Please wait...",false,false);

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            tradeShowYourPost = true;

                            GetSaleData();
                            loadingApp.dismiss();
                        }
                    };
                    mThread.start();

                    textTradeYourPost.setText("All Post");
                }
                else
                {
                    recyclerTradeNormal.setAdapter(null);
                    tradeList.clear();

                    tAdapter = new TradeNormalAdapter(tradeList);
                    recyclerTradeNormal.setItemAnimator(new DefaultItemAnimator());
                    recyclerTradeNormal.setAdapter(tAdapter);

                    recyclerTradeRecommended.setAdapter(null);
                    tradeRecommendedList.clear();

                    trAdapter = new TradeRecommendedAdapter(tradeRecommendedList);
                    recyclerTradeRecommended.setItemAnimator(new DefaultItemAnimator());
                    recyclerTradeRecommended.setAdapter(trAdapter);

                    loadingApp = ProgressDialog.show(MainActivity.this,"Loading...","Please wait...",false,false);

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            tradeShowYourPost = false;

                            GetSaleData();
                            loadingApp.dismiss();
                        }
                    };
                    mThread.start();

                    textTradeYourPost.setText("Your Post");
                }
            }
        });
    }

    public boolean getInstallationState()
    {
        installationState = PreferenceManager.getDefaultSharedPreferences(this);

        // second argument is the default to use if the preference can't be found
        Boolean welcomeScreenShown = installationState.getBoolean(welcomeState, false);

        if (!welcomeScreenShown)
        {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //loadingApp.dismiss();
                    loadingProfile.dismiss();
                }
            });

            Intent myIntent = new Intent(MainActivity.this, WelcomeActivity.class);
            MainActivity.this.startActivity(myIntent);
            MainActivity.this.finish();
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setProfilePicture(String url)
    {
        //ProfileActivity profile = new ProfileActivity();
        //String url = "http://192.168.42.30/smaq/profile_picture/saya.png";
        de.hdodenhof.circleimageview.CircleImageView pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);

        if(Constants.pictureChanged) {
            loadImageFromURL(url,pp);
        }
        else {
            imgLoader.DisplayImage(url, pp);
        }
    }

    public void setCoverPicture(String url)
    {
        //ProfileActivity profile = new ProfileActivity();
        //String url = "http://192.168.42.30/smaq/cover_picture/saya.png";
        ImageView cover = (ImageView) findViewById(R.id.profileImageBig);

        if(Constants.pictureChanged) {
            loadImageFromURL(url, cover);
        }
        else {
            imgLoader.DisplayImage(url, cover);
        }
    }

    public void loadImageFromURL(String fileUrl, ImageView iv)
    {
        try
        {
            URL myFileUrl = new URL (fileUrl);
            HttpURLConnection conn =
                    (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            iv.setImageBitmap(BitmapFactory.decodeStream(is));
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getData(String parameters)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;
        String response = null;

        try
        {
            URL url = new URL(getDataURL);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();

            /**
             Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */

            isr.close();
            reader.close();

        }
        catch (IOException e){
            // Error
            Log.e(TAG, "Couldn't get json from server. error 2");
            // Toast.makeText(context,"Error!!", Toast.LENGTH_LONG).show();

        }

        return response;

    }

    private class loadingProfileData extends AsyncTask<String,Void,Bitmap[]> {
        @Override
        protected Bitmap[] doInBackground(String... urls) {
            Bitmap[] cached= new Bitmap[2];
            Bitmap cachedPP, cachedCover;
            cachedPP = imgLoader.getCachedImage(urls[0]);
            cachedCover = imgLoader.getCachedImage(urls[1]);
            cached[0] = cachedPP;
            cached[1] = cachedCover;
            return cached;
        }
        @Override
        protected void onPostExecute(Bitmap[] bitmap) {
            MainActivity.this.pp.setImageBitmap(bitmap[0]);
            MainActivity.this.cover.setImageBitmap(bitmap[1]);
            /**
             ProfileActivity.this.cachedImage = bitmap;
             */
            MainActivity.this.loadingProfile.dismiss();
        }
    }

    protected void LoadSaleSection()
    {
        MainActivity.this.loadingProfile = ProgressDialog.show(MainActivity.this, "Working..", "Downloading Data...", true, false);
        recyclerTradeNormal = (RecyclerView) findViewById(R.id.trade_normal_recycler);
        tAdapter = new TradeNormalAdapter(tradeList);
        RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTradeNormal.setLayoutManager(normalLayoutManager);
        recyclerTradeNormal.setItemAnimator(new DefaultItemAnimator());
        recyclerTradeNormal.setAdapter(tAdapter);

        recyclerTradeRecommended = (RecyclerView) findViewById(R.id.trade_recommended_recycler);
        trAdapter = new TradeRecommendedAdapter(tradeRecommendedList);
        RecyclerView.LayoutManager recommendedLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerTradeRecommended.setLayoutManager(recommendedLayoutManager);
        recyclerTradeRecommended.setItemAnimator(new DefaultItemAnimator());
        recyclerTradeRecommended.setAdapter(trAdapter);

        OverScrollDecoratorHelper.setUpOverScroll(recyclerTradeNormal, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerTradeRecommended, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        Thread tradeThread = new Thread(){
            public void run(){

                //tradeLoaded = true;
                /*
                if(tradeLoaded) {
                    MainActivity.this.loadingProfile.dismiss();
                }
                */
                try {
                    sleep(300);
                    GetSaleData();
                    MainActivity.this.loadingProfile.dismiss();
                } catch (InterruptedException e) {
                    MainActivity.this.loadingProfile.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();
    }

    protected void LoadPromoSection()
    {
        MainActivity.this.loadingProfile = ProgressDialog.show(MainActivity.this, "Working..", "Downloading Data...", true, false);

        recyclerPromoNormal = (RecyclerView) findViewById(R.id.promotion_normal_recycler);
        pAdapter = new PromoNormalAdapter(promoList);
        RecyclerView.LayoutManager normalPromoLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerPromoNormal.setLayoutManager(normalPromoLayoutManager);
        recyclerPromoNormal.setItemAnimator(new DefaultItemAnimator());
        recyclerPromoNormal.setAdapter(pAdapter);

        recyclerPromoRecommended = (RecyclerView) findViewById(R.id.promotion_recommended_recycler);
        prAdapter = new PromoRecommendedAdapter(promoRecommendedList);
        RecyclerView.LayoutManager recommendedPromoLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerPromoRecommended.setLayoutManager(recommendedPromoLayoutManager);
        recyclerPromoRecommended.setItemAnimator(new DefaultItemAnimator());
        recyclerPromoRecommended.setAdapter(prAdapter);

        OverScrollDecoratorHelper.setUpOverScroll(recyclerPromoNormal, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerPromoRecommended, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        Thread promoThread = new Thread(){
            public void run(){

                try {
                    sleep(300);
                    GetPromoData();
                    MainActivity.this.loadingProfile.dismiss();
                } catch (InterruptedException e) {
                    MainActivity.this.loadingProfile.dismiss();
                    e.printStackTrace();
                }
            }
        };
        promoThread.start();
    }

    protected void GetSaleData()
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "";

        UserProfile profile = new UserProfile();
        String profileid = profile.getUserID(this);

        try
        {
            url = new URL(tradeUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();

            // You can perform UI operations here
            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
//                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
//                    JSONArray profiles = jsonObj.getJSONArray("profile");
                    JSONArray trades = new JSONArray(response);

                    // looping through All Contacts
                    for (int i = 0; i < trades.length(); i++)
                    {
//                        JSONObject c = jsonObj.getJSONObject("profile");
                        JSONObject c = trades.getJSONObject(i);

                        String sale_id = c.getString("sale_id");
                        String sale_title = c.getString("sale_title");
                        String sale_price = c.getString("sale_price");
                        String sale_description = c.getString("sale_description");
                        String sale_imageurl = c.getString("sale_imageurl");
                        String sale_posterid = c.getString("sale_posterid");
                        String sale_priority = c.getString("sale_priority");

                        // tmp hash map for single contact
//                        HashMap<String, String> trade = new HashMap<>();

                        // adding each child node to HashMap key => value
                        /**
                        trade.put("sale_id", sale_id);
                        trade.put("sale_title", sale_title);
                        trade.put("sale_price", sale_price);
                        trade.put("sale_description", sale_description);
                        trade.put("sale_imageurl", sale_imageurl);
                        trade.put("sale_posterid", sale_posterid);
                        trade.put("sale_priority", sale_priority);
                         */

                        Trade trade = new Trade(sale_id, sale_title, sale_price, sale_description, sale_imageurl, sale_posterid, sale_priority);

                        // adding contact to contact list
                        //Log.e(TAG, "sale_posterid: " + sale_posterid);
                        //Log.e(TAG, "profileid: " + profileid);
                        //Log.e(TAG, "tradeShowYourPost: " + tradeShowYourPost);
                        if(tradeShowYourPost == true)
                        {
                            if(sale_posterid.equals(profileid))
                            {
                                tradeList.add(trade);
                            }
                            else
                            {
                                Log.e(TAG, "skipped " + sale_title);
                            }
                        }
                        else
                        {
                            tradeList.add(trade);
                        }

                        if(!sale_priority.equals("99"))
                        {
                            tradeRecommendedList.add(trade);
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tAdapter.notifyDataSetChanged();
                            trAdapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Log.e(TAG, response);
                }
            }
            else if(response.equals("invalid\n"))
            {
                Log.e(TAG, "Invalid Input");
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
            }
            /**
             Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            // Toast.makeText(this,"Error!!\n"+tradeUrl, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

    protected void GetPromoData()
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "";

        try
        {
            url = new URL(promoUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();

            // You can perform UI operations here
            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
//                    JSONObject jsonObj = new JSONObject(response);

                    // Getting JSON Array node
//                    JSONArray profiles = jsonObj.getJSONArray("profile");
                    JSONArray trades = new JSONArray(response);

                    // looping through All Contacts
                    for (int i = 0; i < trades.length(); i++)
                    {
//                        JSONObject c = jsonObj.getJSONObject("profile");
                        JSONObject c = trades.getJSONObject(i);

                        String promo_id = c.getString("promo_id");
                        String promo_title = c.getString("promo_title");
                        String promo_description = c.getString("promo_description");
                        String promo_date = c.getString("promo_date");
                        String promo_imageurl = c.getString("promo_imageurl");
                        String promo_priority = c.getString("promo_priority");

                        // tmp hash map for single contact
//                        HashMap<String, String> trade = new HashMap<>();

                        // adding each child node to HashMap key => value
                        /**
                         trade.put("sale_id", sale_id);
                         trade.put("sale_title", sale_title);
                         trade.put("sale_price", sale_price);
                         trade.put("sale_description", sale_description);
                         trade.put("sale_imageurl", sale_imageurl);
                         trade.put("sale_posterid", sale_posterid);
                         trade.put("sale_priority", sale_priority);
                         */

                        Promo promo = new Promo(promo_id, promo_title, promo_description, promo_date, promo_imageurl, promo_priority);

                        // adding contact to contact list
                        promoList.add(promo);

                        Log.e(TAG, promo_priority);
                        if(!promo_priority.equals("99"))
                        {
                            Log.e(TAG, promo_imageurl);
                            promoRecommendedList.add(promo);
                        }
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pAdapter.notifyDataSetChanged();
                            prAdapter.notifyDataSetChanged();
                        }
                    });
                }
                catch (final JSONException e)
                {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    Log.e(TAG, response);
                }
            }
            else if(response.equals("invalid\n"))
            {
                Log.e(TAG, "Invalid Input");
            }
            else
            {
                Log.e(TAG, "Couldn't get json from server.");
            }
            /**
             Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
             */
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
            // Toast.makeText(this,"Error!!\n"+tradeUrl, Toast.LENGTH_LONG).show();
            Log.e(TAG, "Couldn't get json from server.");
        }
    }

    public void PostTradeData()
    {
        EditText tradePostItTitle = (EditText) findViewById(R.id.tradePostItTitle);
        EditText tradePostItDescription = (EditText) findViewById(R.id.tradePostItContent);
        EditText tradePostItPrice = (EditText) findViewById(R.id.tradePostItPrice);

        UserProfile profile = new UserProfile();

        String title = tradePostItTitle.getText().toString();
        String description = tradePostItDescription.getText().toString();
        String price = tradePostItPrice.getText().toString();
        String imageurl = "trade_pictures/"+fileNameString+".png";
        String posterid = profile.getUserID(this);
        String priority = "99";

        // saving to server database
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtTitle="+title+"&txtPrice="+price+"&txtDescription="+description+"&txtImageurl="+imageurl+"&txtPosterid="+posterid+"&txtPriority="+priority;

        try {
            url = new URL(tradePostUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            uploadImage(Constants.ROOT_URL+"trade_picture.php");
            Log.e(TAG, "response: "+response);

            recyclerTradeNormal = (RecyclerView) findViewById(R.id.trade_normal_recycler);
            recyclerTradeRecommended = (RecyclerView) findViewById(R.id.trade_recommended_recycler);
            recyclerTradeNormal.setAdapter(null);
            recyclerTradeRecommended.setAdapter(null);
            tradeList.clear();
            tradeRecommendedList.clear();

            tAdapter = new TradeNormalAdapter(tradeList);
            recyclerTradeNormal.setItemAnimator(new DefaultItemAnimator());
            recyclerTradeNormal.setAdapter(tAdapter);

            trAdapter = new TradeRecommendedAdapter(tradeRecommendedList);
            recyclerTradeRecommended.setItemAnimator(new DefaultItemAnimator());
            recyclerTradeRecommended.setAdapter(trAdapter);

            GetSaleData();
        }
        catch (IOException e){
                // Error
                Log.e(TAG, "response: "+response);
                //Toast.makeText(MainActivity.this,"Error!!", Toast.LENGTH_LONG).show();
        }

    }

    public void PostPromoData()
    {
        EditText promoPostItTitle = (EditText) findViewById(R.id.promoPostItTitle);
        EditText promoPostItDescription = (EditText) findViewById(R.id.promoPostItContent);
        EditText promoPostItDate = (EditText) findViewById(R.id.promoPostItPrice);

        String title = promoPostItTitle.getText().toString();
        String description = promoPostItDescription.getText().toString();
        String date = promoPostItDate.getText().toString();
        String imageurl = "promo_pictures/"+fileNameString+".png";
        String priority = "99";

        // saving to server database
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtTitle="+title+"&txtDescription="+description+"&txtDate="+date+"&txtImageurl="+imageurl+"&txtPriority="+priority;

        try {
            url = new URL(promoPostUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }

            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            uploadImage(Constants.ROOT_URL+"promo_picture.php");
            Log.e(TAG, "response: "+response);

            recyclerPromoNormal = (RecyclerView) findViewById(R.id.promotion_normal_recycler);
            recyclerPromoRecommended = (RecyclerView) findViewById(R.id.promotion_recommended_recycler);
            recyclerPromoNormal.setAdapter(null);
            recyclerPromoRecommended.setAdapter(null);
            promoList.clear();
            promoRecommendedList.clear();

            pAdapter = new PromoNormalAdapter(promoList);
            recyclerPromoNormal.setItemAnimator(new DefaultItemAnimator());
            recyclerPromoNormal.setAdapter(pAdapter);

            prAdapter = new PromoRecommendedAdapter(promoRecommendedList);
            recyclerPromoRecommended.setItemAnimator(new DefaultItemAnimator());
            recyclerPromoRecommended.setAdapter(prAdapter);

            GetPromoData();
        }
        catch (IOException e){
            // Error
            Log.e(TAG, "response: "+response);
            //Toast.makeText(MainActivity.this,"Error!!", Toast.LENGTH_LONG).show();
        }

    }

    public static void ShowTradeDetailPopup(ConstraintLayout tradeDetailOverlay, ImageView tradeDetailImage, TextView textTradeDetailTitle, TextView textTradeDetailPrice, TextView textTradeDetailDescription)
    {
        //recyclerTradeNormal = (RecyclerView) findViewById(R.id.trade_normal_recycler);
        //recyclerTradeRecommended = (RecyclerView) findViewById(R.id.trade_recommended_recycler);

        textTradeDetailTitle.setText(tradeDetailTitle);
        textTradeDetailPrice.setText(tradeDetailPrice);
        textTradeDetailDescription.setText(tradeDetailDescription);
        tradeDetailOverlay.setVisibility(View.VISIBLE);

    }


//    ********************
//    CHANGE AND SAVE PICTURE START
//    ********************

    private String fileNameString = "image";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private ImageView changedImage;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    private void showFileChooser(ImageView changedPicture)
    {
        changedImage = changedPicture;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            UserProfile profile = new UserProfile();
            String userID = profile.getUserID(this);

            fileNameString = userID+"-"+ts; //getFileName(filePath);

            try {
                //Getting the Bitmap from Gallery
                Bitmap tmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = getResizedBitmap(tmp, 750);
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //upload to server
                //uploadImage(folderURL);

                //Setting the Bitmap to ImageView
                changedImage.setImageBitmap(bitmap);
                Log.e(TAG, "pictureChanged: ");
                Constants.pictureChanged = true;
                ImageLoader imageLoader = new ImageLoader(this);
                imageLoader.clearCache();

                //Toast.makeText(this,folderURL, Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage(String uploadFolder)
    {
        if(fileNameString.equals("") || fileNameString.equals("image"))
        {
            return;
        }

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadFolder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(MainActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(MainActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Getting Image Name
                String name = fileNameString;

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);
                params.put(KEY_NAME, name);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

//    ********************
//    CHANGE AND SAVE PICTURE END
//    ********************

    public void dismissProgressDialog()
    {
        MainActivity.this.loadingProfile.dismiss();
    }
}
