package com.example.widianta.beritasurabaya;

/**
 * Created by widianta on 20-Aug-16.
 */
public class ImageText {
    private String _id;
    private String _judul;
    private String _image;
    private String _berita;
    private String _kategori;

    public ImageText(String id, String judul, String image) {
       _judul = judul;
        _image = image;
        _id = id;
    }
    public ImageText(String id, String judul, String image, String berita, String kategori) {
       _judul = judul;
        _image = image;
        _id = id;
        _berita = berita;
        _kategori = kategori;
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
    public String getBerita() {
        return _berita;
    }
    public String getKategori() { return _kategori; }
}
