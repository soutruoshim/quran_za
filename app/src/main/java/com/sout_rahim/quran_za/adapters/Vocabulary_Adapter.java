package com.sout_rahim.quran_za.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sout_rahim.quran_za.R;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class Vocabulary_Adapter extends RecyclerView.Adapter<Vocabulary_Adapter.ViewHolder> {

    public Cursor cursor;
    public Vocabulary_Adapter(){

    }
    public Vocabulary_Adapter(Cursor cursor){
       this.cursor=cursor;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ayahView = inflater.inflate(R.layout.item_vocabulary, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ayahView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        /*Log.d("Database","Position"+position);*/
        cursor.moveToPosition(position);
        String word1=cursor.getString(1);
        String represent1=cursor.getString(2);

        // Set item views based on your views and data model
        TextView word = holder.word;
        word.setText(word1);
        TextView represent = holder.represent;
        represent.setText(represent1);


    }

    @Override
    public int getItemCount() {
        if(cursor==null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView word;
        public TextView represent;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            word=(TextView)itemView.findViewById(R.id.word);
            represent=(TextView)itemView.findViewById(R.id.represent);

            Typeface tfkh_bold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/KhmerOSb.ttf");
            Typeface tfkh_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/KhmerOS.ttf");
            this.word.setTypeface(tfkh_bold);
            this.represent.setTypeface(tfkh_regular);


        }

       /* @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d("Database","onClick"+position);
            // gets item position
            cursor.moveToPosition(position);
            String ayah=cursor.getString(3);
            String translate=cursor.getString(4);
            String surahName=cursor.getString(1);
            String fullSurahName="سورة "+surahName;
            String ayahNumber=cursor.getString(2);
            String fullAyahNumber="آية "+ayahNumber;

            adapterItemClickListener.onItemClick(ayah,translate,fullSurahName,fullAyahNumber);

        }*/
    }
}
