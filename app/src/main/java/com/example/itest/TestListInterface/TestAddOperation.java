package com.example.itest.TestListInterface;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.itest.R;
import com.example.itest.modelUser.User;
import com.example.itest.modelsOftest.Answer;
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

public class TestAddOperation {
    private static  int counterOfTests = 0;
    private static int counterOftxts = 0;
    private static FirebaseUser CurUser;
    private static DatabaseReference CurUserRef;
    private static String CurUserId;
    private static String UserKeyForTest = "";

    public static int getCounterOfTests() {
        return counterOfTests;
    }

    public static void setCounterOfTests(int counterOfTests) {
        TestAddOperation.counterOfTests = counterOfTests;
    }

    //Add
    public static void  add(Activity activity, ImageButton btn){
        ScrollView scroll = activity.findViewById(R.id.scrollOfAddTest);
        LinearLayout layoutlList = activity.findViewById(R.id.layoutList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView AmountOfTest = null;
                counterOfTests+=1;
                View test_parts = activity.getLayoutInflater().inflate(R.layout.row_add_test_parts,null,false);
                ImageButton btnDelete = test_parts.findViewById(R.id.btnDelete);
                btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        layoutlList.removeView(test_parts);
                        counterOftxts -= 1;
                    }
                });

                layoutlList.addView(test_parts);
                counterOftxts += 1;
                scroll.fullScroll(View.FOCUS_DOWN);
                for (int i = 0; i<layoutlList.getChildCount(); i++){
                    View test_item = layoutlList.getChildAt(i);
                    AmountOfTest = test_item.findViewById(R.id.lblcounterOfTests);
                }

                AmountOfTest.setText(counterOfTests+"");
            }
        });
    }

    //sendToDB;
    public static void saveToDB(Activity activity,Button btn){
        CurUser = FirebaseAuth.getInstance().getCurrentUser();
        CurUserRef = FirebaseDatabase.getInstance().getReference("Users");
        CurUserId = CurUser.getUid();
        CurUserRef.child(CurUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User dataUser = snapshot.getValue(User.class);
                if(dataUser!=null){
                    UserKeyForTest = dataUser.getKey();

                }else{
                    Toast.makeText(activity.getApplicationContext(),"null",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DataProviderTest testProvider = DataProviderTest.getInstance();
        DataProviderAnswer answerProvider = DataProviderAnswer.getInstance();
        DataProviderQuetions quetionProvider = DataProviderQuetions.getInstance();
        ScrollView scroll = activity.findViewById(R.id.scrollOfAddTest);
        LinearLayout layoutlList = activity.findViewById(R.id.layoutList);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtSubject = activity.findViewById(R.id.txtSubject);
                EditText txtTheme = activity.findViewById(R.id.txtTheme);
                String Subject = txtSubject.getText().toString().trim();
                String Theme = txtTheme.getText().toString().trim();
                boolean layoutIsEmpty = true;
                if(layoutlList.getChildCount()>3){
                    layoutIsEmpty = false;
                }else{
                    Snackbar.make(v,"Добавьте вопросы",Snackbar.LENGTH_LONG).show();
                }
                boolean headerIsFull = true;
                if(Subject == null || Subject.isEmpty()){
                    headerIsFull = false;
                    txtSubject.setError("Введите название урока");
                }

                if(Theme == null || Theme.isEmpty()){
                    headerIsFull = false;
                    txtTheme.setError("Введите тему урока");
                }
                int counterOfGroupCorrectAnswer = 0;
                int counterOfUserPrinted = 0;
                int LIMITOFCORRECTANSWERS = 2;
                for (int i = 3; i<layoutlList.getChildCount(); i++){
                    int counterOfCorrectAnswers = 0;
                    boolean bl = true;
                    boolean lbCheckBox = false;
                     View test_item =  layoutlList.getChildAt(i);
                    EditText txtQuetion = test_item.findViewById(R.id.txtQuetion);
                    EditText txtAnswerA = test_item.findViewById(R.id.txtAnswerA);
                    EditText txtAnswerB = test_item.findViewById(R.id.txtAnswerB);
                    EditText txtAnswerC = test_item.findViewById(R.id.txtAnswerC);
                    EditText txtAnswerD = test_item.findViewById(R.id.txtAnswerD);
                    CheckBox checkBox_A = test_item.findViewById(R.id.checkBox_A);
                    CheckBox checkBox_B = test_item.findViewById(R.id.checkBox_B);
                    CheckBox checkBox_C = test_item.findViewById(R.id.checkBox_C);
                    CheckBox checkBox_D = test_item.findViewById(R.id.checkBox_D);
                     if(txtQuetion.getText().toString().trim() == null || txtQuetion.getText().toString().isEmpty()){
                            txtQuetion.setError("Заполниту строку");
                            bl = false;
                        }
                    if(txtAnswerA.getText().toString().trim() == null || txtAnswerA.getText().toString().isEmpty()){
                        txtAnswerA.setError("Заполните строку A");
                        bl = false;
                    }

                    if(txtAnswerB.getText().toString().trim() == null || txtAnswerB.getText().toString().isEmpty()){
                        txtAnswerB.setError("Заполните строку В");
                        bl = false;
                    }

                    if(txtAnswerC.getText().toString().trim() == null || txtAnswerC.getText().toString().isEmpty()){
                        txtAnswerC.setError("Заполните строку C");
                        bl = false;
                    }

                    if(txtAnswerD.getText().toString().trim() == null || txtAnswerD.getText().toString().isEmpty()){
                        txtAnswerD.setError("Заполните строку D");
                        bl  = false;
                    }
                    if(bl){
                        counterOfUserPrinted +=1;
                    }
                    if(counterOfCorrectAnswers<LIMITOFCORRECTANSWERS){
                       if(checkBox_A.isChecked()){
                           counterOfCorrectAnswers+=1;
                           lbCheckBox = true;
                       }

                       if (checkBox_B.isChecked()){
                           counterOfCorrectAnswers+=1;
                           lbCheckBox = true;
                       }

                       if (checkBox_C.isChecked()){
                           counterOfCorrectAnswers+=1;
                           lbCheckBox = true;
                       }

                       if (checkBox_D.isChecked()){
                           counterOfCorrectAnswers+=1;
                           lbCheckBox = true;
                       }
                    }


                    if (counterOfCorrectAnswers>LIMITOFCORRECTANSWERS || counterOfCorrectAnswers<=0){
                        lbCheckBox = false;
                        txtQuetion.setError("Выберите максимум 2 правильных ответа");
                    }else{
                        txtQuetion.setError(null);
                    }

                    if (lbCheckBox){
                        counterOfGroupCorrectAnswer+=1;
                    }
                    if (lbCheckBox == true && bl == true){
                        bl = true;
                    }else{
                        bl = false;
                    }

                }

                if(counterOftxts == counterOfUserPrinted && counterOfGroupCorrectAnswer ==
                        counterOftxts && headerIsFull && counterOftxts !=0 && counterOfUserPrinted != 0 && counterOfGroupCorrectAnswer!=0 && !layoutIsEmpty){
                    Test test = new Test();
                    test.setSubject(Subject);
                    test.setTheme(Theme);
                    test.setUserKey(UserKeyForTest);
                    testProvider.add(test);
                    txtSubject.setText("");
                    txtTheme.setText("");
                    for (int i = 3; i<layoutlList.getChildCount(); i++){
                        View test_item =  layoutlList.getChildAt(i);
                        EditText txtQuetion = test_item.findViewById(R.id.txtQuetion);
                        EditText txtAnswerA = test_item.findViewById(R.id.txtAnswerA);
                        EditText txtAnswerB = test_item.findViewById(R.id.txtAnswerB);
                        EditText txtAnswerC = test_item.findViewById(R.id.txtAnswerC);
                        EditText txtAnswerD = test_item.findViewById(R.id.txtAnswerD);
                        CheckBox checkBox_A = test_item.findViewById(R.id.checkBox_A);
                        CheckBox checkBox_B = test_item.findViewById(R.id.checkBox_B);
                        CheckBox checkBox_C = test_item.findViewById(R.id.checkBox_C);
                        CheckBox checkBox_D = test_item.findViewById(R.id.checkBox_D);
                        String Quetion = txtQuetion.getText().toString().trim();
                        String answer_A = txtAnswerA.getText().toString().trim();
                        String answer_B = txtAnswerB.getText().toString().trim();
                        String answer_C = txtAnswerC.getText().toString().trim();
                        String answer_D = txtAnswerD.getText().toString().trim();
                        Quetion quetion = new Quetion(Quetion);
                        Answer A = new Answer(answer_A);
                        Answer B = new Answer(answer_B);
                        Answer C = new Answer(answer_C);
                        Answer D = new Answer(answer_D);
                        A.setCorrectAnswer(checkBox_A.isChecked());
                        B.setCorrectAnswer(checkBox_B.isChecked());
                        C.setCorrectAnswer(checkBox_C.isChecked());
                        D.setCorrectAnswer(checkBox_D.isChecked());
                        quetion.setTest_key(test.getKey());
                        quetionProvider.add(quetion);
                        A.setQuetion_key(quetion.getKey());
                        B.setQuetion_key(quetion.getKey());
                        C.setQuetion_key(quetion.getKey());
                        D.setQuetion_key(quetion.getKey());
                        answerProvider.add(A);
                        answerProvider.add(B);
                        answerProvider.add(C);
                        answerProvider.add(D);
                        txtQuetion.setText("");
                        txtAnswerA.setText("");
                        txtAnswerB.setText("");
                        txtAnswerC.setText("");
                        txtAnswerD.setText("");


                    }
                    counterOfTests = 0;
                    counterOftxts = 0;
                    activity.finish();
                }
                scroll.fullScroll(View.NOT_FOCUSABLE);
            }
        });
    }
}
