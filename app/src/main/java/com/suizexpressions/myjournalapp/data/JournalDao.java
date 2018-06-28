package com.suizexpressions.myjournalapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journals")
    LiveData<List<JournalEntry>> loadAllEntries();

    @Insert
    void insertJournalEntry(JournalEntry journalEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateJournalEntry(JournalEntry JournalEntry);

    @Delete
    void deleteJournalEntry(JournalEntry journalEntry);

    // Query method that receives an int and returns a journal object
    @Query("SELECT * FROM journals WHERE mId = :id")
    LiveData<JournalEntry> loadJournalById(int id);
}