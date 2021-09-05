package application.locationotes.NotesActivities.Notes;

/**
 * This activity is about show all the notes of the user in a list mode
 * @author Tzion Beniaminov
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

import application.locationotes.DataObjects.Notes.NoteData;
import application.locationotes.R;
import application.locationotes.Util.DataCompare;

public class ListMode extends AppCompatActivity {

    private Intent retrieveData;
    private RecyclerView notesRecyclerView;
    private TextView numOfNotes;

    private ArrayList<NoteData> notesList;
    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private static final String USERS_NOTES = "UsersNotes";

    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_mode);

        retrieveData = getIntent();
        /**DB connection*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference(USERS_NOTES).child(retrieveData.getStringExtra("userGoogleId")).child("NotesList");

        numOfNotes = (TextView) findViewById(R.id.num_of_notes_list_mode_activity);
        notesRecyclerView = findViewById(R.id.list_of_notes_list_mode_activity);
        notesRecyclerView.setHasFixedSize(true);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        /**our lest of notes*/
        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(this, notesList);

        notesRecyclerView.setAdapter(notesAdapter);

        notesList.clear();
        dbRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                notesList.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    NoteData noteData = dataSnapshot.getValue(NoteData.class);
                    notesList.add(noteData);

                }
                Collections.sort(notesList, new DataCompare());
                numOfNotes.setText("You have " + notesAdapter.getItemCount() +" notes");
                numOfNotes.setTextSize(20);
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        findViewById(R.id.add_new_note_button_list_mode_activity).setOnClickListener(view ->{
            startActivity(new Intent(this, AddNewNote.class)
                    .putExtra("userGoogleId",retrieveData.getStringExtra("userGoogleId")));
        });

        findViewById(R.id.delete_note_button_list_mode_activity).setOnClickListener(view ->{
            Intent intent = new Intent(this,DeleteNote.class);
            intent.putExtra("userGoogleId",retrieveData.getStringExtra("userGoogleId"));
            startActivity(intent);
        });
    }
}