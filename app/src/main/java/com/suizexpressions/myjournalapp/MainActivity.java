package com.suizexpressions.myjournalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
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

        Intent intent = getIntent();
        String name = intent.getStringExtra("username");
        String email = intent.getStringExtra("userEmail");

        mUsername.setText(name);
        mUserEmail.setText(email);

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
                Intent addTaskIntent = new Intent(MainActivity.this, JournalEntryActivity.class);
                startActivity(addTaskIntent);
            }
        });
        mDb = JournalDatabase.getInstance(getApplicationContext());
        setUpViewModel();
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
}
