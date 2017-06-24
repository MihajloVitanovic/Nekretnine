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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static com.mvit.mihajlo.nekretnine.activities.MainActivity.NOTIF_STATUS;
import static com.mvit.mihajlo.nekretnine.activities.MainActivity.NOTIF_TOAST;

/**
 * Created by androiddevelopment on 24.6.17..
 */

public class DetailActivity extends AppCompatActivity {

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
    private Nekretnina a;

    private EditText name;
    private EditText bio;

    private EditText image;
    private EditText address;

    private EditText phone;
    private EditText size;

    private EditText rooms;
    private EditText price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);



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
        drawerList.setOnItemClickListener(new DetailActivity.DrawerItemClickListener());
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
        int key = getIntent().getExtras().getInt(MainActivity.NEKRETNINA_KEY);

        try {
            a = getDatabaseHelper().getNekretninaDao().queryForId(key);

            name = (EditText) findViewById(R.id.nekretnina_name);
            bio = (EditText) findViewById(R.id.nekretnina_biography);

            image = (EditText) findViewById(R.id.nekretnina_image);
            address = (EditText) findViewById(R.id.nekretnina_address);

            phone = (EditText) findViewById(R.id.nekretnina_phone);
            size = (EditText) findViewById(R.id.nekretnina_size);

            rooms = (EditText) findViewById(R.id.nekretnina_rooms);
            price = (EditText) findViewById(R.id.nekretnina_price);

            name.setText(a.getmName());
            bio.setText(a.getmBiography());

            image.setText(a.getmImage());
            address.setText(a.getmAddress());

            phone.setText(a.getmPhone());
            size.setText(a.getmSize());

            rooms.setText(a.getmRooms());
            price.setText(a.getmPrice());

        } catch (SQLException e) {
            e.printStackTrace();
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

    private void showMessage(String message){
        //provera podesenja
        boolean toast = prefs.getBoolean(NOTIF_TOAST, false);
        boolean status = prefs.getBoolean(NOTIF_STATUS, false);

        if (toast == true){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        if (status == true){
            showStatusMesage(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.detailmenu_edit:
                //POKUPITE INFORMACIJE SA EDIT POLJA
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
                    getDatabaseHelper().getNekretninaDao().update(a);

                    showMessage("Realestate detail updated");

                } catch (SQLException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.detailmenu_remove:
                try {
                    getDatabaseHelper().getNekretninaDao().delete(a);

                    showMessage("Realestate deleted");

                    finish(); //moramo pozvati da bi se vratili na prethodnu aktivnost
                } catch (SQLException e) {
                    e.printStackTrace();
                }
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
            Intent main = new Intent(DetailActivity.this,MainActivity.class);
            startActivity(main);
        } else if (position == 1){
            Intent settings = new Intent(DetailActivity.this,SettingsActivity.class);
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

        // nakon rada sa bazo podataka potrebno je obavezno
        //osloboditi resurse!
        if (databaseHelper != null) {
            OpenHelperManager.releaseHelper();
            databaseHelper = null;
        }
    }








}
