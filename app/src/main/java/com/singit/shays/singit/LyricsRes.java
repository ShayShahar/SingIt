package com.singit.shays.singit;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by DalitB on 26/05/16.
 */
public class LyricsRes implements Serializable
{
    private static final long serialVersionUID = 1L;
    Bitmap image, thumbnail;
    String title, artist, lyrics, imageURL, thumbnailURL;
    Type type;
    int id;

    enum Type {
        FAV, SEARCH, LYRICS
    }

    LyricsRes(String Title, String Artist, Type type){
        this.title = Title;
        this.artist = Artist;
        this.type = type;
        this.thumbnail = null;
        this.image = null;
    }

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
        this.type = Type.LYRICS;
        this.thumbnail = null;
        this.image = null;
    }

    LyricsRes (String Title, String Artist, String Lyrics, String imageURL,
               String thumbnailURL, int id, Bitmap image, Bitmap thumbnail)
    {
        this.title= Title;
        this.artist= Artist;
        this.lyrics= Lyrics;
        this.imageURL= imageURL;
        this.id= id;
        this.thumbnailURL= thumbnailURL;
        this.image = image;
        this.thumbnail = thumbnail;
    }
}
