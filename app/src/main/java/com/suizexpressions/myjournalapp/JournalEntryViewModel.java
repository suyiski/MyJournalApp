package com.suizexpressions.myjournalapp;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.suizexpressions.myjournalapp.data.JournalDatabase;
import com.suizexpressions.myjournalapp.data.JournalEntry;

public class JournalEntryViewModel extends ViewModel {

    private LiveData<JournalEntry> journalEntry;

    public JournalEntryViewModel(JournalDatabase database, int journalId) {
            journalEntry = database.journalDao().loadJournalById(journalId);
    }

    public LiveData<JournalEntry> getJournalEntry() {
        return journalEntry;
    }
}
