package application.locationotes.NotesActivities.General;
/**
 * This activity is about The main User screen, from here the user will navigate
 * to his notes, create notes, ect...
 * @author Tzion Beniaminov
 */

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import application.locationotes.EntryActivities.MainActivity;
import application.locationotes.NotesActivities.Notes.ListMode;
import application.locationotes.NotesActivities.Notes.AddNewNote;
import application.locationotes.NotesActivities.Notes.MapMode;
import application.locationotes.R;

public class MainUserActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    private static final int PERMISSION_CODE =1000;
    private static final int IMAGE_CAPTURE_CODE =1001;
    private Intent retrieveData;

    private TextView welcomeMassge;
    private ImageView myImage;
    private Uri myImageUri;

    /**connect to DB for the profile image*/
    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private static final String USERS = "Users";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        /**connect to realDB*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference();
        retrieveData = getIntent();

        dbRootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String firstName =  snapshot.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("firstName").getValue().toString();
                String lastName =  snapshot.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).child("lastName").getValue().toString();
                welcomeMassge = (TextView) findViewById(R.id.welcome_massage_to_user_mainUser_activity);
                welcomeMassge.setText("Welcome "+firstName+" "+lastName+" !!");
                welcomeMassge.setTextSize(40);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /**floating action button*/
        findViewById(R.id.floating_action_button_main_user_activity).setOnClickListener(view -> {
            startActivity(new Intent(this, AddNewNote.class)
                    .putExtra("userGoogleId",retrieveData.getStringExtra("userGoogleId")));
            });

        myImage = findViewById(R.id.personal_image_main_user_activity);
        findViewById(R.id.take_picture_main_user_button).setOnClickListener(view ->{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /**asking for permission to access the camera and external storage */
                if (checkSelfPermission(Manifest.permission.CAMERA) ==
                        PackageManager.PERMISSION_DENIED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                PackageManager.PERMISSION_DENIED) {
                    String [] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permission, PERMISSION_CODE);
                }else{/**already have a permission*/
                    openCamera();
                }
            }
            else {openCamera();}
        });

        /**navigation menu*/
        bottomNav =findViewById(R.id.bottom_navigation_menu_main_user_activity);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_list_mode: {
                        startActivity(new Intent(getApplicationContext(), ListMode.class)
                                .putExtra("userGoogleId",retrieveData.getStringExtra("userGoogleId")));
                        return true;
                    }
                    case R.id.nav_map_mode: {
                        startActivity(new Intent(getApplicationContext(), MapMode.class)
                                .putExtra("userGoogleId",retrieveData.getStringExtra("userGoogleId")));
                        return true;
                    }
                    case R.id.nav_logout: {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    }
                }
                return false;
            }
        });
    }

    /**opening the camera to take a picture*/
    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Profile Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        myImageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI , values);

        /**camera intent*/
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, myImageUri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_OK) {
            myImage.setImageURI(myImageUri);

        /**write the uri to users details*/
        dbRootRef.child(USERS).child(retrieveData.getStringExtra("userGoogleId")).setValue(myImageUri.toString());
        }
    }

    /**handling permission user answer*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_CODE:
                /**user let the permission to the access camera*/
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    /**user NOT let the permission to the access camera*/
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show();
                }
        }
    }
}