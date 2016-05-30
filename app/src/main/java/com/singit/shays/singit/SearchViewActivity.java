package com.singit.shays.singit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SearchViewActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Log.d(TAG,"read intent");
        LyricsResWrapper wrapper = (LyricsResWrapper)getIntent().getSerializableExtra("lyrics");
        Log.d(TAG,"read wrapper");
        final ArrayList<LyricsRes> lyrics = wrapper.getItemDetails();
        Log.d(TAG,"read result array");
        ListAdapter adapter = new CustomAdapter(this,lyrics);
        Log.d(TAG,"create adapter");
        ListView list = (ListView) findViewById(R.id.searchResultList);
        Log.d(TAG,"set view");
        list.setAdapter(adapter);
        Log.d(TAG,"set adapter");

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Log.d(TAG,"On item click..");
                LyricsRes selected = lyrics.get(position);
                LyricsRes pass = selected;
                Log.d(TAG,selected.artist);
                try{
                    pass = api.getLyrics(selected);
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                Intent intent = new Intent(SearchViewActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        };



        list.setOnItemClickListener(itemClickListener);
    }



}
