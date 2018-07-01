package com.suizexpressions.myjournalapp.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.suizexpressions.myjournalapp.MainViewModel;
import com.suizexpressions.myjournalapp.R;
import com.suizexpressions.myjournalapp.adapter.JournalAdapter;
import com.suizexpressions.myjournalapp.data.JournalDatabase;
import com.suizexpressions.myjournalapp.data.JournalEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    private JournalAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private JournalDatabase mDb;
    private TextView mUsername;
    private TextView mUserEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUsername = findViewById(R.id.tv_user_name);
        mUserEmail = findViewById(R.id.tv_user_email);

        mRecyclerView = findViewById(R.id.recycler_view_journal_entries);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new JournalAdapter(this, this);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), VERTICAL);
        mRecyclerView.addItemDecoration(decoration);

        FloatingActionButton fabButton = findViewById(R.id.fab);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new intent to start an JournalEntryActivity
                Intent addEntryIntent = new Intent(MainActivity.this, JournalEntryActivity.class);
                startActivity(addEntryIntent);
            }
        });
        mDb = JournalDatabase.getInstance(getApplicationContext());
        setUpViewModel();

        updateUI();
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    public void setUpViewModel() {
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getJournalEntries().observe(this, new Observer<List<JournalEntry>>() {
            @Override
            public void onChanged(@Nullable List<JournalEntry> journalEntries) {
                mAdapter.setJournalEntry(journalEntries);
            }
        });
    }

    @Override
    public void onItemClickListener(int itemId) {
        // Launch JournalEntryActivity adding the itemId as an extra in the intent
        Intent intent = new Intent(MainActivity.this, JournalEntryActivity.class);
        intent.putExtra(JournalEntryActivity.EXTRA_JOURNAL_ID, itemId);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout) {
            Intent intent = new Intent(MainActivity.this, LogOutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUI() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("username");
        String email = intent.getStringExtra("userEmail");

        mUsername.setText(name);
        mUserEmail.setText(email);
    }
}
