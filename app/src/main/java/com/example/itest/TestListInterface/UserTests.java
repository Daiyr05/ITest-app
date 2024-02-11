package com.example.itest.TestListInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itest.R;
import com.example.itest.interfaces.OnDataRetrivered;
import com.example.itest.modelUser.User;
import com.example.itest.modelsOftest.DataProviderAnswer;
import com.example.itest.modelsOftest.DataProviderQuetions;
import com.example.itest.modelsOftest.DataProviderTest;
import com.example.itest.modelsOftest.Quetion;
import com.example.itest.modelsOftest.Test;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserTests extends Fragment {
    private ImageButton btnAddTest;
    private final int REQUEST_ADD_TEST_CODE = 777;
    private final int REQUEST_PASS_TEST_CODE = 155;
    private ArrayList<Test> TESTS;
    private DataProviderTest testProvider;
    private DataProviderAnswer answerProvider;
    private DataProviderQuetions queitonProvider;
    private TestAdapter adapter;
    private ListView testList;
    private FirebaseUser CurUser;
    private  DatabaseReference CurUserRef;
    private  String CurUserId;
    private FirebaseDatabase db;
    private  DatabaseReference quetions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.usertests, container, false);
        db = FirebaseDatabase.getInstance();
        quetions = db.getReference().child("Quetions");
        btnAddTest = view.findViewById(R.id.btnAddTest);
        TESTS = new ArrayList<>();
        testProvider = DataProviderTest.getInstance();
        queitonProvider = DataProviderQuetions.getInstance();
        answerProvider = DataProviderAnswer.getInstance();
        testList = view.findViewById(R.id.testList);
        CurUser = FirebaseAuth.getInstance().getCurrentUser();
        CurUserRef = FirebaseDatabase.getInstance().getReference("Users");
        CurUserId = CurUser.getUid();
        testProvider.getTests(new OnDataRetrivered() {
            @Override
            public void onDataRetrivered(ArrayList<Test> TESTSARR) {
                TESTS.clear();
                TESTS.addAll(TESTSARR);
                adapter.notifyDataSetChanged();
            }
        },CurUserId);

        //when click add btn
        btnAddTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AddTestActivity.class);
                startActivityForResult(intent,REQUEST_ADD_TEST_CODE);
            }
        });



        adapter = new TestAdapter(getContext(),R.layout.test_item_style,TESTS);
        adapter.notifyDataSetChanged();
        testList.setAdapter(adapter);



        //finding current user

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //onclick
        testList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PassTest.class);
                Test t = TESTS.get(position);
                intent.putExtra("TestId",t.getKey());
                intent.putExtra("TestSub",t.getSubject());
                intent.putExtra("TestTheme",t.getTheme());
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivityForResult(intent,REQUEST_PASS_TEST_CODE);

            }
        });

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Выберите операцию");

        //on long item click
        testList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //when click edit
                dialogBuilder.setPositiveButton("Изменить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getContext(), EditTest.class);
                        Test t = TESTS.get(position);
                        intent.putExtra("TestId",t.getKey());
                        startActivity(intent);
                    }
                });

                //when click delete
                dialogBuilder.setNeutralButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Test t = TESTS.get(position);
                        testProvider.remove(t);
                        quetions.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot single : snapshot.getChildren()){
                                    Quetion q = single.getValue(Quetion.class);
                                    if (q.getTest_key().equals(t.getKey())){
                                        queitonProvider.remove(t.getKey());
                                        answerProvider.remove(q.getKey());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                AlertDialog dialog = dialogBuilder.create();
                dialog.show();
                return true;
            }
        });
    }


}
