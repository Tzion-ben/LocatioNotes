package application.locationotes.NotesActivities.Notes;

/**
 * Inthis activity we will delete a note
 * @author Tzion beniaminov
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import application.locationotes.R;

public class DeleteNote extends AppCompatActivity {

    private Intent retrieveData;
    private EditText titleOfNoteToDelete;
    private String  titleOfNoteToDeleteIn;

    /**for the progressBar timer*/
    private Timer progressbarTimer;
    private int count=0;
    private ProgressBar progressBar;

    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private static final String USERS = "Users";
    private static final String USERS_NOTES = "UsersNotes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        progressBar =  (ProgressBar) findViewById(R.id.delete_activity_progressBar);
        titleOfNoteToDelete = (EditText) findViewById(R.id.number_of_note_to_delete);

        retrieveData = getIntent();
        /**DB connection*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference();

        findViewById(R.id.delete_button_delete_activity).setOnClickListener(view ->{
            titleOfNoteToDeleteIn = titleOfNoteToDelete.getText().toString().trim();
            if(TextUtils.isEmpty(titleOfNoteToDeleteIn)){
                titleOfNoteToDelete.setError("required");
                return;
            }

            findViewById(R.id.number_of_note_to_delete).setVisibility(View.GONE);
            findViewById(R.id.delete_button_delete_activity).setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            progressbarTimer = new Timer();
            TimerTask progressbarTimerTask = new TimerTask() {
                @Override
                public void run() {
                    count++;
                    progressBar.setProgress(count);
                    if(count == 70){
                        progressbarTimer.cancel();

                        /**check the db if the input is correct*/
                        deleteNoteFromDb();
                    }
                }
            };

            /**scheduling the timer for the progressBar*/
            progressbarTimer.schedule(progressbarTimerTask,0,70);
        });
    }

    /**will delete a note from DB*/
    private void deleteNoteFromDb(){
        /**DB connection*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference();

        dbRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String numOfNotesUser = snapshot.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("numberOfNotes").getValue().toString();
                dbRootRef.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("numberOfNotes").setValue((Integer.valueOf(numOfNotesUser)-1));

                /**if the title is the same title, will delete it*/
                for(DataSnapshot dataSnapshot: snapshot.child(USERS_NOTES).child(retrieveData.getStringExtra("userGoogleId")).
                        child("NotesList").getChildren()){
                    if(dataSnapshot.child("title").getValue().toString().equals(titleOfNoteToDeleteIn)){
                            dataSnapshot.getRef().removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        finish();
    }
}