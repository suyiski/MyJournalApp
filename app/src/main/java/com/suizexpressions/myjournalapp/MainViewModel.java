package com.suizexpressions.myjournalapp;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.suizexpressions.myjournalapp.data.JournalDatabase;
import com.suizexpressions.myjournalapp.data.JournalEntry;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    // Constant for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<List<JournalEntry>> mJournalEntries;

    public MainViewModel(Application application) {
        super(application);
        JournalDatabase database = JournalDatabase.getInstance(this.getApplication());
        mJournalEntries = database.journalDao().loadAllEntries();
    }

    public LiveData<List<JournalEntry>> getJournalEntries() {return  mJournalEntries;}

}
