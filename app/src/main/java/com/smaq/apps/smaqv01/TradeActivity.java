package com.smaq.apps.smaqv01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class TradeActivity extends AppCompatActivity {

    ImageButton mainMenuMain, mainMenuNongkrongButton, mainMenuPromotionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade);

        addListenerOnButton();
    }

    private void addListenerOnButton() {
        mainMenuMain = (ImageButton) findViewById(R.id.imageButton3);
        mainMenuNongkrongButton = (ImageButton) findViewById(R.id.imageButton4);
        mainMenuPromotionButton = (ImageButton) findViewById(R.id.imageButton7);

        mainMenuMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(TradeActivity.this, MainActivity.class);
                TradeActivity.this.startActivity(myIntent);
                TradeActivity.this.finish();
            }
        });

        mainMenuNongkrongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(TradeActivity.this, MainNongkrongActivity.class);
                TradeActivity.this.startActivity(myIntent);
                TradeActivity.this.finish();
            }
        });

        mainMenuPromotionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent myIntent = new Intent(TradeActivity.this, PromotionActivity.class);
                TradeActivity.this.startActivity(myIntent);
                TradeActivity.this.finish();
            }
        });
    }
}
