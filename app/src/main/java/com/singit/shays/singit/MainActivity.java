package com.singit.shays.singit;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView songName;
    private LyricsAPI api;
    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Log.d(TAG, "onCreate() Start");
        String[] items = {"Something", "SDFSD", "sDSF", "SGSDG"};
        ListAdapter adapter = new CustomAdapter(this,items);
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

        Log.d(TAG, "onClickSearchLyricsButton() Start");

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d(TAG, "interent connected");
        } else {
            Toast.makeText(getApplicationContext(),"Error: No internet connection",Toast.LENGTH_LONG).show();
            return;
        }

        if (songName.getText().toString().isEmpty() || songName.getText().toString() == null){
            return;
        }
        String name = songName.getText().toString();

        try{
            String lyrics = api.getLyrics(name);
            System.out.println ("1");
            Intent intent = new Intent(this,LyricsView.class);
            System.out.println ("2");
            intent.putExtra("lyrics",lyrics);
            System.out.println ("3");
            startActivity(intent);
            System.out.println ("Success");

        }catch (Exception e){
            Log.d(TAG, "onClickSearchLyricsButton() Error");
            System.out.println ("Error");
        }



    }
}
