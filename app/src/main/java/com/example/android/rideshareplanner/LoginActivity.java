package com.example.android.rideshareplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;



public class LoginActivity extends AppCompatActivity {

    private EditText EmailLogin;
    private EditText Password;
    private Button btnSignup;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth   = FirebaseAuth.getInstance();

        firebaseAuthListener =  new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user!=null){
                    Intent intent = new Intent(LoginActivity.this, Main2Activity.class);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        EmailLogin  = (EditText)findViewById(R.id.email);
        EmailLogin.setText("ehtishammalik3484@gmail.com");


        Password = (EditText)findViewById(R.id.password);
        Password.setText("1234567");

        btnLogin = (Button) findViewById(R.id.email_sign_in_button);
        btnSignup = (Button) findViewById(R.id.email_sign_up_button);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSignup_Click(v);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailLogin.getText().toString().isEmpty() || Password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter the required information", Toast.LENGTH_SHORT).show();
                }
                else{
                    btnUserLogin_Click(v);
                }

            }
        });

    }

    public void btnSignup_Click(View v){

        Intent i = new Intent(LoginActivity.this,SignUp.class);
        startActivity(i);
    }

    public void btnUserLogin_Click(View v){

        final ProgressDialog progressDialog = ProgressDialog.show(LoginActivity.this,"Please wait ...", "Processing ...",true);
        (firebaseAuth.signInWithEmailAndPassword(EmailLogin.getText().toString(),Password.getText().toString()))
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()){
                            progressDialog.dismiss();
                            Log.e("ERROR",task.getException().toString());
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        else{
                            progressDialog.dismiss();
                            checkIfEmailVerified();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void checkIfEmailVerified() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        boolean emailverified = user.isEmailVerified();
        if(!emailverified){
            Toast.makeText(this, "Verify your email address", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            finish();
        }else{
            startActivity(new Intent(LoginActivity.this,Main2Activity.class));
        }
    }
}