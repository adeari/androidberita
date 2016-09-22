package com.example.widianta.beritasurabaya.data;

import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;
import com.example.widianta.beritasurabaya.R;

/**
 * Created by awidiyadew on 15/09/16.
 */
public class CustomDataProvider {

    private static final int MAX_LEVELS = 1;

    private static final int LEVEL_1 = 1;

    private static List<BaseItem> mMenu = new ArrayList<>();

    public static List<BaseItem> getInitialItems(SharedPreferences sharedPreferences) {
        //return getSubItems(new GroupItem("root"));

        List<BaseItem> rootMenu = new ArrayList<>();

        /*
        * ITEM = TANPA CHILD
        * GROUPITEM = DENGAN CHILD
        * */
        if (sharedPreferences.getString("usernamenik", "").isEmpty()) {
            rootMenu.add(new Item("Log In", "Log In", R.drawable.ic_alarm_on_green_24dp));
        }
        rootMenu.add(new GroupItem("Berita", null, R.drawable.ic_alarm_on_green_24dp));
        rootMenu.add(new GroupItem("Artikel", null, R.drawable.ic_alarm_on_green_24dp));
        if (!sharedPreferences.getString("usernamenik", "").isEmpty()) {
            rootMenu.add(new GroupItem("Pengaturan", null, R.drawable.ic_alarm_on_green_24dp));
        }
        return rootMenu;
    }

    public static List<BaseItem> getSubItems(BaseItem baseItem) {

        List<BaseItem> result = new ArrayList<>();
        int level = ((GroupItem) baseItem).getLevel() + 1;
        String menuItem = baseItem.getName();

        if (!(baseItem instanceof GroupItem)) {
            throw new IllegalArgumentException("GroupItem required");
        }

        GroupItem groupItem = (GroupItem)baseItem;
        if(groupItem.getLevel() >= MAX_LEVELS){
            return null;
        }

        /*
        * HANYA UNTUK GROUP-ITEM
        * */
        switch (level){
            case LEVEL_1 :
                switch (menuItem){
                    case "Berita" :
                        result = getBerita();
                        break;
                    case "Artikel" :
                        result = getArtikel();
                        break;
                    case "Pengaturan" :
                        result = getPengaturan();
                        break;
                }
                break;

        }

        return result;
    }

    public static boolean isExpandable(BaseItem baseItem) {
        return baseItem instanceof GroupItem;
    }

    private static List<BaseItem> getBerita(){
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Populer", "Populer", R.drawable.ic_local_florist_black_24dp ));
        list.add(new Item("Terbaru", "Terbaru", R.drawable.ic_local_florist_black_24dp));
        return list;
    }

    private static List<BaseItem> getArtikel(){
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Umum", "Umum", R.drawable.ic_clear_all_black_24dp));
        list.add(new Item("Acara", "Acara", R.drawable.ic_clear_all_black_24dp));
        list.add(new Item("Pengaduan", "Pengaduan", R.drawable.ic_clear_all_black_24dp));
        return list;
    }

    private static List<BaseItem> getPengaturan(){
        List<BaseItem> list = new ArrayList<>();
        list.add(new Item("Berita Saya", "Berita Saya", R.drawable.ic_insert_drive_file_black_24dp));
        list.add(new Item("Tambah Berita", "Tambah Berita", R.drawable.ic_add_black_24dp));
        list.add(new Item("Saya", "Saya", R.drawable.ic_person_outline_blue_24dp));
        list.add(new Item("Log Out", "Log Out", R.drawable.ic_alarm_off_red_24dp));
        return list;
    }

}
