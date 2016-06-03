package com.singit.shays.singit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsAPI api = new LyricsAPI();
    private SingItDBHelper dbHelper = new SingItDBHelper(this);
    private TextView search;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: SearchResultsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        search = (TextView) findViewById(R.id.passText);
        handleIntent(getIntent());
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            search.setText(query);
            ArrayList<LyricsRes> result = new ArrayList<>();
            try{
                result = new ArrayList<>(api.Search(query));
            }catch (Exception e) {

            }

            final ArrayList<LyricsRes> search = result;

            ListAdapter adapter = new CustomAdapter(this,search);
            ListView list = (ListView) findViewById(R.id.searchResultList);
            list.setAdapter(adapter);
            list.setEmptyView(findViewById(R.id.emptySearch));
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                    Log.d(TAG,"On item click..");
                    LyricsRes selected = search.get(position);
                    LyricsRes pass = selected;
                    Log.d(TAG,selected.artist);
                    Log.d(TAG,Integer.toString(selected.id));
                    try{
                        pass = api.getLyrics(search.get(position));
                    }catch(Exception e){
                        Log.d(TAG,e.toString());
                    }
                    dbHelper.insert_song_to_last_searches_table(pass,"","");
                    Intent intent = new Intent(SearchResultsActivity.this,LyricsViewActivity.class);
                    intent.putExtra("view",pass);
                    startActivity(intent);
                    Log.d(TAG,"Call another activity");

                }
            };

            list.setOnItemClickListener(itemClickListener);
        }
    }
}
