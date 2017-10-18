package com.smaq.apps.smaqv01;

import android.animation.LayoutTransition;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PublicKey;
import java.util.Hashtable;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ScrollView scrollView;

    TextView tab1, tab2, tab3, tab4;

    ImageButton profileBackButton, profileEditButton;

    TextView textProfileName, textProfileStatus;
    TextView textProfileSchooltitle, textProfileSchoolClass, textProfileSchoolMajor, textProfileSchooladdress;
    TextView textBioBirthday, textBioPhone, textBioEmail, textBioHistory, textBioOrganization, textBioInterest;

    EditText editProfileName, editProfileStatus;
    EditText editProfileSchooltitle, editProfileSchoolClass, editProfileSchoolMajor, editProfileSchooladdress;
    EditText editBioBirthday, editBioPhone, editBioEmail, editBioHistory, editBioOrganization, editBioInterest;

    String userCustomName, userCustomStatus;
    String inputSchooltitle, inputSchoolClass, inputSchoolMajor, inputSchooladdress;
    String userID;

    public boolean statusEditing = false;
    public boolean pictureChanged;

    private int PICK_IMAGE_REQUEST = 1;

    private Bitmap bitmap, cachedImage;

    private ImageView changedImage;

    private EditText editTextName;

    private String fileNameString = "image";
    private String KEY_IMAGE = "image";
    private String KEY_NAME = "name";

    de.hdodenhof.circleimageview.CircleImageView pp;
    ImageView cover;

    String folderURL;
    final String rootURL = Constants.ROOT_URL;
    final String getDataURL = rootURL+"apanel/get_data.php";

    String profilePictureUploadURL = rootURL+"profile_picture.php";
    String coverPictureUploadURL = rootURL+"cover_picture.php";

    String profilePicturePath = rootURL+"profile_picture/";
    String coverPicturePath = rootURL+"cover_picture/";

    private ProgressDialog loadingProfile = null;

    private ImageLoader imgLoader =  new ImageLoader(this);

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.loadingProfile = ProgressDialog.show(this, "Working..", "Downloading Data...", true, false);

        Constants.pictureChanged = false;

        InitiateResources();

        pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        cover = (ImageView) findViewById(R.id.imageView5);

        Thread mThread = new Thread() {
            @Override
            public void run() {
                getUserProfile();

                UserProfile userPictures = new UserProfile();
                userPictures.getUserPictures(ProfileActivity.this);
                userID = userPictures.getUserID(ProfileActivity.this);

                String profilePictureParameter = "&getpp=1&txtUserID="+userID;
                String coverPictureParameter = "&getcp=1&txtUserID="+userID;

                String profilePictureUrl = getData(profilePictureParameter);
                String coverPictureUrl = getData(coverPictureParameter);

                /**
                 setCoverPicture(rootURL+coverPictureUrl);
                 //Log.e(TAG, "userSavedCoverPictures: " + userPictures.userSavedCoverPictures);
                 setProfilePicture(rootURL+profilePictureUrl);
                 //Log.e(TAG, "userSavedProfilePictures: " + userPictures.userSavedProfilePictures);
                 */

                new loadingProfileData().execute(rootURL+profilePictureUrl, rootURL+coverPictureUrl);

            }
        };
        mThread.start();

//        setCoverPicture();
//        setProfilePicture();

        /*
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String,?> keys = prefs.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.d("map values",entry.getKey() + ": " +
                    entry.getValue().toString());
        }
        */

        addListenerOnButton();
//        addListenerOnScroll();
        addListenerOnText();
        setScrollSpacer();
//        setScrollViewBottomPadding();

//        ImageView attendance = (ImageView) findViewById(R.id.imageView9);
//        if (host.getCurrentTab() == 3)
//        {
//            attendance.setVisibility(View.VISIBLE);
//        }
//        else
//        {
//            attendance.setVisibility(View.INVISIBLE);
//        }

//        LinearLayout attendanceTableContainer = (LinearLayout) findViewById(R.id.attendanceTableContainer);
//        ImageView attendanceImage = new ImageView(this);
//        attendanceImage.setImageResource(R.drawable.attendance);
//        if (host.getCurrentTab() == 3)
//        {
//            attendanceTableContainer.addView(attendanceImage);
//        }
//        else
//        {
//            attendanceTableContainer.removeView(attendanceImage);
//        }

    }


    @Override
    public void onClick(View view){

    }

    private void addListenerOnButton()
    {

        final de.hdodenhof.circleimageview.CircleImageView changePP = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        final ImageView changeCP = (ImageView) findViewById(R.id.imageView5);

        //******************************************
        //TAB CONTROLLER
        //***************

        final TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        //TabHost.TabSpec spec = host.newTabSpec("Tab One");
        //spec.setContent(R.id.tab1);
        //spec.setIndicator("Bio");
        //host.addTab(spec);
        host.addTab(host.newTabSpec("Tab One").setIndicator("Bio").setContent(R.id.tab1));

        //Tab 2
        //spec = host.newTabSpec("Tab Two");
        //spec.setContent(R.id.tab2);
        //spec.setIndicator("Schedule");
        //host.addTab(spec);
        host.addTab(host.newTabSpec("Tab Two").setIndicator("Schedule").setContent(R.id.tab2));

        //Tab 3
        //spec = host.newTabSpec("Tab Three");
        //spec.setContent(R.id.tab3);
        //spec.setIndicator("Grades");
        //host.addTab(spec);
        host.addTab(host.newTabSpec("Tab Three").setIndicator("Grades").setContent(R.id.tab3));

        //Tab 4
        //spec = host.newTabSpec("Tab Three");
        //spec.setContent(R.id.tab3);
        //spec.setIndicator("Attendance");
        //host.addTab(spec);
        host.addTab(host.newTabSpec("Tab Four").setIndicator("Attendance").setContent(R.id.tab4));

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
        tab4 = (TextView) host.getTabWidget().getChildAt(3).findViewById(android.R.id.title);
        //tab1bg = host.getTabWidget().getChildAt(0).findViewById(android.R.id.background);
        tab1.setTextSize(10);
        tab2.setTextSize(10);
        tab3.setTextSize(10);
        tab4.setTextSize(10);

        tab1.setAllCaps(false);
        tab2.setAllCaps(false);
        tab3.setAllCaps(false);
        tab4.setAllCaps(false);

        tab1.setTextColor(Color.WHITE);
        tab2.setTextColor(Color.WHITE);
        tab3.setTextColor(Color.WHITE);
        tab4.setTextColor(Color.WHITE);

        if (tab1.isFocused())
        {
            tab1.setTextColor(Color.WHITE);
        }
        else if (tab2.isFocused())
        {
            tab2.setTextColor(Color.WHITE);
        }
        else if (tab3.isFocused())
        {
            tab3.setTextColor(Color.WHITE);
        }
        else if (tab4.isFocused())
        {
            tab4.setTextColor(Color.WHITE);
        }
        else{}

        //******************
        //TAB CONTROLLER END
        //******************************************

        profileBackButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                ProfileActivity.this.finish();
            }

        });

        profileEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (statusEditing == false)
                {
                    profileEditButton.setImageResource(R.drawable.bt_setting_ok);
                    textProfileName.setVisibility(View.INVISIBLE);
                    textProfileStatus.setVisibility(View.INVISIBLE);

                    textProfileSchooltitle.setVisibility(View.INVISIBLE);
                    textProfileSchoolClass.setVisibility(View.INVISIBLE);

                    textProfileSchoolMajor.setVisibility(View.INVISIBLE);
                    textProfileSchooladdress.setVisibility(View.INVISIBLE);

                    textBioBirthday.setVisibility(View.INVISIBLE);
                    textBioPhone.setVisibility(View.INVISIBLE);
                    textBioEmail.setVisibility(View.INVISIBLE);

                    textBioHistory.setVisibility(View.INVISIBLE);
                    textBioOrganization.setVisibility(View.INVISIBLE);
                    textBioInterest.setVisibility(View.INVISIBLE);

                    editProfileName.setVisibility(View.VISIBLE);
                    editProfileStatus.setVisibility(View.VISIBLE);

                    editProfileSchooltitle.setVisibility(View.VISIBLE);
                    editProfileSchoolClass.setVisibility(View.VISIBLE);

                    editProfileSchoolMajor.setVisibility(View.VISIBLE);
                    editProfileSchooladdress.setVisibility(View.VISIBLE);

                    editBioBirthday.setVisibility(View.VISIBLE);
                    editBioPhone.setVisibility(View.VISIBLE);
                    editBioEmail.setVisibility(View.VISIBLE);

                    editBioHistory.setVisibility(View.VISIBLE);
                    editBioOrganization.setVisibility(View.VISIBLE);
                    editBioInterest.setVisibility(View.VISIBLE);

                    editProfileName.requestFocus();
                    statusEditing = true;

                    Toast.makeText(ProfileActivity.this, "Klik pada gambar untuk mengubah" , Toast.LENGTH_LONG).show();
                }
                else
                {
                    final ProgressDialog loadingApp = ProgressDialog.show(ProfileActivity.this,"Saving Data...","Please wait...",false,false);

                    Thread mThread = new Thread() {
                        @Override
                        public void run() {
                            saveUserProfile();
                            loadingApp.dismiss();
                        }
                    };
                    mThread.start();

                    editProfileName.clearFocus();

                    textProfileName.setVisibility(View.VISIBLE);
                    textProfileStatus.setVisibility(View.VISIBLE);

                    textProfileSchooltitle.setVisibility(View.VISIBLE);
                    textProfileSchoolClass.setVisibility(View.VISIBLE);

                    textProfileSchoolMajor.setVisibility(View.VISIBLE);
                    textProfileSchooladdress.setVisibility(View.VISIBLE);

                    textBioBirthday.setVisibility(View.VISIBLE);
                    textBioPhone.setVisibility(View.VISIBLE);
                    textBioEmail.setVisibility(View.VISIBLE);

                    textBioHistory.setVisibility(View.VISIBLE);
                    textBioOrganization.setVisibility(View.VISIBLE);
                    textBioInterest.setVisibility(View.VISIBLE);

                    editProfileName.setVisibility(View.INVISIBLE);
                    editProfileStatus.setVisibility(View.INVISIBLE);

                    editProfileSchooltitle.setVisibility(View.INVISIBLE);
                    editProfileSchoolClass.setVisibility(View.INVISIBLE);

                    editProfileSchoolMajor.setVisibility(View.INVISIBLE);
                    editProfileSchooladdress.setVisibility(View.INVISIBLE);

                    editBioBirthday.setVisibility(View.INVISIBLE);
                    editBioPhone.setVisibility(View.INVISIBLE);
                    editBioEmail.setVisibility(View.INVISIBLE);

                    editBioHistory.setVisibility(View.INVISIBLE);
                    editBioOrganization.setVisibility(View.INVISIBLE);
                    editBioInterest.setVisibility(View.INVISIBLE);

                    profileEditButton.setImageResource(R.drawable.bt_profile_edit_small);
                    statusEditing = false;
                }
            }
        });

        changePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statusEditing)
                {
                    showFileChooser(changePP, profilePictureUploadURL);
                }
                else {}
            }
        });

        changeCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(statusEditing)
                {
                    showFileChooser(changeCP, coverPictureUploadURL);
                }
                else {}
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

        final TabHost host = (TabHost)findViewById(R.id.tabHost);

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

        // tab 2
        TextView scheduleMonday = (TextView) findViewById(R.id.scheduleMonday);
        TextView scheduleTuesday = (TextView) findViewById(R.id.scheduleTuesday);
        TextView scheduleWednesday = (TextView) findViewById(R.id.scheduleWednesday);
        TextView scheduleThursday = (TextView) findViewById(R.id.scheduleThursday);
        TextView scheduleFriday = (TextView) findViewById(R.id.scheduleFriday);
        TextView scheduleSaturday = (TextView) findViewById(R.id.scheduleSaturday);

        TextView subject0 = (TextView) findViewById(R.id.scheduleSubject0);
        TextView subject1 = (TextView) findViewById(R.id.scheduleSubject1);
        TextView subject2 = (TextView) findViewById(R.id.scheduleSubject2);
        TextView subject3 = (TextView) findViewById(R.id.scheduleSubject3);
        TextView subject4 = (TextView) findViewById(R.id.scheduleSubject4);
        TextView subject5 = (TextView) findViewById(R.id.scheduleSubject5);
        TextView subject6 = (TextView) findViewById(R.id.scheduleSubject6);
        TextView subject7 = (TextView) findViewById(R.id.scheduleSubject7);
        TextView subject8 = (TextView) findViewById(R.id.scheduleSubject8);

        final ImageView mondayHighlight = (ImageView) findViewById(R.id.mondayHighlight);
        final ImageView tuesdayHighlight = (ImageView) findViewById(R.id.tuesdayHighlight);
        final ImageView wednesdayHighlight = (ImageView) findViewById(R.id.wednesdayHighlight);
        final ImageView thursdayHighlight = (ImageView) findViewById(R.id.thursdayHighlight);
        final ImageView fridayHighlight = (ImageView) findViewById(R.id.fridayHighlight);
        final ImageView saturdayHighlight = (ImageView) findViewById(R.id.saturdayHighlight);

        scheduleMonday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.VISIBLE);
                tuesdayHighlight.setVisibility(View.INVISIBLE);
                wednesdayHighlight.setVisibility(View.INVISIBLE);
                thursdayHighlight.setVisibility(View.INVISIBLE);
                fridayHighlight.setVisibility(View.INVISIBLE);
                saturdayHighlight.setVisibility(View.INVISIBLE);
            }
        });

        scheduleTuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.INVISIBLE);
                tuesdayHighlight.setVisibility(View.VISIBLE);
                wednesdayHighlight.setVisibility(View.INVISIBLE);
                thursdayHighlight.setVisibility(View.INVISIBLE);
                fridayHighlight.setVisibility(View.INVISIBLE);
                saturdayHighlight.setVisibility(View.INVISIBLE);
            }
        });

        scheduleWednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.INVISIBLE);
                tuesdayHighlight.setVisibility(View.INVISIBLE);
                wednesdayHighlight.setVisibility(View.VISIBLE);
                thursdayHighlight.setVisibility(View.INVISIBLE);
                fridayHighlight.setVisibility(View.INVISIBLE);
                saturdayHighlight.setVisibility(View.INVISIBLE);
            }
        });

        scheduleThursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.INVISIBLE);
                tuesdayHighlight.setVisibility(View.INVISIBLE);
                wednesdayHighlight.setVisibility(View.INVISIBLE);
                thursdayHighlight.setVisibility(View.VISIBLE);
                fridayHighlight.setVisibility(View.INVISIBLE);
                saturdayHighlight.setVisibility(View.INVISIBLE);
            }
        });

        scheduleFriday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.INVISIBLE);
                tuesdayHighlight.setVisibility(View.INVISIBLE);
                wednesdayHighlight.setVisibility(View.INVISIBLE);
                thursdayHighlight.setVisibility(View.INVISIBLE);
                fridayHighlight.setVisibility(View.VISIBLE);
                saturdayHighlight.setVisibility(View.INVISIBLE);
            }
        });

        scheduleSaturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mondayHighlight.setVisibility(View.INVISIBLE);
                tuesdayHighlight.setVisibility(View.INVISIBLE);
                wednesdayHighlight.setVisibility(View.INVISIBLE);
                thursdayHighlight.setVisibility(View.INVISIBLE);
                fridayHighlight.setVisibility(View.INVISIBLE);
                saturdayHighlight.setVisibility(View.VISIBLE);
            }
        });

        // tab 3
        TextView gradeSubject1 = (TextView) findViewById(R.id.subTabSubject1);
        TextView gradeSubject2 = (TextView) findViewById(R.id.subTabSubject2);
        TextView gradeSubject3 = (TextView) findViewById(R.id.subTabSubject3);
        TextView gradeSubject4 = (TextView) findViewById(R.id.subTabSubject4);
        TextView gradeSubject5 = (TextView) findViewById(R.id.subTabSubject5);
        TextView gradeSubject6 = (TextView) findViewById(R.id.subTabSubject6);

        final ImageView subjectHighlight1 = (ImageView) findViewById(R.id.subTabSubjectHighlight1);
        final ImageView subjectHighlight2 = (ImageView) findViewById(R.id.subTabSubjectHighlight2);
        final ImageView subjectHighlight3 = (ImageView) findViewById(R.id.subTabSubjectHighlight3);
        final ImageView subjectHighlight4 = (ImageView) findViewById(R.id.subTabSubjectHighlight4);
        final ImageView subjectHighlight5 = (ImageView) findViewById(R.id.subTabSubjectHighlight5);
        final ImageView subjectHighlight6 = (ImageView) findViewById(R.id.subTabSubjectHighlight6);

        gradeSubject1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.VISIBLE);
                subjectHighlight2.setVisibility(View.INVISIBLE);
                subjectHighlight3.setVisibility(View.INVISIBLE);
                subjectHighlight4.setVisibility(View.INVISIBLE);
                subjectHighlight5.setVisibility(View.INVISIBLE);
                subjectHighlight6.setVisibility(View.INVISIBLE);
            }
        });

        gradeSubject2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.INVISIBLE);
                subjectHighlight2.setVisibility(View.VISIBLE);
                subjectHighlight3.setVisibility(View.INVISIBLE);
                subjectHighlight4.setVisibility(View.INVISIBLE);
                subjectHighlight5.setVisibility(View.INVISIBLE);
                subjectHighlight6.setVisibility(View.INVISIBLE);
            }
        });

        gradeSubject3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.INVISIBLE);
                subjectHighlight2.setVisibility(View.INVISIBLE);
                subjectHighlight3.setVisibility(View.VISIBLE);
                subjectHighlight4.setVisibility(View.INVISIBLE);
                subjectHighlight5.setVisibility(View.INVISIBLE);
                subjectHighlight6.setVisibility(View.INVISIBLE);
            }
        });

        gradeSubject4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.INVISIBLE);
                subjectHighlight2.setVisibility(View.INVISIBLE);
                subjectHighlight3.setVisibility(View.INVISIBLE);
                subjectHighlight4.setVisibility(View.VISIBLE);
                subjectHighlight5.setVisibility(View.INVISIBLE);
                subjectHighlight6.setVisibility(View.INVISIBLE);
            }
        });

        gradeSubject5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.INVISIBLE);
                subjectHighlight2.setVisibility(View.INVISIBLE);
                subjectHighlight3.setVisibility(View.INVISIBLE);
                subjectHighlight4.setVisibility(View.INVISIBLE);
                subjectHighlight5.setVisibility(View.VISIBLE);
                subjectHighlight6.setVisibility(View.INVISIBLE);
            }
        });

        gradeSubject6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                subjectHighlight1.setVisibility(View.INVISIBLE);
                subjectHighlight2.setVisibility(View.INVISIBLE);
                subjectHighlight3.setVisibility(View.INVISIBLE);
                subjectHighlight4.setVisibility(View.INVISIBLE);
                subjectHighlight5.setVisibility(View.INVISIBLE);
                subjectHighlight6.setVisibility(View.VISIBLE);
            }
        });
    }

    private void addListenerOnScroll() {
        final TextView text8 = (TextView) findViewById(R.id.textView8);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        /**
        final LinearLayout fakeTabWidget = (LinearLayout) findViewById(R.id.fakeTabWidget);
        final LinearLayout[] tabs = new LinearLayout[4];
        tabs[0] = (LinearLayout) findViewById(R.id.tab1);
        tabs[1] = (LinearLayout) findViewById(R.id.tab2);
        tabs[2] = (LinearLayout) findViewById(R.id.tab3);
        tabs[3] = (LinearLayout) findViewById(R.id.tab4);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollYpx = scrollView.getScrollY();
                int scrollYdp = (int) (scrollYpx/(Resources.getSystem().getDisplayMetrics().ydpi/ DisplayMetrics.DENSITY_DEFAULT));
//                int scrollPositionY = 0;
//                text8.setText(String.valueOf(scrollYdp));

                if (scrollYdp >= 120)
                {
                    fakeTabWidget.setVisibility(View.VISIBLE);
                }
                else
                {
                    fakeTabWidget.setVisibility(View.INVISIBLE);
                }
            }
        });
         */
    }

    public void setScrollSpacer()
    {
        ConstraintLayout layout = (ConstraintLayout) findViewById(R.id.activity_profile);
        final TextView textView8 = (TextView) findViewById(R.id.textProfileStatus);
        final TextView textView11 = (TextView) findViewById(R.id.textView11);

        layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                float spacerTop = textView8.getBottom();
                float spacerBottom = textView11.getBottom();
            }
        });
    }

    public void setScrollViewBottomPadding()
    {
        LinearLayout buttonSocialMedia = (LinearLayout) findViewById(R.id.buttonSocialMedia);
        /**
        final LinearLayout linearTabContainer = (LinearLayout) findViewById(R.id.linearTabContainer);
        final LinearLayout tab1 = (LinearLayout) findViewById(R.id.tab1);
        final LinearLayout tab2 = (LinearLayout) findViewById(R.id.tab2);
        final LinearLayout tab3 = (LinearLayout) findViewById(R.id.tab3);
        final LinearLayout tab4 = (LinearLayout) findViewById(R.id.tab4);
        final ImageButton imageButton22 = (ImageButton) findViewById(R.id.imageButton22);
        final TextView textView8 = (TextView) findViewById(R.id.textView8);
        final TextView textView11 = (TextView) findViewById(R.id.textView11);

        buttonSocialMedia.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int buttonHeight = imageButton22.getHeight();
                float spacerTop = textView8.getBottom();
                float spacerBottom = textView11.getBottom();
                float spacerHeight = spacerBottom - spacerTop;
                int bottomPadding = buttonHeight;// + (int)spacerHeight;
                LinearLayout[] scrollingTab = new LinearLayout[4];
                scrollingTab[0] = tab1;
                scrollingTab[1] = tab2;
                scrollingTab[2] = tab3;
                scrollingTab[3] = tab4;

                for (int i=0;i<4;i++)
                {
                    scrollingTab[i].setPadding(0,0,0,bottomPadding);
                }
            }
        });
         */
    }

    public void getUserProfile()
    {
        UserProfile profile = new UserProfile();
        profile.getUserStatus(this);
        profile.getUserSchoolInfo(this);
        profile.getUserProfile(this);
        profile.getUserBio(this);

        userCustomName = profile.userSavedName;
        userCustomStatus = profile.userSavedStatus;

        inputSchooltitle = profile.userSavedSchooltitle;
        inputSchoolClass = profile.userSavedSchoolClass;
        inputSchoolMajor = profile.userSavedSchoolMajor;
        inputSchooladdress = profile.userSavedSchooladdress;

        textProfileName.setText(userCustomName);
        textProfileStatus.setText(userCustomStatus);

        textProfileSchooltitle.setText(inputSchooltitle);
        textProfileSchoolClass.setText(inputSchoolClass);
        textProfileSchoolMajor.setText(inputSchoolMajor);
        textProfileSchooladdress.setText(inputSchooladdress);

        textBioBirthday.setText(profile.userSavedBioBirthdate);
        textBioPhone.setText(profile.userSavedProfilePhone);
        textBioEmail.setText(profile.userSavedProfileEmail);

        textBioHistory.setText(profile.userSavedBioHistory);
        textBioOrganization.setText(profile.userSavedBioOrganization);
        textBioInterest.setText(profile.userSavedBioInterest);

        editProfileName.setText(userCustomName);
        editProfileStatus.setText(userCustomStatus);

        editProfileSchooltitle.setText(inputSchooltitle);
        editProfileSchoolClass.setText(inputSchoolClass);
        editProfileSchoolMajor.setText(inputSchoolMajor);
        editProfileSchooladdress.setText(inputSchooladdress);

        editBioBirthday.setText(profile.userSavedBioBirthdate);
        editBioPhone.setText(profile.userSavedProfilePhone);
        editBioEmail.setText(profile.userSavedProfileEmail);

        editBioHistory.setText(profile.userSavedBioHistory);
        editBioOrganization.setText(profile.userSavedBioOrganization);
        editBioInterest.setText(profile.userSavedBioInterest);
    }

    public void saveUserProfile()
    {
        final String name = editProfileName.getText().toString();
        final String status = editProfileStatus.getText().toString();

        final String schooltitle = editProfileSchooltitle.getText().toString();
        final String schoolClass = editProfileSchoolClass.getText().toString();
        final String schoolMajor = editProfileSchoolMajor.getText().toString();
        final String schooladdress = editProfileSchooladdress.getText().toString();

        final String birthday = editBioBirthday.getText().toString();
        final String phone = editBioPhone.getText().toString();
        final String email = editBioEmail.getText().toString();

        final String history = editBioHistory.getText().toString();
        final String organization = editBioOrganization.getText().toString();
        final String interest = editBioInterest.getText().toString();

        ProfileActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textProfileName.setText(name);
                textProfileStatus.setText(status);

                textProfileSchooltitle.setText(schooltitle);
                textProfileSchoolClass.setText(schoolClass);
                textProfileSchoolMajor.setText(schoolMajor);
                textProfileSchooladdress.setText(schooladdress);

                textBioBirthday.setText(birthday);
                textBioPhone.setText(phone);
                textBioEmail.setText(email);

                textBioHistory.setText(history);
                textBioOrganization.setText(organization);
                textBioInterest.setText(interest);

            }
        });

        UserProfile profile = new UserProfile();
        profile.saveUserStatus(this, name, status);
        profile.saveUserSchoolInfo(this, schooltitle, schoolClass, schoolMajor, schooladdress);
        profile.saveUserBio(this, phone, email, history, birthday, organization, interest);
    }


    public void setProfilePicture(String url)
    {
        //String url = "http://192.168.42.30/smaq/profile_picture/saya.png";

        Log.e(TAG, "profile picture url: " + url);

        /**
        if (pictureChanged) {
            loadImageFromURL(url,pp);
        }
        else {
            //imgLoader.DisplayImage(url, pp);
            pp.setImageBitmap(imgLoader.getCachedImage(url));
        }
         */
/**
        this.loadingProfile = ProgressDialog.show(this, "Working..", "Downloading Data...", true, false);
        new loadingProfileData().execute(url);
        pp.setImageBitmap(cachedImage);*/
    }

    public void setCoverPicture(String url)
    {
        //String url = "http://192.168.42.30/smaq/cover_picture/saya.png";

        /**
        Log.e(TAG, "cover picture url: " + url);
        if(pictureChanged) {
            loadImageFromURL(url, cover);
        }
        else {
            //imgLoader.DisplayImage(url, cover);
            cover.setImageBitmap(imgLoader.getCachedImage(url));
        }
         */

        this.loadingProfile = ProgressDialog.show(this, "Working..", "Downloading Data...", true, false);
        new loadingProfileData().execute(url);
        cover.setImageBitmap(cachedImage);

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

//    ********************
//    CHANGE AND SAVE PICTURE START
//    ********************
    private void showFileChooser(ImageView changedPicture, String uploadFolder)
    {
        folderURL = uploadFolder;
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
            fileNameString = userID; //getFileName(filePath);
            try {
                //Getting the Bitmap from Gallery
                Bitmap tmp = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                bitmap = getResizedBitmap(tmp, 750);
                //bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                //upload to server
                uploadImage(folderURL);

                //Setting the Bitmap to ImageView
                changedImage.setImageBitmap(bitmap);
                Log.e(TAG, "pictureChanged: ");
                Constants.pictureChanged = true;
                ImageLoader imageLoader = new ImageLoader(this);
                imageLoader.clearCache();

                Toast.makeText(this,folderURL, Toast.LENGTH_LONG).show();
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
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public String getFileName(Uri uri)
    {
        String uriString = uri.toString();
        File myFile = new File(uriString);
        String path = myFile.getAbsolutePath();
        String displayName = null;

        if (uriString.startsWith("content://"))
        {
            Cursor cursor = null;
            try
            {
                cursor = this.getContentResolver().query(uri, null, null, null, null);
                //getActivity().getContentResolver().query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst())
                {
                    displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }
            finally
            {
                cursor.close();
            }
        }
        else if (uriString.startsWith("file://"))
        {
            displayName = myFile.getName();
        }

        return displayName;
    }

    private void uploadImage(String uploadFolder)
    {
        //Showing the progress dialog
        final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, uploadFolder,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(ProfileActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        loading.dismiss();

                        //Showing toast
                        Toast.makeText(ProfileActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
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

        UserProfile userProfile = new UserProfile();
        if(uploadFolder.equals(profilePictureUploadURL))
        {
            userProfile.saveUserProfilePicture(this, profilePicturePath+fileNameString+".png");
        }
        else
        {
            userProfile.saveUserCoverPicture(this, coverPicturePath + fileNameString + ".png");
        }

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

//    ********************
//    CHANGE AND SAVE PICTURE END
//    ********************


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
            ProfileActivity.this.pp.setImageBitmap(bitmap[0]);
            ProfileActivity.this.cover.setImageBitmap(bitmap[1]);
            /**
            ProfileActivity.this.cachedImage = bitmap;
             */
            ProfileActivity.this.loadingProfile.dismiss();
        }
    }

    public void InitiateResources()
    {
        profileBackButton = (ImageButton) findViewById(R.id.profileBackButton);
        profileEditButton = (ImageButton) findViewById(R.id.profileEditButton);

        textProfileName = (TextView) findViewById(R.id.textProfileName);
        textProfileStatus = (TextView) findViewById(R.id.textProfileStatus);

        textProfileSchooltitle = (TextView) findViewById(R.id.profileSchooltitle);
        textProfileSchoolClass = (TextView) findViewById(R.id.profileSchoolClass);

        textProfileSchoolMajor = (TextView) findViewById(R.id.profileSchoolMajor);
        textProfileSchooladdress = (TextView) findViewById(R.id.profileSchooladdress);

        textBioBirthday = (TextView) findViewById(R.id.textProfileDetailContentBasicBirthdate);
        textBioPhone = (TextView) findViewById(R.id.textProfileDetailContentBasicPhone);
        textBioEmail = (TextView) findViewById(R.id.textProfileDetailContentBasicEmail);

        textBioHistory = (TextView) findViewById(R.id.subTabContententSchool1);
        textBioOrganization = (TextView) findViewById(R.id.subTabContententOrganization1);
        textBioInterest = (TextView) findViewById(R.id.subTabContententInterest);

        editProfileName = (EditText) findViewById(R.id.editProfileName);
        editProfileStatus = (EditText) findViewById(R.id.editProfileStatus);

        editProfileSchooltitle = (EditText) findViewById(R.id.editSchooltitle);
        editProfileSchoolClass = (EditText) findViewById(R.id.editSchoolClass);

        editProfileSchoolMajor = (EditText) findViewById(R.id.editSchoolMajor);
        editProfileSchooladdress = (EditText) findViewById(R.id.editSchooladdress);

        editBioBirthday = (EditText) findViewById(R.id.editProfileDetailContentBasicBirthdate);
        editBioPhone = (EditText) findViewById(R.id.editProfileDetailContentBasicPhone);
        editBioEmail = (EditText) findViewById(R.id.editProfileDetailContentBasicEmail);

        editBioHistory = (EditText) findViewById(R.id.subTabFieldContententSchool1);
        editBioOrganization = (EditText) findViewById(R.id.subTabFieldContententOrganization1);
        editBioInterest = (EditText) findViewById(R.id.subTabFieldContententInterest);

    }

}
