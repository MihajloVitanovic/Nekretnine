package com.mvit.mihajlo.nekretnine.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.mvit.mihajlo.nekretnine.R;
import com.mvit.mihajlo.nekretnine.adapters.DrawerListAdapter;
import com.mvit.mihajlo.nekretnine.adapters.NavigationItem;
import com.mvit.mihajlo.nekretnine.db.DatabaseHelper;
import com.mvit.mihajlo.nekretnine.db.model.Nekretnina;
import com.mvit.mihajlo.nekretnine.dialogs.AboutDialog;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by androiddevelopment on 24.6.17..
 */

public class MainActivity extends AppCompatActivity  {

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItemFromDrawer(position);
        }
    }

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private ActionBarDrawerToggle drawerToggle;
    private RelativeLayout drawerPane;
    private CharSequence drawerTitle;
    private CharSequence title;

    private ArrayList<NavigationItem> navigationItems = new ArrayList<NavigationItem>();

    private AlertDialog dialog;

    private boolean landscapeMode = false;
    private boolean listShown = false;
    private boolean detailShown = false;

    private int productId = 0;

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;

    public static String NEKRETNINA_KEY = "NEKRETNINA_KEY";
    public static String NOTIF_TOAST = "notif_toast";
    public static String NOTIF_STATUS = "notif_statis";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        /*Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }*/

        // Draws navigation items
        navigationItems.add(new NavigationItem(getString(R.string.drawer_home), getString(R.string.drawer_home_long), R.drawable.ic_action_product));
        navigationItems.add(new NavigationItem(getString(R.string.drawer_settings), getString(R.string.drawer_Settings_long), R.drawable.ic_action_settings));
        title = drawerTitle = getTitle();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        drawerList = (ListView) findViewById(R.id.navList);

        // Populate the Navigtion Drawer with options
        drawerPane = (RelativeLayout) findViewById(R.id.drawerPane);
        DrawerListAdapter adapter = new DrawerListAdapter(this, navigationItems);

        // set a custom shadow that overlays the main content when the drawer opens
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
        drawerList.setAdapter(adapter);

        // Enable ActionBar app icon to behave as action to toggle nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final android.support.v7.app.ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer);
            actionBar.setHomeButtonEnabled(true);
            actionBar.show();
        }

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                toolbar,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(drawerTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };




        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        final ListView listView = (ListView) findViewById(R.id.nekretnine_list);

        try {
            List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryForAll();

            ListAdapter adapter2 = new ArrayAdapter<>(MainActivity.this, R.layout.list_item, list);
            listView.setAdapter(adapter2);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Nekretnina p = (Nekretnina) listView.getItemAtPosition(position);

                    Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                    intent.putExtra(NEKRETNINA_KEY, p.getmId());
                    startActivity(intent);
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        ListView listview = (ListView) findViewById(R.id.nekretnine_list);
        if (listview != null){
            ArrayAdapter<Nekretnina> adapter = (ArrayAdapter<Nekretnina>) listview.getAdapter();
            if(adapter!= null)
            {
                try {
                    adapter.clear();
                    List<Nekretnina> list = getDatabaseHelper().getNekretninaDao().queryForAll();
                    adapter.addAll(list);
                    adapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void showStatusMesage(String message){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.ic_launcher);
        mBuilder.setContentTitle("Nekretnina");
        mBuilder.setContentText(message);

        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_add);

        mBuilder.setLargeIcon(bm);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add_new_realestate:
                //DIALOG ZA UNOS PODATAKA
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.add_nekretnina_layout);

                Button add = (Button) dialog.findViewById(R.id.add_nekretnina);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText name = (EditText) dialog.findViewById(R.id.nekretnina_name);
                        EditText bio = (EditText) dialog.findViewById(R.id.nekretnina_biography);

                        EditText image = (EditText) dialog.findViewById(R.id.nekretnina_image);
                        EditText address = (EditText) dialog.findViewById(R.id.nekretnina_address);

                        EditText phone = (EditText) dialog.findViewById(R.id.nekretnina_phone);
                        EditText size = (EditText) dialog.findViewById(R.id.nekretnina_size);

                        EditText rooms = (EditText) dialog.findViewById(R.id.nekretnina_rooms);
                        EditText price = (EditText) dialog.findViewById(R.id.nekretnina_price);
                        //RatingBar rating = (RatingBar) dialog.findViewById(R.id.acrtor_rating);
                        //EditText birth = (EditText) dialog.findViewById(R.id.actor_birth);

                        Nekretnina a = new Nekretnina();
                        a.setmName(name.getText().toString());
                        a.setmBiography(bio.getText().toString());

                        a.setmImage(image.getText().toString());
                        a.setmAddress(address.getText().toString());

                        a.setmPhone(phone.getText().toString());
                        a.setmSize(size.getText().toString());

                        a.setmRooms(rooms.getText().toString());
                        a.setmPrice(price.getText().toString());
                        //a.setmBirth(birth.getText().toString());
                        //a.setmScore(rating.getRating());

                        try {
                            getDatabaseHelper().getNekretninaDao().create(a);

                            //provera podesenja
                            boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
                            boolean status = prefs.getBoolean(NOTIF_STATUS, false);

                            if (toast){
                                Toast.makeText(MainActivity.this, "Added new realestate", Toast.LENGTH_SHORT).show();
                            }

                            if (status){
                                showStatusMesage("Added new realestate");
                            }

                            //REFRESH
                            refresh();

                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        dialog.dismiss();

                    }
                });

                Button cancel = (Button) dialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void selectItemFromDrawer(int position) {
        if (position == 0){
        } else if (position == 1){
            Intent settings = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settings);
        }

        drawerList.setItemChecked(position, true);
        setTitle(navigationItems.get(position).getTitle());
        drawerLayout.closeDrawer(drawerPane);
    }




    //Metoda koja komunicira sa bazom podataka
    public DatabaseHelper getDatabaseHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }




}
