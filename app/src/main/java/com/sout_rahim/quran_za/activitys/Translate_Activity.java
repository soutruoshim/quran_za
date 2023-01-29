package com.sout_rahim.quran_za.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.ultils.MyDatabase;

import java.util.Locale;

public class Translate_Activity extends AppCompatActivity {
     private TextView ayahText;
    private TextView translateText;
    private TextView ayahNumber;
    private TextView surahName;

    private MyDatabase myDatabase2;

    String ayah,translate,surahName1,ayahName1;

    private String surahNumber,ayahNumbers;
    private String ID;
    private MenuItem menuItemBookmark;
    private boolean checkedFavorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String language=sharedPrefs.getString("language", "en");
        setLanguage(language);

        setContentView(R.layout.activity_translate_);

        ayahText=(TextView) findViewById(R.id.ayahText1);
        translateText=(TextView) findViewById(R.id.translateText1);
        ayahNumber=(TextView) findViewById(R.id.ayahNumber1);
        surahName=(TextView) findViewById(R.id.surahName1);
        /*menuItemBookmark=(MenuItem) findViewById(R.id.action_bookmark);*/

        Typeface tf_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/UthmanicHafs.otf");
        Typeface tfkh_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/KhmerOS.ttf");
        this.ayahText.setTypeface(tf_regular);
        this.translateText.setTypeface(tfkh_regular);



        ayah=getIntent().getStringExtra("ayah");
        translate=getIntent().getStringExtra("translate");
        surahName1=getIntent().getStringExtra("surahName");
        ayahName1=getIntent().getStringExtra("ayahName");

        surahNumber=getIntent().getStringExtra("surahNumber");
        ayahNumbers=getIntent().getStringExtra("ayahNumber");
        ID=getIntent().getStringExtra("ID");

        ayahText.setText(ayah);
        translateText.setText(translate);
        surahName.setText(surahName1);
        ayahNumber.setText(ayahName1);

        myDatabase2=new MyDatabase(getApplicationContext());

       //Button Next and Pre
        Button btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] argString = {"" + ID};

                Cursor cursor = myDatabase2.getReadableDatabase().rawQuery("SELECT * FROM Quran_Ayah LIMIT 1 OFFSET ?", argString);
                if (cursor.moveToFirst()) {/*  Log.d("Database","BtnNext"+ID);*/
                    ayah=cursor.getString(3);
                    translate=cursor.getString(4);
                    surahName1="سورة "+cursor.getString(6);

                    surahNumber=cursor.getString(1);
                    ayahNumbers=cursor.getString(2);
                    ayahName1="آية "+ayahNumbers;

                    ID=cursor.getString(0);

                    ayahText.setText(ayah);
                    translateText.setText(translate);
                    surahName.setText(surahName1);
                    ayahNumber.setText(ayahName1);
                    cursor.close();
                }
                loadIconFavorite(surahNumber,ayahNumbers);
            }
        });
        Button btnPre=(Button)findViewById(R.id.btnPre);
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] argString = {"" + ID};
                /*Log.d("Database","BtnPre"+ID);*/
                Cursor cursor = myDatabase2.getReadableDatabase().rawQuery("SELECT * FROM Quran_Ayah  WHERE ID<? order by ID desc limit 1", argString);
                if (cursor.moveToFirst()) {
                    ayah=cursor.getString(3);
                    translate=cursor.getString(4);
                    surahName1="سورة "+cursor.getString(6);

                    surahNumber=cursor.getString(1);
                    ayahNumbers=cursor.getString(2);
                    ayahName1="آية "+ayahNumbers;

                    ID=cursor.getString(0);

                    ayahText.setText(ayah);
                    translateText.setText(translate);
                    surahName.setText(surahName1);
                    ayahNumber.setText(ayahName1);
                    cursor.close();
                }
                loadIconFavorite(surahNumber,ayahNumbers);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_translate, menu);
        menuItemBookmark=menu.findItem(R.id.action_bookmark);
        loadIconFavorite(surahNumber,ayahNumbers);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id== R.id.action_bookmark){
            insertDataOrRemove();
        }else if (id == R.id.action_send) {
            shareText();
            return true;
        }else if(id==R.id.action_copy) {
             /*setClipboard(getApplicationContext(),"﴿"+ayah+"﴾["+surahName1+"("+ayahNumbers+")]");*/
            setClipboard(getApplicationContext(),"﴿"+ayah+"﴾\t["+surahName1+"("+ayahNumbers+")]\n"+translate);
        }else if (id== android.R.id.home) {
                Intent intent = NavUtils.getParentActivityIntent(this);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                NavUtils.navigateUpTo(this, intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void shareText(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "﴿"+ayah+"﴾\t["+surahName1+"("+ayahNumbers+")]\n"+translate);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
    }
    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
        Toast.makeText(context, "Ayah Copied to Clipboard!", Toast.LENGTH_LONG).show();
    }
    private void setLanguage(String language){
        Locale locale2 = new Locale(language);
        Locale.setDefault(locale2);
        Configuration config2 = new Configuration();
        config2.locale = locale2;
        getBaseContext().getResources().updateConfiguration(config2, getBaseContext().getResources().getDisplayMetrics());
    }


    private void loadIconFavorite(String surahID,String verseID)
    {
        String[] stringArg1={surahID,verseID};
        try {
            Cursor cursor=myDatabase2.getReadableDatabase().rawQuery("SELECT * FROM Favorite WHERE SuraID=? AND VerseID=?",stringArg1);
            if (!isCursorEmpty(cursor)) {
                Log.d("loadingfavorite","worded");
                menuItemBookmark.setIcon(getResources().getDrawable(R.drawable.ic_favorite));
                checkedFavorite=true;
            } else {
                menuItemBookmark.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border));
                checkedFavorite=false;
            }
            cursor.close();
        }catch (Exception e)
        {
             e.printStackTrace();
        }
    }
    public boolean isCursorEmpty(Cursor cursor){
        if(!cursor.moveToFirst() || cursor.getCount() == 0) return true;
        return false;
    }
    private void insertDataOrRemove()
    {
        if(checkedFavorite){
            deleteAyahFavorite(surahNumber,ayahNumbers);
            loadIconFavorite(surahNumber,ayahNumbers);
            Log.d("checkedFavorite:","true");
        }
        else {
            insertAyahFavorite(surahNumber,ayahNumbers);
            loadIconFavorite(surahNumber,ayahNumbers);
            Log.d("checkedFavorite:","false");
        }
    }
    private void insertAyahFavorite(String surahId,String verseId)
    {
        String row = "INSERT INTO Favorite (SuraID,VerseID) Values ('"+surahId+"', '"+verseId+"')";
        try {
            myDatabase2.getWritableDatabase().execSQL(row);
            myDatabase2.close();

        }catch (Exception e){
            Log.d("Error:",e.getMessage());
        }
    }
    private void deleteAyahFavorite(String surahId,String verseId)
    {
        try {
            String row = "DELETE FROM Favorite WHERE  SuraID='" + surahId + "' AND VerseID='" + verseId + "' ";
            myDatabase2.getWritableDatabase().execSQL(row);
            myDatabase2.close();
        }catch (Exception e){
            Log.d("Error:",e.getMessage());
        }
    }
}
