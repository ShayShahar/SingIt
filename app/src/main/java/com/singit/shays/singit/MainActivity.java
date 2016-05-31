package com.singit.shays.singit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ProgressBar spinner;
    private TextView songName;
    private LyricsAPI api;
    private String name;
    private ArrayList<LyricsRes> result;
    private SingItDBHelper db;

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    private String lyrics;
    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.db = new SingItDBHelper(this);
        LyricsRes lyrics1 = new LyricsRes("Song name test","Author name test","Song song song...","http://www.api.com/12545","http://www.api.com/12545",
                1);
        LyricsRes lyrics11 = new LyricsRes("Song name test","Author name test","Song song song...","http://www.api.com/12545","http://www.api.com/12545",
                2);
        LyricsRes lyrics111 = new LyricsRes("Song name test","Author name test","Song song song...","http://www.api.com/12545","http://www.api.com/12545",
                3);
        LyricsRes lyrics2 = new LyricsRes("Song name test222","Author name test222","Song song song...22","http://www.api.com/1254522","http://www.api.com/1254522",
                654321);
        this.db.insert_song_to_favorites_table(lyrics2, "/data/data/...","/data/data/...");
        this.db.insert_song_to_favorites_table(lyrics2, "/data/data/...","/data/data/...");
        this.db.insert_song_to_favorites_table(lyrics2, "/data/data/...","/data/data/...");
        this.db.insert_song_to_last_searches(lyrics1,   "/data/data/...1","/data/data/...");
        this.db.insert_song_to_last_searches(lyrics11,  "/data/data/...2","/data/data/...");
        this.db.insert_song_to_last_searches(lyrics111, "/data/data/...3","/data/data/...");
        /*ArrayList<LyricsRes> LyricsRes_list;
        LyricsRes_list = this.db.get_last_searched_songs();

        for (LyricsRes lyrics:LyricsRes_list)
        {
            Log.d(TAG, lyrics.title);
            Log.d(TAG, lyrics.artist);
            Log.d(TAG, lyrics.imageURL);
            Log.d(TAG, lyrics.thumbnailURL);
            Log.d(TAG, String.valueOf(lyrics.id));
        }*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);

        Log.d(TAG, "onCreate() Start");
        ArrayList<LyricsRes> last_searches = db.get_last_searched_songs();
        ListAdapter adapter = new CustomAdapter(this,last_searches);
        ListView list = (ListView) findViewById(R.id.lastSearchesList);
        list.setAdapter(adapter);

        songName  = (TextView) findViewById(R.id.songNameTxt);
        api = new LyricsAPI();

        Log.d(TAG, "onCreate() Finish");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void onClickSearchLyricsButton(View view){

        spinner.setVisibility(View.VISIBLE);

        Log.d(TAG, "onClickSearchLyricsButton() Start");

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "interent connected");
        } else {
            Toast.makeText(getApplicationContext(),"Error: No internet connection",Toast.LENGTH_LONG).show();
            spinner.setVisibility(View.GONE);
            return;
        }

        if (songName.getText().toString().isEmpty() || songName.getText().toString() == null){
            spinner.setVisibility(View.GONE);
            return;
        }

        name = songName.getText().toString();

        new SearchLyrics().execute();
    }


    class SearchLyrics extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (result != null){
                LyricsResWrapper wrapper = new LyricsResWrapper(result);
                Log.d(TAG,"create wrapper");
                Intent intent = new Intent(MainActivity.this,SearchViewActivity.class);
                Log.d(TAG,"create intent");
                intent.putExtra("lyrics",wrapper);
                intent.putExtra("name",name);
                Log.d(TAG,"put extra");
                startActivity(intent);
                Log.d(TAG,"Intent search result");
                spinner.setVisibility(View.GONE);
            }

            else{
                Toast.makeText(getApplicationContext(),"Server connection error",Toast.LENGTH_LONG).show();
                spinner.setVisibility(View.GONE);

            }

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Log.d(TAG, "Searching lyrics...");
                result = new ArrayList<LyricsRes>(api.Search(name));
                Log.d(TAG,"lyrics recieved");
            }catch (Exception e) {
                Log.d(TAG, "Server error");
            }

            return null;
        }
    }
}
