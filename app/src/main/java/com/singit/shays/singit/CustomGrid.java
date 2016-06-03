package com.singit.shays.singit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shays on 02/06/2016.
 */
public class CustomGrid extends BaseAdapter {


    private Context context;
    private ArrayList<LyricsRes> result;
    private Bitmap bmp;
    private ImageView img;
    private TextView artist;
    private TextView song;
    private static LayoutInflater inflater = null;

    public CustomGrid(MainActivity mainActivity, ArrayList<LyricsRes> resArrayList) {
        result=resArrayList;
        context=mainActivity;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = inflater.inflate(R.layout.custom_grid, null);
            artist =(TextView) grid.findViewById(R.id.artistNameGrid);
            song =(TextView) grid.findViewById(R.id.songNameGrid);
            img =(ImageView) grid.findViewById(R.id.coverImage);
            artist.setText(result.get(position).artist);
            song.setText(result.get(position).title);

            new DownloadImageTask(img)
                    .execute(result.get(position).imageURL);
        } else {
            grid = (View) convertView;
        }

        return grid;
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
                bmp = mIcon11;

            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
