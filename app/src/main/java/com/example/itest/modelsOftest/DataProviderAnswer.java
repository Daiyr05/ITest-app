package com.example.itest.modelsOftest;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.itest.interfaces.OnDataAnswerRetrivered;
import com.example.itest.interfaces.OnDataRetrivered;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProviderAnswer {
    private ArrayList<Answer> ANSWERS;

    private FirebaseDatabase db;
    private DatabaseReference answers;
    private static DataProviderAnswer dataProviderAnswer = new DataProviderAnswer();
    //constructor
        private DataProviderAnswer() {
            this.ANSWERS = new ArrayList<>();
            db = FirebaseDatabase.getInstance();
            answers = db.getReference().child("Answers");
        }

    //getters
        public ArrayList<Answer> getANSWERS() {
            return ANSWERS;
        }


        public static DataProviderAnswer getInstance(){
            return dataProviderAnswer;
        }
    //CRUD
        public void add(Answer answer){
            DatabaseReference push = answers.push();
            answer.setKey(push.getKey());
            push.setValue(answer);
        }

        public void getAnswers(OnDataAnswerRetrivered listener){
            Query query = answers;
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ANSWERS.clear();
                    for (DataSnapshot single : snapshot.getChildren()){
                        Answer dataAnswer = single.getValue(Answer.class);
                        ANSWERS.add(dataAnswer);
                    }
                    listener.onDataAnswerRetrivered(ANSWERS);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void remove(String queitonKey){
            answers.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot single : snapshot.getChildren()){
                        Answer an = single.getValue(Answer.class);
                        if(an.getQuetion_key().equals(queitonKey)){
                            answers.child(an.getKey()).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void update(Answer a){
            answers.child(a.getKey()).setValue(a);
        }
}
