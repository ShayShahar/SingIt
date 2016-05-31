package com.singit.shays.singit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsAPI api = new LyricsAPI();
    private SingItDBHelper dbHelper = new SingItDBHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText search = (EditText) findViewById(R.id.passText);
        Intent intent = getIntent();
        search.setText(intent.getStringExtra("name"));
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
                Log.d(TAG,Integer.toString(selected.id));
                try{
                    pass = api.getLyrics(lyrics.get(position));
                }catch(Exception e){
                    Log.d(TAG,e.toString());
                }
                dbHelper.insert_song_to_last_searches_table(pass,"","");
                Intent intent = new Intent(SearchViewActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",pass);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        };

        list.setOnItemClickListener(itemClickListener);
    }
}
