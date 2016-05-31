package com.singit.shays.singit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

    Bitmap bmp;
    private static final String TAG = "SingDebug";

    CustomAdapter(Context context, ArrayList<LyricsRes> details){
        super(context,R.layout.custom_row,details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);
        final LyricsRes result = getItem(position);

        TextView artistText = (TextView) customView.findViewById(R.id.artistText);
        TextView songText = (TextView) customView.findViewById(R.id.songText);
        Log.d(TAG,"set components");
        de.hdodenhof.circleimageview.CircleImageView artistImage = (de.hdodenhof.circleimageview.CircleImageView) customView.findViewById(R.id.profile_image);
        Log.d(TAG,"set image");
        artistText.setText(result.artist);
        songText.setText(result.title);

            new DownloadImageTask(artistImage)
                    .execute(result.imageURL);
            //result.image = bmp;


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
                bmp = mIcon11;
                Log.d(TAG,"image set");

            } catch (Exception e) {
                ImageView image = (ImageView) getView().findViewById(R.id.artistImage);
                InputStream is = getContext().getResources().openRawResource(drawable.no_img);
                Bitmap originalBitmap = BitmapFactory.decodeStream(is);
                bmp = originalBitmap;
                /*bmp = BitmapFactory.decodeResource(getContext().getResources(),
                        R.drawable.no_img);*/
                Log.d(TAG,"no image");
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
