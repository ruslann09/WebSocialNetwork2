package com.thecheckoutme.theyoufine.thebestplace.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.thecheckoutme.theyoufine.thebestplace.R;

public class ActivityAge extends AppCompatActivity {

    EditText editText;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        editText = (EditText) findViewById(R.id.name_edit_text);
        textView = (TextView) findViewById(R.id.textView2);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText != null && !editText.getText().toString().isEmpty()) {
                    startActivity(new Intent(getApplicationContext(), ActivityStaticAuthorize.class));
                }
            }
        });

    }
}
