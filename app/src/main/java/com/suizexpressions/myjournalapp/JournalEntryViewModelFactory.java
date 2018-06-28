package com.suizexpressions.myjournalapp;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.suizexpressions.myjournalapp.data.JournalDatabase;

public class JournalEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final JournalDatabase mDb;
    private final int mJournalId;

    public JournalEntryViewModelFactory(JournalDatabase database, int journalId) {
        mDb = database;
        mJournalId = journalId;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new JournalEntryViewModel(mDb, mJournalId);
    }
}
