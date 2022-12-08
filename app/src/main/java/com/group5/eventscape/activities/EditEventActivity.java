package com.group5.eventscape.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.group5.eventscape.R;
import com.group5.eventscape.databinding.ActivityEditEventBinding;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.viewmodels.EventViewModel;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class EditEventActivity extends AppCompatActivity {

    private ActivityEditEventBinding binding;
    private static final int GalleryPick = 1;
    private EventViewModel eventViewModel;
    ImageView eventImage;
    Button btnEventTime;
    int hour, minute;
    private String imageUUID;
    String[] category = {"Music", "Sports", "Concerts", "Expos", "Festivals", "Workshops"};

    ArrayAdapter<String> adapterItems;
    private Uri imageUri;
    private StorageReference storageReference;
    private DatePickerDialog.OnDateSetListener setListener;
    private DatePickerDialog.OnDateSetListener setListener2;
    Event editEvent = new Event();
    private Event curEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = ActivityEditEventBinding.inflate(getLayoutInflater());
        setContentView(this.binding.getRoot());

        this.curEvent = (Event) getIntent().getParcelableExtra("editEvent");

        eventViewModel = EventViewModel.getInstance(getApplication());
        eventImage = binding.ivEventImage;
        AutoCompleteTextView autoCompleteTextView = binding.autoCompleteText;

        adapterItems = new ArrayAdapter<String>(this, R.layout.category_list,category);



        ImageButton btnAddImage = binding.iBtnAddUpdateEventImage;
        EditText eventTitle = binding.etEventTitle;
        EditText eventDescription = binding.etEventDescription;
        EditText eventAddress = binding.etEventAddress;
        EditText eventCity = binding.etEventCity;
        EditText eventProvince = binding.etEventProvince;

        EditText eventPostCode = binding.etEventPostCode;
        TextView eventDate = binding.datePicker;
        TextView eventDate2 = binding.datePicker2;
        btnEventTime = binding.btnEventTime;
        EditText eventPrice = binding.etEventPrice;
        AppCompatButton btnEditEvent = binding.btnEditEvent;

        Picasso.get().load(this.curEvent.getImage()).into(this.eventImage );
        eventTitle.setText(this.curEvent.getTitle());
        eventDescription.setText(this.curEvent.getDesc());
        eventAddress.setText(this.curEvent.getAddress());
        eventCity.setText(this.curEvent.getCity());
        eventProvince.setText(this.curEvent.getProvince());
        eventPostCode.setText(this.curEvent.getPostCode());
        eventDate.setText(this.curEvent.getDate());
        eventDate2.setText(this.curEvent.getDate2());
        btnEventTime.setText(this.curEvent.getTime());
        eventPrice.setText(this.curEvent.getPrice());
        autoCompleteTextView.setText(this.curEvent.getCategory());
        autoCompleteTextView.setAdapter(adapterItems);



        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year2 = calendar.get(Calendar.YEAR);
        final int month2 = calendar.get(Calendar.MONTH);
        final int day2 = calendar.get(Calendar.DAY_OF_MONTH);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener,year,month,day);
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

        eventDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        EditEventActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, setListener2,year2,month2,day2);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        setListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year2, int month2, int dayOfMonth2) {
                month2 = month2+1;
                String date2 = dayOfMonth2+"/"+month2+"/"+year2;
                eventDate2.setText(date2);
            }
        };

        btnAddImage.setOnClickListener(view -> {
            if (ContextCompat.checkSelfPermission(EditEventActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GalleryPick);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        btnEditEvent.setOnClickListener(view -> {

            Geocoder geocoder = new Geocoder(this);
            List<Address> addressList;
            double longitude;
            double latitude;

            String address = eventAddress.getText().toString() + "," + eventCity.getText().toString();

            try {
                addressList = geocoder.getFromLocationName(address,1);

                if(addressList != null){
                    longitude = addressList.get(0).getLongitude();
                    latitude = addressList.get(0).getLatitude();

                    if (eventTitle.getText().toString().isEmpty() || eventAddress.getText().toString().isEmpty() || eventDescription.getText().toString().isEmpty() || eventDate.getText().toString().isEmpty() || btnEventTime.getText().toString().isEmpty() || eventPostCode.getText().toString().isEmpty() || eventPrice.getText().toString().isEmpty()) {
                        Toast.makeText(EditEventActivity.this, "All details should be Filled ", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadImage();
                        editEvent.setTitle(eventTitle.getText().toString());
                        editEvent.setUser(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                        editEvent.setAddress(eventAddress.getText().toString());
                        editEvent.setCity(eventCity.getText().toString());
                        editEvent.setProvince(eventProvince.getText().toString());

                        editEvent.setLongitude(String.valueOf(longitude));
                        editEvent.setLatitude(String.valueOf(latitude));
                        editEvent.setPostCode(eventPostCode.getText().toString());
                        editEvent.setDesc(eventDescription.getText().toString());
                        editEvent.setCategory(autoCompleteTextView.getText().toString());
                        editEvent.setDate(eventDate.getText().toString());
                        editEvent.setDate2(eventDate2.getText().toString());
                        editEvent.setTime(btnEventTime.getText().toString());
                        editEvent.setPrice(eventPrice.getText().toString());
                        editEvent.setId(curEvent.getId());

                        String Date1 = eventDate.getText().toString();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date date = sdf.parse(Date1);
                            long startDateToMilli = date.getTime();
                            editEvent.setStartDateInMilli(startDateToMilli);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        eventViewModel.editEvent(editEvent);
                        finish();
                    }
                }else{
                    Toast.makeText(EditEventActivity.this, "Please Enter Correct Address ", Toast.LENGTH_SHORT).show();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }


        });

    }

    public void popTimePicker(View view){
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                hour = i;
                minute = i1;
                btnEventTime.setText(String.format(Locale.getDefault(), "%02d:%02d",hour,minute));
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Event Time");
        timePickerDialog.show();
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
            Toast.makeText(EditEventActivity.this, "Please allow storage permission to add photos", Toast.LENGTH_SHORT).show();
        }
    });

    private void uploadImage() {
        if (imageUri == null) {
            editEvent.setImage(curEvent.getImage());
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
                    editEvent.setImage(uri.toString());
                    eventViewModel.editEvent(editEvent);
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