package com.singit.shays.singit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private GridView gridView;
    private SingItDBHelper db;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LyricsAPI api;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    private String lyrics;
    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.db = new SingItDBHelper(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        gridView = (GridView) findViewById(R.id.gridView);
        final ArrayList<LyricsRes> last_searches = db.get_last_searched_songs();
        gridView.setAdapter(new CustomGrid(this,last_searches));


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                LyricsRes selected = last_searches.get(position);
                LyricsRes pass = selected;
                try{
                    pass = api.getLyrics(selected);
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                Intent intent = new Intent(MainActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
            }
        });
/*
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Log.d(TAG,"On item click..");
                LyricsRes selected = (LyricsRes)gridView.getItemAtPosition(position);
                LyricsRes pass = selected;
                Log.d(TAG,selected.artist);
                Log.d(TAG,Integer.toString(selected.id));
                try{
                    pass = api.getLyrics((LyricsRes)gridView.getItemAtPosition(position));
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                Intent intent = new Intent(MainActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        });*/

        Log.d(TAG, "onCreate() Finish");
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        ArrayList<LyricsRes> last_searches = db.get_last_searched_songs();
        ListAdapter adapter = new CustomAdapter(this,last_searches);
        ListView list = (ListView) findViewById(R.id.lastSearchesList);
        list.setAdapter(adapter);*/
    }



}
