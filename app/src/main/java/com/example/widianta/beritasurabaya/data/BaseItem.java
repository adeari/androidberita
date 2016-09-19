package com.example.widianta.beritasurabaya.data;

/**
 * Created by awidiyadew on 12/09/16.
 */
public class BaseItem {
    private String mName;
    private String functionName;
    private int imageMenu;

    public BaseItem(String name, String functionName1, int imageMenu1) {
        mName = name;
        functionName = functionName1;
        imageMenu = imageMenu1;
    }

    public String getName() {
        return mName;
    }
    public String getFunctionName() {
        return functionName;
    }

    public int getImageMenu() {
        return imageMenu;
    }
}
