package com.example.widianta.beritasurabaya;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private MainActivity mainActivity;
    private ProgressBar populerProgressBar;
    private ListView populerListView;
    private DrawerLayout drawer;
    private ListImageText _listImageText;
    private GetPopulerData getPopulerData;
    private ProgressBar beritaAddProgressbar;
    private ProgressBar beritaDetailProgressBar;
    private ImageView beritaAddImageimageView;
    private File fileUpload;
    private SecureRandom secureRandom;
    private String viewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secureRandom = new SecureRandom();
        mainActivity = this;
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        populerListView =(ListView) findViewById(R.id.listPopuler);

        populerProgressBar = (ProgressBar) findViewById(R.id.progressBarPopuler);
        populerProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);

        beritaAddProgressbar = (ProgressBar) findViewById(R.id.beritaAddProgressbar);
        beritaAddProgressbar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaAddProgressbar.getLayoutParams().height = 200;
        beritaAddProgressbar.setVisibility(View.GONE);

        beritaDetailProgressBar = (ProgressBar) findViewById(R.id.beritadetailProgressBar);
        beritaDetailProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaDetailProgressBar.setVisibility(View.GONE);

        beritaAddImageimageView = (ImageView) findViewById(R.id.beritaAddImage);
        beritaAddImageimageView.getLayoutParams().height = 200;
        beritaAddImageimageView.setVisibility(View.GONE);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        _arrayImageTexts = new ArrayList<ImageText>();
        _listImageText = new ListImageText(mainActivity, _arrayImageTexts);
        populerListView.setAdapter(_listImageText);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] {"Umum", "Acara", "Pengaduan"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.beritaaddSpinner)).setAdapter(dataAdapter);

        Button beritaaddGaleriButton = (Button) findViewById(R.id.beritaaddGaleriButton);
        beritaaddGaleriButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                }
        );

        Button beritaaddKameraButton = (Button) findViewById(R.id.beritaaddKameraButton);
        beritaaddKameraButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7777);
                    }
                }
        );

        Button beritaaddSimpan = (Button) findViewById(R.id.beritaaddSimpan);
        beritaaddSimpan.setOnClickListener(
                new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                    if (((EditText) findViewById(R.id.beritaaddJudulBerita)).getText().length() == 0) {
                        Toast.makeText(MainActivity.this, "Tulis Judul Berita",
                                Toast.LENGTH_SHORT).show();
                    } else if (((EditText) findViewById(R.id.beritaaddDeskripsi)).getText().length() == 0) {
                       Toast.makeText(MainActivity.this, "Tulis Deskripsi Berita",
                               Toast.LENGTH_SHORT).show();
                   } else {
                       postBerita getFileNameForUpload = new postBerita();
                       getFileNameForUpload.execute();
                   }
               }
           }
        );
        new GetPopulerData().execute();
        closeLayoutsFirst();
        if (savedInstanceState == null) {
            setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
            viewLayout = "populer";
        } else {
            if (savedInstanceState.getString("viewlayout").equals("beritaadd")) {
                setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
                viewLayout = "beritaadd";
            } else if (savedInstanceState.getString("viewlayout").equals("populer")) {
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
                viewLayout = "populer";
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("viewlayout", viewLayout);
    }

    private class postBerita extends AsyncTask<String, Void, String> {
        private String judulBerita;
        private String deskripsi;
        private String kategori;
        @Override
        protected String doInBackground(String... params) {
            String fileNameForUpload = "";
            if (fileUpload != null ) {
                String extension = "";
                int i = fileUpload.getName().lastIndexOf('.');
                if (i > 0) {
                    extension = fileUpload.getName().substring(i + 1);
                    if (extension.length() > 0) {
                        BufferedReader inputStream = null;
                        URL myurl = null;
                        FTPClient con = null;
                        FileInputStream in = null;

                        try {
                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension));
                            URLConnection dc = myurl.openConnection();
                            inputStream = new BufferedReader(new InputStreamReader(
                                    dc.getInputStream()));
                            fileNameForUpload = inputStream.readLine();
                            if (fileNameForUpload.length() > 0) {
                                Bitmap bitmapImage = BitmapFactory.decodeFile(fileUpload.getAbsolutePath());
                                int height = bitmapImage.getHeight();
                                int width = bitmapImage.getWidth();
                                if (width > 277) {
                                    height = 277 * height / width;
                                    width = 277;
                                }
                                Bitmap bitmapReady = Bitmap.createScaledBitmap(bitmapImage, width, height, true);
                                File sd = Environment.getExternalStorageDirectory();
                                File dest = new File(sd, fileNameForUpload);

                                FileOutputStream out = null;
                                try {
                                    out = new FileOutputStream(dest);
                                    bitmapReady.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                    out.flush();
                                    out.close();

                                    fileUpload = dest;
                                    con = new FTPClient();
                                    con.connect(PropertiesData.ftpdomain);
                                    if (con.login(PropertiesData.ftpusername, PropertiesData.ftppassword)) {
                                        con.enterLocalPassiveMode(); // important!
                                        con.setFileType(FTP.BINARY_FILE_TYPE);
                                        in = new FileInputStream(fileUpload);
                                        con.storeFile(fileUpload.getName(), in);
                                        con.logout();
                                    }
                                    dest.delete();
                                    out = null;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        out.close();
                                    }
                                    if (con != null) {
                                        con.disconnect();
                                    }
                                    if (in != null) {
                                        in.close();
                                    }
                                }
                            }

                        } catch (MalformedURLException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } catch (IOException e) {
                            Log.e(e.getLocalizedMessage(), e.getMessage());
                        } finally {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/berita-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("judul", judulBerita));
            nameValuePairs.add(new BasicNameValuePair("deskripsi", deskripsi));
            nameValuePairs.add(new BasicNameValuePair("kategori", kategori));
            nameValuePairs.add(new BasicNameValuePair("filename", fileNameForUpload));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            beritaAddProgressbar.setVisibility(View.GONE);
            ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
            ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
            fileUpload = null;
            Toast.makeText(MainActivity.this, "Tersimpan",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPreExecute() {
            beritaAddProgressbar.setVisibility(View.VISIBLE);
            judulBerita = ((EditText) findViewById(R.id.beritaaddJudulBerita)).getText().toString();
            deskripsi = ((EditText) findViewById(R.id.beritaaddDeskripsi)).getText().toString();
            kategori = ((Spinner) findViewById(R.id.beritaaddSpinner)).getSelectedItem().toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    beritaAddImageimageView.setImageURI(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    beritaAddImageimageView.setImageURI(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 7777:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    beritaAddImageimageView.setImageBitmap(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);

                    FileOutputStream out = null;
                    File sd = Environment.getExternalStorageDirectory();
                    fileUpload = new File(sd, randomString(11).concat(".jpg"));
                    try {
                        out = new FileOutputStream(fileUpload);
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();
                        out = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
        }
    }


    private String randomString( int len ){
        String AB = "0123456789abcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder( len );
        for( int i = 0; i < len; i++ )
            sb.append( AB.charAt( secureRandom.nextInt(AB.length()) ) );
        return sb.toString();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onPause();
    }

    private ArrayList<ImageText> _arrayImageTexts;
    private void getPopulerJson() {
        BufferedReader inputStream = null;
        URL myurl = null;
        try {
            myurl = new URL(PropertiesData.domain.concat("android/populer"));
            URLConnection dc = myurl.openConnection();
            inputStream = new BufferedReader(new InputStreamReader(
                    dc.getInputStream()));
            String resultStream = inputStream.readLine();

            JSONArray jsonArray = new JSONArray(resultStream);

            _arrayImageTexts.clear();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    _arrayImageTexts.add(new ImageText(jsonObject.get("id").toString() ,jsonObject.get("judul").toString(), jsonObject.get("filename").toString()));

                }

            }
        } catch (MalformedURLException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } catch (IOException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } catch (JSONException e) {
            Log.e(e.getLocalizedMessage(), e.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private class GetPopulerData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            getPopulerJson();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            populerProgressBar.setVisibility(View.GONE);
            populerListView.setVisibility(View.VISIBLE);
            _listImageText.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            populerProgressBar.setVisibility(View.VISIBLE);
            populerListView.setVisibility(View.GONE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }, 0);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateSupportNavigateUpTaskStack(TaskStackBuilder builder) {
        super.onCreateSupportNavigateUpTaskStack(builder);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.populermenu) {
            closeLayouts();
            setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
            viewLayout = "populer";
            new GetPopulerData().execute();
        } else if (id == R.id.beritaaddmenu) {
            closeLayouts();
            beritaAddProgressbar.setVisibility(View.GONE);
            beritaAddImageimageView.setVisibility(View.GONE);
            fileUpload = null;

            ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
            ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
            beritaAddImageimageView.setVisibility(View.GONE);
            setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
            viewLayout = "beritaadd";
        }

        new Handler().postDelayed(new Runnable() {
            public void run() {
                drawer.closeDrawer(GravityCompat.START);
            }
        }, 0);
        return true;
    }

    private String getFileName(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }

    private void setViewLayout(View view, int status) {
        if (view.getVisibility() == View.VISIBLE) {
            if (status == View.GONE) {
                view.animate().translationX(view.getWidth());
            } else {
                view.animate().translationX(0);
            }
        } else if (status == View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void closeLayouts() {
        setViewLayout((View) findViewById(R.id.populer), View.GONE);
        setViewLayout((View) findViewById(R.id.beritadetail), View.GONE);
        setViewLayout((View) findViewById(R.id.beritaadd), View.GONE);
    }

    public void closeLayoutsFirst() {
        findViewById(R.id.populer).setVisibility(View.GONE);
        findViewById(R.id.beritadetail).setVisibility(View.GONE);
        findViewById(R.id.beritaadd).setVisibility(View.GONE);
    }

    private class GetBeritaDetail extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private String _id;
        public GetBeritaDetail(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id));
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());
            } catch (MalformedURLException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (IOException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                ((TextView) findViewById(R.id.judulBeritaDetail)).setText(jsonObject.get("judul").toString());
                ((TextView) findViewById(R.id.deskripsiberitadetail)).setText(jsonObject.get("deskripsi").toString());
                ((TextView) findViewById(R.id.dateberitadetail)).setText(jsonObject.get("tanggal").toString());
                ImageView imageView = (ImageView) findViewById(R.id.imageberitadetail);
                if (jsonObject.get("filename").toString().length() > 0) {
                    imageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL(jsonObject.get("filename").toString()).getContent()));
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            beritaDetailProgressBar.setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            ((TextView) findViewById(R.id.judulBeritaDetail)).setText("");
            ((TextView) findViewById(R.id.deskripsiberitadetail)).setText("");
            ((TextView) findViewById(R.id.dateberitadetail)).setText("");
            ((ImageView) findViewById(R.id.imageberitadetail)).setVisibility(View.GONE);
            closeLayouts();
            setViewLayout((View) findViewById(R.id.beritadetail), View.VISIBLE);
            beritaDetailProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void  openBeritaDetailAsync(String id) {
        GetBeritaDetail getBeritaDetail = new GetBeritaDetail(id);
        getBeritaDetail.execute();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            if (findViewById(R.id.beritadetail).getVisibility() == View.VISIBLE) {
                closeLayouts();
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
                return false;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
