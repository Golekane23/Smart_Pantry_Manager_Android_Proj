package com.example.smartpantry;

import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle b){super.onCreate(b);setContentView(R.layout.activity_settings);
        DatabaseHelper db=new DatabaseHelper(this);CheckBox c=findViewById(R.id.chkExpiry);c.setChecked(db.getExpiryPreference());
        c.setOnCheckedChangeListener((button,checked)->db.setExpiryPreference(checked));
    }
}
