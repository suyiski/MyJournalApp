package com.suizexpressions.myjournalapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.suizexpressions.myjournalapp.activity.FirebaseJournalEntryActivity;
import com.suizexpressions.myjournalapp.R;
import com.suizexpressions.myjournalapp.data.FirebaseJournalEntry;

import java.util.List;

public class FirebaseJournalAdapter extends RecyclerView.Adapter<FirebaseJournalAdapter.ViewHolder> {

    private List<FirebaseJournalEntry> mFirebaseJournalList;
    private Context mContext;
    private FirebaseFirestore mFirestoreDb;

    public FirebaseJournalAdapter(List<FirebaseJournalEntry> firebaseJournalEntry, Context context, FirebaseFirestore firestoreDb) {
        mFirebaseJournalList = firebaseJournalEntry;
        mContext = context;
        mFirestoreDb = firestoreDb;
    }

    @NonNull
    @Override
    public FirebaseJournalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.firebase_journal_list_item, parent, false);

        return new FirebaseJournalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int itemPosition = position;
        final FirebaseJournalEntry entry = mFirebaseJournalList.get(itemPosition);

        holder.title.setText(entry.getTitle());
        holder.body.setText(entry.getContent());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEntry(entry);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEntry(entry.getId(), itemPosition);
            }
        });
    }



    @Override
    public int getItemCount() {
        return mFirebaseJournalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;
        ImageView edit;
        ImageView delete;

        public ViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.firebase_journal_entry_title);
            body = view.findViewById(R.id.firebase_journal_entry_content);
            edit = view.findViewById(R.id.image_firebase_edit_entry);
            delete = view.findViewById(R.id.image_firebase_delete_entry);

        }
    }

    private void updateEntry(FirebaseJournalEntry entry) {
        Intent intent = new Intent(mContext, FirebaseJournalEntryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("UpdateJournalId", entry.getId());
        intent.putExtra("UpdateJournalTitle", entry.getTitle());
        intent.putExtra("UpdateJournalBody", entry.getContent());
        mContext.startActivity(intent);
    }


    private void deleteEntry(String id, final int position) {
        mFirestoreDb.collection("journalentries")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mFirebaseJournalList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mFirebaseJournalList.size());
                        Toast.makeText(mContext, "Entry has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
