package com.example.itest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.itest.TestListInterface.UserTests;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainUserInterface extends AppCompatActivity {
    private final int REQUEST_CODE_USER_TEST = 111;
    private final int REQUEST_CODE_USER_PROGRESS = 222;
    private final int REQUEST_CODE_USER_GOAL = 333;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_user_interface);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        bottomNav.setSelectedItemId(R.id.testsActivity);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()){
                case R.id.testsActivity:
                    selectedFragment = new UserTests();

                    break;
                case R.id.progressesActivity:
                    selectedFragment = new UserProgress();
                    break;
                case R.id.goalsActivity:
                    selectedFragment = new UserGoal();
                    break;
            }
           getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
            return true;
        }
    };

}