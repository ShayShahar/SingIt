package com.singit.shays.singit;

import android.os.StrictMode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class LyricsAPI {
    private final String API_KEY = "013339f8cb75cabdbb35a5aeae1f77";

    public LyricsAPI()
    {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    public String getLyrics(String query) throws IOException
    {
        String encodedQuery = URLEncoder.encode(query, "UTF-8");
        String apiURL = "http://api.lyricsnmusic.com/songs?api_key="+this.API_KEY+"&q="+encodedQuery;
        String apiResult = httpRequest(apiURL);
        String songUrl = extractUrl(apiResult);

        if(songUrl == null)
            return null;

        String songHtml = httpRequest(songUrl);
        return extractLyrics(songHtml);
    }

    private String extractLyrics(String html)
    {
        int lyricsStartIndex = html.indexOf("<pre itemprop='description'>")+28;
        if(lyricsStartIndex < 0)
            return null;

        int lyricsEndIndex = html.indexOf("</pre>");
        return html.substring(lyricsStartIndex, lyricsEndIndex);
    }

    private String extractUrl(String apiResult)
    {
        int urlStartIndex = apiResult.indexOf("url\":")+6;
        if(urlStartIndex < 0)
            return null;

        int urlEndIndex = apiResult.indexOf('"', urlStartIndex+1);
        return apiResult.substring(urlStartIndex, urlEndIndex);
    }

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