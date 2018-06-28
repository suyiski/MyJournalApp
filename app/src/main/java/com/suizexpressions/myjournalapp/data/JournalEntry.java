package com.suizexpressions.myjournalapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity (tableName = "journals")
public class JournalEntry {

    @PrimaryKey (autoGenerate = true)
    private int mId;
    private String mJournalTitle;
    @ColumnInfo(name = "updated_at")
    private Date mUpdatedAt;
    private String mJournalEntryDescription;

    @Ignore
    public JournalEntry(String journalTitle, Date updatedAt, String journalEntryDescription) {
        mJournalTitle = journalTitle;
        mUpdatedAt = updatedAt;
        mJournalEntryDescription = journalEntryDescription;
    }

    public JournalEntry(int id, String journalTitle, Date updatedAt, String journalEntryDescription) {
        mId = id;
        mJournalTitle = journalTitle;
        mUpdatedAt = updatedAt;
        mJournalEntryDescription = journalEntryDescription;
    }

    public int getId() {return mId;}
    public void setId(int id) {mId = id;}
    public String getJournalTitle() {return mJournalTitle;}
    public Date getUpdatedAt() {return mUpdatedAt;}
    public String getJournalEntryDescription() {return mJournalEntryDescription;}
}
