package com.singit.shays.singit.entities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by lions on 07/05/2016.
 */

public class SingItDBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DBDebug";
    private static final String DB_NAME = "SingItDataBase.db";
    private static final int DB_VERSION = 1;
    private static final String ARTIST_NAME = "artist_name";
    private static final String SONG_id = "song_id";
    private static final String SONG_NAME = "song_name";
    private static final String LYRICS = "lyrics";
    private static final String IMAGE_URL = "image_url";
    private static final String THUMBNAIL_URL = "thumbnail_url";
    private static final String IMAGE_PATH = "image_path";
    private static final String THUMBNAIL_PATH = "thumbnail_path";
    private static final String FAVORITES_TABLE = "favorites";
    private static final String LAST_SEARCHES_TABLE = "last_searches";
    private static final String IMAGE_DIR = "image_directory";
    private static final String THUMBNAIL_DIR = "thumbnail_directory";
    private ImageSaver image_manager;
    private Context context;

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database.
     */
    public SingItDBHelper(Context context) {
        //The second parameter is th DB name, null for debug purpose.
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        this.image_manager = new ImageSaver(context);
        this.image_manager.setDirectoryName("image_directory");
        Log.d(TAG, "DB creation started");
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
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
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDB(db, oldVersion, newVersion);
    }

    /**
     * Updates the DB according to the version.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    private void updateMyDB(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            create_favorites_table(db);
            create_last_searches_table(db);
            Log.d(TAG, "DB creation finished.");
        }
    }

    public static String getImageDir() {
        return IMAGE_DIR;
    }

    public static String getThumbnailDir() {
        return THUMBNAIL_DIR;
    }


    /**
     * Creates favorites table.
     *
     * @param db The database.
     */
    private void create_favorites_table(SQLiteDatabase db) {
        String sql_create_table;
        sql_create_table = "CREATE TABLE " + FAVORITES_TABLE + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SONG_id + " INTEGER NOT NULL UNIQUE, "
                + ARTIST_NAME + " TEXT NOT NULL, "
                + SONG_NAME + " TEXT NOT NULL, "
                + LYRICS + " TEXT NOT NULL, "
                + IMAGE_URL + " TEXT, "
                + THUMBNAIL_URL + " TEXT, "
                + IMAGE_PATH + " TEXT, "
                + THUMBNAIL_PATH + " TEXT);";

        db.execSQL(sql_create_table);
    }

    /**
     * Creates last searches table.
     *
     * @param db The database.
     */
    private static void create_last_searches_table(SQLiteDatabase db) {
        String sql_create_table;
        sql_create_table = "CREATE TABLE " + LAST_SEARCHES_TABLE + " ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SONG_id + " INTEGER NOT NULL UNIQUE, "
                + ARTIST_NAME + " TEXT NOT NULL, "
                + SONG_NAME + " TEXT NOT NULL, "
                + LYRICS + " TEXT NOT NULL, "
                + IMAGE_URL + " TEXT, "
                + THUMBNAIL_URL + " TEXT, "
                + IMAGE_PATH + " TEXT, "
                + THUMBNAIL_PATH + " TEXT);";

        db.execSQL(sql_create_table);
    }

    /**
     *
     * @param directory
     * @param name
     * @param image
     */
    public void save_image(String directory, String name, Bitmap image)
    {
        if(image != null)
        {
            new ImageSaver(this.context).
                    setFileName(name + ".png").
                    setDirectoryName(directory).
                    save(image);
        }
    }

    /**
     * Internal method for images loading.
     * @param directory The image directory.
     * @param name      The image name.
     * @return          Bitmap of the image.
     */
    private Bitmap load_picture(String directory, String name)
    {
        return new ImageSaver(this.context).
                setFileName(name + ".png").
                setDirectoryName(directory).
                load();
    }

    /**
     * Returns image from the device.
     * @param song_id   The name of the song.
     * @return          Returns the Bitmap of the image.
     */
    public Bitmap get_image(int song_id)
    {
        String name = String.valueOf(song_id);

        return load_picture(IMAGE_DIR, name);
    }

    /**
     * Returns thumbnail from the device.
     * @param song_id   The name of the song.
     * @return          Returns the Bitmap of the image.
     */
    public Bitmap get_thumbnail(int song_id)
    {
        String name = String.valueOf(song_id);

        return load_picture(THUMBNAIL_DIR, name);
    }


    /**
     * Insert song to the favorites table, after chose as favorite.
     *
     * @param lyrics        LyricsRes object of the song.
     * @param image         The lyrics image.
     * @param thumbnail     The lyrics thumbnail.
     * @return              DBResult of the status.
     * @throws              SQLiteConstraintException
     */
    public DBResult insert_song_to_favorites_table(LyricsRes lyrics, Bitmap image, Bitmap thumbnail)
            throws SQLiteConstraintException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues song_values = new ContentValues();

        song_values.put(SONG_id, lyrics.getId());
        song_values.put(ARTIST_NAME, lyrics.getArtist());
        song_values.put(SONG_NAME, lyrics.getTitle());
        song_values.put(LYRICS, lyrics.getLyrics());
        song_values.put(IMAGE_URL, lyrics.getImageURL());
        song_values.put(THUMBNAIL_URL, lyrics.getThumbnailURL());

        Thread t = new Thread(new SaveRunnable(this, lyrics, image, thumbnail));
        t.start();

        try {
            db.insertOrThrow(FAVORITES_TABLE, null, song_values);
        } catch (SQLiteConstraintException e) {
            Log.d(TAG, "SQLiteConstraintException " + e.getStackTrace().toString());
            return DBResult.ITEM_NOT_EXISTS_ERROR;
        }
        return DBResult.OK;
    }

    /**
     * Insert song to last searches song.
     *
     * @param lyrics        LyricsRes object of the song.
     * @param image         The lyrics image.
     * @param thumbnail     The lyrics thumbnail.
     * @return              DBResult of the status.
     * @throws              SQLiteConstraintException
     */
    public DBResult insert_song_to_last_searches_table(LyricsRes lyrics, Bitmap image, Bitmap thumbnail)
            throws SQLiteConstraintException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues song_values = new ContentValues();

        song_values.put(SONG_id, lyrics.getId());
        song_values.put(ARTIST_NAME, lyrics.getArtist());
        song_values.put(SONG_NAME, lyrics.getTitle());
        song_values.put(LYRICS, lyrics.getLyrics());
        song_values.put(IMAGE_URL, lyrics.getImageURL());
        song_values.put(THUMBNAIL_URL, lyrics.getThumbnailURL());

        Thread t = new Thread(new SaveRunnable(this, lyrics, image, thumbnail));
        t.start();

        try {
            db.insertOrThrow(LAST_SEARCHES_TABLE, null, song_values);
        } catch (SQLiteConstraintException e) {
            delete_song_by_song_id(LAST_SEARCHES_TABLE, lyrics.getId());
            insert_song_to_last_searches_table(lyrics, null, null);
        }
        return DBResult.OK;
    }

    /**
     * Goes over the table in the DB and returns the table as list of LyricsRes objects.
     *
     * @param table The table of songs needed to be retrieved.
     * @return ArrayList of all the rows in a table as LyricsRes.
     */
    private ArrayList<LyricsRes> get_all_songs(String table) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> map_list = new ArrayList<>();
        ArrayList<LyricsRes> result_list = new ArrayList<>();

        Cursor result = db.rawQuery("SELECT * FROM " + table, null);

        // Looping through all rows and adding to list.
        if (result.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<>();

                for (int i = 1; i < result.getColumnCount(); i++) {
                    if (!result.getColumnName(i).equals(SONG_id))
                        map.put(result.getColumnName(i), result.getString(i));
                    else
                        map.put(result.getColumnName(i), String.valueOf(result.getInt(i)));
                }
                map_list.add(map);
            } while (result.moveToNext());
        }

        // Looping through the map list and filling the result list.
        for (HashMap<String, String> song : map_list) {
            LyricsRes lyrics_result = new LyricsRes(song.get(SONG_NAME),
                    song.get(ARTIST_NAME),
                    song.get(LYRICS),
                    song.get(IMAGE_URL),
                    song.get(THUMBNAIL_URL),
                    Integer.valueOf(song.get(SONG_id)));

            result_list.add(lyrics_result);
        }

        result.close();
        return result_list;
    }

    /**
     * Get the last searched songs from the DB.
     *
     * @return ArrayList of all the rows in a last_searches table as LyricsRes objects.
     */
    public ArrayList<LyricsRes> get_last_searched_songs() {
        ArrayList<LyricsRes> result_list = get_all_songs(LAST_SEARCHES_TABLE);
        Collections.reverse(result_list);

        return result_list;
    }

    /**
     *
     * @param table The table to get from the songs.
     * @return      The last six songs from a table.
     */
    private ArrayList<LyricsRes> get_last_six_songs(String table)
    {
        ArrayList<LyricsRes> result_list = get_all_songs(table);
        int limit = 6;

        Collections.reverse(result_list);
        if(result_list.size() <= limit)
        {
            return result_list;
        }
        else
        {
            int num_to_remove, result_size;
            result_size = result_list.size();
            num_to_remove = result_list.size() - limit;
            for (int i = 0; i< num_to_remove ; i++)
            {
                result_list.remove(result_size - i - 1);
            }
        }
        return result_list;
    }

        /**
         * Get the last six searched songs from the DB.
         *
         * @return ArrayList of all the six first rows in a last_searches table as LyricsRes objects.
         */
    public ArrayList<LyricsRes> get_last_six_searched_songs() {
        return get_last_six_songs("last_searches");
    }

    /**
     * Get the last six favorites songs from the DB.
     *
     * @return ArrayList of all the six first rows in a favorites table as LyricsRes objects.
     */
    public ArrayList<LyricsRes> get_last_six_favorites_songs() {
        return get_last_six_songs("favorites");

    }

    /**
     * Get the last favorite songs from the DB.
     *
     * @return ArrayList of all the rows in a favorites table as LyricsRes objects.
     */
    public ArrayList<LyricsRes> get_favorite_songs() {
        return get_all_songs(FAVORITES_TABLE);
    }

    /**
     * Deletes song from table in the DB by song id.
     *
     * @param table   Table of the lyrics to be deleted.
     * @param song_id Song id to be deleted.
     * @return DBResult of ITEM_NOT_EXISTS_ERROR or OK.
     * @throws SQLiteException
     */
    private DBResult delete_song_by_song_id(String table, int song_id)
            throws SQLiteException {
        String sql_delete_song;
        SQLiteDatabase db = this.getWritableDatabase();

        sql_delete_song = "DELETE FROM " + table + " WHERE song_id = " + song_id + ";";

        try {
            db.execSQL(sql_delete_song);
        } catch (SQLiteException e) {
            Log.d(TAG, "SQLiteException " + e.getStackTrace().toString());
            return DBResult.ITEM_NOT_EXISTS_ERROR;
        }

        return DBResult.OK;
    }

    /**
     * Deletes song from favorites table, by song id.
     *
     * @param song_id Song id to be deleted.
     * @return DBResult.
     * @throws SQLiteException
     */
    public DBResult delete_song_from_favorites(int song_id)
            throws SQLiteException {
        return delete_song_by_song_id(FAVORITES_TABLE, song_id);
    }

    /**
     *
     * @param   song_id The id of the song to check.
     * @return  DBResult of ITEM_EXISTS or ITEM_NOT_EXISTS.
     */
    public DBResult is_favorite_song(int song_id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql_check_favorite = "SELECT * FROM " + FAVORITES_TABLE + " WHERE " + SONG_id + " = " + song_id;
        Cursor result = db.rawQuery(sql_check_favorite, null);

        if(result.getCount() <= 0)
        {
            result.close();
            return DBResult.ITEM_NOT_EXISTS;
        }
        result.close();
        return DBResult.ITEM_EXISTS;
    }
}


