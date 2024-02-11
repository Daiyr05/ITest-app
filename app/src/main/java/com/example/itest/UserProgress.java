package com.example.itest;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;

import com.example.itest.Authentication.LogIn;
import com.example.itest.TestListInterface.TestAdapter;
import com.example.itest.interfaces.OnDataRetrivered;
import com.example.itest.modelUser.User;
import com.example.itest.modelsOftest.DataProviderTest;
import com.example.itest.modelsOftest.Test;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProgress extends Fragment {

    private PieChart pieChart;
    private int totalCountOfTests;
    private int counterOfUserCompiletedTests;
    private DataProviderTest dataProviderTest;
    private FirebaseUser CurUser;
    private  DatabaseReference CurUserRef;
    private  String CurUserId;
    private ArrayList<Test> TESTLIST;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.progress, container, false);
        CurUser = FirebaseAuth.getInstance().getCurrentUser();
        CurUserRef = FirebaseDatabase.getInstance().getReference("Users");
        CurUserId = CurUser.getUid();
        pieChart = v.findViewById(R.id.testStatistic);
        dataProviderTest = DataProviderTest.getInstance();
        TESTLIST = new ArrayList<>();
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dataProviderTest.getTests(new OnDataRetrivered() {
            @Override
            public void onDataRetrivered(ArrayList<Test> TESTSARR) {
                TESTLIST.clear();
                TESTLIST.addAll(TESTSARR);
                for (Test t : TESTLIST){
                    if(t.isCompleted()){
                        counterOfUserCompiletedTests+=1;
                    }
                }
                totalCountOfTests = TESTLIST.size();
                setupPieChart();
                loadPieChart(counterOfUserCompiletedTests,totalCountOfTests);
            }
        },CurUserId);



    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Ваш прогресс");
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5.0f,5.0f,5.0f,15.0f);
        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setTextSize(15);
        l.setEnabled(true);

    }

    private void loadPieChart(int single,int total){
        ArrayList<PieEntry> entries = new ArrayList<>();
        float userCompletePersent = (float)((single*1.0)/(total*1.0));
        float remainTestPersent = (float)(1.0-userCompletePersent);
        entries.add(new PieEntry(userCompletePersent, "Выполненные тесты"));
        entries.add(new PieEntry(remainTestPersent, "Оставшиеся тесты"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }
}
