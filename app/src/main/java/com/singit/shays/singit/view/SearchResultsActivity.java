package com.singit.shays.singit.view;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.singit.shays.singit.R;
import com.singit.shays.singit.adapters.CustomAdapter;
import com.singit.shays.singit.entities.LyricsAPI;
import com.singit.shays.singit.entities.LyricsRes;
import com.singit.shays.singit.entities.SingItDBHelper;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shays on 05/06/2016.
 * The class handles the search result activity logics.
 * The class called by searchable activities.
 */
public class SearchResultsActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsAPI api = new LyricsAPI();
    private SingItDBHelper dbHelper = new SingItDBHelper(this);
    private TextView search;
    private Toolbar toolbar;
    private Bitmap bmp1, bmp2;
    private LyricsRes curr;

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

    /**
     * Handle the search results and initialize the ListView.
     * @param intent
     */
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
                    try{
                        pass = api.getLyrics(search.get(position));
                        curr = pass;
                    }catch(Exception e){
                        Log.d(TAG,e.toString());
                    }
                    new DownloadImageTask().execute(pass.getThumbnailURL(),pass.getImageURL());
                    Intent intent = new Intent(SearchResultsActivity.this,LyricsViewActivity.class);
                    intent.putExtra("view",pass);
                    startActivity(intent);
                    Log.d(TAG,"Call another activity");

                }
            };

            list.setOnItemClickListener(itemClickListener);
        }
    }

    /**
     * Download thumbnails in background.
     */
    class DownloadImageTask extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... urls) {
            String tmb = urls[0];
            String img = urls[1];
            Bitmap mIcon11 = null;
            Bitmap mIcon12 = null;
            try {
                InputStream in = new java.net.URL(tmb).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
                bmp1 = mIcon11;
                in = new java.net.URL(img).openStream();
                mIcon12 = BitmapFactory.decodeStream(in);
                bmp2 = mIcon12;

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void e)  {
            dbHelper.insert_song_to_last_searches_table(curr,bmp2,bmp1);

        }
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
}
