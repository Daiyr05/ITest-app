  package com.example.itest.TestListInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.provider.FontRequest;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itest.R;
import com.example.itest.interfaces.OnDataAnswerRetrivered;
import com.example.itest.interfaces.OnDataQuetionRerivered;
import com.example.itest.modelsOftest.Answer;
import com.example.itest.modelsOftest.DataProviderAnswer;
import com.example.itest.modelsOftest.DataProviderQuetions;
import com.example.itest.modelsOftest.Quetion;
import com.example.itest.modelsOftest.Test;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PassTest extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<Quetion> QUETIONLIST;
    private ArrayList<Answer> ANSWERLIST;
    private int totalCorrectAnswers, userCorrectAnswer;
    private FirebaseDatabase db;
    private DatabaseReference answers;
    private DatabaseReference quetions;
    private DatabaseReference tests;
    private ArrayList<Quetion> QUETIONARR;
    private ArrayList<Answer> ANSWERARR;
    private AppCompatButton option1, option2, option3, option4;
    private AppCompatButton btnNext;
    private int quetion_counter = 0;
    private ArrayList<Answer> RESANSWERLIST;
    TextView lblQuetion, lblTestQuetionCounter;
    private boolean isChosen = false;
    String test_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pass_test);
        Intent intent = getIntent();
        String test_subject = intent.getExtras().getString("TestSub");
        String test_theme = intent.getExtras().getString("TestTheme");
         test_key = intent.getExtras().getString("TestId");
        QUETIONARR = new ArrayList<>();
        ANSWERARR = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        quetions = db.getReference().child("Quetions");
        answers = db.getReference().child("Answers");
        tests = db.getReference().child("Tests");
        QUETIONLIST = new ArrayList<>();
        ANSWERLIST = new ArrayList<>();
        RESANSWERLIST = new ArrayList<>();

        lblTestQuetionCounter = findViewById(R.id.lblTestQuetionCounter);
        lblQuetion = findViewById(R.id.test_lbl_quetion);
        option1 = findViewById(R.id.test_answerA);
        option2 = findViewById(R.id.test_answerB);
        option3 = findViewById(R.id.test_answerC);
        option4 = findViewById(R.id.test_answerD);
        btnNext = findViewById(R.id.test_btnNext);


        toolbar = findViewById(R.id.customToolbarForPassTest);
        toolbar.setTitle(test_subject+":"+test_theme);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //get data from firebase
       quetions.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               QUETIONARR.clear();
               for (DataSnapshot single : snapshot.getChildren()) {
                   Quetion dataQuetion = single.getValue(Quetion.class);
                   QUETIONARR.add(dataQuetion);
               }
               QUETIONLIST.clear();
               for (Quetion q :QUETIONARR){
                   if (q.getTest_key().equals(test_key)){
                       QUETIONLIST.add(q);
                   }
               }

               //set first quetions

               lblQuetion.setText(QUETIONLIST.get(quetion_counter).getQuetion());
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });



        answers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ANSWERARR.clear();
                for (DataSnapshot single : snapshot.getChildren()) {
                    Answer a = single.getValue(Answer.class);
                    ANSWERARR.add(a);
                }

                for (int i = 0; i <QUETIONLIST.size(); i++){
                    for (int j = 0; j <ANSWERARR.size(); j++){
                        if (QUETIONLIST.get(i).getKey().equals(ANSWERARR.get(j).getQuetion_key())){
                            ANSWERLIST.add(ANSWERARR.get(j));
                        }
                    }
                }
                for (int i = 0 ; i  < ANSWERLIST.size(); i++){
                    if (ANSWERLIST.get(i).getQuetion_key().equals(QUETIONLIST.get(quetion_counter).getKey())){
                        RESANSWERLIST.add(ANSWERLIST.get(i));
                        if (ANSWERLIST.get(i).isCorrectAnswer()){
                            totalCorrectAnswers+=1;
                        }
                    }
                }
                //set first options
                lblTestQuetionCounter.setText((quetion_counter+1)+"/"+QUETIONLIST.size());
                option1.setText(RESANSWERLIST.get(0).getAnswer());
                option2.setText(RESANSWERLIST.get(1).getAnswer());
                option3.setText(RESANSWERLIST.get(2).getAnswer());
                option4.setText(RESANSWERLIST.get(3).getAnswer());
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //when chose options
        option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChosen = true;
                if (isAnswerOfOptionTrue(option1.getText().toString().trim())){
                    option1.setBackground(getResources().getDrawable(R.drawable.background_green_btn_to_answer));
                    userCorrectAnswer+=1;
                }else{
                    option1.setBackground(getResources().getDrawable(R.drawable.background_red_btn));
                }
            }
        });

        option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChosen = true;
                if (isAnswerOfOptionTrue(option2.getText().toString().trim())){
                    option2.setBackground(getResources().getDrawable(R.drawable.background_green_btn_to_answer));
                    userCorrectAnswer+=1;
                }else{
                    option2.setBackground(getResources().getDrawable(R.drawable.background_red_btn));
                }
            }
        });

        option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChosen = true;
                if (isAnswerOfOptionTrue(option3.getText().toString().trim())){
                    option3.setBackground(getResources().getDrawable(R.drawable.background_green_btn_to_answer));
                    userCorrectAnswer+=1;
                }else{
                    option3.setBackground(getResources().getDrawable(R.drawable.background_red_btn));
                }
            }
        });

        option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChosen = true;
                if (isAnswerOfOptionTrue(option4.getText().toString().trim())){
                    option4.setBackground(getResources().getDrawable(R.drawable.background_green_btn_to_answer));
                    userCorrectAnswer+=1;
                }else{
                    option4.setBackground(getResources().getDrawable(R.drawable.background_red_btn));
                }
            }
        });


        //onclicklisteners

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (isChosen){
                   nextQuetion();
                   RESANSWERLIST.clear();
                   lblQuetion.setText(QUETIONLIST.get(quetion_counter).getQuetion());
                   for (int i = 0 ; i  < ANSWERLIST.size(); i++){
                       if (ANSWERLIST.get(i).getQuetion_key().equals(QUETIONLIST.get(quetion_counter).getKey())){
                           RESANSWERLIST.add(ANSWERLIST.get(i));
                       }
                   }
                   lblTestQuetionCounter.setText((quetion_counter+1)+"/"+QUETIONLIST.size());
                   //to basic situation
                   option1.setBackground(getResources().getDrawable(R.drawable.background_white_btn));
                   option2.setBackground(getResources().getDrawable(R.drawable.background_white_btn));
                   option3.setBackground(getResources().getDrawable(R.drawable.background_white_btn));
                   option4.setBackground(getResources().getDrawable(R.drawable.background_white_btn));

                   //next quetion and options
                   option1.setText(RESANSWERLIST.get(0).getAnswer());
                   option2.setText(RESANSWERLIST.get(1).getAnswer());
                   option3.setText(RESANSWERLIST.get(2).getAnswer());
                   option4.setText(RESANSWERLIST.get(3).getAnswer());
                   isChosen = false;
               }else{
                   Snackbar.make(v,"Выберите хоть ответ",Snackbar.LENGTH_SHORT).show();
               }
            }
        });
    }

    //get next quetion
    private void nextQuetion(){
        quetion_counter+=1;
//        if (quetion_counter == QUETIONLIST.size()){
//            for (Quetion q :QUETIONARR){
//                if (q.getTest_key().equals(test_key)){
//
//                }
//            }
            quetion_counter = 0;
            Intent intent = new Intent(getApplicationContext(),TestFinished.class);
            intent.putExtra("totalCorrectAnswers",totalCorrectAnswers);
            intent.putExtra("userCorrectAnswers",userCorrectAnswer);
            startActivity(intent);
        }
    }

    //getAnswerOfOption
    private boolean isAnswerOfOptionTrue(String option){
        for (int i = 0; i<RESANSWERLIST.size(); i++){
            if (RESANSWERLIST.get(i).getAnswer().equals(option)){
                return RESANSWERLIST.get(i).isCorrectAnswer();
            }
        }
        return false;
    }

}