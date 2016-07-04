package com.singit.shays.singit.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.EditText;
import android.util.Log;

import com.singit.shays.singit.R;
import com.singit.shays.singit.adapters.CustomGrid;
import com.singit.shays.singit.entities.LyricsAPI;
import com.singit.shays.singit.entities.LyricsRes;
import com.singit.shays.singit.entities.SingItDBHelper;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity{

    private MyGridView gridView;
    private MyGridView gridView2;
    private SingItDBHelper db;
    private Toolbar toolbar;
    private LyricsAPI api;
    private static final String TAG = "SingDebug";
    private static LyricsRes emptyFav;
    private static LyricsRes emptySearch;

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
        gridView = (MyGridView) findViewById(R.id.gridView);
        final ArrayList<LyricsRes> last_searches = db.get_last_six_searched_songs();
        if (last_searches.isEmpty()){
            Log.d(TAG,"empty search");
            emptySearch = new LyricsRes("Make your first Search!","Start singing now", LyricsRes.Type.SEARCH);
            ArrayList<LyricsRes> emptys = new ArrayList<>();
            emptys.add(emptySearch);
            gridView.setAdapter(new CustomGrid(this, emptys));
        }
        else{
            gridView.setAdapter(new CustomGrid(this, last_searches));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    LyricsRes selected = last_searches.get(position);
                    Log.d(TAG,"get last searches click");
                    LyricsRes pass = selected;
                    try {
                        Log.d(TAG,"in try");
                        pass = api.getLyrics(selected);
                        Log.d(TAG,"after try");
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                    Intent intent = new Intent(MainActivity.this, LyricsViewActivity.class);
                    intent.putExtra("view", pass);
                    startActivity(intent);
                }
            });
        }

        gridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });


        gridView2 = (MyGridView) findViewById(R.id.gridView2);
        final ArrayList<LyricsRes> last_fav = db.get_last_six_favorites_songs();

        if (last_fav.size() == 0){
            Log.d(TAG,"empty fav");
            emptyFav = new LyricsRes("Store your favorites!","Save them now", LyricsRes.Type.FAV);
            ArrayList<LyricsRes> emptys = new ArrayList<>();
            emptys.add(emptyFav);
            gridView2.setAdapter(new CustomGrid(this, emptys));
        }
        else{
            gridView2.setAdapter(new CustomGrid(this, last_fav));
            gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    LyricsRes selected = last_fav.get(position);
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

        gridView2.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
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

        final ArrayList<LyricsRes> last_searches = db.get_last_six_searched_songs();
        if (last_searches.isEmpty()){
            Log.d(TAG,"empty search");
            emptySearch = new LyricsRes("Make your first Search!","Start singing now", LyricsRes.Type.SEARCH);
            ArrayList<LyricsRes> emptys = new ArrayList<>();
            emptys.add(emptySearch);
            gridView.setAdapter(new CustomGrid(this, emptys));
        }
        else{
            gridView.setAdapter(new CustomGrid(this, last_searches));
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    LyricsRes selected = last_searches.get(position);
                    Log.d(TAG,"get last searches click");
                    LyricsRes pass = selected;
                    try {
                        Log.d(TAG,"in try");
                        pass = api.getLyrics(selected);
                        Log.d(TAG,"after try");
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                    Intent intent = new Intent(MainActivity.this, LyricsViewActivity.class);
                    intent.putExtra("view", pass);
                    startActivity(intent);
                }
            });
        }

        gridView.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });

        final ArrayList<LyricsRes> last_fav = db.get_last_six_favorites_songs();

        if (last_fav.size() == 0){
            Log.d(TAG,"empty fav");
            emptyFav = new LyricsRes("Store your favorites!","Save them now", LyricsRes.Type.FAV);
            ArrayList<LyricsRes> emptys = new ArrayList<>();
            emptys.add(emptyFav);
            gridView2.setAdapter(new CustomGrid(this, emptys));
        }
        else{
            gridView2.setAdapter(new CustomGrid(this, last_fav));
            gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {

                    LyricsRes selected = last_fav.get(position);
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

        gridView2.setOnTouchListener(new View.OnTouchListener(){

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    return true;
                }
                return false;
            }

        });

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

        if (id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onMoreSearchesButtonClick(View view) {

        Intent intent = new Intent(this, MoreSearchesActivity.class);
        this.startActivity(intent);

    }

    public void onMoreFavButtonClick(View view) {

        Intent intent = new Intent(this, FavoritesViewActivity.class);
        this.startActivity(intent);

    }
}
