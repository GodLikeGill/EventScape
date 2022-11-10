package com.group5.eventscape.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.activities.EditProfileActivity;
import com.group5.eventscape.activities.LoginActivity;
import com.group5.eventscape.activities.MyEventsActivity;
import com.group5.eventscape.activities.MyPurchasesActivity;
import com.group5.eventscape.activities.SettingsActivity;
import com.group5.eventscape.models.User;
import com.group5.eventscape.viewmodels.UserViewModel;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    User currentUser;
    ImageButton logout;
    TextView userName;
    TextView userEmail;
    Button editProfile;
    LinearLayout myEvents;
    LinearLayout myPurchases;
    UserViewModel userViewModel;
    CircleImageView image;
    LinearLayout settings;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        logout = view.findViewById(R.id.ibLogout);
        userName = view.findViewById(R.id.tvUserName);
        userEmail = view.findViewById(R.id.tvUserEmail);
        editProfile = view.findViewById(R.id.btnEditProfile);
        myPurchases = view.findViewById(R.id.llMyPurchases);
        image = view.findViewById(R.id.circleProfilePicture);
        myEvents = view.findViewById(R.id.llMyEvents);
        settings = view.findViewById(R.id.llSettings);

        userViewModel = UserViewModel.getInstance(getActivity().getApplication());

        logout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
            myEdit.putBoolean("isLoggedIn", false);
            myEdit.apply();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        myEvents.setOnClickListener(v -> startActivity(new Intent(getContext(), MyEventsActivity.class)));
        myPurchases.setOnClickListener(v -> startActivity(new Intent(getContext(), MyPurchasesActivity.class)));
        settings.setOnClickListener(v -> startActivity(new Intent(getContext(), SettingsActivity.class)));
        editProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), EditProfileActivity.class)));
    }

    @Override
    public void onResume() {
        super.onResume();

        userViewModel.getCurrentUser(FirebaseAuth.getInstance().getUid());
        userViewModel.currentUser.observe(this, user -> {
            currentUser =  user;
            userEmail.setText(user.getEmail());
            userName.setText(user.getFullName());
            if (user.getImage() != null) Picasso.get().load(user.getImage()).into(image);
        });
    }
}