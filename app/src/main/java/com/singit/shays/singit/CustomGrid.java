package com.singit.shays.singit;

import android.content.Context;
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

    private static final String TAG = "SingDebug";
    private Context context;
    private ArrayList<LyricsRes> result;
    private ImageView img;
    private TextView artist;
    private TextView song;
    private static LayoutInflater inflater = null;

    public CustomGrid(MainActivity mainActivity, ArrayList<LyricsRes> resArrayList) {
        result = resArrayList;
        context = mainActivity;
        Log.d(TAG,result.get(0).title);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(TAG,"view");

        View grid;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            Log.d(TAG,"create view");
            grid = inflater.inflate(R.layout.custom_grid, null);
            artist =(TextView) grid.findViewById(R.id.artistNameGrid);
            song =(TextView) grid.findViewById(R.id.songNameGrid);
            img =(ImageView) grid.findViewById(R.id.coverImage);
            Log.d(TAG,result.get(position).artist);
            artist.setText(result.get(position).artist);
            song.setText(result.get(position).title);
            if (result.get(0).type == LyricsRes.Type.SEARCH){
                img.setImageResource(R.drawable.no_search);
            }
            else if (result.get(0).type == LyricsRes.Type.FAV) {
                img.setImageResource(R.drawable.no_fav);
            }
            else{
                if (result.get(position).image == null){
                    new DownloadImageTask(img)
                            .execute(result.get(position).imageURL);
                }
                else{
                    img.setImageBitmap(result.get(position).image);
                }
            }

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
            } catch (Exception e) {
                e.printStackTrace();
                mIcon11 = BitmapFactory.decodeResource(context.getResources(),R.drawable.no_img);
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
