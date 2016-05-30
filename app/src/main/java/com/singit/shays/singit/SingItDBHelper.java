package com.singit.shays.singit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lions on 07/05/2016.
 */
class SingItDBHelper extends SQLiteOpenHelper
{
    /**
     * Insert to db example.
     this.db.insert_song_to_songs_table(12358,"Song name test","Author name test","Translated song song song...");
     this.db.insert_song_to_favorites_table(65989,"Song name test","Author name test","Song song song...","/data/data/...","http://www.api.com/12545");
     this.db.insert_song_to_last_searches(47578,"Song name test","Author name test","Song song song...","/data/data/...","http://www.api.com/125775");
     */

    private static final String DB_NAME = "SingItDataBase.db";
    private static final int DB_VERSION = 1;
    private static final String TAG = "DBDebug";
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
                + "song_id INTEGER, "
                + "artist_name TEXT, "
                + "song_name TEXT, "
                + "translated_lyrics TEXT); ";

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
                + "song_id INTEGER, "
                + "artist_name TEXT, "
                + "song_name TEXT, "
                + "lyrics TEXT, "
                + "image_url TEXT, "
                + "image_path TEXT);";

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
                + "song_id INTEGER, "
                + "artist_name TEXT, "
                + "song_name TEXT, "
                + "lyrics TEXT, "
                + "image_url TEXT, "
                + "image_path TEXT);";


        db.execSQL(sql_create_table);
    }

    /**
     * Insert song to the song table, after translation.
     *
     * @param song_id           Song id in api web.
     * @param song_name         Name of the song to insert.
     * @param artist_name       Name of the song's artist to insert.
     * @param translated_lyrics   The song after being translated.
     */
    public void insert_song_to_songs_table(int song_id, String song_name,String artist_name, String translated_lyrics)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues song_values = new ContentValues();

        song_values.put("song_id",song_id);
        song_values.put("song_name",song_name);
        song_values.put("artist_name",artist_name);
        song_values.put("translated_lyrics",translated_lyrics);

        db.insert("songs",null,song_values);
    }

    /**
     * Insert song to the favorites table, after chose as favorite.
     *
     * @param song_id           Song id in api web.
     * @param artist_name       Name of the song's artist to insert.
     * @param song_name         Name of the song to insert.
     * @param lyrics            Lyrics of the song.
     * @param image_path        Picture path in device.
     * @param image_url         Image URL in api web.
     */
    public void insert_song_to_favorites_table(int song_id, String artist_name, String song_name, String lyrics, String image_url,String image_path)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues song_values = new ContentValues();

        song_values.put("song_id",song_id);
        song_values.put("artist_name",artist_name);
        song_values.put("song_name",song_name);
        song_values.put("lyrics",lyrics);
        song_values.put("image_url",image_url);
        song_values.put("image_path",image_path);

        db.insert("favorites",null,song_values);
    }

    /**
     *  Insert song to last searches song.
     *
     * @param song_id           Song id in api web.
     * @param artist_name       Name of the song's artist to insert.
     * @param song_name         Name of the song to insert.
     * @param lyrics            Lyrics of the song.
     * @param image_path        Picture path in device.
     * @param image_url         Image URL in api web.
     */
    public void insert_song_to_last_searches(int song_id, String artist_name, String song_name, String lyrics, String image_url,String image_path)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues song_values = new ContentValues();

        song_values.put("song_id",song_id);
        song_values.put("artist_name",artist_name);
        song_values.put("song_name",song_name);
        song_values.put("lyrics",lyrics);
        song_values.put("image_url",image_url);
        song_values.put("image_path",image_path);

        db.insert("last_searches",null,song_values);
    }

    /**
     * Goes over the table in the DB and returns the table as list of LyricsRes objects.
     *
     * @param table The table of songs needed to be retrieved.
     * @return ArrayList of all the rows in a table as LyricsRes.
     */
    public ArrayList<LyricsRes> get_all_songs(String table)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> map_list = new ArrayList<>();
        ArrayList<LyricsRes> result_list = new ArrayList<>();

        Cursor result =  db.rawQuery("SELECT * FROM " + table, null);

        // Looping through all rows and adding to list.
        if (result.moveToFirst())
        {
            do
            {
                HashMap<String, String> map = new HashMap<>();

                for(int i=1; i<result.getColumnCount()-1; i++)
                {
                    if (!result.getColumnName(i).equals("song_id"))
                        map.put(result.getColumnName(i), result.getString(i));
                    else
                        map.put(result.getColumnName(i), String.valueOf(result.getInt(i)));
                }
                map_list.add(map);
            } while (result.moveToNext());
        }

        // Looping through the map list and filling the result list.
        for (HashMap<String, String> song:map_list)
        {
            LyricsRes lyrics_result = new LyricsRes(song.get("song_name"),
                    song.get("artist_name"),
                    song.get("lyrics"),
                    song.get("image_url"),
                    Integer.valueOf(song.get("song_id")));

            result_list.add(lyrics_result);
        }

        result.close();
        return result_list;
    }

    /**
     * Get the last searched songs from the DB.
     *
     * @return  ArrayList of all the rows in a last_searches table as LyricsRes objects.
     */
    public ArrayList<LyricsRes> get_last_searched_songs()
    {
        return get_all_songs("last_searches");
    }

    /**
     * Get the last favorite songs from the DB.
     *
     * @return  ArrayList of all the rows in a favorites table as LyricsRes objects.
     */
    public ArrayList<LyricsRes> get_favorite_songs()
    {
        return get_all_songs("favorites");
    }

}
