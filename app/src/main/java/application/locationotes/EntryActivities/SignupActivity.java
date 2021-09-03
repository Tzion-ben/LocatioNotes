package application.locationotes.EntryActivities;

/**
 * Sign Up to the app activity
 * @author Tzion Beniaminov
 */
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import application.locationotes.DataObjects.Personal.UserDetails;
import application.locationotes.NotesActivities.MainUserActivity;
import application.locationotes.R;

public class SignupActivity extends AppCompatActivity {

    private EditText firstName, lastName, email, password;
    private TextView mainMessege;
    private UserDetails newUser;
    private boolean isExist = false; /*will help to go to the right activity*/

    /**for the progressBar timer*/
    private Timer progressbarTimer;
    private int count=0;
    private ProgressBar progressBar;


    private FirebaseDatabase dbRoot;
    private DatabaseReference dbRootRef;
    private FirebaseAuth dbAuthentication;
    private static final String USERS = "Users";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /**DB connection*/
        dbRoot = FirebaseDatabase.getInstance();
        dbRootRef = dbRoot.getReference(USERS);
        dbAuthentication = FirebaseAuth.getInstance();

        firstName = (EditText)findViewById(R.id.first_name_signup_activity);
        lastName = (EditText)findViewById(R.id.last_name_signup_activity);
        email = (EditText)findViewById(R.id.email_signup_activity);
        password = (EditText)findViewById(R.id.password_signup_activity);
        progressBar = findViewById(R.id.progressBar_signUp_signup_activity);
        mainMessege= (TextView)findViewById(R.id.fill_in_text_signup_activity);

        /**clicked the signUp button*/
        findViewById(R.id.signup_button_signup_activity).setOnClickListener(view ->{
            String firstNameIn = firstName.getText().toString().trim();
            String lastNameIn = lastName.getText().toString().trim();
            String emailIn = email.getText().toString().trim();
            String passwordIn = password.getText().toString().trim();

            /**have to fill in all the fields*/
            if(TextUtils.isEmpty(firstNameIn) || TextUtils.isEmpty(lastNameIn) || TextUtils.isEmpty(emailIn)
                || TextUtils.isEmpty(passwordIn) || passwordIn.length() != 8){
                if(TextUtils.isEmpty(firstNameIn)){firstName.setError("required");}
                if(TextUtils.isEmpty(lastNameIn)){lastName.setError("required");}
                if(TextUtils.isEmpty(emailIn)){email.setError("required");}
                if(TextUtils.isEmpty(passwordIn)){password.setError("required");}
                if(password.length() != 8) {password.setError("required 8 signs");}
                return;
                }

            /**will check if the user is already exist*/
            dbRootRef.orderByChild("email").equalTo(emailIn).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getChildrenCount() != 0){
                        Toast.makeText(SignupActivity.this,"The User Is Already Exist",Toast.LENGTH_LONG).show();
                        Toast.makeText(SignupActivity.this,"Login page will open",Toast.LENGTH_LONG).show();
                        isExist = true;
                        /**no such user. will create a new one*/
                    }else{
                        newUser = new UserDetails(emailIn,firstNameIn,lastNameIn,passwordIn);
                        Toast.makeText(SignupActivity.this,"WELCOME "+firstNameIn+" "+lastNameIn+" !",Toast.LENGTH_LONG).show();
                    }

                    firstName.setVisibility(view.GONE);
                    lastName.setVisibility(view.GONE);
                    email.setVisibility(view.GONE);
                    password.setVisibility(view.GONE);
                    password.setVisibility(view.GONE);
                    findViewById(R.id.signup_button_signup_activity).setVisibility(view.GONE);
                    progressBar.setVisibility(view.VISIBLE);

                    /**change the messeges in the right way*/
                    if(!isExist){
                        /**will add to all DB*/
                        signUpNewUserToDB();
                        mainMessege.setText("We sent you a verification E-mail, you have to verify it's before login");
                        mainMessege.setTextSize(22);
                    }else {
                        mainMessege.setText("The user is already exist, Login page will open");
                        mainMessege.setTextSize(22);
                    }

                    /**a timer that will help us let the progressBar to work before move to the next activity*/
                    progressbarTimer = new Timer();
                    TimerTask progressbarTimerTask = new TimerTask() {
                        @Override
                        public void run() {
                            count++;
                            progressBar.setProgress(count);
                            if(count == 70){
                                progressbarTimer.cancel();
                                /**open the specific activity*/
                                openNextActivity(isExist);
                            }
                        }
                    };

                    /**scheduling the timer for the progressBar*/
                    progressbarTimer.schedule(progressbarTimerTask,0,70);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) { }
            });
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }

    /**open the next activity*/
    private void openNextActivity(boolean isExist) {
        if (isExist) { startActivity(new Intent(this, LoginActivity.class)); }
        /**else -->*/
        startActivity(new Intent(this, MainUserActivity.class));
    }

    /**will add the user to FireBase DB*/
    private void signUpNewUserToDB(){
        dbAuthentication.createUserWithEmailAndPassword(this.newUser.getEmail(), this.newUser.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            /**signUp success, will update realTime DB ant firebase Authentication*/
                            FirebaseUser currentUser = dbAuthentication.getCurrentUser();
                            addToRealDB(currentUser.getUid());
                            /**will send verification email to the user from Firebase Authentication*/
                            currentUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                }
                            });

                        }else{
                            Toast.makeText(SignupActivity.this, "Incorrect email. please try again",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                });
    }

    /**added user details to the real DB */
    private void addToRealDB(String GoogleId){
        this.newUser.setGOOGLE_ID(GoogleId);
        dbRootRef.child(this.newUser.getGOOGLE_ID()).setValue(this.newUser);
    }
}