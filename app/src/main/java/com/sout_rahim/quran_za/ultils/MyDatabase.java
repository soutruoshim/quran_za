package com.sout_rahim.quran_za.ultils;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by sout_rahim on 6/10/2016.
 */
public class MyDatabase extends SQLiteAssetHelper {
    private static final String DATABASE_NAME = "qurans_za.db";
    private static final int DATABASE_VERSION = 2;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
