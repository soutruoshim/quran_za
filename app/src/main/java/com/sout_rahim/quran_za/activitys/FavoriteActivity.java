package com.sout_rahim.quran_za.activitys;

import android.content.Intent;
import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.adapters.AyahBookmarkAdapter;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListenerA;
import com.sout_rahim.quran_za.ultils.MyDatabase;

public class FavoriteActivity extends AppCompatActivity {
    private AyahBookmarkAdapter ayahBookmarkAdapter;
    private RecyclerView rvAyah;
    private LinearLayoutManager mLinearLayout;
    private MyDatabase myDatabase;
    private Cursor cursorViewAyah;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        rvAyah = (RecyclerView) findViewById(R.id.ayahRecyclerView);
       /* adapterAyah_full=new AyahAdapter_Full();*/

        ayahBookmarkAdapter =new AyahBookmarkAdapter();

        myDatabase=new MyDatabase(getApplicationContext());


        mLinearLayout=new LinearLayoutManager(this);
        rvAyah.setLayoutManager(mLinearLayout);
        viewAyah();

        //=================adpater click==============
        ayahBookmarkAdapter.setAdapterItemClickListener(new AdapterItemClickListener() {
            @Override
            public void onItemClick(String item) {
               deleteBookmarkItem(item);
            }
        });
        ayahBookmarkAdapter.setAdapterItemClickListenerA(new AdapterItemClickListenerA() {
            @Override
            public void onItemClick(String item, String item1) {
                Intent intent = new Intent(getApplicationContext(), SurahActivity.class);
                Log.d("BookmarkClick:","item:"+item+"item1"+item1);

                intent.putExtra("ID", item);
                intent.putExtra("AYAHID",item1);
                startActivity(intent);
            }

            @Override
            public void onActionClick(int item) {

            }
        });
    }
    private void viewAyah(){
        rvAyah.setAdapter(ayahBookmarkAdapter);

        try {
            String[] stringArg1={"%"};
            //cursorViewAyah=myDatabase.getReadableDatabase().rawQuery("SELECT * FROM Favorite ORDER  WHERE SuraID=?",stringArg1);
            cursorViewAyah=myDatabase.getReadableDatabase().rawQuery( "SELECT Favorite.id, Quran_Ayah.ID, Quran_Ayah.SuraID, Quran_Ayah.VerseID,Quran_Ayah.AyahText,Quran_Ayah.AyahTextKhmer,Quran_Ayah.SurahName FROM Quran_Ayah INNER JOIN Favorite ON Quran_Ayah.SuraID =Favorite.SuraID AND Quran_Ayah.VerseID =Favorite.VerseID WHERE Favorite.id LIKE ? ORDER BY Favorite.id DESC ", stringArg1);
            ayahBookmarkAdapter.setCursor(cursorViewAyah);
        }catch (Exception io){
            Log.d("Exeption","io"+io);
        }
    }
    private void deleteBookmarkItem(String id){
        try {
            String row = "DELETE FROM Favorite WHERE  id='" + id + "' ";
            myDatabase.getWritableDatabase().execSQL(row);
        }catch (Exception e){
            Log.d("Error:",e.getMessage());
        }
        viewAyah();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }
}
