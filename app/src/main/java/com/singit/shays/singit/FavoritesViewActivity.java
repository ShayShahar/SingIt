package com.singit.shays.singit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class FavoritesViewActivity extends AppCompatActivity {

    private SingItDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SingItDBHelper(this);
        ArrayList<LyricsRes> favorites = dbHelper.get_favorite_songs();
        ListAdapter adapter = new CustomAdapter(this,favorites);
        ListView list = (ListView) findViewById(R.id.favoritesList);
        list.setAdapter(adapter);

    }

}
