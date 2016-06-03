package com.singit.shays.singit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.util.Log;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private GridView gridView;
    private SingItDBHelper db;
    private Toolbar toolbar;
    private LyricsAPI api;
    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "on create");
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        api = new LyricsAPI();
        this.db = new SingItDBHelper(this);
        gridView = (GridView) findViewById(R.id.gridView);
        final ArrayList<LyricsRes> last_searches = db.get_last_nine_searched_songs();
        gridView.setAdapter(new CustomGrid(this, last_searches));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                LyricsRes selected = last_searches.get(position);
                LyricsRes pass = selected;
                try {
                    pass = api.getLyrics(selected);
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
                Intent intent = new Intent(MainActivity.this, LyricsViewActivity.class);
                intent.putExtra("view", pass);
                startActivity(intent);
            }
        });
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

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<LyricsRes> last_searches = db.get_last_searched_songs();
        gridView.setAdapter(new CustomGrid(this, last_searches));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

       if (id == R.id.action_favorites) {
            Intent intent = new Intent(this, FavoritesViewActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onMoreSearchesButtonClick(View view) {

        Intent intent = new Intent(this, MoreSearchesActivity.class);
        this.startActivity(intent);

    }
}
