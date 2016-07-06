package com.singit.shays.singit.view;

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
import com.singit.shays.singit.R;
import com.singit.shays.singit.entities.Config;
import com.singit.shays.singit.entities.DBResult;
import com.singit.shays.singit.entities.LyricsRes;
import com.singit.shays.singit.entities.SingItDBHelper;
import com.singit.shays.singit.yandex.translate.language.Language;
import com.singit.shays.singit.yandex.translate.Translate;

import java.io.InputStream;

/**
 * Created by shays on 30/05/2016.
 * The class handles to logics of the lyrics view screen.
 */
public class LyricsViewActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";
    private LyricsRes lyrics;
    private SingItDBHelper dbHelper;
    private boolean isFav = false;
    private android.support.design.widget.FloatingActionButton fab;
    TextView show;
    TextView artist;
    TextView song;

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
        if (dbHelper.is_favorite_song(lyrics.getId()) == DBResult.ITEM_EXISTS){
            isFav = true;
            fab.setImageResource(R.drawable.star_gold);
        }
        ImageView image = (ImageView) findViewById(R.id.artistImage);
        show = (TextView) findViewById(R.id.lyricsTextView);
        artist = (TextView) findViewById(R.id.artistName);
        song = (TextView) findViewById(R.id.songName);
        song.setText(lyrics.getTitle());
        artist.setText(lyrics.getArtist());
        show.setText(lyrics.getLyrics());

        Bitmap bmp = dbHelper.get_image(lyrics.getId());
        if(bmp == null){
            new DownloadImageTask(image)
                    .execute(lyrics.getImageURL());
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

        int id = item.getItemId();

        if (id == R.id.menu_item_share) {

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, show.getText());
            intent.putExtra(Intent.EXTRA_SUBJECT,"Share " + song + " By " + artist);
            this.startActivity(intent);
            return true;
        }

        if (id == R.id.menu_translate){

            try{
                Log.d(TAG,"translate request");
                String translate = show.getText().toString();
                Translate.setKey(Config.YANDEX_API_KEY);
                String translatedText = Translate.execute(translate, Language.ENGLISH, Language.HEBREW);
                show.setText(translatedText);
            }catch(Exception e){
                Log.d(TAG,e.toString());
            }

            return true;
        }

        if (id == R.id.menu_youtube){
            Intent intent = new Intent(LyricsViewActivity.this, VideoSuggestionsActivity.class);
            String qury = artist.getText().toString() + " " + song.getText().toString();
            Log.d(TAG,"[Video request] " + qury);
            intent.putExtra("query", qury);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handles the user's click on floating animated button to add / remove lyrics to / from favorites list.
     * @param view
     */
    public void onClickAddToFavorites(View view) {

        if (isFav == true){
            dbHelper.delete_song_from_favorites(lyrics.getId());
            isFav = false;
            fab.setImageResource(R.drawable.star);
            Toast.makeText(getApplicationContext(),R.string.removed_from_favorites,Toast.LENGTH_LONG).show();

        }
        else{
            if (dbHelper.insert_song_to_favorites_table(lyrics,null,null) == DBResult.OK){
                Toast.makeText(getApplicationContext(),R.string.added_to_favorites,Toast.LENGTH_LONG).show();
                isFav = true;
                fab.setImageResource(R.drawable.star_gold);
            }
        }
    }

    /**
     * DownloadImageTask extends google's AsyncTask,
     * used to download images in background.
     */
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
