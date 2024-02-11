package com.example.itest.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itest.R;
import com.example.itest.modelUser.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ForgotPasw extends AppCompatActivity {
    private EditText txtEmail;
    private Button btnSend;
    private FirebaseAuth auth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private ArrayList<User> USERSLIST;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pasw);
        txtEmail = findViewById(R.id.emailForgotPaswd);
        btnSend = findViewById(R.id.btnSendForgotPaswd);
        USERSLIST = new ArrayList<>();
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference().child("Users");
        Snackbar.make(findViewById(android.R.id.content),"Отправте почту если забыли пароль",Snackbar.LENGTH_LONG).show();

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot single : snapshot.getChildren()){
                    User singleUser = single.getValue(User.class);
                    USERSLIST.add(singleUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //on click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                boolean bl = true;
                if(email == null || email.isEmpty()){
                    txtEmail.setError("введите email");
                    bl = false;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()){
                    txtEmail.setError("введите правильный email");
                    bl = false;
                }
                boolean isUserExit = false;
                for (User u : USERSLIST){
                    if(u.getEmail().equals(email)){
                        isUserExit = true;
                    }
                }

                if (bl && isUserExit){
                    auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Проверьте email",Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(getApplicationContext(),"Что то пошло не так",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else {
                    txtEmail.setError("Аккаунт не найден");
                }
            }
        });
    }
}