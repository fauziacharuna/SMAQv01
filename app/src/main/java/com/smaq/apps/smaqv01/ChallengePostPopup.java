package com.smaq.apps.smaqv01;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.List;

/**
 * Created by felix on 17/02/17.
 */

public class ChallengePostPopup extends Dialog implements android.view.View.OnClickListener{

    public Activity act;
    public Dialog dial;

    public ChallengePostPopup(Context context) {
        super(context);
        this.act = (Activity) context;
    }

    public ChallengePostPopup(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected ChallengePostPopup(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //LayoutInflater inflater = (LayoutInflater) act.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //View viewDialog = inflater.inflate(R.layout.popup_challenge_post, null, false);
        //myDialog.setCanceledOnTouchOutside(false);
        ViewGroup viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        View viewDialog = LayoutInflater.from(act)
                .inflate(R.layout.popup_challenge_post, viewGroup, false);
        act.setContentView(viewDialog);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {

    }
}
