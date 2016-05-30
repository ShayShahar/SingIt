package com.singit.shays.singit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class LyricsViewActivity extends AppCompatActivity {


    private static final String TAG = "SingDebug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_view2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Log.d(TAG, "get inent");
        LyricsRes lyrics = (LyricsRes)getIntent().getSerializableExtra("view");
        Log.d(TAG, "lyrics deserialized");
        Log.d(TAG,lyrics.artist);
        //ImageView artist = (ImageView)
        TextView show = (TextView) findViewById(R.id.lyricsTextView);
        show.setText(lyrics.artist);
        //show.setText(lyrics.lyrics);

    }
}
