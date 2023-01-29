package com.sout_rahim.quran_za.activitys;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.adapters.SurahAdapter;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.ultils.CenteredImageSpan;
import com.sout_rahim.quran_za.ultils.DividerItemDecoration;
import com.sout_rahim.quran_za.ultils.MyDatabase;

import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MyDatabase myDatabase;
    private SurahAdapter adapter;

    private LinearLayoutManager mLinearLayout;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;
    private RecyclerView rvSurah;
    private EditText seachSurah;
    Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set Language to app
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String language = sharedPrefs.getString("language", "en");
        setLanguage(language);


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        rvSurah = (RecyclerView) findViewById(R.id.surahRecyclerView);
        adapter = new SurahAdapter();
        rvSurah.setAdapter(adapter);

        adapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void onItemClick(String item) {
                Intent intent = new Intent(MainActivity.this, SurahActivity.class);
                intent.putExtra("ID", item);
                startActivity(intent);
            }
        });

        myDatabase = new MyDatabase(getApplicationContext());


        // Set layout manager to position the items
        mLinearLayout = new LinearLayoutManager(this);
        rvSurah.setLayoutManager(new LinearLayoutManager(this));

        // Set Decoration
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvSurah.addItemDecoration(itemDecoration);

        seachSurah = (EditText) findViewById(R.id.searchSurah);
        SpannableStringBuilder builder = new SpannableStringBuilder();
        ImageSpan imageSpan = new CenteredImageSpan(getApplicationContext(), R.drawable.ic_search);
        builder.append("\t");
        builder.setSpan(imageSpan, builder.length() - 1, builder.length(), 1);
        builder.append(getApplicationContext().getString(R.string.hintSearchS));
        seachSurah.setHint(builder);

        seachSurah.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setIconClear();
                seachSurah(s.toString());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        seachSurah("");

        btnClear = (Button) findViewById(R.id.btnClear);
        setIconClear();
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seachSurah.setText("");
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        /*drawer.setScrimColor(getResources().getColor(android.R.color.transparent));*/
        /*drawer.setScrimColor(Color.TRANSPARENT);*/
        drawer.setDrawerElevation(0);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        TextView quran_text = (TextView) header.findViewById(R.id.quran_text);
        Typeface tfkh_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/KhmerOSMoul.ttf");
        quran_text.setTypeface(tfkh_regular);


    }

    private void setIconClear() {
        String check = seachSurah.getText().toString();
        if (check.equalsIgnoreCase("")) {
            btnClear.setVisibility(View.GONE);
            seachSurah.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {
            seachSurah.setCompoundDrawablesWithIntrinsicBounds(0, 0, (R.drawable.ic_close), 0);
            btnClear.setVisibility(View.VISIBLE);
        }
    }

    private void seachSurah(String word) {
        try {
            String[] sub={"سورة ","سورة"};
            if(word.startsWith(sub[0])) {
                    word = word.substring(sub[0].length(), word.length());
            }else if(word.startsWith(sub[1])){
                word = word.substring(sub[1].length(), word.length());
            }

            String[] stringArg = {word, "%" + word + "%"};
            Cursor cursor = myDatabase.getReadableDatabase().rawQuery("SELECT * FROM QuranSura WHERE id=? OR name LIKE ?", stringArg);
            adapter.setCursor(cursor);
        } catch (Exception io) {
            Log.d("Exeption", "io" + io);
        }
    }

    public boolean isCursorEmpty(Cursor cursor) {
        if (!cursor.moveToFirst() || cursor.getCount() == 0) return true;
        return false;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_search) {
            startActivity(new Intent(getApplicationContext(), SearchResultsActivity.class));
        }else if(id==R.id.action_more){
              moreApp();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if(id==R.id.bookmarks){
            startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
        }else if (id==R.id.vocabulary){
            startActivity(new Intent(getApplicationContext(), VocabularyActivity.class));
        }else if (id == R.id.settings) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
        } else if (id == R.id.helps) {
            sendFeedBack();
        } else if (id == R.id.about) {
            startActivity(new Intent(getApplicationContext(), AboutActivity.class));
            return true;
        } else if (id == R.id.rateapp) {
            rateApp();
        } else if (id == R.id.shareapp) {
            shareApp();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setLanguage(String language) {
        Locale locale2 = new Locale(language);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = rvSurah.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            rvSurah.getLayoutManager().onRestoreInstanceState(listState);
        }
    }


    private void rateApp(){
        try {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(goToMarket);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getApplicationContext().getPackageName())));
        }
    }
    private void shareApp(){
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, "Share this app");
        share.putExtra(Intent.EXTRA_TEXT,"http://play.google.com/store/apps/details?id="+getApplicationContext().getPackageName());

        startActivity(Intent.createChooser(share, "Share link Download!"));
    }

    private void moreApp(){
        try {
            Intent i = new Intent(android.content.Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://play.google.com/store/apps/developer?id=Srh%20Dp&hl=en"));
            startActivity(i);
        } catch (Exception e) {

        }
    }
    private void sendFeedBack(){

        final Dialog dialog = new Dialog(MainActivity.this);
        // Include dialog.xml file
        dialog.setContentView(R.layout.dialog_feedback);
        // Set dialog title
        dialog.setTitle("Feedback");

        // set values for custom dialog components - text, image and button


        dialog.show();
        Button btnFeedback = (Button) dialog.findViewById(R.id.btnsendFeedback);
        final RadioButton radio_sms=(RadioButton)dialog.findViewById(R.id.radio_sms);
        final EditText commandbox=(EditText)dialog.findViewById(R.id.commandbox);
        // if decline button is clicked, close the custom dialog
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if(radio_sms.isChecked()) {
                    Log.i("Send SMS", "");
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW);

                    smsIntent.setData(Uri.parse("smsto:"));
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.putExtra("address", new String("+85598545423"));
                    smsIntent.putExtra("sms_body", commandbox.getText().toString());

                    try {
                        startActivity(smsIntent);
                        Log.i("Finished sending SMS...", "");
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(),
                                "SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","sout.rahim@gmail.com", null));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, commandbox.getText().toString());
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
                dialog.dismiss();
            }
        });
    }

}
