package com.singit.shays.singit;

import android.app.LauncherActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import java.util.ArrayList;

/**
 * The favorites activity logic
 */
public class FavoritesViewActivity extends AppCompatActivity {

    private SingItDBHelper dbHelper;
    private ListView list;
    private  ListAdapter adapter;
    private static final String TAG = "SingDebug";
    private LyricsAPI api = new LyricsAPI();
    @Override
/**
 * This method run when the activity is created and upload the favorites list
 * @param savedInstanceState
 */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SingItDBHelper(this);
        ArrayList<LyricsRes> favorites = dbHelper.get_favorite_songs();
        adapter = new CustomAdapter(this,favorites);
        list = (ListView) findViewById(R.id.favoritesList);
        list.setEmptyView(findViewById(R.id.emptyFavorites));
        list.setAdapter(adapter);

    list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(FavoritesViewActivity.this);
                adb.setTitle("Delete?");
                LyricsRes item = (LyricsRes)list.getItemAtPosition(position);
                adb.setMessage("Are you sure you want to delete " +item.artist+" "+item.title+"?" );
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"get in on click");
                        LyricsRes item = (LyricsRes)list.getItemAtPosition(position);
                        Log.d(TAG,"get the item"+item.toString());
                        DBResult res = dbHelper.delete_song_from_favorites(item.id);
                        Log.d(TAG,"the result from DB: "+res.toString());
                        ArrayList<LyricsRes> refresh = dbHelper.get_favorite_songs();
                        adapter = new CustomAdapter(getBaseContext(),refresh);
                        list = (ListView) findViewById(R.id.favoritesList);
                        list.setEmptyView(findViewById(R.id.emptyFavorites));
                        list.setAdapter(adapter);                        Log.d(TAG,"notify");

                    }});
                adb.show();
                return true;
            }
        });


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Log.d(TAG,"On item click..");
                LyricsRes selected = (LyricsRes)list.getItemAtPosition(position);
                LyricsRes pass = selected;
                Log.d(TAG,selected.artist);
                Log.d(TAG,Integer.toString(selected.id));
                try{
                    pass = api.getLyrics((LyricsRes)list.getItemAtPosition(position));
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                Intent intent = new Intent(FavoritesViewActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        };

        list.setOnItemClickListener(itemClickListener);

        }
    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<LyricsRes> favorites = dbHelper.get_favorite_songs();
        adapter = new CustomAdapter(this,favorites);
        list = (ListView) findViewById(R.id.favoritesList);
        list.setEmptyView(findViewById(R.id.emptyFavorites));
        list.setAdapter(adapter);

        list.setOnItemLongClickListener( new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> a, View v, final int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(FavoritesViewActivity.this);
                adb.setTitle("Delete?");
                LyricsRes item = (LyricsRes)list.getItemAtPosition(position);
                adb.setMessage("Are you sure you want to delete " +item.artist+" "+item.title+"?" );
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG,"get in on click");
                        LyricsRes item = (LyricsRes)list.getItemAtPosition(position);
                        Log.d(TAG,"get the item"+item.toString());
                        DBResult res = dbHelper.delete_song_from_favorites(item.id);
                        Log.d(TAG,"the result from DB: "+res.toString());
                        ArrayList<LyricsRes> refresh = dbHelper.get_favorite_songs();
                        adapter = new CustomAdapter(getBaseContext(),refresh);
                        list = (ListView) findViewById(R.id.favoritesList);
                        list.setEmptyView(findViewById(R.id.emptyFavorites));
                        list.setAdapter(adapter);                        Log.d(TAG,"notify");

                    }});
                adb.show();
                return true;
            }
        });


        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Log.d(TAG,"On item click..");
                LyricsRes selected = (LyricsRes)list.getItemAtPosition(position);
                LyricsRes pass = selected;
                Log.d(TAG,selected.artist);
                Log.d(TAG,Integer.toString(selected.id));
                try{
                    pass = api.getLyrics((LyricsRes)list.getItemAtPosition(position));
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                Intent intent = new Intent(FavoritesViewActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        };

        list.setOnItemClickListener(itemClickListener);
    }

    }
