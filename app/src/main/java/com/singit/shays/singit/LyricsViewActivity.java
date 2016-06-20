package com.singit.shays.singit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;

public class LyricsViewActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsRes lyrics;
    private SingItDBHelper dbHelper;
    private boolean isFav = false;
    private android.support.design.widget.FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: lyrics view");
        setContentView(R.layout.activity_lyrics_view2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = (android.support.design.widget.FloatingActionButton)findViewById(R.id.fab);
        dbHelper = new SingItDBHelper(this);
        lyrics = (LyricsRes)getIntent().getSerializableExtra("view");
        if (dbHelper.is_favorite_song(lyrics.id) == DBResult.ITEM_EXISTS){
            isFav = true;
            fab.setImageResource(R.drawable.star_gold);
        }
        ImageView image = (ImageView) findViewById(R.id.artistImage);
        TextView show = (TextView) findViewById(R.id.lyricsTextView);
        TextView artist = (TextView) findViewById(R.id.artistName);
        TextView song = (TextView) findViewById(R.id.songName);
        song.setText(lyrics.title);
        artist.setText(lyrics.artist);
        show.setText(lyrics.lyrics);

        Bitmap bmp = dbHelper.get_image(lyrics.id);
        if(bmp == null){
            new DownloadImageTask(image)
                    .execute(lyrics.imageURL);
        }
        else {
            image.setImageBitmap(bmp);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lyrics_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return super.onOptionsItemSelected(item);
    }

    public void onClickAddToFavorites(View view) {

        if (isFav == true){
            dbHelper.delete_song_from_favorites(lyrics.id);
            isFav = false;
            fab.setImageResource(R.drawable.star);
            Toast.makeText(getApplicationContext(),"Lyrics removed from favorites",Toast.LENGTH_LONG).show();

        }
        else{
            if (dbHelper.insert_song_to_favorites_table(lyrics,null,null) == DBResult.OK){
                Toast.makeText(getApplicationContext(),"Lyrics added to favorites!",Toast.LENGTH_LONG).show();
                isFav = true;
                fab.setImageResource(R.drawable.star_gold);
            }
        }
    }

    class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
