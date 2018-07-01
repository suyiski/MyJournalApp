package com.suizexpressions.myjournalapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.suizexpressions.myjournalapp.JournalEntryViewModel;
import com.suizexpressions.myjournalapp.JournalEntryViewModelFactory;
import com.suizexpressions.myjournalapp.JournalExecutors;
import com.suizexpressions.myjournalapp.R;
import com.suizexpressions.myjournalapp.data.JournalDatabase;
import com.suizexpressions.myjournalapp.data.JournalEntry;

import java.util.Date;

public class JournalEntryActivity extends AppCompatActivity {

    // Extra for the journal ID to be received in the intent
    public static String EXTRA_JOURNAL_ID = "extraJournalId";
    // Extra for the journal ID to be received after rotation
    public static final String INSTANCE_JOURNAL_ID = "instanceJournalId";
    // Constant for default journal id to be used when not in update mode
    private static final int DEFAULT_JOURNAL_ID = -1;
    // Constant for logging
    private static final String TAG = JournalEntryActivity.class.getSimpleName();

    private EditText mJournalTitle;
    private EditText mJournalEntry;
    private Button mButtonSaveEntry;
    private Handler mHandler;

    private int mJournalId = DEFAULT_JOURNAL_ID;

    JournalDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_entry);

        initialiseViews();


        mDb = JournalDatabase.getInstance(getApplicationContext());

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_JOURNAL_ID)) {
            mJournalId = savedInstanceState.getInt(INSTANCE_JOURNAL_ID, DEFAULT_JOURNAL_ID);
        }

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_JOURNAL_ID)) {
            mButtonSaveEntry.setText(R.string.update_button);
            if (mJournalId == DEFAULT_JOURNAL_ID) {
                // populate the UI
                mJournalId = intent.getIntExtra(EXTRA_JOURNAL_ID, DEFAULT_JOURNAL_ID);

                JournalEntryViewModelFactory factory = new JournalEntryViewModelFactory(mDb, mJournalId);
                final JournalEntryViewModel viewModel =
                        ViewModelProviders.of(this, factory).get(JournalEntryViewModel.class);

                        viewModel.getJournalEntry().observe(this, new Observer<JournalEntry>() {
                            @Override
                            public void onChanged(@Nullable JournalEntry journalEntry) {
                                viewModel.getJournalEntry().removeObserver(this);
                                populateUI(journalEntry);
                            }
                        });
                    }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId){
            case R.id.item_delete:
                dialogListenerDelete();
                break;
            case android.R.id.home:

            }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(INSTANCE_JOURNAL_ID, mJournalId);
        super.onSaveInstanceState(outState);
    }

    private void initialiseViews() {
        mJournalTitle = findViewById(R.id.editText_title);
        mJournalEntry = findViewById(R.id.editText_Body);
        mButtonSaveEntry = findViewById(R.id.btn_save_entry);
        mButtonSaveEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSaveButtonClicked();
            }
        });
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new journal entry into the underlying database.
     */
    public void onSaveButtonClicked() {

        final String title = mJournalTitle.getText().toString();

        final Date date = new Date();

        final String body = mJournalEntry.getText().toString();

        if(TextUtils.isEmpty(title)) {
            Toast.makeText(JournalEntryActivity.this, "Please enter a title before saving",
                    Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(body)) {
            dialogListenerSave();
            return;
        }

        final JournalEntry journalEntry = new JournalEntry(title, date, body);
        JournalExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mJournalId == DEFAULT_JOURNAL_ID) {
                    // insert new journal entry
                    mDb.journalDao().insertJournalEntry(journalEntry);
                } else {
                    //update task
                    journalEntry.setId(mJournalId);
                    mDb.journalDao().updateJournalEntry(journalEntry);
                }
                finish();
            }
        });
    }

    public void dialogListenerSave() {
        final String title = mJournalTitle.getText().toString();

        final Date date = new Date();

        final String body = mJournalEntry.getText().toString();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        final JournalEntry journalEntry = new JournalEntry(title, date, body);
                        JournalExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mJournalId == DEFAULT_JOURNAL_ID) {
                                    // insert new journal entry
                                    mDb.journalDao().insertJournalEntry(journalEntry);
                                } else {
                                    //update task
                                    journalEntry.setId(mJournalId);
                                    mDb.journalDao().updateJournalEntry(journalEntry);
                                }
                                finish();
                            }
                        });


                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(JournalEntryActivity.this);
        builder.setMessage(R.string.dialog_message_save)
                .setTitle(R.string.dialog_title_save)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.cancel, dialogClickListener)
                .create()
                .show();
    }


    public void dialogListenerDelete() {
        final String title = mJournalTitle.getText().toString();

        final Date date = new Date();

        final String body = mJournalEntry.getText().toString();
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (i){
                    case DialogInterface.BUTTON_POSITIVE:
                        final JournalEntry journalEntry = new JournalEntry(title, date, body);
                        JournalExecutors.getInstance().diskIO().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mJournalId == DEFAULT_JOURNAL_ID) {
                                    // insert new journal entry
                                    unsavedToast();
                                    Message message = mHandler.obtainMessage();
                                    message.sendToTarget();
                                    return;
                                } else {
                                    //update task
                                    journalEntry.setId(mJournalId);
                                    mDb.journalDao().deleteJournalEntry(journalEntry);
                                }
                                finish();
                            }
                        });


                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }

            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(JournalEntryActivity.this);
        builder.setMessage(R.string.dialog_message_delete)
                .setTitle(R.string.dialog_title_delete)
                .setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.cancel, dialogClickListener)
                .create()
                .show();
    }

    public void unsavedToast() {
         mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                // This is where you do your work in the UI thread.
                // Your worker tells you in the message what to do.
                Toast.makeText(JournalEntryActivity.this,
                        "This entry is unsaved, can't be deleted", Toast.LENGTH_LONG).show();
            }
        };
    }

    public void populateUI(JournalEntry entry) {
        // return in case of an empty entry
        if (entry == null) {
            return;
        }
        // Update the EditText fields with accurate information
        mJournalTitle.setText(entry.getJournalTitle());
        mJournalEntry.setText(entry.getJournalEntryDescription());
    }

}
