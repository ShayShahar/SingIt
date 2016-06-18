package com.singit.shays.singit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Created by lions on 05/06/2016.
 */
public class ImageSaver {

    private String directoryName = "image_directory";
    private String fileName = "image.png";
    private Context context;

    public ImageSaver(Context context) {
        this.context = context;
    }

    public ImageSaver setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public ImageSaver setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
        return this;
    }

    public void save(Bitmap bitmapImage) {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(createFile());
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @NonNull
    private File createFile() {
        File directory = context.getDir(directoryName, Context.MODE_PRIVATE);
        return new File(directory, fileName);
    }

    public Bitmap load() {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(createFile());
            return BitmapFactory.decodeStream(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

/**
 *
 */
class SaveRunnable implements Runnable
{
    private LyricsRes lyrics;
    private Bitmap image, thumbnail;
    private SingItDBHelper db;

    public SaveRunnable(SingItDBHelper db, LyricsRes lyrics, Bitmap image, Bitmap thumbnail)
    {
        this.db = db;
        this.lyrics = lyrics;
        this.image = image;
        this.thumbnail = thumbnail;
    }

    public void run()
    {
        db.save_image(SingItDBHelper.getImageDir(), String.valueOf(lyrics.id), image);
        db.save_image(SingItDBHelper.getThumbnailDir(), String.valueOf(lyrics.id), thumbnail);
    }
}

//class LoadCallable implements Callable<Bitmap>
//{
//    private SingItDBHelper db;
//    private String dictionary;
//    private String song_id;
//
//    public LoadCallable(SingItDBHelper db, String dictionary, String song_id)
//    {
//        this.db = db;
//        this.dictionary = dictionary;
//        this.song_id = song_id;
//    }
//
//    @Override public Bitmap call()
//            throws Exception
//    {
//        return db.load_picture(dictionary,song_id);
//    }
//}