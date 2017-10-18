package com.smaq.apps.smaqv01;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class ChallengeActivity extends AppCompatActivity {

    private String TAG = ChallengeActivity.class.getSimpleName();

    private TextView tab1, tab2, tab3;

    private int selectedTab;

    ProgressDialog loadingChallenge;

    RecyclerView recyclerChallenge, recyclerMyChallenge;

    ChallengeRecyclerAdapter cAdapter, mcAdapter;

    private List<Challenge> challengeList = new ArrayList<>();
    private List<Challenge> myChallengeList = new ArrayList<>();

    final String getChallengesUrl = Constants.ROOT_URL+"apanel/get_challenge_data.php";

    public String stringChallengeInputTitle, stringChallengeInputDescription, stringChallengeInputDate, stringChallengeInputType;

    private EditText fieldTitle, fieldDescription, fieldDate;
    private Spinner fieldType;
    private ImageButton challengePostImageView;

    SharedPreferences userProfile;
    String userSavedID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        loadingChallenge = ProgressDialog.show(ChallengeActivity.this, "Working..", "Downloading Data...", true, false);

        userProfile = PreferenceManager.getDefaultSharedPreferences(ChallengeActivity.this);
        userSavedID = userProfile.getString("userSavedID", "");

        challengePostImageView = (ImageButton) findViewById(R.id.challengePostCamera);

        selectedTab = 0;

        ChallengeTabController();

        LoadChallenges();

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        ImageButton challengeBackButton = (ImageButton) findViewById(R.id.challengeBackButton);
        final ImageButton floatPostChallenge = (ImageButton) findViewById(R.id.btFloatPostChallenge);
        ImageButton challengePostButtonCancel = (ImageButton) findViewById(R.id.challengePostButtonCancel);
        ImageButton challengePostButtonOk = (ImageButton) findViewById(R.id.challengePostButtonOk);
        ImageButton challengePostCamera = (ImageButton) findViewById(R.id.challengePostCamera);

        final ConstraintLayout challengePostContainer = (ConstraintLayout) findViewById(R.id.challengePostContainer);

        challengeBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChallengeActivity.this.finish();
            }
        });

        floatPostChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengePostContainer.setVisibility(View.VISIBLE);
                floatPostChallenge.setVisibility(View.INVISIBLE);
            }
        });

        challengePostContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //challengePostContainer.setVisibility(View.INVISIBLE);
            }
        });

        challengePostButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengePostContainer.setVisibility(View.INVISIBLE);
                floatPostChallenge.setVisibility(View.VISIBLE);
            }
        });

        challengePostButtonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PostReadInput())
                {
                    uploadData();
                    challengePostContainer.setVisibility(View.INVISIBLE);
                }
                else
                {
                    Toast.makeText(ChallengeActivity.this, "Harap isi semua field" , Toast.LENGTH_LONG).show();
                    Log.e(TAG, "EMPTY: ");
                }
            }
        });

        challengePostCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });
    }

    protected void ChallengeTabController()
    {

        //******************************************
        //TAB CONTROLLER
        //***************

        final TabHost host = (TabHost)findViewById(R.id.challengeTab);
        host.setup();

        //Tab 1
        host.addTab(host.newTabSpec("Tab One").setIndicator("All").setContent(R.id.tab1));

        //Tab 2
        host.addTab(host.newTabSpec("Tab Two").setIndicator("Yours").setContent(R.id.tab2));

        //Tab 3
        host.addTab(host.newTabSpec("Tab Three").setIndicator("Favourites").setContent(R.id.tab3));

        TabWidget widget = host.getTabWidget();
        for(int i = 0; i < widget.getChildCount(); i++) {
            View v = widget.getChildAt(i);

            // Look for the title view to ensure this is an indicator and not a divider.
            TextView tv = (TextView)v.findViewById(android.R.id.title);
            if(tv == null) {
                continue;
            }
            v.setBackgroundResource(R.drawable.tab_indicator);
        }

        //set tab title font size
        tab1 = (TextView) host.getTabWidget().getChildAt(0).findViewById(android.R.id.title);
        tab2 = (TextView) host.getTabWidget().getChildAt(1).findViewById(android.R.id.title);
        tab3 = (TextView) host.getTabWidget().getChildAt(2).findViewById(android.R.id.title);
        //tab1bg = host.getTabWidget().getChildAt(0).findViewById(android.R.id.background);
        tab1.setTextSize(10);
        tab2.setTextSize(10);
        tab3.setTextSize(10);

        tab1.setAllCaps(false);
        tab2.setAllCaps(false);
        tab3.setAllCaps(false);

        tab1.setTextColor(Color.WHITE);
        tab2.setTextColor(Color.WHITE);
        tab3.setTextColor(Color.WHITE);

        selectedTab = host.getCurrentTab();

        //******************
        //TAB CONTROLLER END
        //******************************************

    }

    protected void LoadChallenges()
    {
        Thread tradeThread = new Thread(){
            public void run(){
                recyclerChallenge = (RecyclerView) findViewById(R.id.challenge_all_recycler);
                cAdapter = new ChallengeRecyclerAdapter(challengeList);
                //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerChallenge.setLayoutManager(new GridLayoutManager(ChallengeActivity.this, 2));
                recyclerChallenge.setItemAnimator(new DefaultItemAnimator());
                recyclerChallenge.setAdapter(cAdapter);

                recyclerMyChallenge = (RecyclerView) findViewById(R.id.challenge_yours_recycler);
                mcAdapter = new ChallengeRecyclerAdapter(myChallengeList);
                //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerMyChallenge.setLayoutManager(new GridLayoutManager(ChallengeActivity.this, 2));
                recyclerMyChallenge.setItemAnimator(new DefaultItemAnimator());
                recyclerMyChallenge.setAdapter(mcAdapter);

                GetChallengesData();

                //OverScrollDecoratorHelper.setUpOverScroll(recyclerChallenge, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

                //tradeLoaded = true;
                try {
                    Thread.sleep(5000);
                    loadingChallenge.dismiss();
                } catch (InterruptedException e) {
                    loadingChallenge.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();
    }

    protected void GetChallengesData()
    {
        SharedPreferences userProfile = PreferenceManager.getDefaultSharedPreferences(ChallengeActivity.this);
        String userID = userProfile.getString("userSavedID", "1");

        challengeList.clear();
        myChallengeList.clear();

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
            url = new URL(getChallengesUrl);
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

                        String chal_id = c.getString("chal_id");
                        String chal_title = c.getString("chal_title");
                        String chal_description = c.getString("chal_description");
                        String chal_type = c.getString("chal_type");
                        String chal_date = c.getString("chal_date");
                        String chal_imageurl = c.getString("chal_imageurl");
                        String chal_authorid = c.getString("chal_authorid");
                        String chal_authorname = c.getString("chal_authorname");
                        String chal_authorprofilepicture = c.getString("chal_authorprofilepicture");

                        //Log.e(TAG, "chal_authorname " + c.toString());
                        //Log.e(TAG, "chal_authorprofilepicture " + chal_authorprofilepicture);

                        Challenge challenge = new Challenge(chal_id, chal_title, chal_description, chal_type, chal_date, chal_imageurl, chal_authorid, chal_authorname, chal_authorprofilepicture);

                        challengeList.add(challenge);//temporary

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
                    }

                    ChallengeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            cAdapter.notifyDataSetChanged();
                            mcAdapter.notifyDataSetChanged();
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
        fieldTitle = (EditText) findViewById(R.id.challengePostTitle);
        fieldDescription = (EditText) findViewById(R.id.challengePostContent);
        fieldDate = (EditText) findViewById(R.id.challengePostDate);
        fieldType = (Spinner) findViewById(R.id.challengeSpinner);

        stringChallengeInputTitle = fieldTitle.getText().toString();
        stringChallengeInputDescription = fieldDescription.getText().toString();
        stringChallengeInputDate = fieldDate.getText().toString();
        stringChallengeInputType = fieldType.getSelectedItem().toString().toLowerCase();

        if(stringChallengeInputTitle.equals(null)
                || stringChallengeInputTitle.equals("")
                || stringChallengeInputDescription.equals(null)
                || stringChallengeInputDescription.equals("")
                || stringChallengeInputDate.equals(null)
                || stringChallengeInputDate.equals(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }


//    ********************
//    GET INPUT PICTURE
//    ********************

    private String fileNameString = "image";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;

    private void showFileChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();

            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();

            fileNameString = userSavedID+"-"+ts; //getFileName(filePath);

            try {
                //Getting the Bitmap from Gallery
                Bitmap tmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = getResizedBitmap(tmp, 750);
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //upload to server
                //uploadImage(folderURL);

                //Setting the Bitmap to ImageView
                challengePostImageView.setImageBitmap(bitmap);
                /*
                Log.e(TAG, "pictureChanged: ");
                Constants.pictureChanged = true;
                ImageLoader imageLoader = new ImageLoader(this);
                imageLoader.clearCache();
                */

                //Toast.makeText(this,folderURL, Toast.LENGTH_LONG).show();
            }
            catch (IOException e) {
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

    private void uploadData()
    {
        if(fileNameString.equals("") || fileNameString.equals("image"))
        {
            return;
        }

        String uploadFolder = Constants.ROOT_URL+"apanel/add_challenge_data.php";

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadFolder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        LoadChallenges();

                        //Showing toast message of the response
                        Toast.makeText(ChallengeActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.e(TAG, "s: "+s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        LoadChallenges();

                        //Showing toast
                        Toast.makeText(ChallengeActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, "volleyError.getMessage().toString(): "+volleyError.getMessage().toString());
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
                params.put("txtTitle", stringChallengeInputTitle);
                params.put("txtDescription", stringChallengeInputDescription);
                params.put("txtType", stringChallengeInputType);
                params.put("txtDate", stringChallengeInputDate);
                params.put("txtImageurl", fileNameString);
                params.put("txtUserID", userSavedID);
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

}
