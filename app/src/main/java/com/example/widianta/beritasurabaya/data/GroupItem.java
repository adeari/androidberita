package com.example.widianta.beritasurabaya.data;

/**
 * Created by awidiyadew on 12/09/16.
 */
public class GroupItem extends BaseItem {
    private int mLevel;

    public GroupItem(String name, String functionName, int imageMenu) {
        super(name, functionName, imageMenu);
        mLevel = 0;
    }

    public void setLevel(int level){
        mLevel = level;
    }

    public int getLevel(){
        return mLevel;
    }
}
