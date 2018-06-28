package com.suizexpressions.myjournalapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suizexpressions.myjournalapp.data.JournalEntry;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class JournalAdapter extends RecyclerView.Adapter<JournalAdapter.JournalViewHolder> {

    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";
    // Date format
    private SimpleDateFormat mDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;

    private Context mContext;
    private List<JournalEntry> mListJournalEntry;

    public JournalAdapter(Context context, ItemClickListener itemClickListener) {
        mContext = context;
        mItemClickListener = itemClickListener;
    }


    @NonNull
    @Override
    public JournalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the journal list item layout to a view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.journal_list_item, parent, false);
        return new JournalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalViewHolder holder, int position) {
        JournalEntry journalEntry = mListJournalEntry.get(position);
        String title = journalEntry.getJournalTitle();
        String updatedAt = mDateFormat.format(journalEntry.getUpdatedAt());

        holder.journalEntryTitle.setText(title);
        holder.journalEntryDate.setText(updatedAt);

    }

    @Override
    public int getItemCount() {
        if (mListJournalEntry == null) {
            return 0;
        }
        return mListJournalEntry.size();
    }

    /**
     * When data changes, this method updates the list of taskEntries
     * and notifies the adapter to use the new values on it
     */
    public void setJournalEntry(List<JournalEntry> listJournalEntry) {
        mListJournalEntry = listJournalEntry;
        notifyDataSetChanged();
    }


    public class JournalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView journalEntryTitle;
        TextView journalEntryDate;

        public JournalViewHolder(View itemView) {
            super(itemView);
            journalEntryTitle = itemView.findViewById(R.id.journal_entry_title);
            journalEntryDate = itemView.findViewById(R.id.journal_entry_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = mListJournalEntry.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(id);
        }
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);
    }


}
