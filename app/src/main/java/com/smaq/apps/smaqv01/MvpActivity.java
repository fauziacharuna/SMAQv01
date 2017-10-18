package com.smaq.apps.smaqv01;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class MvpActivity extends AppCompatActivity {

    private RecyclerView recyclerNominees;
    private MvpNomineeRecyclerAdapter nAdapter;
    private List<MvpNominee> nomineeList = new ArrayList<>();
    private ProgressDialog loadingNominees;

    private String TAG = MvpActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        ImageButton mvpBackButton = (ImageButton) findViewById(R.id.mvpBackButton);

        mvpBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MvpActivity.this.finish();
            }
        });

        TextView textMvpRecommendationList = (TextView) findViewById(R.id.textMvpRecommendationList);

        textMvpRecommendationList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MvpActivity.this, NomineeListActivity.class);
                MvpActivity.this.startActivity(myIntent);
            }
        });

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

}
