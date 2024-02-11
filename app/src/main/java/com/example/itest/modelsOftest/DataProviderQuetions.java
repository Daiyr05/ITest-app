package com.example.itest.modelsOftest;

import androidx.annotation.NonNull;

import com.example.itest.interfaces.OnDataQuetionRerivered;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataProviderQuetions {
    private ArrayList<Quetion> QUETIONS;
    private FirebaseDatabase db;
    private DatabaseReference quetions;
    private static DataProviderQuetions dataProviderQuetions= new DataProviderQuetions();
    private DataProviderQuetions() {
        this.QUETIONS = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        quetions = db.getReference().child("Quetions");
    }

    //getters
    public ArrayList<Quetion> getQUIETIONS() {
        return QUETIONS;
    }


    //
    public static DataProviderQuetions getInstance(){
        return dataProviderQuetions;
    }



    //CRUD
    public void add(Quetion queiton){
        DatabaseReference push = quetions.push();
        queiton.setKey(push.getKey());
        push.setValue(queiton);
    }

    public void getQuetions(OnDataQuetionRerivered listener){
        Query query = quetions;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QUETIONS.clear();
                for (DataSnapshot single : snapshot.getChildren()){
                    Quetion quetion = single.getValue(Quetion.class);
                    QUETIONS.add(quetion);
                }
                listener.onDataQuetionRetrivered(QUETIONS);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void remove(String testKey){
        quetions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()){
                    Quetion q = single.getValue(Quetion.class);
                    if(q.getTest_key().equals(testKey)){
                        quetions.child(q.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void update(Quetion q){
        quetions.child(q.getKey()).setValue(q);
    }
}
