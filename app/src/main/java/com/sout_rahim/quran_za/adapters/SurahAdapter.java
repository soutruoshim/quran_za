package com.sout_rahim.quran_za.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.R;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class SurahAdapter extends RecyclerView.Adapter<SurahAdapter.ViewHolder> {

    public Cursor cursor;
    AdapterItemClickListener adapterItemClickListener;
    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener){
        this.adapterItemClickListener=adapterItemClickListener;
    }
    public SurahAdapter(){

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
        View surahView = inflater.inflate(R.layout.item_surah, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(surahView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        //Log.d("Database","Position"+position);
        cursor.moveToPosition(position);
        String surah=cursor.getString(1);
        String fullSurah=" سورة "+surah;
        String surahID=cursor.getString(0);
        String surahType=cursor.getString(2);
        String ayahN="الآيات "+cursor.getString(3);

        // Set item views based on your views and data model




        TextView surahText1 = holder.surahText;
        TextView surahID1 = holder.surahID;
        TextView surahType1 = holder.type;
        TextView ayahN1 = holder.ayahN;

        surahText1.setText(fullSurah);
        surahID1.setText(surahID);
        surahType1.setText(surahType);
        ayahN1.setText(ayahN);

    }

    @Override
    public int getItemCount() {
        if(cursor==null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView surahID;
        public TextView surahText;
        public TextView ayahN;
        public TextView type;

        public ViewHolder(View itemView,Context context) {
            super(itemView);
            this.context=context;
            surahID=(TextView)itemView.findViewById(R.id.surahID);
            surahText=(TextView)itemView.findViewById(R.id.surah_name);
            ayahN=(TextView)itemView.findViewById(R.id.ayahN);
            type=(TextView)itemView.findViewById(R.id.surahType);

            Typeface tf_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/trado.ttf");
            Typeface tf_bold = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/tradoB.ttf");

            this.surahText.setTypeface(tf_bold);
            this.type.setTypeface(tf_regular);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                   /* Log.d("Database","onClick"+position);*/
                    // gets item position
                    cursor.moveToPosition(position);
                    String id=cursor.getString(0);

                     adapterItemClickListener.onItemClick(id);
                }
            });
        }
    }
}
