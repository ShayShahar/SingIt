package com.singit.shays.singit.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.singit.shays.singit.entities.LyricsRes;
import com.singit.shays.singit.R;
import com.singit.shays.singit.entities.SingItDBHelper;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by shays on 20/05/2016.
 * This class connects between LyricsRes list to Android ListView component.
 */
public class CustomAdapter extends ArrayAdapter<LyricsRes> {

    Bitmap bmp;
    private static final String TAG = "SingDebug";
    private int lastPosition = -1;
    private SingItDBHelper dbHelper;
    private Context context;

    /**
     * C'tor of CustomAdapter class
     * @param context
     * @param details
     */
    public CustomAdapter(Context context,ArrayList<LyricsRes> details){
        super(context, R.layout.custom_row,details);
        this.context = context;
    }

    /**
     * See ArrayAdapter.getView
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        dbHelper = new SingItDBHelper(context);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);
        final LyricsRes result = getItem(position);

        TextView artistText = (TextView) customView.findViewById(R.id.artistText);
        TextView songText = (TextView) customView.findViewById(R.id.songText);
        de.hdodenhof.circleimageview.CircleImageView artistImage = (de.hdodenhof.circleimageview.CircleImageView) customView.findViewById(R.id.profile_image);
        Bitmap bmp2 = dbHelper.get_thumbnail(result.getId());

        if (bmp2 == null){
            new DownloadImageTask(artistImage)
                    .execute(result.getThumbnailURL());
        }
        else{
            artistImage.setImageBitmap(bmp2);
        }

        artistText.setText(result.getArtist());
        songText.setText(result.getTitle());

        Animation animation = AnimationUtils.loadAnimation(getContext(),(position>lastPosition)? R.anim.up_from_buttom : R.anim.down_from_top);
        customView.startAnimation(animation);
        lastPosition = position;

        return customView;
    }


    /**
     * DownloadImageTask extends google's AsyncTask
     * This task download the artist's cover in background.
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
                bmp = mIcon11;
                Log.d(TAG,"image set");

            } catch (Exception e) {
                Log.d(TAG,"no image");
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}
