package com.group5.eventscape;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextView forgotPass, register;
    EditText email, pass;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        forgotPass = findViewById(R.id.tvForgotPassword);
        register = findViewById(R.id.tvRegister);
        login = findViewById(R.id.btnLogin);
        email = findViewById(R.id.etEmail);
        pass = findViewById(R.id.etPassword);

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
//                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    finish();
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
            } else {
                Toast.makeText(this, "Please enter valid information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}