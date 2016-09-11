package com.example.widianta.beritasurabaya;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by widianta on 20-Aug-16.
 */
public class ListImageTextRalat extends ArrayAdapter<ImageText> {
    private Activity _context;
    private MainActivity _mainActivity;
    private List<PositionListImageText> positionListImageTexts;
    private String selectedID;
    private View viewSelected;

    public ListImageTextRalat(MainActivity mainActivity, ArrayList<ImageText> imageText) {
        super((Activity) mainActivity, R.layout.listimagetextralat, imageText);
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
        View rowView = inflater.inflate(R.layout.listimagetextralat, null,true);
        rowView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewOnClick) {
                EditText editText = (EditText) viewOnClick.findViewById(R.id.idberita);
                _mainActivity.openBeritaDetailAsync(editText.getText().toString());
            }
        });

        ((Button) rowView.findViewById(R.id.bertiasayahapus)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewOnClick) {
                RelativeLayout relativeLayout = (RelativeLayout) viewOnClick.getParent();
                LinearLayout linearLayout = (LinearLayout) relativeLayout.getParent();
                selectedID = ((EditText) linearLayout.findViewById(R.id.idberita)).getText().toString();
                new AlertDialog.Builder(_context)
                        .setTitle("Yakin Akan Dihapus")
                        .setMessage("Apakah Berita ".concat(((TextView) linearLayout.findViewById(R.id.item)).getText().toString()).concat(" akan dihapus ?"))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                _mainActivity.deleteBerita(selectedID);
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });
        ((Button) rowView.findViewById(R.id.beritasayaralat)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewOnClick) {
                RelativeLayout relativeLayout = (RelativeLayout) viewOnClick.getParent();
                LinearLayout linearLayout = (LinearLayout) relativeLayout.getParent();
                selectedID = ((EditText) linearLayout.findViewById(R.id.idberita)).getText().toString();
                _mainActivity.prePareEditBerita(selectedID);
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
