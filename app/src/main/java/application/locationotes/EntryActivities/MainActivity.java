package application.locationotes.EntryActivities;

/**
 * This is the main activity, will start that up
 * at the first time
 * @author Tzion Beniaminov
 */

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import application.locationotes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*will lead the user to login page*/
        findViewById(R.id.login_main_activity_button).setOnClickListener(view -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        /*will lead the user to signup page*/
        findViewById(R.id.signup_main_activity_butten).setOnClickListener(view -> {
            startActivity(new Intent(this, SignupActivity.class));
        });
    }
}