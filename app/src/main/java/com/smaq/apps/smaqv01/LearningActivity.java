package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

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

import de.hdodenhof.circleimageview.CircleImageView;
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class LearningActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageButton learningBackButton, learningSearchButton;
    Animation slideSearch;
    android.widget.SearchView searchField;
    boolean searchFieldShown;
    private RecyclerView recyclerLearning;
    private LearningSubjectsAdapter sAdapter;
    private List<Subject> subjectList = new ArrayList<>();
    private ProgressDialog loadingChallenge;

    private String TAG = LearningActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        searchFieldShown = false;
        searchField = (android.widget.SearchView) findViewById(R.id.learningSearchField);
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchField.findViewById(searchField.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.BLACK);
//        search_text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.searchfield_text_size));
        search_text.setGravity(Gravity.BOTTOM);

        addListenerOnButton();

        searchField.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchField();
                return false;
            }
        });

        LoadSubjects();
    }

    private void addListenerOnButton() {
        learningBackButton = (ImageButton) findViewById(R.id.learningBackButton);
        learningSearchButton = (ImageButton) findViewById(R.id.learningSearchButton);

//        learningSearchField = (EditText) findViewById(R.id.learningSearchField);

//        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_left_in);
        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_search);
        slideSearch.setAnimationListener(this);

        searchField = (android.widget.SearchView) findViewById(R.id.learningSearchField);

        learningBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
//                Intent myIntent = new Intent(MainActivity.this, ProfileActivity.class);
//                MainActivity.this.startActivity(myIntent);
                LearningActivity.this.finish();
            }

        });

        learningSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                learningSearchField.setVisibility(View.VISIBLE);
//                learningSearchField.startAnimation(slideSearch);
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
        searchField = (android.widget.SearchView) findViewById(R.id.learningSearchField);

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

    protected void LoadSubjects()
    {
        recyclerLearning = (RecyclerView) findViewById(R.id.subject_list_recycler);
        sAdapter = new LearningSubjectsAdapter(subjectList);
        recyclerLearning.setLayoutManager(new GridLayoutManager(LearningActivity.this, 3));
        recyclerLearning.setItemAnimator(new DefaultItemAnimator());
        recyclerLearning.setAdapter(sAdapter);

        loadingChallenge = ProgressDialog.show(LearningActivity.this, "Working..", "Downloading Data...", true, false);

        Thread tradeThread = new Thread(){
            public void run(){

                GetSubjectsData();

                //tradeLoaded = true;
                try {
                    Thread.sleep(5000);
                    loadingChallenge.dismiss();
                } catch (InterruptedException e) {
                    loadingChallenge.dismiss();
                    e.printStackTrace();
                }
                loadingChallenge.dismiss();
            }
        };
        tradeThread.start();

        OverScrollDecoratorHelper.setUpOverScroll(recyclerLearning, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    protected void GetSubjectsData()
    {
        subjectList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "";

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_elearning_subject.php");
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

                        String sub_id = c.getString("sub_id");
                        String sub_title = c.getString("sub_title");
                        String sub_imageurl = c.getString("sub_imageurl");

                        Log.e(TAG, "sub_id " + sub_id);
                        Log.e(TAG, "sub_title " + sub_title);

                        Subject subject = new Subject(sub_id, sub_title, sub_imageurl);

                        subjectList.add(subject);
                    }

                    LearningActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            sAdapter.notifyDataSetChanged();
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
