package com.group5.eventscape.fragments;

import android.content.Intent;
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
import com.group5.eventscape.activities.MainActivity;
import com.group5.eventscape.viewmodels.EventViewModel;

public class ProfileFragment extends Fragment {

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

        ImageButton logout = view.findViewById(R.id.ibLogout);
        TextView username = view.findViewById(R.id.tvUserName);
        TextView useremail = view.findViewById(R.id.tvUserEmail);
        Button editProfile = view.findViewById(R.id.btnEditProfile);
        LinearLayout myPurchases = view.findViewById(R.id.llMyPurchases);
        LinearLayout myEvents = view.findViewById(R.id.llMyEvents);

        useremail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        logout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
            getActivity().finish();
        });

        editProfile.setOnClickListener(v -> {startActivity(new Intent(getContext(), EditProfileActivity.class));});
    }
}