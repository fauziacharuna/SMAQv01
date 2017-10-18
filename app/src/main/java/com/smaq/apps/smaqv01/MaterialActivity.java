package com.smaq.apps.smaqv01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MaterialActivity extends AppCompatActivity {

    private String mat_title;
    private String mat_contents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        mat_title = getIntent().getStringExtra("mat_title");
        mat_contents = getIntent().getStringExtra("mat_contents");

        TextView materialTitleText = (TextView) findViewById(R.id.materialTitleText);
        materialTitleText.setText(mat_title);
        materialTitleText.setSelected(true);

        TextView materialContent = (TextView) findViewById(R.id.materialContent);
        materialContent.setText(mat_contents);

        addListenerOnButton();

    }

    private void addListenerOnButton() {
        ImageButton materialBackButton = (ImageButton) findViewById(R.id.materialBackButton);

        materialBackButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                MaterialActivity.this.finish();
            }

        });
    }

}
