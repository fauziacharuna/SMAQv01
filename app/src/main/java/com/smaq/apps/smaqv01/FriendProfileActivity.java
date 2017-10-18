package com.smaq.apps.smaqv01;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendProfileActivity extends AppCompatActivity {

    private String friend_name, friend_email, friend_phone, friend_status, friend_history, friend_birthdate, friend_organization, friend_interest, friend_schooltitle, friend_schoolcity, friend_class, friend_major, friend_profilepicture, friend_coverpicture;
    private TextView textProfileName, textProfileStatus, textProfileSchooltitle, textProfileSchoolClass,textProfileSchoolMajor, textProfileSchooladdress, textBioBirthday, textBioPhone, textBioEmail, textBioHistory, textBioOrganization, textBioInterest;

    private CircleImageView pp;
    private ImageView cover;

    final String rootURL = Constants.ROOT_URL;

    private ProgressDialog loadingProfile = null;
    private ImageLoader imgLoader =  new ImageLoader(this);
    private String TAG = FriendProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);

        this.loadingProfile = ProgressDialog.show(this, "Working..", "Downloading Data...", true, false);

        friend_name = getIntent().getStringExtra("friend_name");
        friend_email = getIntent().getStringExtra("friend_email");
        friend_phone = getIntent().getStringExtra("friend_phone");
        friend_status = getIntent().getStringExtra("friend_status");
        friend_history = getIntent().getStringExtra("friend_history");
        friend_birthdate = getIntent().getStringExtra("friend_birthdate");
        friend_organization = getIntent().getStringExtra("friend_organization");
        friend_interest = getIntent().getStringExtra("friend_interest");
        friend_schooltitle = getIntent().getStringExtra("friend_schooltitle");
        friend_schoolcity = getIntent().getStringExtra("friend_schoolcity");
        friend_class = getIntent().getStringExtra("friend_class");
        friend_major = getIntent().getStringExtra("friend_major");
        friend_profilepicture = getIntent().getStringExtra("friend_profilepicture");
        friend_coverpicture = getIntent().getStringExtra("friend_coverpicture");

        InitiateResources();
        setFriendProfilePage();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                Log.e(TAG, "friend_profilepicture : "+rootURL+friend_profilepicture);
                Log.e(TAG, "friend_coverpicture : "+rootURL+friend_coverpicture);
                new loadingProfileData().execute(rootURL+friend_profilepicture, rootURL+friend_coverpicture);
            }
        };
        mThread.start();

        addListenerOnButton();
        addListenerOnText();
    }

    private void addListenerOnButton()
    {
        ImageButton friendProfileBackButton = (ImageButton) findViewById(R.id.friendProfileBackButton);

        friendProfileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FriendProfileActivity.this.finish();
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

    public void InitiateResources()
    {
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

        pp = (de.hdodenhof.circleimageview.CircleImageView) findViewById(R.id.profile_image);
        cover = (ImageView) findViewById(R.id.imageView5);
    }

    public void setFriendProfilePage()
    {
        String userCustomName = friend_name;
        String userCustomStatus = friend_status;

        String inputSchooltitle = friend_schooltitle;
        String inputSchoolClass = friend_class;
        String inputSchoolMajor = friend_major;
        String inputSchooladdress = friend_schoolcity;

        textProfileName.setText(userCustomName);
        textProfileStatus.setText(userCustomStatus);

        textProfileSchooltitle.setText(inputSchooltitle);
        textProfileSchoolClass.setText(inputSchoolClass);
        textProfileSchoolMajor.setText(inputSchoolMajor);
        textProfileSchooladdress.setText(inputSchooladdress);

        textBioBirthday.setText(friend_birthdate);
        textBioPhone.setText(friend_phone);
        textBioEmail.setText(friend_email);

        textBioHistory.setText(friend_history);
        textBioOrganization.setText(friend_organization);
        textBioInterest.setText(friend_interest);
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
            if(bitmap[0] != null) {
                FriendProfileActivity.this.pp.setImageBitmap(bitmap[0]);
            }
            if(bitmap[1] != null) {
                FriendProfileActivity.this.cover.setImageBitmap(bitmap[1]);
            }
            /**
             ProfileActivity.this.cachedImage = bitmap;
             */
            FriendProfileActivity.this.loadingProfile.dismiss();
        }
    }

}
