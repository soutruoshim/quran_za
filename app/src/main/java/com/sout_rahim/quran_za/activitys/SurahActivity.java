package com.sout_rahim.quran_za.activitys;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.adapters.AyahAdapter_Full;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListenerA;
import com.sout_rahim.quran_za.ultils.Ayah;
import com.sout_rahim.quran_za.ultils.DividerItemDecoration;
import com.sout_rahim.quran_za.ultils.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class SurahActivity extends AppCompatActivity {

    private AyahAdapter_Full adapterAyah_full2;
    private RecyclerView rvAyah;
    private LinearLayoutManager mLinearLayout;
    private MyDatabase myDatabase;
    private Spinner spinner;
    private TextView surahName1;
    private List<Ayah> ayahList;
    public static final String key_fontS="KEY_FONT";
    private Toolbar toolbar;
    private Cursor cursorViewAyah;
    private int positionClicked;
    LinearLayout bookmarkBotton;
    ImageView imageBookmark;
    TextView bookmarktext;
    private boolean checkedFavorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surah);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

        rvAyah = (RecyclerView) findViewById(R.id.ayahRecyclerView);
       /* adapterAyah_full=new AyahAdapter_Full();*/

        adapterAyah_full2=new AyahAdapter_Full();
        int font_size=getPreferences(getApplicationContext(), key_fontS);
        adapterAyah_full2.setFont(font_size);
        ayahList=new ArrayList<>();

        myDatabase=new MyDatabase(getApplicationContext());


        mLinearLayout=new LinearLayoutManager(this);
        rvAyah.setLayoutManager(mLinearLayout);

        surahName1=(TextView)findViewById(R.id.surahName1);
        Typeface tfkh_regular = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/tradoB.ttf");
        surahName1.setTypeface(tfkh_regular);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvAyah.addItemDecoration(itemDecoration);

        spinner=(Spinner)findViewById(R.id.spinner) ;
        String id=getIntent().getStringExtra("ID");


        viewAyah(id);
       if(isFromFavorite()){
           Log.d("gotoayah","worked");
           String ayahID=getIntent().getStringExtra("AYAHID");
           mLinearLayout.scrollToPositionWithOffset(Integer.parseInt(ayahID),0);
       }

       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(position!=0) {
                   String item = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                   Toast.makeText(getApplicationContext(), item, Toast.LENGTH_LONG).show();

                   int itemScroll = Integer.parseInt(item);
               /*mLinearLayout.scrollToPosition(itemScroll);*/
                   mLinearLayout.scrollToPositionWithOffset(itemScroll, 0);
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

        //-------------set background to spinner--------------------
      if (Build.VERSION.SDK_INT < 23) {

           spinner.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ln_style2));
        }
        //--------------bottom sheet action click--------------

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        LinearLayout copyBotton = (LinearLayout) bottomSheetDialog.findViewById(R.id.copy_Botton);
        bookmarkBotton = (LinearLayout) bottomSheetDialog.findViewById(R.id.bookmark_Botton);
        imageBookmark=(ImageView)bottomSheetDialog.findViewById(R.id.imgbookmark);
        bookmarktext =(TextView)bottomSheetDialog.findViewById(R.id.bookmarktext);

        LinearLayout sendBotton = (LinearLayout) bottomSheetDialog.findViewById(R.id.send_Botton);
        LinearLayout cancelButton=(LinearLayout)bottomSheetDialog.findViewById(R.id.cancel_Botton);

        copyBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CopyClick","positionClicked:"+positionClicked);
                Ayah ayahObject=ayahList.get(positionClicked);
                if(ayahObject.getSuraID().equals("1")){
                    cursorViewAyah.moveToPosition(positionClicked);
                }else{
                    cursorViewAyah.moveToPosition(positionClicked-1);
                }

                String ayah=ayahObject.getAyahText();
                String translate=ayahObject.getAyahTextKhmer();
                String surahName1="سورة "+cursorViewAyah.getString(6);
                String ayahNumbers=ayahObject.getAyahID();

                setClipboard(getApplicationContext(),"﴿"+ayah+"﴾\t["+surahName1+"("+ayahNumbers+")]\n"+translate);
                bottomSheetDialog.hide();
            }
        });
        bookmarkBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ayah ayahObject=ayahList.get(positionClicked);


                String surahID=ayahObject.getSuraID();
                String ayahNumber=ayahObject.getAyahID();

                if(checkedFavorite){
                   deleteAyahFavorite(surahID,ayahNumber);
                }
                else {
                   insertAyahFavorite(surahID,ayahNumber);
                }
                bottomSheetDialog.hide();
            }
        });
        sendBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Ayah ayahObject=ayahList.get(positionClicked);
                if(ayahObject.getSuraID().equals("1")){
                    cursorViewAyah.moveToPosition(positionClicked);
                }else{
                    cursorViewAyah.moveToPosition(positionClicked-1);
                }
                String ayah=ayahObject.getAyahText();
                String translate=ayahObject.getAyahTextKhmer();
                String surahName1="سورة "+cursorViewAyah.getString(6);
                String ayahNumbers=ayahObject.getAyahID();

                shareText("﴿"+ayah+"﴾\t["+surahName1+"("+ayahNumbers+")]\n"+translate);
                bottomSheetDialog.hide();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        BottomSheetBehavior behavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    Toast.makeText(getApplicationContext(),"hide:",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        adapterAyah_full2.setAdapterItemClickListenerA(new AdapterItemClickListenerA() {
            @Override
            public void onItemClick(String item, String item1) {

            }

            @Override
            public void onActionClick(int item) {
                positionClicked=item;
                Ayah ayahObject=ayahList.get(positionClicked);

                String[] stringArg1={ayahObject.getSuraID(),ayahObject.getAyahID()};
                Cursor cursor=myDatabase.getReadableDatabase().rawQuery("SELECT * FROM Favorite WHERE SuraID=? AND VerseID=?",stringArg1);

                if(!isCursorEmpty(cursor)){
                    bookmarktext.setText(getResources().getString(R.string.remove_bookmark));
                    imageBookmark.setImageResource(R.drawable.ic_bottom_sheet_favorited);
                    checkedFavorite=true;
                }
                else {
                    bookmarktext.setText(getResources().getString(R.string.add_bookmark));
                    imageBookmark.setImageResource(R.drawable.ic_bottom_sheet_favorite_border);
                    checkedFavorite=false;
                }
                bottomSheetDialog.show();
            }
        });
    }

    public boolean isCursorEmpty(Cursor cursor){
        if(!cursor.moveToFirst() || cursor.getCount() == 0) return true;
        return false;
    }

    private boolean isFromFavorite() {
        return getIntent().hasExtra("AYAHID");
    }

    private void insertAyahFavorite(String surahId,String verseId)
    {
        String row = "INSERT INTO Favorite (SuraID,VerseID) Values ('"+surahId+"', '"+verseId+"')";
        try {
            myDatabase.getWritableDatabase().execSQL(row);
            myDatabase.close();
            Log.d("Favorite:","Success!");
        }catch (Exception e){
            Log.d("Error:",e.getMessage());
        }
    }
    private void deleteAyahFavorite(String surahId,String verseId)
    {
        try {
            String row = "DELETE FROM Favorite WHERE  SuraID='" + surahId + "' AND VerseID='" + verseId + "' ";
            myDatabase.getWritableDatabase().execSQL(row);
        }catch (Exception e){
            Log.d("Error:",e.getMessage());
        }
    }

    private void shareText(String text){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
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

    private void viewAyah(String id){
        rvAyah.setAdapter(adapterAyah_full2);

        try {
            String[] stringArg1={id};
            cursorViewAyah=myDatabase.getReadableDatabase().rawQuery("SELECT * FROM Quran_Ayah WHERE SuraID=?",stringArg1);
            setAyahNumToSpaner(cursorViewAyah);

            if(cursorViewAyah.moveToFirst()) {
                surahName1.setText("سورة " + cursorViewAyah.getString(6));

                if((cursorViewAyah.getString(1).equalsIgnoreCase("1")) || cursorViewAyah.getString(1).equalsIgnoreCase("9")){
                }else {ayahList.add(new Ayah("0", "0", "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ", "ក្នុងនាមអល់ឡោះ មហាសប្បុរស មហាអាណិតស្រលាញ់", "0"));}
                do {
                    ayahList.add(new Ayah(cursorViewAyah.getString(1),cursorViewAyah.getString(2),cursorViewAyah.getString(3),cursorViewAyah.getString(4),cursorViewAyah.getString(5)));
                    Log.d("cursorspinner",cursorViewAyah.getString(2));
                } while (cursorViewAyah.moveToNext());
                adapterAyah_full2.setAyahList(ayahList);
            }
        }catch (Exception io){
            Log.d("Exeption","io"+io);
        }
        mLinearLayout.scrollToPosition(0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_surah, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id== android.R.id.home) {
            onBackPressed();
            return true;
        }if(id==R.id.action_zoom_in){
           int font_size= getPreferences(getApplicationContext(), key_fontS);
            if(font_size<=28) {
                font_size += 2;
            }
            savePreferencesForReasonCode(getApplicationContext(),
                    key_fontS, font_size);
            adapterAyah_full2.setFont(font_size);


        }if(id==R.id.action_zoom_out){
            int font_size= getPreferences(getApplicationContext(), key_fontS);
            if(font_size>=16) {
                font_size -= 2;
            }
            savePreferencesForReasonCode(getApplicationContext(),
                    key_fontS, font_size);
            adapterAyah_full2.setFont(font_size);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
    }
  private List<String> getAyahN(Cursor cursor)
  {
      List<String> labels = new ArrayList<>();

      if (cursor.moveToFirst()) {
          labels.add("آية");
          do {
              labels.add(cursor.getString(2));
              Log.d("cursorspinner",cursor.getString(2));
          } while (cursor.moveToNext());
      }
      return labels;
  }
    private void setAyahNumToSpaner(Cursor cursor){
        List<String> labels = new ArrayList<>();
        labels=getAyahN(cursor);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                R.layout.spinner, labels);
        dataAdapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(dataAdapter);
    }
    public void savePreferencesForReasonCode(Context context,
                                             String key, int value) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
    public int getPreferences(Context context, String prefKey) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPreferences.getInt(prefKey, 22);
    }
}
