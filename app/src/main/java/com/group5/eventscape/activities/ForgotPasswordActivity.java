package com.group5.eventscape.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.databinding.ActivityForgotPasswordBinding;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityForgotPasswordBinding binding;
    private FirebaseAuth mAuth;
    private final String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.binding.btnResetPassword.setOnClickListener(this);
        this.binding.tvLogin.setOnClickListener(this);


        this.mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        if (view != null){
            switch (view.getId()){
                case R.id.btnResetPassword:{
                    Log.d(TAG, "onClick: Reset Password Clicked");
                    this.resetPassword();
                    break;
                }
                case R.id.tvLogin:{
                    Log.d(TAG, "onClick: Login Clicked");

                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;
                }
            }
        }
    }

    private void resetPassword(){
        String emailText = this.binding.etEmail.getText().toString();

        if (!emailText.isEmpty()) {
            mAuth.sendPasswordResetEmail(emailText)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgotPasswordActivity.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ForgotPasswordActivity.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(this, "Please enter valid email address", Toast.LENGTH_SHORT).show();
        }
    }
}