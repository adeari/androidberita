package com.example.widianta.beritasurabaya;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.AppCompatImageButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.widianta.beritasurabaya.data.BaseItem;
import com.example.widianta.beritasurabaya.data.CustomDataProvider;
import com.example.widianta.beritasurabaya.views.LevelBeamView;

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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Map;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class MainActivity extends AppCompatActivity {
    private MainActivity mainActivity;
    private ProgressBar populerProgressBar;
    private ListView populerListView;
    private DrawerLayout drawer;
    private ListImageText _listImageText;
    private ListImageTextRalat _listImageTextRalat;

    private ProgressBar beritaAddProgressbar;
    private ProgressBar beritaDetailProgressBar;
    private ProgressBar progressbarKomentarAdd;
    private ProgressBar loginProgressbar;
    private ProgressBar profileProgressBar;

    private ImageView beritaAddImageimageView;
    private File fileUpload;
    private SecureRandom secureRandom;

    private ImageView imageberitadetail;
    private ImageView beritadetailkomentarimageview;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private String viewLayout;
    private String beritaShow;
    private String imageAction;
    private String idKomentarDeleted;

    private Button beritAddHapusGambar;
    private Button bertiaDetailAddKomentar;

    private ImageView imageViewAccess;
    private CircleImageView profileImage;

    private MultiLevelListView multiLevelListView;

    private LinearLayout linerlayoutVerticalDAta;

    private List<BaseItem> baseItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        secureRandom = new SecureRandom();
        mainActivity = this;
        beritaShow = "";
        imageAction = "";


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        populerListView =(ListView) findViewById(R.id.listPopuler);

        populerProgressBar = (ProgressBar) findViewById(R.id.progressBarPopuler);
        populerProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);

        beritaAddProgressbar = (ProgressBar) findViewById(R.id.beritaAddProgressbar);
        beritaAddProgressbar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaAddProgressbar.getLayoutParams().height = 200;
        beritaAddProgressbar.setVisibility(View.GONE);

        progressbarKomentarAdd = (ProgressBar) findViewById(R.id.progressbarKomentarAdd);
        progressbarKomentarAdd.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        progressbarKomentarAdd.setVisibility(View.GONE);

        loginProgressbar = (ProgressBar) findViewById(R.id.loginprogressbar);

        profileProgressBar = (ProgressBar) findViewById(R.id.profileProgressBar);
        profileProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        profileProgressBar.setVisibility(View.GONE);

        beritaDetailProgressBar = (ProgressBar) findViewById(R.id.beritadetailProgressBar);
        beritaDetailProgressBar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
        beritaDetailProgressBar.setVisibility(View.GONE);

        beritaAddImageimageView = (ImageView) findViewById(R.id.beritaAddImage);
        beritaAddImageimageView.getLayoutParams().height = 250;
        beritaAddImageimageView.setVisibility(View.GONE);

        profileImage = (CircleImageView) findViewById(R.id.profileImage);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        _arrayImageTexts = new ArrayList<ImageText>();
        _listImageText = new ListImageText(mainActivity, _arrayImageTexts);
        _listImageTextRalat = new ListImageTextRalat(mainActivity, _arrayImageTexts);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, new String[] {"Umum", "Acara", "Pengaduan"});
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ((Spinner)findViewById(R.id.beritaaddSpinner)).setAdapter(dataAdapter);

        ((Button) findViewById(R.id.beritaaddGaleriButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 1);
                    }
                }
        );

        ((Button) findViewById(R.id.bertiadetailgallerikomentarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 12);
                    }
                }
        );

        ((AppCompatImageButton) findViewById(R.id.profilegalerybutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 41);
                    }
                }
        );

        ((Button) findViewById(R.id.beritaaddKameraButton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 7777);
                    }
                }
        );
        ((Button) findViewById(R.id.bertiadetailkamerakomentarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 13);
                    }
                }
        );

        ((AppCompatImageButton) findViewById(R.id.profilekamerabutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent, 42);
                    }
                }
        );

        beritAddHapusGambar = (Button) findViewById(R.id.beritaddhapusgambar);
        beritAddHapusGambar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageAction = "hapus";
                        beritaAddImageimageView.setVisibility(View.GONE);
                    }
                }
        );

        ((Button) findViewById(R.id.beritaaddSimpan)).setOnClickListener(
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

        ((Button) findViewById(R.id.daftarbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.userregister), View.VISIBLE);
                        findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
                    }
                }
        );
        ((Button) findViewById(R.id.daftarsimpanbutton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwordRegister = ((EditText) findViewById(R.id.daftarpassword)).getText().toString();
                        String passwordreRegister = ((EditText) findViewById(R.id.daftarrepassword)).getText().toString();
                        String usernamenik = ((EditText) findViewById(R.id.daftarusernamenik)).getText().toString();
                        if (usernamenik.length() == 0) {
                            Toast.makeText(MainActivity.this, "Tulis User Name / NIK",
                                    Toast.LENGTH_SHORT).show();
                        } else if (passwordRegister.length() == 0) {
                            Toast.makeText(MainActivity.this, "Tulis Password",
                                    Toast.LENGTH_SHORT).show();
                        } else if (!passwordRegister.equals(passwordreRegister)) {
                            Toast.makeText(MainActivity.this, "Password dan Re Password harus sama",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            new registeruser().execute();
                        }
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

        bertiaDetailAddKomentar = (Button) findViewById(R.id.bertiadetailaddkomentar);
        bertiaDetailAddKomentar.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        findViewById(R.id.beritadetailkomentarform).setVisibility(View.VISIBLE);
                        beritadetailkomentarimageview.setVisibility(View.GONE);
                        ((EditText) findViewById(R.id.beritadetailkomentar)).setText("");
                        fileUpload = null;
                        imageAction = "";
                        bertiaDetailAddKomentar.setVisibility(View.GONE);
                        ((ScrollView) findViewById(R.id.beritadetail)).requestChildFocus(findViewById(R.id.beritadetaillastcomponent),
                                findViewById(R.id.beritadetaillastcomponent));
                    }
                }
        );

        ((Button) findViewById(R.id.beritadetailsimpankomentar)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((EditText) findViewById(R.id.beritadetailkomentar)).getText().length() == 0) {
                            Toast.makeText(MainActivity.this, "Tulis komentar",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            new PostKomentar().execute();
                        }
                    }
                }
        );

        sharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = sharedPreferences.edit();
        confMenu();

        imageberitadetail = (ImageView) findViewById(R.id.imageberitadetail);
        imageberitadetail.getLayoutParams().height = 250;
        beritadetailkomentarimageview = (ImageView) findViewById(R.id.beritadetailkomentarimageview);
        beritadetailkomentarimageview.getLayoutParams().height = 250;

        linerlayoutVerticalDAta = (LinearLayout) findViewById(R.id.linerlayoutVerticalDAta);

        closeLayoutsFirst();
        new GetPopulerData().execute();
        setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);
    }

    private void confMenu() {
        multiLevelListView = (MultiLevelListView) findViewById(R.id.multiLevelMenu);

        // custom ListAdapter
        ListAdapter listAdapter = new ListAdapter();

        multiLevelListView.setAdapter(listAdapter);
        multiLevelListView.setOnItemClickListener(mOnItemClickListener);
        if (baseItemList != null) {
            baseItemList.clear();
        }
        baseItemList = CustomDataProvider.getInitialItems(sharedPreferences);
        listAdapter.setDataItems(baseItemList);
    }

    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
            ImageView iconMenu;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            // DIEKSEKUSI SAAT KLIK PADA GROUP-ITEM
            return CustomDataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return CustomDataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.data_item, null);
                //viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                viewHolder.iconMenu = (ImageView) convertView.findViewById(R.id.iconmenu);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            viewHolder.iconMenu.setBackgroundResource(((BaseItem) object).getImageMenu());

            if (itemInfo.isExpandable()) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_expand_less : R.drawable.ic_expand_more);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }
    }

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            BaseItem baseItem = (BaseItem) object;
            if (baseItem.getFunctionName() != null) {
                setTitle(baseItem.getName());
                switch (baseItem.getFunctionName()) {
                    case "Populer" : showPopulerBeritaList("populer"); break;
                    case "Terbaru" : showPopulerBeritaList("terbaru"); break;

                    case "Umum" : showPopulerBeritaList("umum"); break;
                    case "Acara" : showPopulerBeritaList("acara"); break;
                    case "Pengaduan" : showPopulerBeritaList("pengaduan"); break;

                    case "Saya" :
                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.profileuser), View.VISIBLE);
                        fileUpload = null;
                        imageAction = "";
                        new GetMyProfile().execute();
                        break;
                    case "Berita Saya" : showPopulerBeritaList("beritasaya"); break;
                    case "Tambah Berita" : closeLayouts();
                        beritaAddProgressbar.setVisibility(View.GONE);
                        beritaAddImageimageView.setVisibility(View.GONE);
                        fileUpload = null;
                        imageAction = "";

                        ((EditText) findViewById(R.id.beritaaddJudulBerita)).setText("");
                        ((EditText) findViewById(R.id.beritaaddDeskripsi)).setText("");
                        ((TextView) findViewById(R.id.idberitaedit)).setText("");
                        beritAddHapusGambar.setVisibility(View.GONE);
                        setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
                        break;
                    case "Log Out" :
                        editor.clear();
                        editor.commit();
                        confMenu();
                        break;
                    case "Log In" :
                        ((EditText) findViewById(R.id.loginusernamenik)).setText("");
                        ((EditText) findViewById(R.id.loginpassword)).setText("");

                        closeLayouts();
                        setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
                        loginProgressbar.setVisibility(View.GONE);
                        break;

                }
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
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
            imageAction = "";
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
                    setImageFromGaleri(selectedImage, beritaAddImageimageView);
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
                    setImageFromGaleri(selectedImage, beritaAddImageimageView);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                    if (((TextView) findViewById(R.id.idberitaedit)).getText().length() >0) {
                        imageAction = "ubah";
                    }
                }
                break;
            case 12:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, beritadetailkomentarimageview);
                    beritadetailkomentarimageview.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 13:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    beritadetailkomentarimageview.setImageBitmap(selectedImage);
                    beritadetailkomentarimageview.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                }
                break;
            case 7777:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    beritaAddImageimageView.setImageBitmap(selectedImage);
                    beritaAddImageimageView.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                }
                break;
            case 221:
                if(resultCode == RESULT_OK){
                    Uri imageUri = imageReturnedIntent.getData();
                    setImageFromGaleri(imageUri, imageViewAccess);
                    imageViewAccess.setVisibility(View.VISIBLE);
                    fileUpload = new File(getFileName(imageUri));
                    imageAction = "ubah";
                }
                break;
            case 222:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imageViewAccess.setImageBitmap(selectedImage);
                    imageViewAccess.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                    imageAction = "ubah";
                }
                break;
            case 41:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    setImageFromGaleri(selectedImage, profileImage);
                    fileUpload = new File(getFileName(selectedImage));
                }
                break;
            case 42:
                if(resultCode == RESULT_OK){
                    Bitmap selectedImage = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    profileImage.setImageBitmap(selectedImage);
                    profileImage.setVisibility(View.VISIBLE);
                    setFileUploadCamera(selectedImage);
                }
                break;
        }
    }

    private void setImageFromGaleri(Uri imageUri, ImageView imageView) {
        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            imageView.setImageBitmap(selectedImage);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, stream);

            byte [] data = stream.toByteArray();
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), selectedImage);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap bmp = drawable.getBitmap();
            Bitmap b = Bitmap.createScaledBitmap(bmp, selectedImage.getHeight() * 500 / selectedImage.getHeight(), 500, false);
            imageView.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            Log.e("FileEx", e.getMessage());
        }
    }

    private void setFileUploadCamera(Bitmap selectedImage) {
        FileOutputStream out = null;
        File sd = Environment.getExternalStorageDirectory();
        fileUpload = new File(sd, randomString(11).concat(".jpg"));
        try {
            out = new FileOutputStream(fileUpload);
            selectedImage.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            if (((TextView) findViewById(R.id.idberitaedit)).getText().length() > 0) {
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
        private String resultPostHTml;
        @Override
        protected String doInBackground(String... params) {
            resultPostHTml = "";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/mendaftar"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", usernamenik));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("email", email));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httpPost);
                BufferedReader rd = null;
                String body = "";
                try {
                    rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                    while ((body = rd.readLine()) != null) {
                        resultPostHTml = resultPostHTml.concat(body);
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
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(resultPostHTml);
                if (jsonObject.getString("success").equals("1")) {
                    Toast.makeText(MainActivity.this, "User Tersimpan",
                            Toast.LENGTH_SHORT).show();
                    setViewLayout((View) findViewById(R.id.userregister), View.GONE);
                    setViewLayout((View) findViewById(R.id.userlogin), View.VISIBLE);
                } else {
                    Toast.makeText(MainActivity.this, jsonObject.getString("msg"),
                            Toast.LENGTH_SHORT).show();
                }
                findViewById(R.id.daftarprogressbar).setVisibility(View.GONE);
            } catch (JSONException e) {
                Log.e("JSONE", e.getMessage());
            }
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
            loginProgressbar.setVisibility(View.GONE);
            if (resultPostHTml.equals("1")) {
                setViewLayout((View) findViewById(R.id.userlogin), View.GONE);
                setViewLayout((View) findViewById(R.id.populer), View.VISIBLE);

                editor.putString("usernamenik", usernamenik);
                editor.putString("password", password);
                editor.commit();

                confMenu();
            } else {
                Toast.makeText(MainActivity.this, "Username / Password salah",
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            usernamenik = ((EditText) findViewById(R.id.loginusernamenik)).getText().toString();
            password = ((EditText) findViewById(R.id.loginpassword)).getText().toString();
            loginProgressbar.setVisibility(View.VISIBLE);
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
        } else if (viewLayout.equalsIgnoreCase("beritaadd")) {
            closeLayouts();
            showPopulerBeritaList("beritasaya");
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
                view.animate().translationX(view.getWidth() * 4);
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
                case R.id.userregister: viewLayout = "userregister";
                    break;
                case R.id.userlogin: viewLayout = "userlogin";
                    break;
                case R.id.beritaadd: viewLayout = "beritaadd";
                    break;
                case R.id.profileuser: viewLayout = "profileuser";
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
        setViewLayout((View) findViewById(R.id.profileuser), View.GONE);
    }

    public void closeLayoutsFirst() {
        findViewById(R.id.populer).setVisibility(View.GONE);
        findViewById(R.id.beritadetail).setVisibility(View.GONE);
        findViewById(R.id.beritaadd).setVisibility(View.GONE);
        findViewById(R.id.userregister).setVisibility(View.GONE);
        findViewById(R.id.userlogin).setVisibility(View.GONE);
        findViewById(R.id.profileuser).setVisibility(View.GONE);
    }

    private class GetBeritaDetail extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        private String _id;
        private ArrayList<KomentarText> komentarTexts;

        public GetBeritaDetail(String id) {
            super();
            _id = id;
        }
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id));
                } else {
                    myurl = new URL(PropertiesData.domain.concat("android/beritadetail-").concat(_id)
                            .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""))
                    );
                }

                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());

                JSONArray jsonArray = jsonObject.getJSONArray("komentars");
                komentarTexts = getKomentarTexts(jsonArray);
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
                ((TextView) findViewById(R.id.beritadetailidberita)).setText(_id);

                if (jsonObject.get("filename").toString().length() > 0) {
                    imageberitadetail.setImageBitmap(BitmapFactory.decodeStream((InputStream)new URL(jsonObject.get("filename").toString()).getContent()));
                    imageberitadetail.setVisibility(View.VISIBLE);
                    Glide.with(imageberitadetail.getContext())
                            .load(jsonObject.get("filename").toString())
                            .listener(new RequestListener<String, GlideDrawable>() {
                                @Override
                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                    beritaDetailProgressBar.setVisibility(View.GONE);
                                    setKomentar(komentarTexts);
                                    return false;
                                }
                            })
                            .into(imageberitadetail);
                } else {
                    imageberitadetail.setVisibility(View.GONE);
                    beritaDetailProgressBar.setVisibility(View.GONE);
                    setKomentar(komentarTexts);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                bertiaDetailAddKomentar.setVisibility(View.GONE);
            } else {
                bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPreExecute() {
            ((TextView) findViewById(R.id.judulBeritaDetail)).setText("");
            ((TextView) findViewById(R.id.deskripsiberitadetail)).setText("");
            ((TextView) findViewById(R.id.dateberitadetail)).setText("");
            imageberitadetail.setVisibility(View.GONE);
            closeLayouts();
            setViewLayout((View) findViewById(R.id.beritadetail), View.VISIBLE);
            beritaDetailProgressBar.setVisibility(View.VISIBLE);

            findViewById(R.id.beritadetailkomentarform).setVisibility(View.GONE);
            bertiaDetailAddKomentar.setVisibility(View.GONE);
            linerlayoutVerticalDAta.removeAllViews();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private void setKomentar(ArrayList<KomentarText> komentarTexts) {
        linerlayoutVerticalDAta.removeAllViews();
        for (KomentarText komentarText : komentarTexts) {
            LayoutInflater inflater = mainActivity.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listkomentar, null, true);

            ((TextView) rowView.findViewById(R.id.komentar)).setText(komentarText.getKomentar());
            ImageView imageView = (ImageView) rowView.findViewById(R.id.imagekomentar);
            ((TextView) rowView.findViewById(R.id.komentarid)).setText(komentarText.getId());

            if (komentarText.getImage().length() > 0) {
                imageView.setVisibility(View.VISIBLE);
                ((TextView) rowView.findViewById(R.id.imagelinkkomentarralat)).setText(komentarText.getImage());
                Glide.with(imageView.getContext())
                        .load(komentarText.getImage())
                        .placeholder(R.drawable.ic_local_florist_black_24dp)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(imageView);
            } else {
                imageView.setVisibility(View.GONE);
            }

            if (komentarText.getIsAccess().equals("1")) {
                rowView.findViewById(R.id.ralatlayout).setVisibility(View.VISIBLE);
                ((Button) rowView.findViewById(R.id.hapuskomentar)).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LinearLayout linearLayout = (LinearLayout) v.getParent().getParent();
                                LinearLayout linearLayoutParent = (LinearLayout) v.getParent().getParent().getParent();
                                idKomentarDeleted = ((TextView) linearLayoutParent.findViewById(R.id.komentarid)).getText().toString();
                                new AlertDialog.Builder(mainActivity)
                                        .setTitle("Yakin Akan Dihapus")
                                        .setMessage("Apakah Komentar ".concat(((TextView) linearLayout.findViewById(R.id.komentar)).getText().toString()).concat(" akan dihapus ?"))
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                new DeleteKomentar(idKomentarDeleted).execute();
                                            }
                                        })
                                        .setNegativeButton(android.R.string.no, null).show();
                            }
                        }
                );
                ((Button) rowView.findViewById(R.id.ralatkomentar)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileUpload = null;
                        imageAction = "";
                        LinearLayout linearLayout = (LinearLayout) v.getParent().getParent();
                        RelativeLayout relativeLayoutRalat = (RelativeLayout) linearLayout.findViewById(R.id.komentarformralat);
                        relativeLayoutRalat.findViewById(R.id.komentarformralat).setVisibility(View.VISIBLE);

                        bertiaDetailAddKomentar.setVisibility(View.GONE);
                        linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.GONE);
                        RelativeLayout ralatLayout = (RelativeLayout) linearLayout.findViewById(R.id.ralatlayout);
                        ralatLayout.getLayoutParams().height = 0;
                        ralatLayout.setVisibility(View.GONE);
                        LinearLayout layoutkomentar = (LinearLayout) linearLayout.findViewById(R.id.layoutkomentar);
                        layoutkomentar.getLayoutParams().height = 0;
                        layoutkomentar.setVisibility(View.GONE);

                        ((EditText)linearLayout.findViewById(R.id.komentaredittextralat)).setText(((TextView) linearLayout.findViewById(R.id.komentar)).getText());

                        TextView imageLink = ((TextView) linearLayout.findViewById(R.id.imagelinkkomentarralat));
                        ImageView imageRalatKomentar = ((ImageView) linearLayout.findViewById(R.id.komentarralatimage));
                        if (imageLink.getText().length() > 0) {
                            imageRalatKomentar.setVisibility(View.VISIBLE);
                            Glide.with(imageRalatKomentar.getContext())
                                    .load(imageLink.getText().toString())
                                    .placeholder(R.drawable.ic_local_florist_black_24dp)
                                    .listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                            return false;
                                        }
                                    })
                                    .into(imageRalatKomentar);
                            linearLayout.findViewById(R.id.komentarlayouthapusgambar).setVisibility(View.VISIBLE);
                        } else {
                            imageRalatKomentar.setVisibility(View.GONE);
                            linearLayout.findViewById(R.id.komentarlayouthapusgambar).setVisibility(View.GONE);
                        }
                    }
                });

                ((Button) rowView.findViewById(R.id.komentarralatbatal)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fileUpload = null;
                        imageAction = "";
                        LinearLayout linearLayout = (LinearLayout) v.getParent().getParent().getParent();

                        RelativeLayout ralatLayout = (RelativeLayout) linearLayout.findViewById(R.id.ralatlayout);
                        ralatLayout.getLayoutParams().height = RelativeLayout.LayoutParams.WRAP_CONTENT;
                        ralatLayout.setVisibility(View.VISIBLE);
                        LinearLayout layoutkomentar = (LinearLayout) linearLayout.findViewById(R.id.layoutkomentar);
                        layoutkomentar.getLayoutParams().height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        layoutkomentar.setVisibility(View.VISIBLE);
                        bertiaDetailAddKomentar.setVisibility(View.VISIBLE);

                        linearLayout.findViewById(R.id.komentarformralat).setVisibility(View.GONE);
                    }
                });

                ((Button) rowView.findViewById(R.id.komentarralatgaleributton)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto , 221);
                    }
                });

                ((Button) rowView.findViewById(R.id.komentarlayouthapusgambar)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                        imageViewAccess.setVisibility(View.GONE);
                        imageAction = "hapus";
                    }
                });

                ((Button) rowView.findViewById(R.id.komentarralatkamerabutton)).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                imageViewAccess = ((ImageView) ((LinearLayout) v.getParent().getParent().getParent()).findViewById(R.id.komentarralatimage));
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, 222);
                            }
                        });
                ((Button) rowView.findViewById(R.id.komentarralatsimpan)).setOnClickListener(
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new PostRalatKomentar(((LinearLayout) v.getParent().getParent().getParent().getParent())).execute();
                            }
                        });

                ProgressBar komentarRalatProgressbar = (ProgressBar) rowView.findViewById(R.id.komentarralatprogressbar);
                komentarRalatProgressbar.getIndeterminateDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.MULTIPLY);
                komentarRalatProgressbar.getLayoutParams().height = 60;
            } else {
                rowView.findViewById(R.id.ralatlayout).setVisibility(View.GONE);
            }
            rowView.findViewById(R.id.komentarformralat).setVisibility(View.GONE);
            linerlayoutVerticalDAta.addView(rowView);
        }
    }

    private class DeleteKomentar extends AsyncTask<String, Void, String> {
        private String id;
        public DeleteKomentar(String idKomentar) {
            super();
            id = idKomentar;
        }
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-deleted"));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("usernamenik", sharedPreferences.getString("usernamenik", "")));
            nameValuePairs.add(new BasicNameValuePair("password", sharedPreferences.getString("password", "")));
            nameValuePairs.add(new BasicNameValuePair("idkomentar", id));
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                viewhtmlPost(httpclient.execute(httpPost));
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
            new GetKomentar().execute();
            Toast.makeText(MainActivity.this, "Komentar Terhapus",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            beritaDetailProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetKomentar extends AsyncTask<String, Void, String> {
        private JSONArray jsonArray;
        private String _id;
        private ArrayList<KomentarText> komentarTexts;

        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                    myurl = new URL(PropertiesData.domain.concat("android/komentar-data-").concat(_id));
                } else {
                    myurl = new URL(PropertiesData.domain.concat("android/komentar-data-").concat(_id)
                            .concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                            .concat("&password=").concat(sharedPreferences.getString("password", ""))
                    );
                }
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonArray = new JSONArray(inputStream.readLine());
                komentarTexts = getKomentarTexts(jsonArray);
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
            setKomentar(komentarTexts);
            beritaDetailProgressBar.setVisibility(View.GONE);
            ((ScrollView) findViewById(R.id.beritadetail)).requestChildFocus(findViewById(R.id.beritadetaillastcomponent),
                    findViewById(R.id.beritadetaillastcomponent));
        }

        @Override
        protected void onPreExecute() {
            beritaDetailProgressBar.setVisibility(View.VISIBLE);
            _id = ((EditText) findViewById(R.id.beritadetailidberita)).getText().toString();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private ArrayList<KomentarText> getKomentarTexts(JSONArray jsonArray) {
        ArrayList<KomentarText> komentarTexts = new ArrayList<KomentarText>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObjectKomentar = jsonArray.getJSONObject(i);
                komentarTexts.add(new KomentarText(jsonObjectKomentar.get("komentar").toString(),
                        jsonObjectKomentar.get("gambar").toString(),
                        jsonObjectKomentar.get("id").toString(),
                        jsonObjectKomentar.get("useridinput").toString(),
                        jsonObjectKomentar.get("idberita").toString(),
                        jsonObjectKomentar.get("isaccess").toString()));
            } catch (JSONException e) {
                Log.e(e.getLocalizedMessage(), e.getMessage());
            }
        }
        return komentarTexts;
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
                beritAddHapusGambar.setVisibility(View.VISIBLE);
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
            beritAddHapusGambar.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.idberitaedit)).setText("");
            fileUpload = null;
            imageAction = "";
            setViewLayout((View) findViewById(R.id.beritaadd), View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PostKomentar extends AsyncTask<String, Void, String> {
        private String komentar;
        private String idberita;
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
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("idkomentar", ""));
            nameValuePairs.add(new BasicNameValuePair("komentar", komentar));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("gambar", fileNameForUpload));
            nameValuePairs.add(new BasicNameValuePair("idberita", idberita));
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
            progressbarKomentarAdd.setVisibility(View.GONE);
            findViewById(R.id.beritadetailkomentarform).setVisibility(View.GONE);
            if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
                bertiaDetailAddKomentar.setVisibility(View.GONE);
            } else {
                bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            }
            Toast.makeText(MainActivity.this, "Tersimpan", Toast.LENGTH_LONG).show();
            new GetKomentar().execute();
            fileUpload = null;
            imageAction = "";
        }

        @Override
        protected void onPreExecute() {
            komentar = ((EditText) findViewById(R.id.beritadetailkomentar)).getText().toString();
            idberita = ((EditText) findViewById(R.id.beritadetailidberita)).getText().toString();
            progressbarKomentarAdd.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class PostRalatKomentar extends AsyncTask<String, Void, String> {
        private String komentar;
        private String idKomentar;
        private LinearLayout linearLayout;
        private PostRalatKomentar(LinearLayout linearLayout1) {
            super();
            linearLayout = linearLayout1;
        }
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
            HttpPost httpPost = new HttpPost(PropertiesData.domain.concat("android/komentar-add"));

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Log.i("dddd", idKomentar);
            nameValuePairs.add(new BasicNameValuePair("idkomentar", idKomentar));
            nameValuePairs.add(new BasicNameValuePair("komentar", komentar));
            nameValuePairs.add(new BasicNameValuePair("imageaction", imageAction));
            nameValuePairs.add(new BasicNameValuePair("gambar", fileNameForUpload));
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
            linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.GONE);
            new GetKomentar().execute();
            bertiaDetailAddKomentar.setVisibility(View.VISIBLE);
            fileUpload = null;
            imageAction = "";
        }

        @Override
        protected void onPreExecute() {
            idKomentar = ((TextView)linearLayout.findViewById(R.id.komentarid)).getText().toString();
            komentar = ((EditText)linearLayout.findViewById(R.id.komentaredittextralat)).getText().toString();
            linearLayout.findViewById(R.id.komentarralatprogressbar).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    private class GetMyProfile extends AsyncTask<String, Void, String> {
        private JSONObject jsonObject;
        @Override
        protected String doInBackground(String... params) {
            BufferedReader inputStream = null;
            URL myurl = null;
            try {
                myurl = new URL(PropertiesData.domain.concat("android/profileuser").concat("?usernamenik=").concat(sharedPreferences.getString("usernamenik", ""))
                        .concat("&password=").concat(sharedPreferences.getString("password", "")));
                URLConnection dc = myurl.openConnection();
                inputStream = new BufferedReader(new InputStreamReader(
                        dc.getInputStream()));
                jsonObject = new JSONObject(inputStream.readLine());
                Log.i("d", jsonObject.get("name").toString());
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
                ((EditText) findViewById(R.id.profileNama)).setText(jsonObject.get("name").toString());
                ((EditText) findViewById(R.id.profileNik)).setText(jsonObject.get("nik").toString());
                ((EditText) findViewById(R.id.profileEmail)).setText(jsonObject.get("email").toString());
                String imageLink = jsonObject.get("gambar").toString();
                if (imageLink.isEmpty()) {
                    profileProgressBar.setVisibility(View.GONE);
                } else {
                    Glide.with(beritaAddImageimageView.getContext())
                        .load(imageLink)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                profileProgressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(profileImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            profileImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.ic_person_blue_24dp));
            ((EditText) findViewById(R.id.profileNama)).setText("");
            ((EditText) findViewById(R.id.profileNik)).setText("");
            ((EditText) findViewById(R.id.profileEmail)).setText("");
            profileProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
