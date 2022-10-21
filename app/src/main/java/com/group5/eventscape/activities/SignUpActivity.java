package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.databinding.ActivitySignUpBinding;
import com.group5.eventscape.models.User;
import com.group5.eventscape.viewmodels.UserViewModel;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;
    private UserViewModel usersViewModel;
    User newUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        usersViewModel = UserViewModel.getInstance(getApplication());

        binding.btnRegister.setOnClickListener(v -> {
            validateRegisterDetails();
        });
        binding.tvLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
    }

    private void validateRegisterDetails(){
        String fullName = this.binding.etFullName.getText().toString();
        String email = this.binding.etEmail.getText().toString();
        String password = this.binding.etPassword.getText().toString();
        String confirmPassword = this.binding.etConfirmPassword.getText().toString();

        if (!fullName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty() ){
            if(password.equals(confirmPassword)){
                if( binding.cbTermsAndCondition.isChecked()){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                        newUser.setFullName(fullName);
                        newUser.setId(FirebaseAuth.getInstance().getUid());
                        newUser.setEmail(email);
                        usersViewModel.addUser(newUser);
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }).addOnFailureListener(e -> Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
                }else {
                    Toast.makeText(this, "Please check the terms and condition box ", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Please enter same password", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter all information", Toast.LENGTH_SHORT).show();
        }
    }
}