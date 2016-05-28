package com.singit.shays.singit;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;


/**
 * Created by lions on 07/05/2016.
 */
class SingItDBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "SingItDataBase.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "SingDebug";
    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     */
    public SingItDBHelper(Context context)
    {
        //The second parameter is th DB name, null for debug purpose.
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(TAG, "DB creation started");
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        updateMyDB(db, 0, DB_VERSION);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * The SQLite ALTER TABLE documentation can be found
     * <a href="http://sqlite.org/lang_altertable.html">here</a>. If you add new columns
     * you can use ALTER TABLE to insert them into a live table. If you rename or remove columns
     * you can use ALTER TABLE to rename the old table, then create the new table and then
     * populate the new table with the contents of the old table.
     * This method executes within a transaction.  If an exception is thrown, all changes
     * will automatically be rolled back.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateMyDB(db, oldVersion, newVersion);
    }

    /**
    * Updates the DB according to the version.
    * @param db         The database.
    * @param oldVersion The old database version.
    * @param newVersion The new database version.
    */
    public void updateMyDB(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if ( oldVersion < 1)
        {
            create_songs_table(db);
            create_favorites_table(db);
            create_last_searches_table(db);
            Log.d(TAG, "DB creation finished.");
        }
    }

    /**
     * Creates song table.
     * @param db The database
     */
    public void create_songs_table(SQLiteDatabase db)
    {
        String sql_create_table;
        sql_create_table = "CREATE TABLE songs ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "artist_name TEXT, "
                + "song_name TEXT,"
                + "translated_song TEXT); ";

        db.execSQL(sql_create_table);
    }

    /**
     * Creates favorites table.
     * @param db The database
     */
    public void create_favorites_table(SQLiteDatabase db)
    {
        String sql_create_table;
        sql_create_table = "CREATE TABLE favorites ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "song_id INTEGER"
                + "artist_name TEXT, "
                + "song_name TEXT, "
                + "picture_path TEXT); ";

        db.execSQL(sql_create_table);
    }

    /**
     * Creates last searches table.
     * @param db The database
     */
    public static void create_last_searches_table(SQLiteDatabase db)
    {
        String sql_create_table;
        sql_create_table = "CREATE TABLE last_searches ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "song_id INTEGER"
                + "artist_name TEXT, "
                + "song_name TEXT, "
                + "picture_path TEXT); ";

        db.execSQL(sql_create_table);
    }

    /**
     * Insert song to the song table, after translation.
     * @param song_name         Name of the song to insert.
     * @param artist_name       Name of the song's artist to insert.
     * @param translated_song   The song after being translated.
     */
    public void insert_song_to_songs_table(String song_name,String artist_name, String translated_song)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues song_values = new ContentValues();
        song_values.put("song_name",song_name);
        song_values.put("artist_name",artist_name);
        song_values.put("translated_song",translated_song);
        db.insert("songs",null,song_values);
    }

    /**
     * Insert song to the favorites table, after chose as favorite.
     * @param song_name         Name of the song to insert.
     * @param artist_name       Name of the song's artist to insert.
     * @param picture_path      Picture path in device.
     */
    public void insert_song_to_favorites_table(String song_name, String artist_name, String picture_path)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues song_values = new ContentValues();
        song_values.put("song_name",song_name);
        song_values.put("artist_name",artist_name);
        song_values.put("picture_path",picture_path);
        db.insert("favorites",null,song_values);
    }

    /**
     * Insert song to the favorites table, just some last searches to b defined.
     * @param song_name         Name of the song to insert.
     * @param artist_name       Name of the song's artist to insert.
     * @param picture_path      Picture path in device.
     */
    public void insert_song_to_last_searches(String song_name, String artist_name, String picture_path)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues song_values = new ContentValues();
        song_values.put("song_name",song_name);
        song_values.put("artist_name",artist_name);
        song_values.put("picture_path",picture_path);
        db.insert("last_searches",null,song_values);
    }


}
