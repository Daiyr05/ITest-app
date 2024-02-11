package com.example.itest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.itest.Authentication.LogIn;
import com.example.itest.modelUser.User;
import com.example.itest.modelsOftest.DataProviderAnswer;
import com.example.itest.modelsOftest.DataProviderQuetions;
import com.example.itest.modelsOftest.Quetion;
import com.example.itest.modelsOftest.Test;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserGoal extends Fragment {
    private Button signOut;
    private Button btnDeleteUser;
    private FirebaseUser CurUser;
    private DatabaseReference CurUserRef;
    private  String CurUserId;
    private String userNickName;
    private String userEmail;
    private TextView lblNickName;
    private TextView lblEmail;
    private  FirebaseDatabase db;
    private DatabaseReference tests;
    private DatabaseReference queitons;
    private DataProviderAnswer dataProviderAnswer;
    private DataProviderQuetions dataProviderQuetions;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.goal, container, false);
        dataProviderAnswer = DataProviderAnswer.getInstance();
        dataProviderQuetions = DataProviderQuetions.getInstance();
        db = FirebaseDatabase.getInstance();
        tests = db.getReference().child("Tests");
        queitons = db.getReference().child("Quetions");
        lblNickName = v.findViewById(R.id.UserNickName);
        lblEmail = v.findViewById(R.id.ValueOfEmail);
        signOut = v.findViewById(R.id.btnSignOut);
        btnDeleteUser = v.findViewById(R.id.btnDeleteUser);
        CurUser = FirebaseAuth.getInstance().getCurrentUser();
        CurUserRef = FirebaseDatabase.getInstance().getReference("Users");
        CurUserId = CurUser.getUid();
        CurUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()){
                    User u = single.getValue(User.class);
                    if(u.getKey().equals(CurUserId)){
                        userNickName = u.getNickName();
                        lblNickName.setText(userNickName);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userEmail = CurUser.getEmail().trim();
        lblEmail.setText("email: "+userEmail);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //when click btn sign out
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Вы уверенный?");
                builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        getActivity().finish();
                        Intent intent = new Intent(getContext(), LogIn.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        startActivity(intent);
                    }
                });
                builder.setNeutralButton("Отмена",null);
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        //when click btn delete User
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Вы уверенный что хотите удалить аккаунт?");
                builder.setNeutralButton("Отмена",null);
                builder.setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    //remove user
                        CurUserRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot single : snapshot.getChildren()){
                                    User u = single.getValue(User.class);
                                    if(u.getKey().equals(CurUserId)){
                                        CurUserRef.child(CurUserId).removeValue();
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    //remove tests quetions and answers
                        tests.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot single : snapshot.getChildren()){
                                    Test t = single.getValue(Test.class);
                                    if(t.getUserKey().equals(CurUserId)){
                                        tests.child(t.getKey()).removeValue();
                                        String testKey = t.getKey();
                                        queitons.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot singleQuetion : snapshot.getChildren()){
                                                    Quetion q = singleQuetion.getValue(Quetion.class);
                                                    if(q.getTest_key().equals(testKey)){
                                                        dataProviderQuetions.remove(testKey);
                                                        dataProviderAnswer.remove(q.getKey());
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        //remove user from firebase authentication
                        CurUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(),"User succesfully deleted",Toast.LENGTH_LONG).show();
                                    getActivity().finish();
                                    Intent intent = new Intent(getContext(), LogIn.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    startActivity(intent);
                                }else {
                                    Toast.makeText(getContext(),task.getException()+"",Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}
