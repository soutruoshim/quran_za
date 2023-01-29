package com.sout_rahim.quran_za.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListenerA;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class AyahBookmarkAdapter extends RecyclerView.Adapter<AyahBookmarkAdapter.ViewHolder> {

    public Cursor cursor;
    public AdapterItemClickListener adapterItemClickListener;
    public AdapterItemClickListenerA adapterItemClickListenerA;
    public int font_size;
    public AyahBookmarkAdapter(){

    }
    public void setFont(int font_size){
        this.font_size=font_size;
        notifyDataSetChanged();
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    public void setAdapterItemClickListenerA(AdapterItemClickListenerA adapterItemClickListenerA) {
        this.adapterItemClickListenerA = adapterItemClickListenerA;
    }

    public void setAdapterItemClickListener(AdapterItemClickListener adapterItemClickListener){
        this.adapterItemClickListener=adapterItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View ayahView = inflater.inflate(R.layout.item_bookmark_ayah, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ayahView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        /*Log.d("Database","Position"+position);*/

        cursor.moveToPosition(position);

        String ayah=cursor.getString(4);
        String translate=cursor.getString(5);
        String surahName1="سورة "+cursor.getString(6);
        String ayahNumber=cursor.getString(3);
        String ayahName1="آية "+ayahNumber;


        TextView ayahTextView = holder.ayahText;
        TextView translateTextView = holder.translateText;
        TextView surahNameTextView = holder.surahName;
        TextView ayahNumberTextView = holder.ayahNumber;

        ayahTextView.setText(ayah);
        translateTextView.setText(translate);
        surahNameTextView.setText(surahName1);
        ayahNumberTextView.setText(ayahName1);

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

        private ImageButton btnDelete;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            ayahText=(TextView)itemView.findViewById(R.id.ayahText);
            translateText=(TextView)itemView.findViewById(R.id.translateText);
            surahName=(TextView)itemView.findViewById(R.id.surahName);
            ayahNumber=(TextView)itemView.findViewById(R.id.ayahNumber);

           btnDelete=(ImageButton)itemView.findViewById(R.id.btnDelete);

            Typeface tf_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UthmanicHafs.otf");
            Typeface tfkh_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/KhmerOS.ttf");
            this.ayahText.setTypeface(tf_regular);
            this.translateText.setTypeface(tfkh_regular);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    cursor.moveToPosition(position);
                    adapterItemClickListenerA.onItemClick(cursor.getString(2),cursor.getString(3));
                }
            });
           btnDelete.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position=getAdapterPosition();
                   cursor.moveToPosition(position);
                   adapterItemClickListener.onItemClick(cursor.getString(0));
               }
           });
        }

    }
}
