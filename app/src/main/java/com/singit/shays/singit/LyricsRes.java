package com.singit.shays.singit;

/**
 * Created by DalitB on 26/05/16.
 */
public class LyricsRes
{
    String title, artist, lyrics, imageURL ;
    int id;

    LyricsRes (String Title, String Artist, String Lyrics, String imageURL, int id)
    {
        this.title = Title;
        this.artist = Artist;
        this.lyrics = Lyrics;
        this.imageURL= imageURL;
        this.id= id;
    }
}
