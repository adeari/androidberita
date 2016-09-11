package com.example.widianta.beritasurabaya;

/**
 * Created by widianta on 11-Sep-16.
 */
public class KomentarText {
    private String komentar;
    private String image;

    public KomentarText(String komentar1, String image1) {
        komentar = komentar1;
        image = image1;
    }
    public String getKomentar() {
        return komentar;
    }
    public String getImage() {
        return image;
    }
}
