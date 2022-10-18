package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPass, register;
    EditText email, pass;
    Button login;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPass = findViewById(R.id.tvForgotPassword);
        register = findViewById(R.id.tvRegister);
        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPassword);
        rememberMe = findViewById(R.id.cbRememberMe);

        forgotPass.setOnClickListener(v -> {
            startActivity(new Intent(this, ForgotPasswordActivity.class));
        });

        register.setOnClickListener(v -> {
            startActivity(new Intent(this, SignUpActivity.class));
        });

        login.setOnClickListener(v -> {
            String emailText = email.getText().toString();
            String passText = pass.getText().toString();
            if (!emailText.isEmpty() || !passText.isEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(emailText, passText).addOnSuccessListener(authResult -> {
                    if (rememberMe.isChecked()){
                        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putBoolean("isLoggedIn", true);
                        myEdit.apply();
                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}