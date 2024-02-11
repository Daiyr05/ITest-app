package com.example.itest.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.itest.MainUserInterface;
import com.example.itest.R;
import com.example.itest.alertDialog.LoadingDialog;
import com.example.itest.modelUser.User;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

import static com.google.firebase.FirebaseError.ERROR_EMAIL_ALREADY_IN_USE;

public class SignUp extends AppCompatActivity {
    private EditText txtUserNickName;
    private EditText txtUserEmail;
    private EditText txtUserPassword;
    private Button btnSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        txtUserNickName = findViewById(R.id.signUpUserName);
        txtUserEmail = findViewById(R.id.signUpEmail);
        txtUserPassword = findViewById(R.id.signUpPassword);
        btnSignUp = findViewById(R.id.signUpbtn);
        mAuth = FirebaseAuth.getInstance();

        //onclick
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickName = txtUserNickName.getText().toString();
                String email = txtUserEmail.getText().toString().toString();
                String password = txtUserPassword.getText().toString();
                boolean bl = true;

                if(nickName == null || nickName.isEmpty()){
                    txtUserNickName.setError("введите nickName");
                    bl = false;
                }

                if (email == null || email.isEmpty()){
                    txtUserEmail.setError("введите email");
                    bl = false;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches() && !email.isEmpty()){
                    txtUserEmail.setError("введите правильный email");
                    bl = false;
                }

                if(password == null || password.isEmpty()){
                    txtUserPassword.setError("введите пароль");
                    bl = false;
                }
                if(password.length()<6 && !password.isEmpty()){
                    txtUserPassword.setError("пароль должен состоят из 6 или более символов");
                    bl = false;
                }

                if(bl){
                    LoadingDialog loadingDialog = new LoadingDialog(SignUp.this);
                    loadingDialog.setCancelable(false);
                    loadingDialog.startLoadingDiaolog();
                    mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                User user = new User(nickName,email);
                                DatabaseReference push = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                user.setKey(push.getKey());
                                push.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Snackbar.make(v,"Пользователь успешно зарегестрирован",Snackbar.LENGTH_LONG).show();
                                            Intent intent = new Intent(getApplicationContext(), MainUserInterface.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Snackbar.make(v,"Что то пошло не так",Snackbar.LENGTH_LONG).show();
                                        }
                                        loadingDialog.dismissDiaolog();
                                    }
                                });


                            }else{
                                loadingDialog.dismissDiaolog();
                                try {
                                    throw task.getException();
                                }catch(FirebaseAuthUserCollisionException e) {
                                    txtUserEmail.setError("Аккаунт уже сущетсвует");
                                   Toast.makeText(getApplicationContext(),"Аккаунт уже сущетсвует",Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    Snackbar.make(v,"Что-то пошло не так:"+task.getException(),Snackbar.LENGTH_LONG).show();
                                }

                            }
                        }
                    });






                }
            }
        });
    }



}