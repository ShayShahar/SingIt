package com.singit.shays.singit;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by DalitB on 26/05/16.
 */
public class LyricsRes implements Serializable
{
    private static final long serialVersionUID = 1L;
    String title, artist, lyrics, imageURL, thumbnailURL;
    int id;

    /**
     * Constructor
     * @param Title
     * @param Artist
     * @param Lyrics
     * @param imageURL
     * @param thumbnailURL
     * @param id
     */
    LyricsRes (String Title, String Artist, String Lyrics, String imageURL,String thumbnailURL, int id)
    {
        this.title= Title;
        this.artist= Artist;
        this.lyrics= Lyrics;
        this.imageURL= imageURL;
        this.id= id;
        this.thumbnailURL= thumbnailURL;
    }
}
