package com.smaq.apps.smaqv01;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;

import com.smaq.apps.smaqv01.Important.LoginActivity;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class WelcomeActivity extends AppCompatActivity {

    ImageButton welcomeStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        HorizontalScrollView welcomeSlideScroll = (HorizontalScrollView) findViewById(R.id.welcomeSlideScroll);
        OverScrollDecoratorHelper.setUpOverScroll(welcomeSlideScroll);

        addListenerOnButton();
    }

    private void addListenerOnButton()
    {
        welcomeStartButton = (ImageButton) findViewById(R.id.welcomeButtonMain);

        welcomeStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isStoragePermissionGranted(WelcomeActivity.this)) {
                    Intent myIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    WelcomeActivity.this.startActivity(myIntent);
                    WelcomeActivity.this.finish();
                }
            }
        });
    }

    public  boolean isStoragePermissionGranted(Context context) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v(TAG,"Permission is granted");
                return true;
            }
            else {
                Activity activity = (Activity) context;
                //Log.v(TAG,"Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            //Log.v(TAG,"Permission is granted");
            return true;
        }
    }
}
