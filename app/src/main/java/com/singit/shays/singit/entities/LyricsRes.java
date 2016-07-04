package com.singit.shays.singit.entities;

import java.io.Serializable;

/**
 * Created by DalitB on 26/05/16.
 */
public class LyricsRes implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String artist;
    private String lyrics;
    private String imageURL;
    private String thumbnailURL;
    private Type type;
    private int id;

    public enum Type {
        FAV, SEARCH, LYRICS
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LyricsRes(String Title, String Artist, Type type){
        this.title = Title;
        this.artist = Artist;
        this.type = type;
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
    }

}
