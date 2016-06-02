package com.singit.shays.singit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LyricsAPI {
    private final String API_KEY = "e971f3a40404f1b0eacaa5e567b28736";
    private final String baseURL = "http://api.musixmatch.com/ws/1.1/";
    private static final String TAG = "SingDebug";
    private static HashMap<Integer,LyricsRes> lyricsCache = new HashMap<Integer,LyricsRes>();
    private static HashMap<String,List<LyricsRes>> searchesCache = new HashMap<String,List<LyricsRes>>();

    public LyricsAPI()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    /**
     * Search the song lyrics from the query, return List of songs suggestions
     * @param query
     * @return List of LyricsRes
     * @throws IOException
     * @throws JSONException
     */
    public List<LyricsRes> Search (String query) throws IOException, JSONException {
        if (searchesCache.containsKey(query)) // check if exist in the lyricsCache
        {
            return searchesCache.get(query);
        }
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        String searchURL = baseURL + "track.search?apikey="+this.API_KEY+"&f_has_lyrics=1&s_artist_rating=desc&s_track_rating=desc&q=" + encodedQuery;
        String json = httpRequest(searchURL);
        List<LyricsRes> resList = extractList(json);
        searchesCache.put(query, resList);
        return resList;
    }

    /**
     * find the lyrics, fill it in the LyricsRes song object and return the full object.
     * @param song
     * @return full object- LyricsRes song (with the lyrics)
     * @throws IOException
     * @throws JSONException
     */
    public LyricsRes getLyrics(LyricsRes song) throws IOException, JSONException {
        if (lyricsCache.containsKey(song.id)) // check if exist in the lyricsCache
        {
            return lyricsCache.get(song.id);
        }
        String lyricsURL = baseURL + "track.lyrics.get?track_id="+song.id+"&apikey="+this.API_KEY;
        String json = httpRequest(lyricsURL);
        JSONObject jsnOb = new JSONObject(json);

        song.lyrics =jsnOb.getJSONObject("message").getJSONObject("body").getJSONObject("lyrics").getString("lyrics_body");
        song.lyrics = song.lyrics.split("\\*\\*\\*\\*\\*\\*\\*")[0];

        //add song to cache
        lyricsCache.put(song.id, song);
        return song;
    }

    /**
     * parse the json to ArrayList of LyricsRes
     * @param json
     * @return List of LyricsRes
     * @throws JSONException
     */
    private List<LyricsRes> extractList(String json) throws JSONException
    {
        JSONObject jsnOb = new JSONObject(json);
        JSONArray arrayOfJacksons = jsnOb.getJSONObject("message").getJSONObject("body").getJSONArray("track_list");

        List<LyricsRes> listRes = new ArrayList<LyricsRes>();
        for (int i=0; i<arrayOfJacksons.length(); i++)
        {
            JSONObject track = arrayOfJacksons.getJSONObject(i).getJSONObject("track");
            LyricsRes tmp= new LyricsRes (track.getString("track_name"), track.getString("artist_name"), "", track.getString("album_coverart_500x500"), track.getString("album_coverart_100x100"), track.getInt("track_id") );
            listRes.add (tmp);
        }

        return listRes;
    }

    /**
     * This is a skeleton method
     * @param html
     * @return
     */
    private String Dinosaur(String html)
    {
        int lyricsStartIndex = html.indexOf("<pre itemprop='description'>")+28;
        if(lyricsStartIndex < 0)
            return null;

        int lyricsEndIndex = html.indexOf("</pre>");
        return html.substring(lyricsStartIndex, lyricsEndIndex);
    }

    /**
     * basic http GET Request
     * @param url
     * @return String response
     * @throws IOException
     */
    private String httpRequest(String url) throws IOException
    {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        con.setRequestMethod("GET");

        con.setReadTimeout(15*1000);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));


        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine+'\n');
        }
        in.close();

        return response.toString();

    }
    
}