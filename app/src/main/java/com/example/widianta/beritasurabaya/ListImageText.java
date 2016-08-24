package com.example.widianta.beritasurabaya;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by widianta on 20-Aug-16.
 */
public class ListImageText extends ArrayAdapter<ImageText> {
    private Activity _context;
    private MainActivity _mainActivity;

    public ListImageText(MainActivity mainActivity, ArrayList<ImageText> imageText) {
        super((Activity) mainActivity, R.layout.listimagetext, imageText);
        // TODO Auto-generated constructor stub
        _context = (Activity) mainActivity;
        _mainActivity = mainActivity;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = _context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listimagetext, null,true);
        rowView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewOnClick) {
                EditText editText = (EditText) viewOnClick.findViewById(R.id.idberita);
                _mainActivity.openBeritaDetailAsync(editText.getText().toString());
            }
        });

        TextView txtTitle = (TextView) rowView.findViewById(R.id.item);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        EditText editText = (EditText) rowView.findViewById(R.id.idberita);

        ImageText imageText = getItem(position);
        editText.setText(imageText.getId());
        txtTitle.setText(imageText.getJudul());
        try {
            imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL(imageText.getImage()).getContent()));
        } catch (IOException e) {
            imageView.setImageResource(R.drawable.ic_local_florist_black_24dp);
        }
        return rowView;
    };
}
