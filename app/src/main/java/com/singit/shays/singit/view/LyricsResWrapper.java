package com.singit.shays.singit.view;

import com.singit.shays.singit.entities.LyricsRes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shays on 30/05/2016.
 */
public class LyricsResWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<LyricsRes> itemDetails;

    public LyricsResWrapper(ArrayList<LyricsRes> items) {
        this.itemDetails = items;
    }

    public ArrayList<LyricsRes> getItemDetails() {
        return itemDetails;
    }
}
