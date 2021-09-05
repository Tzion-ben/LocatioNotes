package application.locationotes.NotesActivities.Notes;
/**
 * This activity is about add new note for the user and store it in the DB
 * @author Tzion Beniaminov
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import application.locationotes.DataObjects.Notes.NoteData;
import application.locationotes.NotesActivities.General.MainUserActivity;
import application.locationotes.R;

public class AddNewNote extends AppCompatActivity {

    private Intent retrieveData;
    private EditText title, description;
    private NoteData newUserNote;

    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private static final String USERS_NOTES = "UsersNotes";
    private static final String USERS = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_node);

        retrieveData = getIntent();
        title = (EditText)findViewById(R.id.add_new_note_title_activity);
        description = (EditText)findViewById(R.id.add_new_note_description_activity);

        /**DB connection*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference();

        /**clicked on SAVE NEW NOTE*/
        findViewById(R.id.add_new_note_activity_save_note_button).setOnClickListener(view ->{
            String titleIn = title.getText().toString();
            String descriptionIn = description.getText().toString();
            long createdTime = System.currentTimeMillis();

            newUserNote = new NoteData(titleIn, descriptionIn, createdTime);
            addToDb();

        });
    }

    /**adding the note to the DB*/
    private void addToDb() {
        dbRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numOfNotes_of_all_time = snapshot.child(USERS_NOTES).child(retrieveData.getStringExtra("userGoogleId")).child("numOfNotesInAllTime").getValue().toString();
                dbRootRef.child(USERS_NOTES).child(retrieveData.getStringExtra("userGoogleId")).child("NotesList").child("note_"+(Integer.valueOf(numOfNotes_of_all_time)+1)).setValue(newUserNote);
                dbRootRef.child(USERS_NOTES).child(retrieveData.getStringExtra("userGoogleId")).child("numOfNotesInAllTime").setValue((Integer.valueOf(numOfNotes_of_all_time)+1));

                String numOfNotes = snapshot.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("numberOfNotes").getValue().toString();
                dbRootRef.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("numberOfNotes").setValue((Integer.valueOf(numOfNotes)+1));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        runOnUiThread(() ->Toast.makeText(this,"Note saved",Toast.LENGTH_LONG).show());
        finish();
    }
}