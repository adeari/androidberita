package com.example.widianta.beritasurabaya;

/**
 * Created by widianta on 11-Sep-16.
 */
public class KomentarText {
    private String komentar;
    private String image;
    private String id;
    private String useridinput;
    private String idberita;
    private String isAccess;

    public KomentarText(String komentar1, String image1, String id1, String useridinput1, String idberita1, String isAccess1) {
        komentar = komentar1;
        image = image1;
        id = id1;
        useridinput = useridinput1;
        idberita = idberita1;
        isAccess = isAccess1;
    }
    public String getKomentar() {
        return komentar;
    }
    public String getImage() {
        return image;
    }
    public String getId() {
        return id;
    }
    public String getUseridinput() {
        return useridinput;
    }
    public String getIdberita() {
        return idberita;
    }
    public String getIsAccess() {
        return isAccess;
    }
}
