package com.example.widianta.beritasurabaya;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by widianta on 20-Aug-16.
 */
public class ListImageText extends ArrayAdapter<ImageText> {
    private Activity _context;
    private MainActivity _mainActivity;
    private List<PositionListImageText> positionListImageTexts;

    public ListImageText(MainActivity mainActivity, ArrayList<ImageText> imageText) {
        super((Activity) mainActivity, R.layout.listimagetext, imageText);
        // TODO Auto-generated constructor stub
        _context = (Activity) mainActivity;
        _mainActivity = mainActivity;
        positionListImageTexts = new ArrayList<PositionListImageText>();
    }

    public void clearList(){
        positionListImageTexts.clear();
    }

    public boolean isPositionExist(int position) {
        for (PositionListImageText positionListImageText : positionListImageTexts) {
            if (positionListImageText.getPosition() == position) {
                return true;
            }
        }
        return false;
    }

    public View getView(final int position, View view, ViewGroup parent) {
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
        ProgressBar listItemTextProggressBar = (ProgressBar) rowView.findViewById(R.id.listItemTextProggressBar);
        listItemTextProggressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        ImageText imageText = getItem(position);
        editText.setText(imageText.getId());
        txtTitle.setText(imageText.getJudul());

        if (imageText.getImage().length() > 0) {
            if (!isPositionExist(position)) {
                listItemTextProggressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                positionListImageTexts.add(new PositionListImageText(position, rowView));
            }
            Glide.with(imageView.getContext())
                    .load(imageText.getImage())
                    .placeholder(R.drawable.ic_local_florist_black_24dp)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            for (PositionListImageText positionListImageText: positionListImageTexts) {
                                if (positionListImageText.getPosition() == position) {
                                    positionListImageText.getView().findViewById(R.id.listItemTextProggressBar).setVisibility(View.GONE);
                                    positionListImageText.getView().findViewById(R.id.icon).setVisibility(View.VISIBLE);
                                }
                            }
                            return false;
                        }
                    })
                    .into(imageView);
        }
        return rowView;
    };
}
