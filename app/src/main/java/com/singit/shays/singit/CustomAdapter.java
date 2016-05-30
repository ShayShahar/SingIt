package com.singit.shays.singit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shays on 20/05/2016.
 */
class CustomAdapter extends ArrayAdapter<LyricsRes> {

    CustomAdapter(Context context, ArrayList<LyricsRes> details){
        super(context,R.layout.custom_row,details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);
        LyricsRes res = getItem(position);

        TextView artistText = (TextView) customView.findViewById(R.id.artistText);
        TextView songText = (TextView) customView.findViewById(R.id.songText);
        ImageView artistImage = (ImageView) customView.findViewById(R.id.artistImage);

        artistText.setText(res.artist);
        songText.setText(res.title);

        new DownloadImageTask(artistImage)
                .execute(res.imageURL);
        return customView;
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
