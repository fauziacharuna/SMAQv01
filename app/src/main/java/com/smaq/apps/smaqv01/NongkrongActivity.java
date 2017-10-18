package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class NongkrongActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageButton nongkrongBackButton, nongkrongSearchButton;
    TextView textFashion, textEntertainment, textSport, textTravel, textShop, textScience, textTechno, textFinance;
    ConstraintLayout popupFriendListContainer;
    Animation slideSearch;
    android.widget.SearchView searchField;
    boolean searchFieldShown;

    private RecyclerView recyclerProfiles;
    private ProfileListRecyclerAdapter pAdapter;
    private List<ProfileList> profileLists = new ArrayList<>();
    private ProgressDialog loadingProfiles;

    private String TAG = NongkrongActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nongkrong);

        searchFieldShown = false;
        searchField = (android.widget.SearchView) findViewById(R.id.nongkrongSearchField);
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchField.findViewById(searchField.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.BLACK);
        search_text.setGravity(Gravity.BOTTOM);

        addListenerOnButton();
        addListenerOnText();

        LoadProfiles();

        searchField.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchField();
                return false;
            }
        });

        popupFriendListContainer = (ConstraintLayout) findViewById(R.id.popupFriendsContainerOverlay);
        popupFriendListContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupFriendListContainer.setVisibility(View.INVISIBLE);
            }
        });
    }

    private void addListenerOnButton() {
        nongkrongBackButton = (ImageButton) findViewById(R.id.nongkrongBackButton);
        nongkrongSearchButton = (ImageButton) findViewById(R.id.nongkrongSearchButton);

        nongkrongBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                NongkrongActivity.this.finish();
            }
        });

        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_search);
        slideSearch.setAnimationListener(this);

        searchField = (android.widget.SearchView) findViewById(R.id.nongkrongSearchField);

        nongkrongSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchField.startAnimation(slideSearch);
                searchField.setVisibility(View.VISIBLE);
                searchFieldShown = true;
            }
        });

        final ConstraintLayout popupFriendProfileOverlay = (ConstraintLayout) findViewById(R.id.popupFriendProfileOverlay);
        final ConstraintLayout popupFriendProfileContainer = (ConstraintLayout) findViewById(R.id.popupFriendProfileContainer);
        popupFriendListContainer = (ConstraintLayout) findViewById(R.id.popupFriendsContainerOverlay);
        final ImageButton profileBackButton = (ImageButton) findViewById(R.id.profileBackButton);

        /*
        for (int i=0;i<12;i++)
        {
            subjects[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupFriendListContainer.setVisibility(View.INVISIBLE);
                    popupFriendProfileOverlay.setVisibility(View.VISIBLE);
                }
            });
        }
        */

        profileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupFriendProfileOverlay.setVisibility(View.INVISIBLE);
                popupFriendListContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addListenerOnText()
    {
        // tab 1
        TextView basicInfo = (TextView) findViewById(R.id.textView9);
        TextView schoolInfo = (TextView) findViewById(R.id.textView10);
        TextView eventOrganization = (TextView) findViewById(R.id.textView11);
        TextView hobbyInterest = (TextView) findViewById(R.id.subTabInterest);

        final ImageView basicInfoHighlight = (ImageView) findViewById(R.id.basicInfoHighlight);
        final ImageView SchoolInfoHighlight = (ImageView) findViewById(R.id.schoolHighlight);
        final ImageView eventOrganizationHighlight = (ImageView) findViewById(R.id.organizationHighlight);
        final ImageView hobbyInterestHighlight = (ImageView) findViewById(R.id.interestHighlight);

        final ConstraintLayout basicInfoContainer = (ConstraintLayout) findViewById(R.id.basicInfoContainer);
        final ConstraintLayout schoolInfoContainer = (ConstraintLayout) findViewById(R.id.schoolInfoContainer);
        final ConstraintLayout organizationInfoContainer = (ConstraintLayout) findViewById(R.id.organizationInfoContainer);
        final ConstraintLayout hobbyInterestContainer = (ConstraintLayout) findViewById(R.id.hobbyInterestContainer);

        basicInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInfoHighlight.setVisibility(View.VISIBLE);
                SchoolInfoHighlight.setVisibility(View.INVISIBLE);
                eventOrganizationHighlight.setVisibility(View.INVISIBLE);
                hobbyInterestHighlight.setVisibility(View.INVISIBLE);

                basicInfoContainer.setVisibility(View.VISIBLE);
                schoolInfoContainer.setVisibility(View.INVISIBLE);
                organizationInfoContainer.setVisibility(View.INVISIBLE);
                hobbyInterestContainer.setVisibility(View.INVISIBLE);
            }
        });

        schoolInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInfoHighlight.setVisibility(View.INVISIBLE);
                SchoolInfoHighlight.setVisibility(View.VISIBLE);
                eventOrganizationHighlight.setVisibility(View.INVISIBLE);
                hobbyInterestHighlight.setVisibility(View.INVISIBLE);

                basicInfoContainer.setVisibility(View.INVISIBLE);
                schoolInfoContainer.setVisibility(View.VISIBLE);
                organizationInfoContainer.setVisibility(View.INVISIBLE);
                hobbyInterestContainer.setVisibility(View.INVISIBLE);
            }
        });

        eventOrganization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInfoHighlight.setVisibility(View.INVISIBLE);
                SchoolInfoHighlight.setVisibility(View.INVISIBLE);
                eventOrganizationHighlight.setVisibility(View.VISIBLE);
                hobbyInterestHighlight.setVisibility(View.INVISIBLE);

                basicInfoContainer.setVisibility(View.INVISIBLE);
                schoolInfoContainer.setVisibility(View.INVISIBLE);
                organizationInfoContainer.setVisibility(View.VISIBLE);
                hobbyInterestContainer.setVisibility(View.INVISIBLE);
            }
        });

        hobbyInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                basicInfoHighlight.setVisibility(View.INVISIBLE);
                SchoolInfoHighlight.setVisibility(View.INVISIBLE);
                eventOrganizationHighlight.setVisibility(View.INVISIBLE);
                hobbyInterestHighlight.setVisibility(View.VISIBLE);

                basicInfoContainer.setVisibility(View.INVISIBLE);
                schoolInfoContainer.setVisibility(View.INVISIBLE);
                organizationInfoContainer.setVisibility(View.INVISIBLE);
                hobbyInterestContainer.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if(searchFieldShown) {
            searchShowInput();
        }

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public void searchShowInput()
    {
        searchField = (android.widget.SearchView) findViewById(R.id.nongkrongSearchField);

        searchField.setIconified(false);
        searchField.requestFocus();
        searchField.isFocused();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    public void hideSearchField()
    {
        searchFieldShown = false;

        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.hide_search);
        slideSearch.setAnimationListener(this);

        searchField.startAnimation(slideSearch);
//        searchField.clearFocus();
//        searchField.setIconified(true);
        searchField.setVisibility(View.INVISIBLE);

        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_search);
        slideSearch.setAnimationListener(this);
    }

    public class Transfer
    {
        public void Transfer()
        {
            Log.e(TAG, "uji coba interface");
        }
    }

    protected void LoadProfiles()
    {
        recyclerProfiles = (RecyclerView) findViewById(R.id.recyclerProfileList);
        pAdapter = new ProfileListRecyclerAdapter(profileLists);
        recyclerProfiles.setLayoutManager(new GridLayoutManager(NongkrongActivity.this, 3));
        recyclerProfiles.setItemAnimator(new DefaultItemAnimator());
        recyclerProfiles.setAdapter(pAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerProfiles, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        /*
        recyclerProfiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(NongkrongActivity.this, "jeng jeng" , Toast.LENGTH_LONG).show();
            }
        });
        */

        //Log.e(TAG, "recyclerProfiles " + recyclerProfiles.getAdapter().toString());

        loadingProfiles = ProgressDialog.show(NongkrongActivity.this, "Working..", "Downloading Data...", true, false);

        Thread tradeThread = new Thread(){
            public void run(){

                GetProfilesData();

                //OverScrollDecoratorHelper.setUpOverScroll(recyclerChallenge, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

                //tradeLoaded = true;
                try {
                    Thread.sleep(300);
                    NongkrongActivity.this.loadingProfiles.dismiss();
                } catch (InterruptedException e) {
                    NongkrongActivity.this.loadingProfiles.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();

    }

    protected void GetProfilesData()
    {
        profileLists.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "";

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_profiles.php");
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

            response = sb.toString();

            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONArray nominees = new JSONArray(response);

                    for (int i = 0; i < nominees.length(); i++)
                    {
                        JSONObject c = nominees.getJSONObject(i);

                        String acc_name = c.getString("acc_name");
                        String acc_schooltitle = c.getString("acc_schooltitle");
                        String acc_profilepicture = c.getString("acc_profilepicture");
                        String acc_id = c.getString("acc_id");
                        String acc_studentid = c.getString("acc_studentid");
                        String acc_email = c.getString("acc_email");
                        String acc_phone = c.getString("acc_phone");
                        String acc_fullname = c.getString("acc_fullname");
                        String acc_status = c.getString("acc_status");
                        String acc_history = c.getString("acc_history");
                        String acc_birthdate = c.getString("acc_birthdate");
                        String acc_organization = c.getString("acc_organization");
                        String acc_interest = c.getString("acc_interest");
                        String acc_schoolcity = c.getString("acc_schoolcity");
                        String acc_class = c.getString("acc_class");
                        String acc_major = c.getString("acc_major");
                        String acc_coverpicture = c.getString("acc_coverpicture");

                        //Log.e(TAG, "mvpnom_name " + mvpnom_name);
                        //Log.e(TAG, "mvpnom_schooltitle " + mvpnom_schooltitle);
                        //Log.e(TAG, "chal_authorname " + c.toString());

                        //MvpNominee nominee = new MvpNominee(mvp_description, mvp_id, mvp_studentid, mvp_name, mvp_email, mvp_phone, mvp_fullname, mvp_status, mvp_history, mvp_birthdate, mvp_organization, mvp_interest, mvp_schooltitle, mvp_schoolcity, mvp_class, mvp_major, mvp_profilepicture, mvp_coverpicture);
                        ProfileList profile = new ProfileList(acc_id, acc_studentid, acc_name, acc_email, acc_phone, acc_fullname, acc_status, acc_history, acc_birthdate, acc_organization, acc_interest, acc_schooltitle, acc_schoolcity, acc_class, acc_major, acc_profilepicture, acc_coverpicture);

                        profileLists.add(profile);//temporary
                    }

                    NongkrongActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            pAdapter.notifyDataSetChanged();
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

}
