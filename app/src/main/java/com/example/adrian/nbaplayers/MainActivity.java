package com.example.adrian.nbaplayers;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;


public class MainActivity extends Activity implements ScheduleFragment.OnGameSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_container);

        ScheduleFragment scheduleFragment = new ScheduleFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, scheduleFragment);
        transaction.commit();
    }


    @Override
    public void onGameSelected(String gameId) {

        GameFragment gameFragment = new GameFragment();
        Bundle bundle = new Bundle();
        bundle.putString("gameId", gameId);
        gameFragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_container, gameFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}
