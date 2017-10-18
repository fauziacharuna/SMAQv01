package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class NomineeListActivity extends AppCompatActivity {

    private RecyclerView recyclerNominees;
    private MvpNomineeRecyclerAdapter nAdapter;
    private List<MvpNominee> nomineeList = new ArrayList<>();
    private ProgressDialog loadingNominees;

    private String TAG = MvpActivity.class.getSimpleName();
    private EditText fieldName;
    private EditText fieldSchool;
    private EditText fieldDescription;
    private String stringNomineeInputName;
    private String stringNomineeInputSchool;
    private String stringNomineeInputDescription;
    private SharedPreferences userProfile;
    private String userSavedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nominee_list);

        LoadNominees();

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        final ImageButton mvpBackButton = (ImageButton) findViewById(R.id.nomineeListBack);
        final ImageButton btFloatPostNominee = (ImageButton) findViewById(R.id.btFloatPostNominee);
        final ImageButton postNomineeButtonCancel = (ImageButton) findViewById(R.id.postNomineeButtonCancel);
        final ImageButton postNomineeButtonOk = (ImageButton) findViewById(R.id.postNomineeButtonOk);

        final ConstraintLayout postNomineeContainer = (ConstraintLayout) findViewById(R.id.postNomineeContainer);

        mvpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NomineeListActivity.this.finish();
            }
        });

        btFloatPostNominee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNomineeContainer.setVisibility(View.VISIBLE);
                btFloatPostNominee.setVisibility(View.INVISIBLE);
            }
        });

        postNomineeButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNomineeContainer.setVisibility(View.INVISIBLE);
                btFloatPostNominee.setVisibility(View.VISIBLE);
            }
        });

        postNomineeButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(PostReadInput())
                {
                    if(UploadNomineeData())
                    {
                        postNomineeContainer.setVisibility(View.INVISIBLE);
                        btFloatPostNominee.setVisibility(View.VISIBLE);
                    }
                    else
                    {

                    }
                }
                else
                {
                    Toast.makeText(NomineeListActivity.this, "Harap isi semua field" , Toast.LENGTH_LONG).show();
                    Log.e(TAG, "EMPTY: ");
                }
            }
        });
    }


    protected void LoadNominees()
    {
        recyclerNominees = (RecyclerView) findViewById(R.id.mvp_nominee_recycler);
        nAdapter = new MvpNomineeRecyclerAdapter(nomineeList);
        recyclerNominees.setLayoutManager(new GridLayoutManager(NomineeListActivity.this, 3));
        recyclerNominees.setItemAnimator(new DefaultItemAnimator());
        recyclerNominees.setAdapter(nAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(recyclerNominees, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);


        Log.e(TAG, "recyclerNominees " + recyclerNominees.getAdapter().toString());

        loadingNominees = ProgressDialog.show(NomineeListActivity.this, "Working..", "Downloading Data...", true, false);

        Thread tradeThread = new Thread(){
            public void run(){

                GetNomineesData();

                //OverScrollDecoratorHelper.setUpOverScroll(recyclerChallenge, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

                //tradeLoaded = true;
                try {
                    Thread.sleep(300);
                    loadingNominees.dismiss();
                } catch (InterruptedException e) {
                    loadingNominees.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();

    }

    protected void GetNomineesData()
    {
        nomineeList.clear();

        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "";

        try
        {
            url = new URL(Constants.ROOT_URL+"apanel/get_mvp_nominee.php");
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

                        String mvpnom_description = c.getString("mvpnom_description");
                        String mvpnom_name = c.getString("mvpnom_name");
                        String mvpnom_schooltitle = c.getString("mvpnom_schooltitle");
                        String mvpnom_profilepicture = c.getString("mvpnom_profilepicture");
                        
                        String mvpnom_id = c.getString("mvpnom_id");
                        String mvpnom_studentid = c.getString("mvpnom_studentid");
                        String mvpnom_email = c.getString("mvpnom_email");
                        String mvpnom_phone = c.getString("mvpnom_phone");
                        String mvpnom_fullname = c.getString("mvpnom_fullname");
                        String mvpnom_status = c.getString("mvpnom_status");
                        String mvpnom_history = c.getString("mvpnom_history");
                        String mvpnom_birthdate = c.getString("mvpnom_birthdate");
                        String mvpnom_organization = c.getString("mvpnom_organization");
                        String mvpnom_interest = c.getString("mvpnom_interest");
                        String mvpnom_schoolcity = c.getString("mvpnom_schoolcity");
                        String mvpnom_class = c.getString("mvpnom_class");
                        String mvpnom_major = c.getString("mvpnom_major");
                        String mvpnom_coverpicture = c.getString("mvpnom_coverpicture");

                        //Log.e(TAG, "mvpnom_name " + mvpnom_name);
                        //Log.e(TAG, "mvpnom_schooltitle " + mvpnom_schooltitle);
                        //Log.e(TAG, "chal_authorname " + c.toString());

                        MvpNominee nominee = new MvpNominee(mvpnom_description, mvpnom_id, mvpnom_studentid, mvpnom_name, mvpnom_email, mvpnom_phone, mvpnom_fullname, mvpnom_status, mvpnom_history, mvpnom_birthdate, mvpnom_organization, mvpnom_interest, mvpnom_schooltitle, mvpnom_schoolcity, mvpnom_class, mvpnom_major, mvpnom_profilepicture, mvpnom_coverpicture);
                        //MvpNominee nominee = new MvpNominee(mvpnom_description, mvpnom_name, mvpnom_schooltitle, mvpnom_profilepicture);

                        nomineeList.add(nominee);//temporary
                    }

                    NomineeListActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nAdapter.notifyDataSetChanged();
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

    private boolean PostReadInput()
    {
        userProfile = PreferenceManager.getDefaultSharedPreferences(NomineeListActivity.this);
        userSavedID = userProfile.getString("userSavedID", "");

        fieldName = (EditText) findViewById(R.id.postNomineeName);
        fieldSchool = (EditText) findViewById(R.id.postNomineeSchool);
        fieldDescription = (EditText) findViewById(R.id.postNomineeDescription);

        stringNomineeInputName = fieldName.getText().toString();
        stringNomineeInputSchool = fieldSchool.getText().toString();
        stringNomineeInputDescription = fieldDescription.getText().toString();

        if(stringNomineeInputName.equals(null)
                || stringNomineeInputName.equals("")
                || stringNomineeInputSchool.equals(null)
                || stringNomineeInputSchool.equals("")
                || stringNomineeInputDescription.equals(null)
                || stringNomineeInputDescription.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    private boolean UploadNomineeData()
    {
        // saving to server database
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "txtName="+stringNomineeInputName+"&txtSchool="+stringNomineeInputSchool+"&txtDescription="+stringNomineeInputDescription+"&txtID="+userSavedID;

        try {
            url = new URL(Constants.ROOT_URL+"apanel/add_mvp_nominee.php");
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
//                else if (response != null && response != "invalid" && response != "")
            if (!response.equals("invalid\n") && !response.equals("\n"))
            {
                Log.e(TAG, "Success");
                Toast.makeText(this,"Success!!", Toast.LENGTH_LONG).show();
                return true;
            }
            else
            {
                Log.e(TAG, "invalid");
                Toast.makeText(this,"Error!!", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch (IOException e){
            // Error
            Log.e(TAG, "IOException");
            Toast.makeText(this,"Error!!", Toast.LENGTH_LONG).show();
            return false;
        }

    }
}
