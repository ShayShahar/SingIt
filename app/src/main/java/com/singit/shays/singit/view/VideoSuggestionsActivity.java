package com.singit.shays.singit.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.singit.shays.singit.R;
import com.singit.shays.singit.youtube.VideoItem;
import com.singit.shays.singit.youtube.YoutubeConnector;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shays on 24/06/2016.
 * The class implements YoutubeAPI and includes the result list of available video clips from youtube.
 */
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
        addClickListener();
        String query = getIntent().getStringExtra("query");
        Log.d(TAG,"[Query recieved] " + query);
        handler = new Handler();
        searchOnYoutube(query);
        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle(R.string.video_suggest);
    }

    /**
     * Search videos on youtube with the requested keywords.
     * @param keywords
     */
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

    /**
     * Update the available videos list with the search result.
     */
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

    /**
     * Set item click listener to results list.
     */
    private void addClickListener(){
        videosFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                Intent intent = new Intent(VideoSuggestionsActivity.this, PlayerActivity.class);
                intent.putExtra("videoItm", searchResults.get(pos));
                startActivity(intent);
            }
        });
    }
}
