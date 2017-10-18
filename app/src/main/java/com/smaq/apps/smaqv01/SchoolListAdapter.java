package com.smaq.apps.smaqv01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by felix on 06/03/17.
 */

public class SchoolListAdapter extends ArrayAdapter<School> {
    private ArrayList<School> schools;
    private ArrayList<School> schoolsAll;
    private ArrayList<School> schoolsSuggestions;
    private int viewResourceId;

    public SchoolListAdapter(Context context, int resource, ArrayList<School> schools) {
        super(context, resource, schools);
        this.schools = schools;
        this.schoolsAll = (ArrayList<School>) schools.clone();
        this.schoolsSuggestions = new ArrayList<School>();
        this.viewResourceId = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(viewResourceId, null);
        }
        School school = schools.get(position);
        /*
        if (school != null) {
            TextView customerNameLabel = (TextView) v.findViewById(R.id.customerNameLabel);
            if (customerNameLabel != null) {
                Log.i(MY_DEBUG_TAG, "getView Customer Name:"+customer.getName());
                customerNameLabel.setText(school.getTitle());
            }
        }
        */
        return v;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((School)(resultValue)).getTitle();
            return str;
        }
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if(constraint != null) {
                schoolsSuggestions.clear();
                for (School school : schoolsAll) {
                    if(school.getTitle().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                        schoolsSuggestions.add(school);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = schoolsSuggestions;
                filterResults.count = schoolsSuggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<School> filteredList = (ArrayList<School>) results.values;
            if(results != null && results.count > 0) {
                clear();
                for (School s : filteredList) {
                    add(s);
                }
                notifyDataSetChanged();
            }
        }
    };
}
