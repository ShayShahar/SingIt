package com.singit.shays.singit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MoreSearchesActivity extends AppCompatActivity {

    private SingItDBHelper dbHelper;
    private Toolbar toolbar;
    private LyricsAPI api;
    private ListView list;
    private ListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_searches);
        api = new LyricsAPI();
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dbHelper = new SingItDBHelper(this);
        ArrayList<LyricsRes> searches = dbHelper.get_last_searched_songs();
        adapter = new CustomAdapter(this,searches);
        list = (ListView) findViewById(R.id.moreSearches);
        list.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                LyricsRes selected = (LyricsRes)list.getItemAtPosition(position);
                LyricsRes pass = selected;

                try{
                    pass = api.getLyrics((LyricsRes)list.getItemAtPosition(position));
                }catch(Exception e){
                }
                Intent intent = new Intent(MoreSearchesActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);

            }
        };

        list.setOnItemClickListener(itemClickListener);
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
        ((EditText)searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);

        return true;
    }

}
