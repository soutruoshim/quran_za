package com.sout_rahim.quran_za.activitys;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.adapters.Vocabulary_Adapter;
import com.sout_rahim.quran_za.ultils.CenteredImageSpan;
import com.sout_rahim.quran_za.ultils.DividerItemDecoration;
import com.sout_rahim.quran_za.ultils.MyDatabase;

public class VocabularyActivity extends AppCompatActivity {
    private Vocabulary_Adapter vocabulary_adapter;
    private MyDatabase myDatabase;
    private RecyclerView rvVocabulary;
    EditText searchText;
    Button btnClear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        rvVocabulary = (RecyclerView) findViewById(R.id.rvVocabulary);

        vocabulary_adapter=new Vocabulary_Adapter();
        myDatabase=new MyDatabase(getApplicationContext());

        rvVocabulary.setAdapter(vocabulary_adapter);
        rvVocabulary.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST);
        rvVocabulary.addItemDecoration(itemDecoration);
        searchText=(EditText) findViewById(R.id.searchText);

        SpannableStringBuilder builder = new SpannableStringBuilder();
        ImageSpan imageSpan = new CenteredImageSpan(getApplicationContext(), R.drawable.ic_search);
        builder.append("\t");
        builder.setSpan(imageSpan, builder.length() - 1, builder.length(), 1);
        builder.append(getApplicationContext().getString(R.string.hintSearch));
        searchText.setHint(builder);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchText(s.toString());
                setIconClear();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchText("");
        btnClear=(Button)findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchText.setText("");
            }
        });
    }
    private void searchText(String word)
    {
        try {
            String[] stringArg={"%"+word+"%"};
            Cursor cursor=myDatabase.getReadableDatabase().rawQuery("SELECT * FROM vocabulary WHERE word LIKE ?",stringArg);
            vocabulary_adapter.setCursor(cursor);
        }catch (Exception io){
            Log.d("Exeption","io"+io);
        }
    }
    private void setIconClear() {
        String check = searchText.getText().toString();
        if (check.equalsIgnoreCase("")) {
            btnClear.setVisibility(View.GONE);
            searchText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        } else {

            searchText.setCompoundDrawablesWithIntrinsicBounds(0, 0, (R.drawable.ic_close), 0);
            btnClear.setVisibility(View.VISIBLE);
        }
    }
}

