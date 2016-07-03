package com.singit.shays.singit;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView playerView;
    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Log.d(TAG,"[Launch Video]");
        playerView = (YouTubePlayerView)findViewById(R.id.player_view);
        Log.d(TAG,"[Start Initialize]");
        playerView.initialize(ApiKeys.YOUTUBE_WATCH_API_KEY, this);
        Log.d(TAG,"[Finish Initialize]");
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
        Log.d(TAG,"[Launch Failed]");
        Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
        Log.d(TAG,"[Launch Success]");

        if(!restored){
            player.cueVideo(getIntent().getStringExtra("VIDEO_ID"));
        }
    }
}
