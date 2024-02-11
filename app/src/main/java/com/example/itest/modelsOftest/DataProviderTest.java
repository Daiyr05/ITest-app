package com.example.itest.modelsOftest;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.itest.TestListInterface.UserTests;
import com.example.itest.interfaces.OnDataRetrivered;
import com.example.itest.modelUser.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProviderTest {
    private ArrayList<Test> TESTS;
    private FirebaseDatabase db;
    private DatabaseReference tests;
    private static  DataProviderTest dataProviderTest = new DataProviderTest();
    private ArrayList<Test> finishArr;
    private DataProviderTest(){
        db = FirebaseDatabase.getInstance();
        tests = db.getReference().child("Tests");
        TESTS = new ArrayList<>();
        finishArr = new ArrayList();

    }

    public static DataProviderTest getInstance(){
        return dataProviderTest;
    }

    //CRUD
    public void add(Test test){
        DatabaseReference push = tests.push();
        test.setKey(push.getKey());
        push.setValue(test);
    }


    public void getTests(OnDataRetrivered listener,String CurUserKey){
        Query query = tests;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                TESTS.clear();
                for (DataSnapshot singleTest : snapshot.getChildren()){
                    Test t = singleTest.getValue(Test.class);
                        TESTS.add(t);

                }
                finishArr.clear();
                for (int i = 0; i< TESTS.size(); i++){
                    if(TESTS.get(i).getUserKey().equals(CurUserKey)){
                        finishArr.add(TESTS.get(i));
                    }
                }
                listener.onDataRetrivered(finishArr);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void remove(Test t){
        tests.child(t.getKey()).removeValue();
    }
}
