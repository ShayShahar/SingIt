package com.singit.shays.singit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchViewActivity extends AppCompatActivity {

    private static final String TAG = "SingDebug";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        LyricsResWrapper wrapper = (LyricsResWrapper)getIntent().getSerializableExtra("lyrics");
        ArrayList<LyricsRes> lyrics = wrapper.getItemDetails();
        ListAdapter adapter = new CustomAdapter(this,lyrics);
        final ListView list = (ListView) findViewById(R.id.searchResultList);
        list.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View container, int position, long id) {

                Log.d(TAG,"On item click..");
                LyricsRes selected = (LyricsRes)list.getSelectedItem();
                Log.d(TAG,"get object");
                Intent intent = new Intent(SearchViewActivity.this,LyricsViewActivity.class);
                intent.putExtra("view",selected);
                startActivity(intent);
                Log.d(TAG,"Call another activity");

            }
        };



        list.setOnItemClickListener(itemClickListener);
    }



}
