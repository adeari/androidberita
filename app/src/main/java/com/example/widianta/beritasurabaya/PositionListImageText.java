package com.example.widianta.beritasurabaya;

import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by widianta on 31-Aug-16.
 */
public class PositionListImageText {
    private int position;
    private View view;

    public PositionListImageText(int position1, View view1) {
        position = position1;
        view = view1;
    }

    public int getPosition(){
        return position;
    }

    public View getView(){
        return view;
    }
}
