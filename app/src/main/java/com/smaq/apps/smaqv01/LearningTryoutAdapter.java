package com.smaq.apps.smaqv01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by felix on 23/02/17.
 */

public class LearningTryoutAdapter extends RecyclerView.Adapter{

    private String TAG = LearningTryoutAdapter.class.getSimpleName();

    private List<Tryout> tryoutList;
    private Context context;

    //private boolean seeResult = false;

    private SharedPreferences results;
    private SharedPreferences.Editor editor;

    public class LearningTryoutViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, answer;
        public CheckBox result;
        public LearningTryoutViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.textTryTitle);
            answer = (TextView) itemView.findViewById(R.id.textViewTryAnswer);
            //result = (CheckBox) itemView.findViewById(R.id.checkBoxTryResult);
        }
    }

    public LearningTryoutAdapter(List<Tryout> tryoutList)
    {
        this.tryoutList = tryoutList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_tryout_item, parent, false);

        context = parent.getContext();

        results = PreferenceManager.getDefaultSharedPreferences(context);

        return new LearningTryoutAdapter.LearningTryoutViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Tryout tryout = tryoutList.get(position);
        final int pos = position;

        LearningTryoutAdapter.LearningTryoutViewHolder viewHolder = (LearningTryoutAdapter.LearningTryoutViewHolder) holder;
        viewHolder.title.setText(tryout.getTitle());

        //seeResult = viewHolder.result.isEnabled();

        /*
        viewHolder.result.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked())
                {
                    compoundButton.setChecked(true);
                    //seeResult = true;

                    String showResult = "showResult"+pos;

                    editor = results.edit();
                    editor.putBoolean(showResult, true);
                    editor.commit();
                }
                else
                {
                    compoundButton.setChecked(false);
                    //seeResult = false;

                    String showResult = "showResult"+pos;

                    editor = results.edit();
                    editor.putBoolean(showResult, false);
                    editor.commit();
                }
            }
        });
        */

        viewHolder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, QuizActivity.class);

                myIntent.putExtra("try_id", tryout.getID());
                myIntent.putExtra("try_title", tryout.getTitle());

                String showResultPos = "showResult"+pos;
                boolean showResult = results.getBoolean(showResultPos, false);

                myIntent.putExtra("seeResult", showResult);
                myIntent.putExtra("seeAnswer", Constants.QUIZ_STATE_QUESTION);
                //Log.e(TAG, "seeResult: "+showResult);

                editor = results.edit();
                editor.putBoolean(showResultPos, false);
                editor.commit();
                //myIntent.putExtra("mat_contents", material.getContents());

                context.startActivity(myIntent);
            }
        });

        viewHolder.answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(context, QuizActivity.class);

                myIntent.putExtra("try_id", tryout.getID());
                myIntent.putExtra("try_title", tryout.getTitle());

                String showResultPos = "showResult"+pos;
                boolean showResult = results.getBoolean(showResultPos, false);

                myIntent.putExtra("seeResult", showResult);
                myIntent.putExtra("seeAnswer", Constants.QUIZ_STATE_ANSWERS);
                //Log.e(TAG, "seeResult: "+showResult);

                editor = results.edit();
                editor.putBoolean(showResultPos, false);
                editor.commit();
                //myIntent.putExtra("mat_contents", material.getContents());

                context.startActivity(myIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return tryoutList.size();
    }
}
