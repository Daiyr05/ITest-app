package com.example.itest.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itest.MainUserInterface;
import com.example.itest.R;
import com.example.itest.alertDialog.LoadingDialog;
import com.example.itest.modelUser.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthWebException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LogIn extends AppCompatActivity {
    private EditText txtLogInEmail;
    private EditText txtLogInPassword;
    private Button LogInBtn;
    private TextView lblForSignUp;
    private TextView lblForgotPassword;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference users;
    private ArrayList<User> USERSLISTS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        txtLogInEmail = findViewById(R.id.LogInEmail);
        txtLogInPassword = findViewById(R.id.LogInPassword);
        LogInBtn = findViewById(R.id.LogInBtn);
        lblForSignUp = findViewById(R.id.lblforSignUp);
        mAuth = FirebaseAuth.getInstance();
        lblForgotPassword = findViewById(R.id.lblForgotPassword);
        USERSLISTS = new ArrayList<>();
        db = FirebaseDatabase.getInstance();
        users = db.getReference().child("Users");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot singleUser : snapshot.getChildren()){
                    User User = singleUser.getValue(User.class);
                    USERSLISTS.add(User);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //onclick

        //Forgot Password
        lblForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),ForgotPasw.class));
            }
        });
        //register
        lblForSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
            }
        });



        //logIn
        LogInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtLogInEmail.getText().toString().trim();
                String password = txtLogInPassword.getText().toString().trim();
                boolean bl = true;

                if(email == null || email.isEmpty()){
                    txtLogInEmail.setError("введите email");
                    bl = false;
                }

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()){
                    txtLogInEmail.setError("введите правильный email");
                    bl = false;
                }

                if(password == null || password.isEmpty()){
                    txtLogInPassword.setError("введите пароль");
                    bl = false;
                }

                if(password.length()<6 && !password.isEmpty()){
                    txtLogInPassword.setError("не правильный пароль");
                    bl = false;
                }

                boolean blEmail = false;
                for (User u : USERSLISTS){
                    if(u.getEmail().equals(email)){
                        blEmail = true;
                    }
                }
                if(blEmail == false){
                    txtLogInEmail.setError(" email не найден");
                }

                if(bl && blEmail){
                    LoadingDialog loadingDialog = new LoadingDialog(LogIn.this);
                    loadingDialog.setCancelable(false);
                    loadingDialog.startLoadingDiaolog();
                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>(){
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                loadingDialog.dismissDiaolog();
                                try {
                                    throw task.getException();
                                }catch (FirebaseAuthInvalidCredentialsException e){
                                    txtLogInPassword.setError("не правильный пароль");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Intent intent = new Intent(getApplicationContext(), MainUserInterface.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                                finish();
                            }
                            loadingDialog.dismissDiaolog();

                        }
                    });
                }
            }
        });
    }

    public void onStart(){
        super.onStart();
        FirebaseUser cUser = mAuth.getCurrentUser();
        if(cUser != null){
            Intent intent = new Intent(getApplicationContext(), MainUserInterface.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
            Toast.makeText(getApplicationContext(),cUser.getEmail()+"",Toast.LENGTH_LONG).show();
        }else{

        }
    }
}