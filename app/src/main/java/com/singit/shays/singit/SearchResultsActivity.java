package com.singit.shays.singit;

import android.app.SearchManager;
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

public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsAPI api = new LyricsAPI();
    private SingItDBHelper dbHelper = new SingItDBHelper(this);
    private EditText search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: SearchResultsActivity");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        search = (EditText) findViewById(R.id.passText);
        handleIntent(getIntent());
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
