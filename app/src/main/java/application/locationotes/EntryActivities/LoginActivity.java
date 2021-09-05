package application.locationotes.EntryActivities;
/**
 * This activity is logIn activity
 * @author Tzion Beniaminov
 */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.Timer;
import java.util.TimerTask;

import application.locationotes.DataObjects.Personal.LoginData;
import application.locationotes.NotesActivities.General.MainUserActivity;
import application.locationotes.R;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private TextView mainMessege;
    private LoginData loginDataFromInput;

    /**for the progressBar timer*/
    private Timer progressbarTimer;

    private int count=0;
    private ProgressBar progressBar;

    private FirebaseAuth dbAuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**will lead the user to signUp page*/
        findViewById(R.id.go_to_signUp_button_login_activity).setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
        });

        /**will lead to create a new password page*/
        findViewById(R.id.forget_password_button_login_activity).setOnClickListener(view -> {
            startActivity(new Intent(this, ForgetPassword.class));
        });

        email = (EditText) findViewById(R.id.email_login_activity);
        password = (EditText) findViewById(R.id.password_login_activity);
        progressBar =  (ProgressBar) findViewById(R.id.progressBar_login_activity);
        mainMessege =  (TextView) findViewById(R.id.login_welcome_login_activity);

        /**DB connection*/
        dbAuthentication = FirebaseAuth.getInstance();

        /**will lead to main user page if it's correct*/
        findViewById(R.id.signin_button_login_activity).setOnClickListener(view -> {
            String emailIn = email.getText().toString().trim();
            String passwordIn = password.getText().toString().trim();

            if(TextUtils.isEmpty(emailIn) || TextUtils.isEmpty(passwordIn) || passwordIn.length() != 8){
                if(TextUtils.isEmpty(emailIn)){email.setError("required");}
                if(TextUtils.isEmpty(passwordIn)){password.setError("required");}
                if(password.length() != 8) {password.setError("required 8 signs");}
                return;
            }

            loginDataFromInput = new LoginData(emailIn,passwordIn);

            email.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            findViewById(R.id.forget_password_button_login_activity).setVisibility(View.GONE);
            findViewById(R.id.signin_button_login_activity).setVisibility(View.GONE);
            findViewById(R.id.go_to_signUp_button_login_activity).setVisibility(View.GONE);
            findViewById(R.id.sign_In_login_activity).setVisibility(View.GONE);
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
                        signInToDB();
                    }
                }
            };

            /**scheduling the timer for the progressBar*/
            progressbarTimer.schedule(progressbarTimerTask,0,70);
        });
    }

    /**sign in with firebase Authentication*/
    public void signInToDB(){
        /**checking if the user is current. if sign In is succeed so yes*/
        dbAuthentication.signInWithEmailAndPassword(loginDataFromInput.getEmail(), loginDataFromInput.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser currentUser = dbAuthentication.getCurrentUser();
                            if(currentUser.isEmailVerified()) {
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainUserActivity.class)
                                        .putExtra("userGoogleId", currentUser.getUid()));
                            }
                            else{
                                runOnUiThread(() ->Toast.makeText(LoginActivity.this,"E-mail was not verified ",Toast.LENGTH_LONG).show());
                                dbAuthentication.signOut();
                                finish();
                                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            }
                        } else {
                            runOnUiThread(() ->Toast.makeText(LoginActivity.this,"Not Valid User Or Password, please try again",Toast.LENGTH_LONG).show());
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class));
    }
}