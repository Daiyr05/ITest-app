package com.example.itest.TestListInterface;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.os.Bundle;

import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.itest.R;
import com.example.itest.modelUser.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddTestActivity extends AppCompatActivity {
    private ScrollView scroll;
    private LinearLayout layoutlList;
    private ImageButton addTest;
    private Button sendDataToDB;
    private Toolbar toolbar;
    private  LinearLayout clickLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_test);
        TestAddOperation.setCounterOfTests(0);
        layoutlList = findViewById(R.id.layoutList);
        clickLayout = findViewById(R.id.clickLayout);
        addTest = findViewById(R.id.btnAddTest);
        sendDataToDB = findViewById(R.id.sendDataToBD);
        TestAddOperation.add(this,addTest);
        TestAddOperation.saveToDB(this,sendDataToDB);
        toolbar = findViewById(R.id.customToolbarForAddTest);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        final View activityRootView = findViewById(R.id.idOfAddTest);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = activityRootView.getRootView().getHeight() - activityRootView.getHeight();
                if (heightDiff > dpToPx(getApplicationContext(), 200)) { // if more than 200 dp, it's probably a keyboard...
                    clickLayout.setVisibility(View.GONE);
                }else{
                    clickLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();

    }
}