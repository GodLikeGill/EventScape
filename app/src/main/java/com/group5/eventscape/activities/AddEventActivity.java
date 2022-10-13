package com.group5.eventscape.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group5.eventscape.R;
import com.group5.eventscape.databinding.ActivityAddEventBinding;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.viewmodels.EventViewModel;

import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

public class AddEventActivity extends AppCompatActivity {

    private ActivityAddEventBinding binding;
    private static final int GalleryPick = 1;
    private EventViewModel eventViewModel;
    ImageView eventImage;
    private String imageUUID;
    String[] category = {"Music", "Sports", "Concert"};

    ArrayAdapter<String> adapterItems;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatePickerDialog.OnDateSetListener setListener;
    Event newEvent = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        eventViewModel = EventViewModel.getInstance(getApplication());
        eventImage = binding.ivEventImage;
        AutoCompleteTextView autoCompleteTextView = binding.autoCompleteText;

        adapterItems = new ArrayAdapter<String>(this, R.layout.category_list,category);
        autoCompleteTextView.setAdapter(adapterItems);

        ImageButton btnAddImage = binding.iBtnAddUpdateEventImage;
        EditText eventTitle = binding.etEventTitle;
        EditText eventDescription = binding.etEventDescription;
        EditText eventAddress = binding.etEventAddress;
        EditText eventCity = binding.etEventCity;
        EditText eventProvince = binding.etEventProvince;
        EditText eventPostCode = binding.etEventPostCode;
        TextView eventDate = binding.datePicker;
        EditText eventTime = binding.etEventTime;
        EditText eventPrice = binding.etEventPrice;
        AppCompatButton btnAddEvent = binding.btnAddEvent;

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = dayOfMonth+"/"+month+"/"+year;
                eventDate.setText(date);
            }
        };

        btnAddImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(AddEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        btnAddEvent.setOnClickListener(view -> {
            if (eventTitle.getText().toString().isEmpty() || eventAddress.getText().toString().isEmpty() || eventDescription.getText().toString().isEmpty() || eventDate.getText().toString().isEmpty() || eventTime.getText().toString().isEmpty() || eventPrice.getText().toString().isEmpty()) {
                Toast.makeText(AddEventActivity.this, "All details should be Filled ", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
                newEvent.setTitle(eventTitle.getText().toString());
                newEvent.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                newEvent.setAddress(eventAddress.getText().toString());
                newEvent.setCity(eventCity.getText().toString());
                newEvent.setProvince(eventProvince.getText().toString());
                newEvent.setPostCode(eventPostCode.getText().toString());
                newEvent.setDesc(eventDescription.getText().toString());
                newEvent.setCategory(autoCompleteTextView.getText().toString());
                newEvent.setDate(eventDate.getText().toString());
                newEvent.setTime(eventTime.getText().toString());
                newEvent.setPrice(eventPrice.getText().toString());
                newEvent.setId(UUID.randomUUID().toString());
                eventViewModel.addEvent(newEvent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                eventImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
        if (isGranted) {
            Intent galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);
        } else {
            Toast.makeText(AddEventActivity.this, "Please allow storage permission to add photos", Toast.LENGTH_SHORT).show();
        }
    });

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(this, "You must select product photo.", Toast.LENGTH_SHORT).show();
            return;
        }

        imageUUID = UUID.randomUUID().toString();
        storageReference = FirebaseStorage.getInstance().getReference("images/" + imageUUID);
        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {

            eventImage.setImageURI(null);
            Log.d("TAG", "uploadImage: Successfully uploaded images to storage");

            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    newEvent.setImage(uri.toString());
                    eventViewModel.addEvent(newEvent);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("TAG", "uploadImage: Error uploading images to storage " + e.getLocalizedMessage());
                }
            });

        }).addOnFailureListener(e -> {
            Log.e("TAG", "uploadImage: Error uploading images to storage " + e.getLocalizedMessage());
        });
    }

}