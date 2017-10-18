package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.List;
import java.util.Map;

public class FindTeamActivity extends AppCompatActivity implements Animation.AnimationListener {

    ImageButton teamBackButton, teamSearchButton, teamPostItAddButton, teamPostItCancelButton, teamPostItOkButton;
    ConstraintLayout postItContainer;
    Animation slideSearch;
    android.widget.SearchView searchField;
    boolean searchFieldShown;
    private RecyclerView recyclerTeam;
    private RecyclerView recyclerMyTeam;
    private ChallengeRecyclerAdapter tAdapter;
    private ChallengeRecyclerAdapter mtAdapter;
    private List<Challenge> teamList = new ArrayList<>();
    private List<Challenge> myTeamList = new ArrayList<>();
    private ProgressDialog loadingTeam;

    final String getTeamsUrl = Constants.ROOT_URL+"apanel/get_team_data.php";

    private String TAG = FindTeamActivity.class.getSimpleName();
    private EditText fieldTitle;
    private EditText fieldDescription;
    private Spinner fieldType;
    private String stringFindTeamInputTitle;
    private String stringFindTeamInputDescription;
    private String stringFindTeamInputType;
    private SharedPreferences userProfile;
    private String userSavedID;
    private ImageButton findTeamPostImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_team);

        loadingTeam = ProgressDialog.show(FindTeamActivity.this, "Working..", "Downloading Data...", true, false);

        userProfile = PreferenceManager.getDefaultSharedPreferences(FindTeamActivity.this);
        userSavedID = userProfile.getString("userSavedID", "");

        findTeamPostImageView = (ImageButton) findViewById(R.id.findTeamCamera);

        searchFieldShown = false;
        searchField = (android.widget.SearchView) findViewById(R.id.findTeamSearchField);
        AutoCompleteTextView search_text = (AutoCompleteTextView) searchField.findViewById(searchField.getContext().getResources().getIdentifier("android:id/search_src_text", null, null));
        search_text.setTextColor(Color.BLACK);
        search_text.setGravity(Gravity.BOTTOM);

        LoadTeams();

        addListenerOnButton();

        searchField.setOnCloseListener(new android.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                hideSearchField();
                return false;
            }
        });

        FindTeamTabController();
    }

    private void FindTeamTabController()
    {
        TextView tab1, tab2, tab3;
        TabHost host = (TabHost)findViewById(R.id.findTeamTab);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("ALL");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Yours");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Favourites");
        host.addTab(spec);

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

        tab1.setTextSize(10);
        tab2.setTextSize(10);
        tab3.setTextSize(10);

        tab1.setAllCaps(false);
        tab2.setAllCaps(false);
        tab3.setAllCaps(false);

        tab1.setTextColor(Color.WHITE);
        tab2.setTextColor(Color.WHITE);
        tab3.setTextColor(Color.WHITE);

    }

    private void addListenerOnButton() {
        teamBackButton = (ImageButton) findViewById(R.id.findTeamBackButton);
        teamSearchButton = (ImageButton) findViewById(R.id.findTeamSearchButton);
        teamPostItAddButton = (ImageButton) findViewById(R.id.findTeamPostItAddButton);
        teamPostItCancelButton = (ImageButton) findViewById(R.id.findTeamPostItButtonCancel);
        teamPostItOkButton = (ImageButton) findViewById(R.id.findTeamPostItButtonOk);

        postItContainer = (ConstraintLayout) findViewById(R.id.findTeamPostItContainer);

        teamBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                FindTeamActivity.this.finish();
            }
        });

        teamPostItAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItContainer.setVisibility(View.VISIBLE);
                teamPostItAddButton.setVisibility(View.INVISIBLE);
            }
        });

        teamPostItCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItContainer.setVisibility(View.INVISIBLE);
                teamPostItAddButton.setVisibility(View.VISIBLE);
            }
        });

        teamPostItOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (PostReadInput())
                {
                    uploadData();
                    postItContainer.setVisibility(View.INVISIBLE);
                    teamPostItAddButton.setVisibility(View.VISIBLE);
                }
                else
                {
                    Toast.makeText(FindTeamActivity.this, "Harap isi semua field" , Toast.LENGTH_LONG).show();
                    Log.e(TAG, "EMPTY: ");
                }
            }
        });

        findTeamPostImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        slideSearch = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.show_search);
        slideSearch.setAnimationListener(this);

        searchField = (android.widget.SearchView) findViewById(R.id.findTeamSearchField);

        teamSearchButton.setOnClickListener(new View.OnClickListener() {
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
        searchField = (android.widget.SearchView) findViewById(R.id.findTeamSearchField);

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

    protected void LoadTeams()
    {
        recyclerTeam = (RecyclerView) findViewById(R.id.team_all_recycler);
        tAdapter = new ChallengeRecyclerAdapter(teamList);
        //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerTeam.setLayoutManager(new GridLayoutManager(FindTeamActivity.this, 2));
        recyclerTeam.setItemAnimator(new DefaultItemAnimator());
        recyclerTeam.setAdapter(tAdapter);

        recyclerMyTeam = (RecyclerView) findViewById(R.id.team_yours_recycler);
        mtAdapter = new ChallengeRecyclerAdapter(myTeamList);
        //RecyclerView.LayoutManager normalLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerMyTeam.setLayoutManager(new GridLayoutManager(FindTeamActivity.this, 2));
        recyclerMyTeam.setItemAnimator(new DefaultItemAnimator());
        recyclerMyTeam.setAdapter(mtAdapter);

        Thread tradeThread = new Thread(){
            public void run(){

                GetTeamsData();

                //OverScrollDecoratorHelper.setUpOverScroll(recyclerChallenge, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

                //tradeLoaded = true;
                try {
                    Thread.sleep(5000);
                    loadingTeam.dismiss();
                } catch (InterruptedException e) {
                    loadingTeam.dismiss();
                    e.printStackTrace();
                }
            }
        };
        tradeThread.start();
    }

    protected void GetTeamsData()
    {
        SharedPreferences userProfile = PreferenceManager.getDefaultSharedPreferences(FindTeamActivity.this);
        String userID = userProfile.getString("userSavedID", "1");

        teamList.clear();
        myTeamList.clear();

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
            url = new URL(getTeamsUrl);
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

                        teamList.add(challenge);//temporary

                        if (chal_authorid.equals(userID))
                        {
                            Log.e(TAG, "chal_authorid " + chal_authorid);
                            Log.e(TAG, "userID " + userID);
                            myTeamList.add(challenge);//temporary
                        }
                        else
                        {
                            Log.e(TAG, "skipped " + chal_title);
                            Log.e(TAG, "chal_authorid " + chal_authorid);
                            Log.e(TAG, "userID " + userID);
                        }
                    }

                    FindTeamActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tAdapter.notifyDataSetChanged();
                            mtAdapter.notifyDataSetChanged();
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
        fieldTitle = (EditText) findViewById(R.id.findTeamPostItTitle);
        fieldDescription = (EditText) findViewById(R.id.findTeamPostItContent);
        fieldType = (Spinner) findViewById(R.id.findTeamSpinner);

        stringFindTeamInputTitle = fieldTitle.getText().toString();
        stringFindTeamInputDescription = fieldDescription.getText().toString();
        stringFindTeamInputType = fieldType.getSelectedItem().toString().toLowerCase();

        if(stringFindTeamInputTitle.equals(null)
                || stringFindTeamInputTitle.equals("")
                || stringFindTeamInputDescription.equals(null)
                || stringFindTeamInputDescription.equals(""))
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
                findTeamPostImageView.setImageBitmap(bitmap);
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

        String uploadFolder = Constants.ROOT_URL+"apanel/add_team_data.php";

        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadFolder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        LoadTeams();

                        //Showing toast message of the response
                        Toast.makeText(FindTeamActivity.this, s , Toast.LENGTH_LONG).show();
                        Log.e(TAG, "s: "+s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();
                        LoadTeams();

                        //Showing toast
                        Toast.makeText(FindTeamActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                params.put("txtTitle", stringFindTeamInputTitle);
                params.put("txtDescription", stringFindTeamInputDescription);
                params.put("txtType", stringFindTeamInputType);
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
