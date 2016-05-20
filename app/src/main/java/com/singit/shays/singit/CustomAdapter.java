package com.singit.shays.singit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by shays on 20/05/2016.
 */
class CustomAdapter extends ArrayAdapter<String> {

    CustomAdapter(Context context, String[] details){
        super(context,R.layout.custom_row,details);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_row,parent,false);

        String artist = getItem(position);
        TextView artistText = (TextView) customView.findViewById(R.id.artistText);
        TextView songText = (TextView) customView.findViewById(R.id.songText);
        ImageView artistImage = (ImageView) customView.findViewById(R.id.artistImage);

        artistText.setText(artist);
        artistImage.setImageResource(R.drawable.no_img);

        return customView;
    }
}
