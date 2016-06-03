package com.singit.shays.singit;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
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
import android.util.Log;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private GridView gridView;
    private SingItDBHelper db;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private LyricsAPI api;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private static final String TAG = "SingDebug";
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "on create");
        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(this);

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

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

    public void selectDrawerItem(MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.home:
                Log.d(TAG,"home");
                break;
            case R.id.action_favorites:
                Log.d(TAG,"action_favorites");
                break;
            case R.id.action_about:
                Log.d(TAG,"about");
                break;
            default:
                Log.d(TAG,"home");
                break;
        }


        // Highlight the selected item has been done by NavigationView

        menuItem.setChecked(true);
        // Set action bar title

        setTitle(menuItem.getTitle());
        // Close the navigation drawer

        drawerLayout.closeDrawers();
    }


    public void onMoreSearchesButtonClick(View view) {

        Intent intent = new Intent(this, MoreSearchesActivity.class);
        this.startActivity(intent);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
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

        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setHintTextColor(Color.WHITE);
        ((EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text)).setTextColor(Color.WHITE);

        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ArrayList<LyricsRes> last_searches = db.get_last_searched_songs();
        gridView.setAdapter(new CustomGrid(this, last_searches));
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        selectDrawerItem(item);
        return false;
    }
}
