package com.group5.eventscape.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.group5.eventscape.R;
import com.group5.eventscape.adapters.MyEventsAdapter;
import com.group5.eventscape.interfaces.OnRowClicked;
import com.group5.eventscape.models.Event;
import com.group5.eventscape.viewmodels.EventViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity implements OnRowClicked {

    ImageButton back;
    MyEventsAdapter adapter;
    RecyclerView recyclerView;
    EventViewModel eventViewModel;
    ArrayList<Event> myEvents = new ArrayList<Event>();
    private String TAG = this.getClass().getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        back = findViewById(R.id.ibBackMyEvents);
        recyclerView = findViewById(R.id.rvMyEvents);

        adapter = new MyEventsAdapter( this.myEvents, this::onRowClicked);
        eventViewModel = EventViewModel.getInstance(getApplication());

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        back.setOnClickListener(v -> finish());
    }

    @Override
    public void onRowClicked(Event events) {
        Log.e(TAG, "Edit Event : Clicked" + events.getId() );
        Intent intent = new Intent(this, EditEventActivity.class);

        intent.putExtra("editEvent", events);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        eventViewModel.getAllEvents();
        eventViewModel.allListings.observe(this, events -> {
            myEvents.clear();
            for (Event event : events) {
                if (FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(event.getUser())) {
                    myEvents.add(event);
                }
            }
        });
        Log.d("TAG", "onResume: " + myEvents);
    }
}