package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

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

public class LearningSubjectActivity extends AppCompatActivity {

    ImageButton learningSubjectBackButton;
    TextView tab1, tab2;
    private RecyclerView recyclerMaterial;
    private LearningMaterialsAdapter mAdapter;
    private List<Material> materialList = new ArrayList<>();
    private ProgressDialog loadingChallenge;

    private String TAG = LearningSubjectActivity.class.getSimpleName();
    private String sub_id;
    private String sub_title;
    private RecyclerView recyclerTryout;
    private LearningTryoutAdapter tAdapter;
    private List<Tryout> tryoutList = new ArrayList<>();
    private ProgressDialog loadingMaterial;
    private ProgressDialog loadingTryout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning_subject);

        sub_id = getIntent().getStringExtra("sub_id");
        sub_title = getIntent().getStringExtra("sub_title");

        TextView learningSubjectTitleText = (TextView) findViewById(R.id.learningSubjectTitleText);
        learningSubjectTitleText.setText(sub_title);

        addListenerOnButton();

        TabHost host = (TabHost)findViewById(R.id.learningSubjectTab);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tabMaterial);
        spec.setIndicator("Materials");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tabTryOut);
        spec.setIndicator("Try Outs");
        host.addTab(spec);

        tab1 = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tab2 = (TextView) host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tab1.setAllCaps(false);
        tab2.setAllCaps(false);

        loadingMaterial = ProgressDialog.show(LearningSubjectActivity.this, "Working..", "Downloading Data...", true, false);

        Thread tradeThread = new Thread(){
            public void run(){
                LoadMaterials();
                LoadTryouts();
                try
                {
                    sleep(5000);
                    //tradeLoaded = true;
                    loadingMaterial.dismiss();
                } catch (InterruptedException e) {
                    loadingMaterial.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();

    }

    private void addListenerOnButton() {
        learningSubjectBackButton = (ImageButton) findViewById(R.id.learningSubjectBackButton);

        learningSubjectBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                LearningSubjectActivity.this.finish();
            }

        });
    }

    protected void LoadMaterials()
    {
        recyclerMaterial = (RecyclerView) findViewById(R.id.material_list_recycler);
        mAdapter = new LearningMaterialsAdapter(materialList);
        RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerLearning.setLayoutManager(new GridLayoutManager(LearningSubjectActivity.this, 3));
        recyclerMaterial.setLayoutManager(normalLayoutManager);
        recyclerMaterial.setItemAnimator(new DefaultItemAnimator());
        recyclerMaterial.setAdapter(mAdapter);

        GetMaterialsData();

        OverScrollDecoratorHelper.setUpOverScroll(recyclerMaterial, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    protected void LoadTryouts()
    {
        recyclerTryout = (RecyclerView) findViewById(R.id.tryout_list_recycler);
        tAdapter = new LearningTryoutAdapter(tryoutList);
        RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        //recyclerLearning.setLayoutManager(new GridLayoutManager(LearningSubjectActivity.this, 3));
        recyclerTryout.setLayoutManager(normalLayoutManager);
        recyclerTryout.setItemAnimator(new DefaultItemAnimator());
        recyclerTryout.setAdapter(tAdapter);

        GetTryoutsData();

        OverScrollDecoratorHelper.setUpOverScroll(recyclerTryout, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    protected void GetMaterialsData()
    {
        materialList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "subID="+sub_id;

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_subject_material.php");
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

            Log.e(TAG, "response: "+response);

            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONArray trades = new JSONArray(response);

                    for (int i = 0; i < trades.length(); i++)
                    {
                        JSONObject c = trades.getJSONObject(i);

                        String mat_id = c.getString("mat_id");
                        String sub_id = c.getString("sub_id");
                        String mat_title = c.getString("mat_title");
                        String mat_contents = c.getString("mat_contents");
                        String mat_imageurl = c.getString("mat_imageurl");

                        Log.e(TAG, "mat_id " + mat_id);
                        Log.e(TAG, "mat_title " + mat_title);

                        Material material = new Material(mat_id, sub_id, mat_title, mat_contents, mat_imageurl);

                        materialList.add(material);
                    }

                    LearningSubjectActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
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

    protected void GetTryoutsData()
    {
        tryoutList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
//        String parameters = "txtPhone="+mPhone+"&txtPassword="+mPassword;
        String parameters = "subID="+sub_id;

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_subject_tryout.php");
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

            Log.e(TAG, "response: "+response);

            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                try
                {
                    JSONArray trades = new JSONArray(response);

                    for (int i = 0; i < trades.length(); i++)
                    {
                        JSONObject c = trades.getJSONObject(i);

                        String try_id = c.getString("try_id");
                        String sub_id = c.getString("sub_id");
                        String try_title = c.getString("try_title");

                        Log.e(TAG, "mat_id " + try_id);
                        Log.e(TAG, "try_title " + try_title);

                        Tryout tryout = new Tryout(try_id, sub_id, try_title);

                        tryoutList.add(tryout);
                    }

                    LearningSubjectActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tAdapter.notifyDataSetChanged();
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
