package com.sout_rahim.quran_za.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.activitys.Translate_Activity;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class AyahAdapter extends RecyclerView.Adapter<AyahAdapter.ViewHolder> {

    public Cursor cursor;
    public AdapterItemClickListener adapterItemClickListener;
    public String query="";
    public AyahAdapter(){

    }
    public AyahAdapter(Cursor cursor){
       this.cursor=cursor;
        notifyDataSetChanged();
    }
    public void setCursorandQuery(Cursor cursor,String query)
    {
        this.cursor = cursor;
        this.query = query;
        notifyDataSetChanged();
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener){
        this.adapterItemClickListener=adapterItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ayahView = inflater.inflate(R.layout.item_ayah, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ayahView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
       // Log.d("Database","Position"+position);
        cursor.moveToPosition(position);
        String ayah=cursor.getString(3);
        String translate=cursor.getString(4);
        String surahName=cursor.getString(6);
        String fullSurahName="سورة "+surahName;
        String ayahNumber=cursor.getString(2);
        String fullAyahNumber="آية "+ayahNumber;

        // Set item views based on your views and data model
        TextView ayahTextView = holder.ayahText;
        ayahTextView.setText(ayah);
        TextView translateTextView = holder.translateText;
        translateTextView.setText(translate);
        TextView surahNameTextView = holder.surahName;
        surahNameTextView.setText(fullSurahName);
        TextView ayahNumberTextView = holder.ayahNumber;
        ayahNumberTextView.setText(fullAyahNumber);

    }

    @Override
    public int getItemCount() {
        if(cursor==null) return 0;
        return cursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView ayahText;
        public TextView translateText;
        public TextView surahName;
        public TextView ayahNumber;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            ayahText=(TextView)itemView.findViewById(R.id.ayahText);
            translateText=(TextView)itemView.findViewById(R.id.translateText);
            surahName=(TextView)itemView.findViewById(R.id.surahName);
            ayahNumber=(TextView)itemView.findViewById(R.id.ayahNumber);

            Typeface tf_regular1 = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UthmanicHafs.otf");
            Typeface tfkh_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/KhmerOS.ttf");

            this.ayahText.setTypeface(tf_regular1);
            this.translateText.setTypeface(tfkh_regular);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    // gets item position
                    cursor.moveToPosition(position);
                    String ayah=cursor.getString(3);
                    String translate=cursor.getString(4);

                    String surahNumber=cursor.getString(1);
                    String surahName=cursor.getString(6);
                    String fullSurahName="سورة "+surahName;

                    String ayahNumber=cursor.getString(2);
                    String fullAyahNumber="آية "+ayahNumber;
                    String ID=cursor.getString(0);

                 /*   Log.d("Database","Onclick"+ayah+","+translate);*/

                    Intent intent=new Intent(context,Translate_Activity.class);

                    intent.putExtra("ayah",ayah);
                    intent.putExtra("translate",translate);
                    intent.putExtra("surahName",fullSurahName);
                    intent.putExtra("ayahName",fullAyahNumber);
                    intent.putExtra("ID",ID);
                    intent.putExtra("surahNumber",surahNumber);
                    intent.putExtra("ayahNumber",ayahNumber);

                    context.startActivity(intent);
                }
            });
        }
    }
}
