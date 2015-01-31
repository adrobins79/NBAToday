package com.example.adrian.nbaplayers;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.HashMap;


/**
 * Created by adrian on 1/31/15.
 */

public class ScheduleFragment extends Fragment {
    OnGameSelectedListener onGameSelectedListener;

    // The container Activity must implement this interface so the frag can deliver messages
    public interface OnGameSelectedListener {
        /** Called by ScheduleFragment when a list item is selected */
        public void onGameSelected(String gameId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.schedule, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ListView list = (ListView) getActivity().findViewById(R.id.list);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    HashMap<String,String> dataMap = (HashMap<String,String>)adapterView.getItemAtPosition(position);
                    ((MainActivity)getActivity()).onGameSelected(dataMap.get("id"));
                }
            }
        );
        new GetScheduleTask(getActivity()).execute();
    }
    @Override
    public void onStart(){
        super.onStart();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            onGameSelectedListener = (OnGameSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }

    }

}