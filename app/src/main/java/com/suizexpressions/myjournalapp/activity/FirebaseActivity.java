package com.suizexpressions.myjournalapp.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.suizexpressions.myjournalapp.R;
import com.suizexpressions.myjournalapp.adapter.FirebaseJournalAdapter;
import com.suizexpressions.myjournalapp.data.FirebaseJournalEntry;

import java.util.ArrayList;
import java.util.List;

public class FirebaseActivity extends AppCompatActivity {

    private static final String TAG = FirebaseActivity.class.getSimpleName();

    private RecyclerView mRecyclerView;
    private FirebaseJournalAdapter mAdapter;

    private FirebaseFirestore mFirestoreDb;
    private ListenerRegistration mFirestoreListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase);
        mRecyclerView = findViewById(R.id.firebase_recycler_view);
        mFirestoreDb = FirebaseFirestore.getInstance();

        loadJournalEntries();

        FloatingActionButton fabButton = findViewById(R.id.fab_firebase);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addEntryIntent = new Intent(FirebaseActivity.this, FirebaseJournalEntryActivity.class);
                startActivity(addEntryIntent);
            }
        });

        mFirestoreListener = mFirestoreDb.collection("journalentries")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.e(TAG, "Listen failed!", e);
                            return;
                        }
                        List<FirebaseJournalEntry> entriesList = new ArrayList<>();
                        for(DocumentSnapshot doc :documentSnapshots) {
                            FirebaseJournalEntry entry = doc.toObject(FirebaseJournalEntry.class);
                            entry.setId(doc.getId());
                            entriesList.add(entry);
                        }
                        mAdapter = new FirebaseJournalAdapter(entriesList, getApplicationContext(), mFirestoreDb);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }

    private void loadJournalEntries() {
        mFirestoreDb.collection("journalentries")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<FirebaseJournalEntry> entriesList = new ArrayList<>();

                            for (DocumentSnapshot doc : task.getResult()) {
                                FirebaseJournalEntry entry = doc.toObject(FirebaseJournalEntry.class);
                                entry.setId(doc.getId());
                                entriesList.add(entry);
                            }

                            mAdapter = new FirebaseJournalAdapter(entriesList, getApplicationContext(), mFirestoreDb);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                            mRecyclerView.setLayoutManager(mLayoutManager);
                            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            Log.d(TAG, "Error getting Journal entries: ", task.getException());
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mFirestoreListener.remove();
    }

}
