package com.example.testdatabaselogtag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextLoginEmail, editTextLoginPassword;
    private ProgressBar pb_Login;
    private FirebaseAuth authProfile;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");

        editTextLoginEmail = findViewById(R.id.editText_Login_Email);
        editTextLoginPassword = findViewById(R.id.editText_Login_Password);
        pb_Login = findViewById(R.id.pb_Login);

        authProfile = FirebaseAuth.getInstance();

        //Login User

        Button btnLogin = findViewById(R.id.button_Login_Login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textEmail = editTextLoginEmail.getText().toString();
                String textPassword = editTextLoginPassword.getText().toString();

                if(TextUtils.isEmpty(textEmail)){
                    Toast.makeText(LoginActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Email is required!");
                    editTextLoginEmail.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(LoginActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Valid email is required!");
                    editTextLoginEmail.requestFocus();
                } else if(TextUtils.isEmpty(textPassword)){
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextLoginEmail.setError("Password is required!");
                    editTextLoginEmail.requestFocus();
                }else{
                    pb_Login.setVisibility(View.VISIBLE);
                loginUser(textEmail, textPassword);
                }
            }});
    }


    private void loginUser(String email, String password) {
authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()){

            //Get instance of the current User
            FirebaseUser firebaseUser = authProfile.getCurrentUser();
            //Check if email is verified before coming to BillActivity
            if (firebaseUser.isEmailVerified()) {
                Toast.makeText(LoginActivity.this, "You are logged in now", Toast.LENGTH_SHORT).show();

                //Open BillActivity
                Intent intent = new Intent(LoginActivity.this, BillActivity.class);
                startActivity(intent);
                finish(); //to close Login Activity
            }else{
                firebaseUser.sendEmailVerification();
                authProfile.signOut(); //Sign User out before User can try Login again
                showAlertDialod();
            }


        } else{
            try {
                throw task.getException();
            } catch(FirebaseAuthInvalidUserException e){
                editTextLoginEmail.setError("User does not exist or is no longer valid. Please register agian");
                editTextLoginEmail.requestFocus();
            } catch(FirebaseAuthInvalidCredentialsException e){
                editTextLoginEmail.setError("Invalid credentials. Check and re-enter User details");
            } catch(Exception e){
                Log.e(TAG, e.getMessage());
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }

        }
    pb_Login.setVisibility(View.GONE);
    }
});
    }


    private void showAlertDialod() {
        //Setup Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You can not login without email verification.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // Go to email App, not open within Mini CashRegister
                startActivity(intent);
            }
        });
        //Create AlertDialog
        AlertDialog alertDialog = builder.create();
        //Show AlertDialog
        alertDialog.show();
    }

    //checking if User is already logged in -> yes = directly to BillActivity


    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser() != null){
            Toast.makeText(LoginActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();

            //Start Bill Activity
            startActivity(new Intent(LoginActivity.this,  BillActivity.class));
            finish(); //Close Login Activity
        }else{
            Toast.makeText(LoginActivity.this, "You can Login now.", Toast.LENGTH_SHORT).show();
        }
    }




}
