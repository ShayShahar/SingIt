package com.singit.shays.singit;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoSuggestionsActivity extends AppCompatActivity {

    private List<VideoItem> searchResults;
    private ListView videosFound;
    private Handler handler;
    private static final String TAG = "SingDebug";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_suggestions);
        videosFound = (ListView)findViewById(R.id.videos_found);
        String query = getIntent().getStringExtra("query");
        Log.d(TAG,"[Query recieved] " + query);
        handler = new Handler();
        searchOnYoutube(query);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.video_suggest);
    }

    private void searchOnYoutube(final String keywords){
        new Thread(){
            public void run(){
                YoutubeConnector yc = new YoutubeConnector(VideoSuggestionsActivity.this);
                searchResults = yc.search(keywords);
                handler.post(new Runnable(){
                    public void run(){
                        updateVideosFound();
                    }
                });
            }
        }.start();
    }

    private void updateVideosFound() {
        ArrayAdapter<VideoItem> adapter = new ArrayAdapter<VideoItem>(getApplicationContext(), R.layout.video_item, searchResults) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.video_item, parent, false);
                }
                ImageView thumbnail = (ImageView) convertView.findViewById(R.id.video_thumbnail);
                TextView title = (TextView) convertView.findViewById(R.id.video_title);
                TextView description = (TextView) convertView.findViewById(R.id.video_description);

                VideoItem searchResult = searchResults.get(position);

                Picasso.with(getApplicationContext()).load(searchResult.getThumbnailURL()).into(thumbnail);
                title.setText(searchResult.getTitle());
                description.setText(searchResult.getDescription());
                return convertView;
            }
        };

        videosFound.setAdapter(adapter);
    }
}
