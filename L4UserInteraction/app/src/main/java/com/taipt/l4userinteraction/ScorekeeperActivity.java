package com.taipt.l4userinteraction;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.taipt.l4userinteraction.databinding.ActivityScorekeeperBinding;

public class ScorekeeperActivity extends AppCompatActivity {

    static final String STATE_SCORE_1 = "Team 1 Score";
    static final String STATE_SCORE_2 = "Team 2 Score";

    private ActivityScorekeeperBinding binding;

    private int mScore1;
    private int mScore2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScorekeeperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (savedInstanceState != null) {
            mScore1 = savedInstanceState.getInt(STATE_SCORE_1);
            mScore2 = savedInstanceState.getInt(STATE_SCORE_2);

            //Set the score text views
            binding.txtScore1.setText(String.valueOf(mScore1));
            binding.txtScore2.setText(String.valueOf(mScore2));
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        // Save the scores.
        outState.putInt(STATE_SCORE_1, mScore1);
        outState.putInt(STATE_SCORE_2, mScore2);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scorekeeper, menu);
        // Change the label of the menu based on the state of the app.
        int nightMode = AppCompatDelegate.getDefaultNightMode();
        if(nightMode == AppCompatDelegate.MODE_NIGHT_YES){
            menu.findItem(R.id.night_mode).setTitle(R.string.day_mode);
        } else{
            menu.findItem(R.id.night_mode).setTitle(R.string.night_mode);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.night_mode) {
            // Get the night mode state of the app.
            int nightMode = AppCompatDelegate.getDefaultNightMode();
            //Set the theme mode for the restarted activity
            if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                AppCompatDelegate.setDefaultNightMode
                        (AppCompatDelegate.MODE_NIGHT_YES);
            }
            // Recreate the activity for the theme change to take effect.
            recreate();
        }
        return true;
    }

    public void decreaseScore(View view) {

        int viewID = view.getId();
        switch (viewID) {
            //If it was on Team 1
            case R.id.ib_decrease_team_1:
                //Decrement the score and update the TextView
                mScore1--;
                binding.txtScore1.setText(String.valueOf(mScore1));
                break;
            //If it was Team 2
            case R.id.ib_decrease_team_2:
                //Decrement the score and update the TextView
                mScore2--;
                binding.txtScore2.setText(String.valueOf(mScore2));
        }

    }
    public void increaseScore (View view){
        //Get the ID of the button that was clicked
        int viewID = view.getId();
        switch (viewID) {
            //If it was on Team 1
            case R.id.ib_increase_team_1:
                //Increment the score and update the TextView
                mScore1++;
                binding.txtScore1.setText(String.valueOf(mScore1));
                break;
            //If it was Team 2
            case R.id.ib_increase_team_2:
                //Increment the score and update the TextView
                mScore2++;
                binding.txtScore2.setText(String.valueOf(mScore2));

        }
    }
}