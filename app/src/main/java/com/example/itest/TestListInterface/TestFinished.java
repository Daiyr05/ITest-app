package com.example.itest.TestListInterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.itest.R;

public class TestFinished extends AppCompatActivity {
    private TextView lblTotalRes, lblUserRes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_finished);
        lblTotalRes = findViewById(R.id.lblTotalRes);
        lblUserRes = findViewById(R.id.lblUserRes);

        Intent intent = getIntent();
        int totalCorrectAnswers = intent.getExtras().getInt("totalCorrectAnswers");
        int userCorrectAnswers = intent.getExtras().getInt("userCorrectAnswers");

        lblTotalRes.setText("total points "+totalCorrectAnswers);
        lblUserRes.setText("правильные: "+userCorrectAnswers);
    }
}