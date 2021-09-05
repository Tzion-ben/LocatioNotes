package application.locationotes.NotesActivities.Notes;

/**
 * This class eill help with the list mode view recycler viewer
 * @author Tzion Beniaminov
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.ArrayList;

import application.locationotes.DataObjects.Notes.NoteData;
import application.locationotes.R;


public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<NoteData> notesList;

    /**will help with thw DELETE and EDIT Notes*/
    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private static final String USERS_NOTES = "UsersNotes";
    private static final String USERS = "Users";

    /**c'tor*/
    public NotesAdapter(Context context, ArrayList<NoteData> notesList) {
        this.context = context;
        this.notesList = notesList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        NoteData currentNote = notesList.get(position);
        holder.titleOutput.setText(currentNote.getTitle());
        holder.descriptionOutput.setText(currentNote.getDescription());

        String formatedTime = DateFormat.getDateTimeInstance().format(currentNote.getCreatedTime());
        holder.timeOutput.setText((formatedTime));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder{

        private TextView titleOutput, descriptionOutput, timeOutput;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleOutput =itemView.findViewById(R.id.title_output_one_item);
            descriptionOutput =itemView.findViewById(R.id.description_output_one_item);
            timeOutput =itemView.findViewById(R.id.time_output_one_item);
        }
    }
}
