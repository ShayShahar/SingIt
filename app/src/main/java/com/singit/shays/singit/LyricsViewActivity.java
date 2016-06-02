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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_view2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new SingItDBHelper(this);
        Intent intent = getIntent();
        lyrics = (LyricsRes)getIntent().getSerializableExtra("view");
        ImageView image = (ImageView) findViewById(R.id.artistImage);
        TextView show = (TextView) findViewById(R.id.lyricsTextView);
        TextView arist = (TextView) findViewById(R.id.artistName);
        TextView song = (TextView) findViewById(R.id.songName);
        song.setText(lyrics.title);
        arist.setText(lyrics.artist);
        show.setText(lyrics.lyrics);
        new DownloadImageTask(image)
                .execute(lyrics.imageURL);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_favorites) {
        }
*/
        return super.onOptionsItemSelected(item);
    }

    public void onClickAddToFavorites(View view) {

        if (dbHelper.insert_song_to_favorites_table(lyrics,"","") == DBResult.OK)
        Toast.makeText(getApplicationContext(),"Lyrics added to favorites!",Toast.LENGTH_LONG).show();
        else{
            Toast.makeText(getApplicationContext(),"Item is already in favorites",Toast.LENGTH_LONG).show();

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
