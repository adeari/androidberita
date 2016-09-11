package com.example.widianta.beritasurabaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

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
    private ListImageTextRalat _listImageTextRalat;
    private GetPopulerData getPopulerData;
    private ProgressBar beritaAddProgressbar;
    private ProgressBar beritaDetailProgressBar;
    private ImageView beritaAddImageimageView;
    private File fileUpload;
    private SecureRandom secureRandom;

    private List<MenuItem> loginMenus;
    private List<MenuItem> logoutMenus;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String viewLayout;
    private String beritaShow;
    private String imageAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        secureRandom = new SecureRandom();
        mainActivity = this;
        setContentView(R.layout.activity_main);
        beritaShow = "";
        imageAction = "";

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

        loginMenus = new ArrayList<MenuItem>();
        logoutMenus = new ArrayList<MenuItem>();
        Menu menu = navigationView.getMenu();
        for (int menuItemIndex = 0; menuItemIndex < menu.size(); menuItemIndex++) {
            MenuItem menuItem= menu.getItem(menuItemIndex);
            if(menuItem.getGroupId() == R.id.logingroupmenu){
                loginMenus.add(menuItem);
            } else if(menuItem.getGroupId() == R.id.logoutgroupmenu){
                logoutMenus.add(menuItem);
            }
        }
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
        _listImageTextRalat = new ListImageTextRalat(mainActivity, _arrayImageTexts);

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

        ((Button) findViewById(R.id.beritaaddKameraButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageAction = "hapus";
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
                       new PostBerita().execute();
                   }
               }
           }
        );

        Button daftarbutton = (Button) findViewById(R.id.daftarbutton);
        daftarbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.userregister), View.VISIBLE);
                        findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
                    }
                }
        );
        Button daftarsimpanbutton = (Button) findViewById(R.id.daftarsimpanbutton);
        daftarsimpanbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new registeruser().execute();
                    }
                }
        );

        ((Button) findViewById(R.id.loginbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new loginuser().execute();
                    }
                }
        );

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
            setLoginMenu(true);
            setLogoutMenu(false);
        } else {
            setLoginMenu(false);
            setLogoutMenu(true);
        }

        closeLayoutsFirst();
        if (savedInstanceState == null) {
            new GetPopulerData().execute();
            setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
        } else {
            if (savedInstanceState.getString("viewlayout").equals("beritaadd")) {
                setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
            } else if (savedInstanceState.getString("viewlayout").equals("populer")) {
                beritaShow = savedInstanceState.getString("beritaShow");
                new GetPopulerData().execute();
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
            } else if (savedInstanceState.getString("viewlayout").equals("userlogin")) {
                setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
            } else if (savedInstanceState.getString("viewlayout").equals("beritadetail")) {
                setViewLayout((View) findViewById(R.id.beritadetail), View.VISIBLE);
            } else if (savedInstanceState.getString("viewlayout").equals("userregister")) {
                setViewLayout((View) findViewById(R.id.userregister), View.VISIBLE);
            }
        }
    }

    private void setLoginMenu(boolean isVisible) {
        for (MenuItem menuItem: loginMenus) {
            menuItem.setVisible(isVisible);
        }
    }
    private void setLogoutMenu(boolean isVisible) {
        for (MenuItem menuItem: logoutMenus) {
            menuItem.setVisible(isVisible);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("viewlayout", viewLayout);
        savedInstanceState.putString("beritaShow", beritaShow);
    }

    private class PostBerita extends AsyncTask<String, Void, String> {
        private String judulBerita;
        private String deskripsi;
        private String kategori;
        private String idBerita;
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

                            myurl = new URL(PropertiesData.domain.concat("android/getfilename-").concat(extension)
                                    .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                                    .concat("&password=").concat(sharedPreferences.getString("password", "")));
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
            nameValuePairs.add(new BasicNameValuePair("idberita", idBerita));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("filename", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                viewhtmlPost(response);
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
            if (idBerita.length() == 0) {
                ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
                ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
                beritaAddImageimageView.setVisibility(View.GONE);
            }
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
            idBerita = ((TextView) findViewById(R.id.idberitaedit)).getText().toString();
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
                    if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                        imageAction = "ubah";
                    }
                }
                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    beritaAddImageimageView.setImageURI(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                    if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                        imageAction = "ubah";
                    }
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
                        if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                            imageAction = "ubah";
                        }
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
            if (beritaShow.isEmpty() || beritaShow.equals("populer")) {
                myurl = new URL(PropertiesData.domain.concat("android/populer"));
            } else if (beritaShow.equals("terbaru")) {
                myurl = new URL(PropertiesData.domain.concat("android/terbaru"));
            } else if (beritaShow.equals("beritasaya")) {
                myurl = new URL(PropertiesData.domain.concat("android/beritasaya").concat("?usernamenik=")
                        .concat(sharedPreferences.getString("usernamenik", ""))
                        .concat("&password=").concat(sharedPreferences.getString("password", "")));
            } else {
                myurl = new URL(PropertiesData.domain.concat("android/artikel-").concat(beritaShow));
            }
            URLConnection dc = myurl.openConnection();
            inputStream = new BufferedReader(new InputStreamReader(
                    dc.getInputStream()));
            String resultStream = inputStream.readLine();

            JSONArray jsonArray = new JSONArray(resultStream);

            _listImageText.clearList();
            _listImageTextRalat.clearList();
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

            if (beritaShow.equals("beritasaya")) {
                populerListView.setAdapter(_listImageTextRalat);
            } else {
                populerListView.setAdapter(_listImageText);
            }
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

    private class registeruser extends AsyncTask<String, Void, String> {
        private String usernamenik;
        private String password;
        private String email;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/mendaftar"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", usernamenik));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                httpclient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, "User Tersimpan",
                    Toast.LENGTH_SHORT).show();
            setViewLayout((View) findViewById(R.id.userregister), View.GONE);
            setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
        }

        @Override
        protected void onPreExecute() {
            email = ((EditText) findViewById(R.id.daftaremail)).getText().toString();
            usernamenik = ((EditText) findViewById(R.id.daftarusernamenik)).getText().toString();
            password = ((EditText) findViewById(R.id.daftarpassword)).getText().toString();
            findViewById(R.id.daftarprogressbar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
    private class loginuser extends AsyncTask<String, Void, String> {
        private String usernamenik;
        private String password;
        private String resultPostHTml;
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/ceklogin"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", usernamenik));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                BufferedReader rd = null;
                String body = "";
                try {
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((body = rd.readLine()) != null) {
                        resultPostHTml = body;
                    }
                } catch (IOException e) {
                    Log.e("IOex1", e.getMessage());
                } finally {
                    if (rd != null) {
                        try {
                            rd.close();
                        } catch (IOException e) {
                            Log.e("IOex", e.getMessage());
                        }
                    }
                }
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            findViewById(R.id.loginprogressbar).setVisibility(View.GONE);
            if (resultPostHTml.equals("1")) {
                setViewLayout((View) findViewById(R.id.userlogin), View.GONE);
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);

                editor.putString("usernamenik", usernamenik);
                editor.putString("password", password);
                editor.commit();

                setLoginMenu(false);
                setLogoutMenu(true);
            } else {
                Toast.makeText(MainActivity.this, "Username / Password salah",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            usernamenik = ((EditText) findViewById(R.id.loginusernamenik)).getText().toString();
            password = ((EditText) findViewById(R.id.loginpassword)).getText().toString();
            findViewById(R.id.loginprogressbar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
//debug post html
    private void viewhtmlPost(HttpResponse response) {
        BufferedReader rd = null;
        String body = "";
        try {
            rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((body = rd.readLine()) != null)
            {
                Log.i("HttpResponse", body);
            }
        } catch (IOException e) {
            Log.e("IOex", e.getMessage());
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException e) {
                    Log.e("IOex", e.getMessage());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (viewLayout.equalsIgnoreCase("beritadetail")) {
                closeLayouts();
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
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

    //menunya
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.populermenu) {
            showPopulerBeritaList("populer");
        } else if (id == R.id.beritaaddmenu) {
            closeLayouts();
            beritaAddProgressbar.setVisibility(View.GONE);
            beritaAddImageimageView.setVisibility(View.GONE);
            fileUpload = null;
            imageAction = "";

            ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
            ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
            ((TextView) findViewById(R.id.idberitaedit)).setText("");
            ((Button) findViewById(R.id.beritaddhapusgambar)).setVisibility(View.GONE);
            setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
        } else if (id == R.id.loginmenu) {
            ((EditText) findViewById(R.id.loginusernamenik)).setText("");
            ((EditText) findViewById(R.id.loginpassword)).setText("");

            closeLayouts();
            setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
            findViewById(R.id.loginprogressbar).setVisibility(View.GONE);
        } else if (id == R.id.logoutmenu) {
            editor.clear();
            editor.commit();
            setLoginMenu(true);
            setLogoutMenu(false);
        } else if (id == R.id.terbarumenu) {
            showPopulerBeritaList("terbaru");
        } else if (id == R.id.umummenu) {
            showPopulerBeritaList("umum");
        } else if (id == R.id.acaramenu) {
            showPopulerBeritaList("acara");
        } else if (id == R.id.pengaduanmenu) {
            showPopulerBeritaList("pengaduan");
        } else if (id == R.id.beritasayamenu) {
            showPopulerBeritaList("beritasaya");
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showPopulerBeritaList(String pilihan) {
        beritaShow = pilihan;
        closeLayouts();
        setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
        new GetPopulerData().execute();
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

        if (status == View.VISIBLE) {
            switch (view.getId()) {
                case R.id.populer: viewLayout = "populer";
                    break;
                case R.id.beritadetail: viewLayout = "beritadetail";
                    break;
                case R.id.beritaadd: viewLayout = "beritadetail";
                    break;
                case R.id.userregister: viewLayout = "beritadetail";
                    break;
                case R.id.userlogin: viewLayout = "userlogin";
                    break;
            }
        }
    }

    public void closeLayouts() {
        setViewLayout((View) findViewById(R.id.populer), View.GONE);
        setViewLayout((View) findViewById(R.id.beritadetail), View.GONE);
        setViewLayout((View) findViewById(R.id.beritaadd), View.GONE);
        setViewLayout((View) findViewById(R.id.userregister), View.GONE);
        setViewLayout((View) findViewById(R.id.userlogin), View.GONE);
    }

    public void closeLayoutsFirst() {
        findViewById(R.id.populer).setVisibility(View.GONE);
        findViewById(R.id.beritadetail).setVisibility(View.GONE);
        findViewById(R.id.beritaadd).setVisibility(View.GONE);
        findViewById(R.id.userregister).setVisibility(View.GONE);
        findViewById(R.id.userlogin).setVisibility(View.GONE);
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
                    Glide.with(imageView.getContext())
                            .load(jsonObject.get("filename").toString())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    beritaDetailProgressBar.setVisibility(View.GONE);
                                    return false;
                                }
                            })
                            .into(imageView);
                } else {
                    imageView.setVisibility(View.GONE);
                    beritaDetailProgressBar.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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

    private class DeleteBerita extends AsyncTask<String, Void, String> {
        private String _id;
        public DeleteBerita(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/berita-deleted"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("id", _id));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
            } catch (UnsupportedEncodingException e) {
                Log.e("Unsupported", e.getMessage());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            showPopulerBeritaList("beritasaya");
            Toast.makeText(MainActivity.this, "Berita Terhapus",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public void  openBeritaDetailAsync(String id) {
        new GetBeritaDetail(id).execute();
    }
    public void deleteBerita(String id) {
         new DeleteBerita(id).execute();
    }
    public void prePareEditBerita(String selectedID) {
        new Get1Berita(selectedID).execute();
    }

    private class Get1Berita extends AsyncTask<String, Void, String> {
        private ImageText imageText;
        private String _id;
        public Get1Berita(String id) {
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
                JSONObject jsonObject = new JSONObject(inputStream.readLine());
                imageText = new ImageText(_id, jsonObject.get("judul").toString(), jsonObject.get("filename").toString()
                        , jsonObject.get("deskripsi").toString(), jsonObject.get("kategori").toString());
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
            ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText(imageText.getJudul());
            ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText(imageText.getBerita());
            ((TextView) findViewById(R.id.idberitaedit)).setText(imageText.getId());
            Spinner spinner = (Spinner)findViewById(R.id.beritaaddSpinner);
            int position = 0;
            while (position < spinner.getAdapter().getCount()) {
                if (spinner.getAdapter().getItem(position).toString().equalsIgnoreCase(imageText.getKategori())) {
                    spinner.setSelection(position);
                    break;
                }
                position++;
            }

            if (imageText.getImage() != null && imageText.getImage().length() > 0) {
                beritaAddImageimageView.setVisibility(View.VISIBLE);
                ((Button) findViewById(R.id.beritaddhapusgambar)).setVisibility(View.VISIBLE);
                Glide.with(beritaAddImageimageView.getContext())
                        .load(imageText.getImage())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                beritaDetailProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(beritaAddImageimageView);
            }
        }

        @Override
        protected void onPreExecute() {
            closeLayouts();
            imageAction = "";
            beritaAddProgressbar.setVisibility(View.GONE);
            beritaAddImageimageView.setVisibility(View.GONE);
            ((Button) findViewById(R.id.beritaddhapusgambar)).setVisibility(View.GONE);
            ((TextView) findViewById(R.id.idberitaedit)).setText("");
            fileUpload = null;
            setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
