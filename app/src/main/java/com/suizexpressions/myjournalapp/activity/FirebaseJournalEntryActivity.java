package com.suizexpressions.myjournalapp.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suizexpressions.myjournalapp.R;
import com.suizexpressions.myjournalapp.data.FirebaseJournalEntry;

import java.util.Map;

public class FirebaseJournalEntryActivity extends AppCompatActivity {

    private static final String TAG = FirebaseJournalEntryActivity.class.getSimpleName();

    private EditText mEditTextTitle;
    private EditText mEditTextBody;
    private FirebaseFirestore mFirestoreDb;
    private String mId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_journal_entry);

        mEditTextTitle = findViewById(R.id.editText_title_firebase);
        mEditTextBody = findViewById(R.id.editText_body_firebase);

        mFirestoreDb = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mId = bundle.getString("UpdateJournalId");

            mEditTextTitle.setText(bundle.getString("UpdateJournalTitle"));
            mEditTextBody.setText(bundle.getString("UpdateJournalBody"));

        }
        Button buttonSave = findViewById(R.id.btn_save_entry_firebase);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FirebaseJournalEntryActivity.this, "I was clicked", Toast.LENGTH_SHORT).show();
                String title = mEditTextTitle.getText().toString();
                String body = mEditTextBody.getText().toString();

                if (title.length() > 0) {
                    if (mId.length() > 0) {
                        updateEntry(mId, title, body);
                    } else {
                        addEntry(title, body);
                    }
                }
                finish();
            }
        });

    }

    private void updateEntry(String id, String title, String body) {
        Map<String, Object> note = (new FirebaseJournalEntry(id, title, body)).toMap();

        mFirestoreDb.collection("journalentries")
                .document(id)
                .set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.e(TAG, "Joournal Entry update successful!");
                        Toast.makeText(getApplicationContext(), "Journal entry has been updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Journal entry", e);
                        Toast.makeText(getApplicationContext(), "Entry could not be updated", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addEntry(String title, String body) {
        Map<String, Object> note = new FirebaseJournalEntry(title, body).toMap();

        mFirestoreDb.collection("journalentries")
                .add(note)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        Toast.makeText(getApplicationContext(), "Entry has been added!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error adding Journal entry", e);
                        Toast.makeText(getApplicationContext(), "Entry could not be added!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
