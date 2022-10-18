package com.group5.eventscape.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group5.eventscape.R;
import com.group5.eventscape.models.User;
import com.group5.eventscape.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    Uri imageUri;
    String imageURL;
    EditText etEmail;
    ImageButton back;
    Button saveProfile;
    EditText etFullName;
    CircleImageView image;

    UserViewModel userViewModel;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    User updatedUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        image = findViewById(R.id.circleProfilePictureEdit);
        etFullName = findViewById(R.id.etFullNameEdit);
        etEmail = findViewById(R.id.etEmailEdit);
        saveProfile = findViewById(R.id.btnSaveProfile);
        back = findViewById(R.id.ibBackEditProfile);

        userViewModel = UserViewModel.getInstance(getApplication());
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        image.setOnClickListener(v -> selectProfilePicture());

        back.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        saveProfile.setOnClickListener(v -> {
            uploadPicture();
            if (!etEmail.getText().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                FirebaseAuth.getInstance().getCurrentUser().updateEmail(etEmail.getText().toString());
            updatedUser.setFullName(etFullName.getText().toString());
            updatedUser.setEmail(etEmail.getText().toString());
            updatedUser.setId(FirebaseAuth.getInstance().getUid());
            updatedUser.setImage(imageURL);
            userViewModel.updateUser(updatedUser);
            finish();
        });
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            selectProfilePicture();
        } else {
            Toast.makeText(this, "Please allow storage permission to add photos", Toast.LENGTH_SHORT).show();
        }
    });

    private void selectProfilePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            image.setImageURI(imageUri);
        }
    }

    private void uploadPicture() {
        if (imageUri != null) {
            String imageUUID = UUID.randomUUID().toString();
            StorageReference ref = storageReference.child("images/users/" + imageUUID);
            ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    updatedUser.setImage(uri.toString());
                    userViewModel.updateUser(updatedUser);
                });
            }).addOnFailureListener(e -> {
                Log.e("TAG", "uploadPicture: " + e.getLocalizedMessage());
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        userViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid());
        userViewModel.currentUser.observe(this, user -> {
            etEmail.setText(user.getEmail());
            etFullName.setText(user.getFullName());
            if (user.getImage() != null && imageUri == null) {
                imageURL = user.getImage();
                Picasso.get().load(user.getImage()).into(image);
            }
        });
    }
}