package com.example.widianta.beritasurabaya;

/**
 * Created by widianta on 20-Aug-16.
 */
public class ImageText {
    private String _id;
    private String _judul;
    private String _image;

    public ImageText(String id, String judul, String image) {
       _judul = judul;
        _image = image;
        _id = id;
    }
    public String getId() {
        return _id;
    }
    public String getJudul() {
        return _judul;
    }
    public void setJudul(String judul) {
        _judul = judul;
    }
    public String getImage() {
        return _image;
    }
    public void setImage(String image) {
        _image = image;
    }
}
