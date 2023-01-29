package com.sout_rahim.quran_za.activitys;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.adapters.AyahAdapter;
import com.sout_rahim.quran_za.ultils.CenteredImageSpan;
import com.sout_rahim.quran_za.ultils.DividerItemDecoration;
import com.sout_rahim.quran_za.ultils.MyDatabase;

public class SearchResultsActivity extends Activity {

    private SearchView searchView;
    private MyDatabase myDatabase;
    private AyahAdapter adapterAyah;
    private RecyclerView rvAyah;
    private LinearLayoutManager mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        super.onResume();
        //===================RecyclerView==================

        rvAyah = (RecyclerView) findViewById(R.id.ayahRecyclerView);
        adapterAyah=new AyahAdapter();

        myDatabase=new MyDatabase(getApplicationContext());
        mLinearLayout=new LinearLayoutManager(this);
        rvAyah.setLayoutManager(mLinearLayout);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvAyah.addItemDecoration(itemDecoration);

        Button btnBack= (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(SearchResultsActivity.this);

            }
        });
        searchView=(SearchView)findViewById(R.id.search_book);

        searchView.setIconifiedByDefault(false);
        ImageView magImage = (ImageView) searchView.findViewById(androidx.appcompat.R.id.search_button);
        magImage.setVisibility(View.GONE);
        magImage.setImageDrawable(null);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        /*builder.append(" ");
        builder.setSpan(new ImageSpan(getApplicationContext(), R.drawable.ic_search),
                builder.length() - 1, builder.length(), 0);*/
        //ImageSpan imageSpan = new CenteredImageSpan(getApplicationContext(), R.drawable.ic_search);
        //builder.append("\t");
        //builder.setSpan(imageSpan, builder.length()-1, builder.length(), 1);
        builder.append(getApplicationContext().getString(R.string.hintSearchA));
        searchView.setQueryHint(builder);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                seachAyah(query.toString());
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("TextQuery :",newText);
                seachAyah(newText.toString());
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    private void seachAyah(String word){
        rvAyah.setAdapter(adapterAyah);
        Cursor cursor1=null;
        String[] stringArg1={"%"+word+"%"};
        try {
            cursor1 =myDatabase.getReadableDatabase().rawQuery("SELECT * FROM Quran_Ayah WHERE AyahNormal LIKE ?",stringArg1);
            if(!isCursorEmpty(cursor1)) {
                adapterAyah.setCursorandQuery(cursor1,word);
            }else {
                try {
                    cursor1 =myDatabase.getReadableDatabase().rawQuery("SELECT * FROM Quran_Ayah WHERE AyahTextKhmer LIKE ?",stringArg1);
                    adapterAyah.setCursorandQuery(cursor1,word);
                }catch (Exception io){
                    Log.d("Exeption","io"+io);
                }
            }
        }catch (Exception io){
            Log.d("Exeption","io"+io);
        }
        mLinearLayout.scrollToPosition(0);

    }
    public boolean isCursorEmpty(Cursor cursor){
        if(!cursor.moveToFirst() || cursor.getCount() == 0) return true;
        return false;
    }
}
