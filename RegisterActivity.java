package com.example.testdatabaselogtag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTxtRegisterUsername, editTxtRegisterEmail, editTxtRegisterPassword, editTxtRegisterConfirmPassword;
    private ProgressBar pbRegister;
    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this, "You can register now", Toast.LENGTH_LONG).show();
        pbRegister = findViewById(R.id.pb_Register);
        editTxtRegisterUsername = findViewById(R.id.editText_Register_Username);
        editTxtRegisterEmail = findViewById(R.id.editText_Register_Email);
        editTxtRegisterPassword = findViewById(R.id.editText_Register_Password);
        editTxtRegisterConfirmPassword = findViewById(R.id.editText_Register_confirm_Password);

        Button buttonRegister = findViewById(R.id.Register_Register_Button);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtain the entered data
                String textUsername = editTxtRegisterUsername.getText().toString();
                String textEmail = editTxtRegisterEmail.getText().toString();
                String textPassword = editTxtRegisterPassword.getText().toString();
                String textConfirmedPassword = editTxtRegisterConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(textUsername)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Username", Toast.LENGTH_LONG).show();
                    editTxtRegisterUsername.setError("Full Name is required");
                    editTxtRegisterUsername.requestFocus();
                } else if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Email", Toast.LENGTH_LONG).show();
                    editTxtRegisterEmail.setError("Full Name is required");
                    editTxtRegisterEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(RegisterActivity.this, "Please re-enter your Email", Toast.LENGTH_LONG).show();
                    editTxtRegisterEmail.setError("Valid Email is required");
                    editTxtRegisterEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your Password", Toast.LENGTH_LONG).show();
                    editTxtRegisterPassword.setError("Password is required");
                    editTxtRegisterPassword.requestFocus();
                } else if (textPassword.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "Your Password should be at least 6 digits", Toast.LENGTH_LONG).show();
                    editTxtRegisterPassword.setError("Password requires 6 digits at least.");
                    editTxtRegisterPassword.requestFocus();
                } else if (TextUtils.isEmpty(textConfirmedPassword)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your Password", Toast.LENGTH_LONG).show();
                    editTxtRegisterConfirmPassword.setError("Corfirming your Password is required");
                    editTxtRegisterConfirmPassword.requestFocus();
                } else if (!textPassword.equals(textConfirmedPassword)) {
                    Toast.makeText(RegisterActivity.this, "The passwords don't match ", Toast.LENGTH_LONG).show();
                    editTxtRegisterPassword.setError("Password Confirmation is required");
                    editTxtRegisterPassword.requestFocus();
                    //Clear entered passwords
                    editTxtRegisterPassword.clearComposingText();
                    editTxtRegisterConfirmPassword.clearComposingText();
                } else {
                    pbRegister.setVisibility(View.VISIBLE);
                    registerUser(textUsername, textEmail, textPassword);
                }
            }
        });

    }

    //Register the User using the credentials given
    private void registerUser(String textUsername, String textEmail, String textPassword) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textEmail, textPassword).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "User registered successfully", Toast.LENGTH_LONG).show();
                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //Enter User Data Database
                    ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textUsername, textEmail, textPassword);


                    firebaseUser.sendEmailVerification();

                    Toast.makeText(RegisterActivity.this, "User registered sucessfully.Please verify your email.", Toast.LENGTH_LONG).show();
                    //Open WelcomeScreen when successful registration
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();


                 } else {
                try {
                    throw task.getException();
                } catch (FirebaseAuthWeakPasswordException e) {
                    editTxtRegisterPassword.setError("Your password is too weak. Kindly use a mix of alphabets, numbers and special characters");
                    editTxtRegisterPassword.requestFocus();
                } catch (FirebaseAuthInvalidCredentialsException e) {
                    editTxtRegisterPassword.setError("Your email is invalid or already in use. Kindly re-enter.");
                    editTxtRegisterPassword.requestFocus();
                } catch (FirebaseAuthUserCollisionException e) {
                    editTxtRegisterPassword.setError("User is already registered with this email. Use another email.");
                    editTxtRegisterPassword.requestFocus();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    pbRegister.setVisibility(View.GONE);
                }
            }
        }
    });
}

}