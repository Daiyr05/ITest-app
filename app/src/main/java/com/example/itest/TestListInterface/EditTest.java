package com.example.itest.TestListInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itest.R;
import com.example.itest.interfaces.OnDataQuetionRerivered;
import com.example.itest.modelsOftest.Answer;
import com.example.itest.modelsOftest.DataProviderAnswer;
import com.example.itest.modelsOftest.DataProviderQuetions;
import com.example.itest.modelsOftest.Quetion;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EditTest extends AppCompatActivity {
    private ArrayList<Quetion> QUETIONLIST;
    private ArrayList<Answer> ANSWERLIST;
    private ArrayList<Quetion> QUETIONARR;
    private ArrayList<Answer> ANSWERARR;
    private LinearLayout layout;
    private int counterForLbl;
    private FirebaseDatabase db;
    private DatabaseReference quetions;
    private DatabaseReference answers;
    private Button btnEdit;
    private DataProviderQuetions dataProviderQuetions;
    private DataProviderAnswer dataProviderAnswer;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);
        dataProviderQuetions = DataProviderQuetions.getInstance();
        dataProviderAnswer = DataProviderAnswer.getInstance();
        Intent intent = getIntent();
        String test_key = (String)intent.getExtras().getString("TestId");
        QUETIONARR = new ArrayList<>();
        ANSWERARR = new ArrayList<>();
        QUETIONLIST = new ArrayList<>();
        ANSWERLIST = new ArrayList<>();
        layout = findViewById(R.id.layoutListOfEditTest);
        db = FirebaseDatabase.getInstance();
        quetions = db.getReference().child("Quetions");
        answers = db.getReference().child("Answers");
        btnEdit = findViewById(R.id.btnEdit);
        toolbar = findViewById(R.id.customToolbarForEditTest);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("");
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //get Quetions
        quetions.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                QUETIONARR.clear();
                for (DataSnapshot single : snapshot.getChildren()){
                    Quetion q = single.getValue(Quetion.class);
                    QUETIONARR.add(q);
                }
                //sort quetions
                for (int i = 0; i<QUETIONARR.size(); i++){
                    if(QUETIONARR.get(i).getTest_key().equals(test_key)){
                        QUETIONLIST.add(QUETIONARR.get(i));
                    }
                }

                //add views and set quetions
                for (int i = 0; i<QUETIONLIST.size(); i++) {
                    View test_part = getLayoutInflater().inflate(R.layout.row_add_test_parts, null, false);
                    layout.addView(test_part);
                    for (int j = 0; j < layout.getChildCount();j++){
                        View test_item = layout.getChildAt(i);
                        EditText lblQuetion = test_item.findViewById(R.id.txtQuetion);
                        lblQuetion.setText(QUETIONLIST.get(i).getQuetion());
                    }
                    ImageButton btnDelete = test_part.findViewById(R.id.btnDelete);
                    btnDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            layout.removeView(test_part);
                        }
                    });

                }

                for (int i = 0 ; i < layout.getChildCount(); i++){
                    counterForLbl = i+1;
                    View test_item = layout.getChildAt(i);
                    TextView lblCounterOfTest = test_item.findViewById(R.id.lblcounterOfTests);
                    lblCounterOfTest.setText(counterForLbl+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        answers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ANSWERARR.clear();
                for (DataSnapshot single : snapshot.getChildren()){
                    Answer a = single.getValue(Answer.class);
                    ANSWERARR.add(a);
                }

                for (int i = 0; i<QUETIONLIST.size(); i++){
                            for (int j = 0; j<ANSWERARR.size(); j++){
                                if(ANSWERARR.get(j).getQuetion_key().equals(QUETIONLIST.get(i).getKey())){
                                    ANSWERLIST.add(ANSWERARR.get(j));
                                }
                            }
                        }

                ArrayList<ArrayList<Answer>> SHOWANSWERLIST = new ArrayList<>();
                for (int k = 0; k<layout.getChildCount(); k++){
                            View test_item = layout.getChildAt(k);
                            EditText txtAnswerA = test_item.findViewById(R.id.txtAnswerA);
                            EditText txtAnswerB = test_item.findViewById(R.id.txtAnswerB);
                            EditText txtAnswerC = test_item.findViewById(R.id.txtAnswerC);
                            EditText txtAnswerD = test_item.findViewById(R.id.txtAnswerD);
                            for (int i = 0; i<QUETIONLIST.size(); i++){
                                SHOWANSWERLIST.add(new ArrayList<Answer>());
                                for (int j = 0; j<ANSWERLIST.size(); j++){
                                    if(ANSWERLIST.get(j).getQuetion_key().equals(QUETIONLIST.get(i).getKey())){
                                        SHOWANSWERLIST.get(i).add(ANSWERLIST.get(j));
                                    }
                                }

                            }

                            if(SHOWANSWERLIST.get(k).size() > 0){
                                txtAnswerA.setText(SHOWANSWERLIST.get(k).get(0).getAnswer()+"");
                                txtAnswerB.setText(SHOWANSWERLIST.get(k).get(1).getAnswer()+"");
                                txtAnswerC.setText(SHOWANSWERLIST.get(k).get(2).getAnswer()+"");
                                txtAnswerD.setText(SHOWANSWERLIST.get(k).get(3).getAnswer()+"");
                            }

                        }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //set onclick listeners
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counterOfSendTestParts = 0;
                ArrayList<Quetion> QUETIONLISTTOEDIT = new ArrayList<>();
                for (int i = 0; i<layout.getChildCount(); i++){
                    View test_item = layout.getChildAt(i);
                    EditText txtQuetion  = test_item.findViewById(R.id.txtQuetion);
                    EditText txtAnswerA = test_item.findViewById(R.id.txtAnswerA);
                    EditText txtAnswerB = test_item.findViewById(R.id.txtAnswerB);
                    EditText txtAnswerC = test_item.findViewById(R.id.txtAnswerC);
                    EditText txtAnswerD = test_item.findViewById(R.id.txtAnswerD);
                    CheckBox A = test_item.findViewById(R.id.checkBox_A);
                    CheckBox B = test_item.findViewById(R.id.checkBox_B);
                    CheckBox C = test_item.findViewById(R.id.checkBox_C);
                    CheckBox D = test_item.findViewById(R.id.checkBox_D);
                    boolean bl = true;
                    boolean isCheckedCorrect = true;
                    String quetionsStr  = txtQuetion.getText().toString().trim();
                    String answerA_Str  = txtAnswerA.getText().toString().trim();
                    String answerB_Str = txtAnswerB.getText().toString().trim();
                    String answerC_Str = txtAnswerC.getText().toString().trim();
                    String answerD_Str = txtAnswerD.getText().toString().trim();
                    int counterOfChecked = 0;
                    if(A.isChecked()){
                        counterOfChecked+=1;
                    }

                    if(B.isChecked()){
                        counterOfChecked+=1;
                    }

                    if(C.isChecked()){
                        counterOfChecked+=1;
                    }
                    if (D.isChecked()){
                        counterOfChecked+=1;
                    }
                    if(counterOfChecked>2 || counterOfChecked==0){
                        txtQuetion.setError("Вы можете выбрать только 2 правильных ответа");
                        isCheckedCorrect = false;
                    }else{
                        txtQuetion.setError(null);
                    }
                    if(quetionsStr.equals(null) || quetionsStr.isEmpty()){
                        txtQuetion.setError("Заполните строку");
                        bl = false;
                    }

                    if(answerA_Str.equals(null) || answerA_Str.isEmpty()){
                        txtAnswerA.setError("Заполните строку А");
                        bl = false;
                    }

                    if(answerB_Str.equals(null) || answerB_Str.isEmpty()){
                        txtAnswerB.setError("Заполните строку В");
                        bl = false;
                    }

                    if(answerC_Str.equals(null) || answerC_Str.isEmpty()){
                        txtAnswerC.setError("Заполните строку С");
                        bl = false;
                    }

                    if(answerD_Str.equals(null) || answerD_Str.isEmpty()){
                        txtAnswerD.setError("Заполните строку D");
                        bl = false;
                    }

                    if(bl && isCheckedCorrect){
                        counterOfSendTestParts+=1;
                    }
                }
                if(counterOfSendTestParts == layout.getChildCount()){
                    for (int i = 0; i<layout.getChildCount(); i++) {
                        View test_item = layout.getChildAt(i);
                        EditText txtQuetion = test_item.findViewById(R.id.txtQuetion);
                        EditText txtAnswerA = test_item.findViewById(R.id.txtAnswerA);
                        EditText txtAnswerB = test_item.findViewById(R.id.txtAnswerB);
                        EditText txtAnswerC = test_item.findViewById(R.id.txtAnswerC);
                        EditText txtAnswerD = test_item.findViewById(R.id.txtAnswerD);
                        CheckBox A = test_item.findViewById(R.id.checkBox_A);
                        CheckBox B = test_item.findViewById(R.id.checkBox_B);
                        CheckBox C = test_item.findViewById(R.id.checkBox_C);
                        CheckBox D = test_item.findViewById(R.id.checkBox_D);
                        boolean bl = true;
                        boolean isCheckedCorrect = true;
                        String quetionsStr = txtQuetion.getText().toString().trim();
                        String answerA_Str = txtAnswerA.getText().toString().trim();
                        String answerB_Str = txtAnswerB.getText().toString().trim();
                        String answerC_Str = txtAnswerC.getText().toString().trim();
                        String answerD_Str = txtAnswerD.getText().toString().trim();
                        Quetion q = new Quetion();
                        q.setTest_key(QUETIONLIST.get(i).getTest_key());
                        q.setKey(QUETIONLIST.get(i).getKey());
                        q.setQuetion(quetionsStr);

                        ArrayList<Answer> ANSWERLISTTOEDIT = new ArrayList<>();
                        for (Answer a : ANSWERLIST){
                            if(a.getQuetion_key().equals(QUETIONLIST.get(i).getKey())){
                                ANSWERLISTTOEDIT.add(a);
                            }
                        }
                        ANSWERLISTTOEDIT.get(0).setAnswer(answerA_Str);
                        ANSWERLISTTOEDIT.get(1).setAnswer(answerB_Str);
                        ANSWERLISTTOEDIT.get(2).setAnswer(answerC_Str);
                        ANSWERLISTTOEDIT.get(3).setAnswer(answerD_Str);
                        if(A.isChecked()){
                            ANSWERLISTTOEDIT.get(0).setCorrectAnswer(true);
                        }else{
                            ANSWERLISTTOEDIT.get(0).setCorrectAnswer(false);
                        }

                        if(B.isChecked()){
                            ANSWERLISTTOEDIT.get(1).setCorrectAnswer(true);
                        }else{
                            ANSWERLISTTOEDIT.get(1).setCorrectAnswer(false);
                        }
                        if(C.isChecked()){
                            ANSWERLISTTOEDIT.get(2).setCorrectAnswer(true);
                        }else{
                            ANSWERLISTTOEDIT.get(2).setCorrectAnswer(false);
                        }

                        if(D.isChecked()){
                            ANSWERLISTTOEDIT.get(3).setCorrectAnswer(true);
                        }else{
                            ANSWERLISTTOEDIT.get(3).setCorrectAnswer(false);
                        }
                        for (Answer a : ANSWERLIST){
                            dataProviderAnswer.update(a);
                        }
                        QUETIONLISTTOEDIT.add(q);
                    }

                 for (Quetion q : QUETIONLISTTOEDIT){
                     dataProviderQuetions.update(q);
                 }


                 for(int i = counterOfSendTestParts; i <QUETIONLIST.size(); i++){
                     quetions.child(QUETIONLIST.get(i).getKey()).removeValue();
                     for(int j = 0; j<ANSWERLIST.size(); j++){
                            if(ANSWERLIST.get(j).getQuetion_key().equals(QUETIONLIST.get(i).getKey())){
                                answers.child(ANSWERLIST.get(j).getKey()).removeValue();
                            }
                     }
                 }
                    finish();
                }

            }
        });
    }
}