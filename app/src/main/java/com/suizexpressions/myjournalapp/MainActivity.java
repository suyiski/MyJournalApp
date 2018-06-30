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
import android.view.MenuItem;
import android.view.View;

import com.suizexpressions.myjournalapp.data.JournalDatabase;
import com.suizexpressions.myjournalapp.data.JournalEntry;

import java.util.List;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

public class MainActivity extends AppCompatActivity implements JournalAdapter.ItemClickListener,
        NavigationView.OnNavigationItemSelectedListener {

    private JournalAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private JournalDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.logout) {
            Intent intent = new Intent(MainActivity.this, LogOutActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
