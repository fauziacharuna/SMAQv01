package com.smaq.apps.smaqv01.Important;

import android.app.ProgressDialog;
import android.graphics.Color;
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
import android.widget.TabHost;
import android.widget.TextView;

import com.smaq.apps.smaqv01.Challenge;
import com.smaq.apps.smaqv01.ChallengeRecyclerAdapter;
import com.smaq.apps.smaqv01.Constants;
import com.smaq.apps.smaqv01.R;

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

public class NewsActivity extends AppCompatActivity implements Animation.AnimationListener {

    private String TAG = NewsActivity.class.getSimpleName();

    ImageButton newsBackButton, newsSearchButton;
    ImageView[] news;
    Animation slideSearch;
    android.widget.SearchView searchField;
    boolean searchFieldShown;
    private ProgressDialog loadingNews;
    private RecyclerView recyclerNews;
    private RecyclerView recyclerUpcomingNews;
    private ChallengeRecyclerAdapter nAdapter;
    private ChallengeRecyclerAdapter unAdapter;
    private List<Challenge> newsList = new ArrayList<>();
    private List<Challenge> upcomingNewsList = new ArrayList<>();
    private String getNewsUrl = Constants.ROOT_URL+"apanel/get_news_data.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TextView tab1, tab2, tab3;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        loadingNews = ProgressDialog.show(NewsActivity.this, "Working..", "Downloading Data...", true, false);

        LoadNews();

        searchFieldShown = false;
        searchField = (android.widget.SearchView) findViewById(R.id.newsSearchField);
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchField.findViewById(searchField.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.BLACK);
        search_text.setGravity(Gravity.BOTTOM);

        addListenerOnButton();

        searchField.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchField();
                return false;
            }
        });

        TabHost host = (TabHost)findViewById(R.id.newsTab);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ALL");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Upcoming");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Favourites");
        host.addTab(spec);

        //set tab title font size
        tab1 = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tab2 = (TextView) host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tab3 = (TextView) host.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
//        tab1.setTextSize(10);
//        tab2.setTextSize(10);
//        tab3.setTextSize(10);

        tab1.setAllCaps(false);
        tab2.setAllCaps(false);
        tab3.setAllCaps(false);

        tab1.setTextColor(Color.WHITE);
        tab2.setTextColor(Color.WHITE);
        tab3.setTextColor(Color.WHITE);
    }

    private void addListenerOnButton() {
        newsBackButton = (ImageButton) findViewById(R.id.newsBackButton);
        newsSearchButton = (ImageButton) findViewById(R.id.newsSearchButton);

        newsBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
//                MainActivity.this.startActivity(myIntent);
                NewsActivity.this.finish();
            }

        });

        /*
        news = new ImageView[6];
        news[0] = (ImageView) findViewById(R.id.news1);
        news[1] = (ImageView) findViewById(R.id.news2);
        news[2] = (ImageView) findViewById(R.id.news3);
        news[3] = (ImageView) findViewById(R.id.news4);
        news[4] = (ImageView) findViewById(R.id.news5);
        news[5] = (ImageView) findViewById(R.id.news6);

        for (int i = 0; i < 6; i++){
            news[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Intent myIntent = new Intent(NewsActivity.this, NewsArticleActivity.class);
                    NewsActivity.this.startActivity(myIntent);
                }

            });
        }
        */

        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_search);
        slideSearch.setAnimationListener(this);

        searchField = (android.widget.SearchView) findViewById(R.id.newsSearchField);

        newsSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchField.startAnimation(slideSearch);
                searchField.setVisibility(View.VISIBLE);
                searchFieldShown = true;
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
        searchField = (android.widget.SearchView) findViewById(R.id.newsSearchField);

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

    protected void LoadNews()
    {
        Thread tradeThread = new Thread(){
            public void run(){
                recyclerNews = (RecyclerView) findViewById(R.id.news_all_recycler);
                nAdapter = new ChallengeRecyclerAdapter(newsList);
                //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerNews.setLayoutManager(new GridLayoutManager(NewsActivity.this, 2));
                recyclerNews.setItemAnimator(new DefaultItemAnimator());
                recyclerNews.setAdapter(nAdapter);

                recyclerUpcomingNews = (RecyclerView) findViewById(R.id.news_upcoming_recycler);
                unAdapter = new ChallengeRecyclerAdapter(upcomingNewsList);
                //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerUpcomingNews.setLayoutManager(new GridLayoutManager(NewsActivity.this, 2));
                recyclerUpcomingNews.setItemAnimator(new DefaultItemAnimator());
                recyclerUpcomingNews.setAdapter(unAdapter);

                GetNewsData();

                //OverScrollDecoratorHelper.setUpOverScroll(recyclerChallenge, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

                //tradeLoaded = true;
                try {
                    Thread.sleep(5000);
                    loadingNews.dismiss();
                } catch (InterruptedException e) {
                    loadingNews.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();
    }

    protected void GetNewsData()
    {
        newsList.clear();
        upcomingNewsList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "";

        try
        {
            url = new URL(getNewsUrl);
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
                    JSONArray trades = new JSONArray(response);

                    for (int i = 0; i < trades.length(); i++)
                    {
                        JSONObject c = trades.getJSONObject(i);

                        String chal_id = c.getString("news_id");
                        String chal_title = c.getString("news_title");
                        String chal_description = c.getString("news_desc");
                        String chal_date = c.getString("news_date");
                        String chal_imageurl = c.getString("news_picture");

                        String chal_type = "";
                        String chal_authorid = "";
                        String chal_authorname = "";
                        String chal_authorprofilepicture = "";

                        //Log.e(TAG, "chal_authorname " + c.toString());
                        //Log.e(TAG, "chal_authorprofilepicture " + chal_authorprofilepicture);

                        Challenge challenge = new Challenge(chal_id, chal_title, chal_description, chal_type, chal_date, chal_imageurl, chal_authorid, chal_authorname, chal_authorprofilepicture);

                        newsList.add(challenge);//temporary
                        upcomingNewsList = newsList;

                        /*
                        if (chal_authorid.equals(userID))
                        {
                            Log.e(TAG, "chal_authorid " + chal_authorid);
                            Log.e(TAG, "userID " + userID);
                            myChallengeList.add(challenge);//temporary
                        }
                        else
                        {
                            Log.e(TAG, "skipped " + chal_title);
                            Log.e(TAG, "chal_authorid " + chal_authorid);
                            Log.e(TAG, "userID " + userID);
                        }
                        */
                    }

                    NewsActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nAdapter.notifyDataSetChanged();
                            unAdapter.notifyDataSetChanged();
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
