package com.sout_rahim.quran_za.adapters;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sout_rahim.quran_za.R;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListener;
import com.sout_rahim.quran_za.ultils.AdapterItemClickListenerA;
import com.sout_rahim.quran_za.ultils.Ayah;
import com.sout_rahim.quran_za.ultils.ConvertNumber;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sout_rahim on 16/07/10.
 */
public class AyahAdapter_Full extends RecyclerView.Adapter<AyahAdapter_Full.ViewHolder> {

    public List<Ayah> ayahList;
    public AdapterItemClickListener adapterItemClickListener;
    public AdapterItemClickListenerA adapterItemClickListenerA;
    public int font_size;
    public AyahAdapter_Full(){

    }
    public void setFont(int font_size){
        this.font_size=font_size;
        notifyDataSetChanged();
    }
    public AyahAdapter_Full(ArrayList<Ayah> ayahList){
        this.ayahList=ayahList;
        notifyDataSetChanged();
    }

    public void setAdapterItemClickListenerA(AdapterItemClickListenerA adapterItemClickListenerA) {
        this.adapterItemClickListenerA = adapterItemClickListenerA;
    }

    public void setAyahList(List<Ayah> ayahList) {
       this.ayahList=ayahList;
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
        View ayahView = inflater.inflate(R.layout.item_ayah_full, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(ayahView,context);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        /*Log.d("Database","Position"+position);*/

        Ayah ayahObject=ayahList.get(position);

        String ayahNumAr=new ConvertNumber(ayahObject.getAyahID()).getOutput();
        String ayah=ayahObject.getAyahText();
        String translate=ayahObject.getAyahTextKhmer();
        String surahName=ayahObject.getSuraID();
        String fullSurahName="سورة "+surahName;
        String ayahNumber=ayahObject.getAyahID();
        String fullAyahNumber="آية "+ayahNumber;

        TextView ayahTextView = holder.ayahText;

        //===sub bismillah first ayah====
        String sub="بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيمِ";
        if(ayahObject.getAyahID().equals("1") && !ayahObject.getSuraID().equals("1")){
           if(ayah.startsWith("بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيم")) {
               ayah = ayah.substring(sub.length(), ayah.length());
           }
        }
        //===spannable allah color========
       /* String text = ayah+" \t";
        SpannableString spannableString = new SpannableString(text);
        String[] query={"اللَّهُ","اللَّهِ","اللَّهَ"};
        Log.d("query allah",""+query.length);
        for(int i=1;i<query.length;i++)
        {
            Pattern pattern = Pattern.compile(query[i].toLowerCase());
            Matcher matcher = pattern.matcher(text.toLowerCase());
            while (matcher.find()) {
                spannableString.setSpan(new StyleSpan(Typeface.NORMAL), matcher.start(), matcher.end(), 0);
                spannableString.setSpan(new RainbowSpan(holder.context.getApplicationContext()), matcher.start(), matcher.end(), 0);
            }
        }*/

        ayahTextView.setText(ayah);
        TextView ayahNums=holder.ayahNums;
        ayahNums.setText(ayahNumber);
        TextView translateTextView = holder.translateText;
        translateTextView.setText(translate);

        TextView surahNameTextView = holder.surahName;
        surahNameTextView.setText(fullSurahName);

        TextView ayahNumberTextView = holder.ayahNumber;
        ayahNumberTextView.setText(fullAyahNumber);

        surahNameTextView.setVisibility(View.GONE);
        ayahNumberTextView.setVisibility(View.GONE);

        //==============set font to fontsize===
        if(ayahObject.getAyahID().equals("0")){
            ayahNums.setVisibility(View.GONE);
            holder.btnAction.setVisibility(View.GONE);
            /*ayahTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);*/
            ayahTextView.setTextSize(font_size);
            translateTextView.setTextSize(font_size-6);
        }else {
            holder.btnAction.setVisibility(View.VISIBLE);
            ayahNums.setVisibility(View.VISIBLE);
            /*ayahTextView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);*/
            ayahNums.setTextSize(font_size-6);
            ayahTextView.setTextSize(font_size);
            translateTextView.setTextSize(font_size-6);
        }

        //=====spannable image ayah=======
/*
        if (Build.VERSION.SDK_INT > 22) {
            String text = ayah + " \t";
            SpannableString spannableString = new SpannableString(text);
            Bitmap bitmap = drawTextToBitmap(holder.context, R.drawable.ic_ns3, ayahNumAr);
            Drawable d = new BitmapDrawable(holder.context.getResources(), bitmap);
            d.setBounds(0, 0, ayahTextView.getLineHeight() - 45, ayahTextView.getLineHeight() - 20);
            final int newColor = Color.rgb(33, 150, 243);
            d.setColorFilter(newColor, PorterDuff.Mode.SRC_ATOP);

            ImageSpan searchImageSpan = new ImageSpan(d);
            //  ImageSpan imageSpan = new CenteredImageSpan(holder.context.getApplicationContext(), myIcon);

            if (!ayahObject.getAyahID().equals("0")) {
                spannableString.setSpan(
                        searchImageSpan,
                        text.length() - 1,
                        text.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
            ayahTextView.setText(spannableString);
            ayahNums.setVisibility(View.GONE);
        }else {
            ayahTextView.setText(ayah);
            ayahNums.setVisibility(View.VISIBLE);
        }*/
    }

    @Override
    public int getItemCount() {
       return ayahList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public Context context;
        public TextView ayahText;
        public TextView translateText;
        public TextView surahName;
        public TextView ayahNumber;
        private TextView ayahNums;
        private ImageButton btnAction;

        public ViewHolder(View itemView, final Context context) {
            super(itemView);
            this.context=context;

            ayahText=(TextView)itemView.findViewById(R.id.ayahText2);
            translateText=(TextView)itemView.findViewById(R.id.translateText2);
            surahName=(TextView)itemView.findViewById(R.id.surahName2);
            ayahNumber=(TextView)itemView.findViewById(R.id.ayahNumber2);
            ayahNums=(TextView)itemView.findViewById(R.id.ayahNums);
            btnAction=(ImageButton)itemView.findViewById(R.id.btnAction);

            Typeface tf_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/UthmanicHafs.otf");
            Typeface tfkh_regular = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/KhmerOS.ttf");
            this.ayahText.setTypeface(tf_regular);
            this.translateText.setTypeface(tfkh_regular);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position=getAdapterPosition();
                    Log.d("Database","onClick"+position);
                }
            });
           btnAction.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int position=getAdapterPosition();
                   adapterItemClickListenerA.onActionClick(position);
               }
           });
        }

    }
    private static class RainbowSpan extends CharacterStyle implements UpdateAppearance {
        private final int[] colors;
        public RainbowSpan(Context context) {
            colors = context.getResources().getIntArray(R.array.rainbow);
        }

        public void updateDrawState(TextPaint paint) {
            paint.setStyle(Paint.Style.FILL);
            Shader shader = new LinearGradient(0, 0, 0, paint.getTextSize() * colors.length, colors,
                    null, Shader.TileMode.MIRROR);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            shader.setLocalMatrix(matrix);
            paint.setShader(shader);
        }
    }
    /*private Bitmap drawTextToBitmap(Context context, int resId, String text) {
        try {
            Resources resources = context.getResources();
            float scale = resources.getDisplayMetrics().density;
            Bitmap bitmap = BitmapFactory.decodeResource(resources, resId);

            android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
            // set default bitmap config if none
            if (bitmapConfig == null) {
                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
            }

            bitmap = bitmap.copy(bitmapConfig, true);

            Canvas canvas = new Canvas(bitmap);
            // new antialised Paint
            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            // text color - #3D3D3D
            paint.setColor(Color.rgb(110, 110, 110));
            // text size in pixels
            paint.setTextSize((int) (38 * scale));
            // text shadow
            paint.setShadowLayer(1f, 0f, 1f, Color.DKGRAY);

            // draw text to the Canvas center
            Rect bounds = new Rect();
            paint.getTextBounds(text, 0, text.length(), bounds);
            int x = (bitmap.getWidth() - bounds.width()) / 2;
            int y = ((bitmap.getHeight() - bounds.height()) / 2)+45;

            // canvas.drawText(text, x * scale, y * scale, paint);
            canvas.drawText(text, x, y, paint);

            return bitmap;
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();

            return null;
        }
    }*/
}
