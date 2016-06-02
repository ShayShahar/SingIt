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


    private Context context;
    private ArrayList<LyricsRes> result;
    private TextView artist;
    private TextView song;
    private ImageView img;
    private Bitmap bmp;


    private static LayoutInflater inflater=null;
    public CustomGrid(MainActivity mainActivity, ArrayList<LyricsRes> resArrayList) {
        result=resArrayList;
        context=mainActivity;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return result.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View rowView;

        rowView = inflater.inflate(R.layout.custom_grid, null);
        artist =(TextView) rowView.findViewById(R.id.artistNameGrid);
        song =(TextView) rowView.findViewById(R.id.songNameGrid);
        img =(ImageView) rowView.findViewById(R.id.coverImage);

        artist.setText(result.get(position).artist);
        song.setText(result.get(position).title);

        new DownloadImageTask(img)
                .execute(result.get(position).imageURL);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
               // Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
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
