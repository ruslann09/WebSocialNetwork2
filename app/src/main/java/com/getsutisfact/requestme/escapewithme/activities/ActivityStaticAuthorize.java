package com.getsutisfact.requestme.escapewithme.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getsutisfact.requestme.escapewithme.R;

public class ActivityStaticAuthorize extends AppCompatActivity {

    private ImageView selectBtn, male, female;
    private TextView title;

    private SharedPreferences sharedPreferences;

    private int state, step;

    private LinearLayout policy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_static);

        policy = (LinearLayout) findViewById(R.id.privacy);

        policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri policy = Uri.parse("");
                Intent policy_link = new Intent (Intent.ACTION_VIEW, policy);
                startActivity (policy_link);
            }
        });

        sharedPreferences = getSharedPreferences(getString(R.string.app_shared_preferences), MODE_PRIVATE);

//        if (sharedPreferences.getBoolean("isFirstLogging", false)) {
//            startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            finish();
//        }

        selectBtn = (ImageView) findViewById(R.id.select_btn);
        male = (ImageView) findViewById(R.id.male);
        female = (ImageView) findViewById(R.id.female);
        title = (TextView) findViewById(R.id.title);

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 1;

                male.setImageDrawable(getResources().getDrawable(R.drawable.selected));
                female.setImageDrawable(getResources().getDrawable(R.drawable.unselected));
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                state = 2;

                male.setImageDrawable(getResources().getDrawable(R.drawable.unselected));
                female.setImageDrawable(getResources().getDrawable(R.drawable.selected));
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (state >= 1 && step == 0) {
                    title.setText(R.string.who_are_you_looking_for);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("sex", state);
                    editor.apply();

                    male.setImageDrawable(getResources().getDrawable(R.drawable.unselected));
                    female.setImageDrawable(getResources().getDrawable(R.drawable.unselected));

                    state = 0;

                    step++;
                } else if (state >= 1 && step == 1) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("sexSearch", state);

                    editor.putBoolean("isFirstLogging", true);
                    editor.apply();

                    startActivity(new Intent(getApplicationContext(), ActivityIsAdult.class));
                    finish();
                }
            }
        });
    }

    private static final int TIME_INTERVAL = 859; // # milliseconds, desired time passed between two back presses.
    private long mBackPressed;

    @Override
    public void onBackPressed()
    {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis())
        {
            super.onBackPressed();
            return;
        }
        else { Toast.makeText(getBaseContext(), "Tap back button in order to exit", Toast.LENGTH_SHORT).show(); }

        mBackPressed = System.currentTimeMillis();
    }
}
