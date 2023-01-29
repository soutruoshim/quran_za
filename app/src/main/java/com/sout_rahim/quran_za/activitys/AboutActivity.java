package com.sout_rahim.quran_za.activitys;

import android.graphics.Typeface;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sout_rahim.quran_za.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView quran_text=(TextView)findViewById(R.id.quran_text1);
        Typeface tfkh_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/KhmerOSMoul.ttf");
        quran_text.setTypeface(tfkh_regular);

    }
}
