package com.mvit.mihajlo.nekretnine.activities;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.mvit.mihajlo.nekretnine.R;
import com.mvit.mihajlo.nekretnine.db.DatabaseHelper;
import com.mvit.mihajlo.nekretnine.db.model.Nekretnina;
import java.sql.SQLException;
import java.util.List;
import static com.mvit.mihajlo.nekretnine.activities.MainActivity.NOTIF_STATUS;
import static com.mvit.mihajlo.nekretnine.activities.MainActivity.NOTIF_TOAST;

/**
 * Created by androiddevelopment on 24.6.17..
 */

public class DetailActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SharedPreferences prefs;
    private Nekretnina a;

    private EditText name;
    private EditText bio;
    private EditText birth;
    private RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        if(toolbar != null) {
            setSupportActionBar(toolbar);
        }

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int key = getIntent().getExtras().getInt(MainActivity.NEKRETNINA_KEY);

        try {
            a = getDatabaseHelper().getNekretninaDao().queryForId(key);

            name = (EditText) findViewById(R.id.actor_name);
            bio = (EditText) findViewById(R.id.actor_biography);
            birth = (EditText) findViewById(R.id.actor_birth);
            rating = (RatingBar) findViewById(R.id.acrtor_rating);

            name.setText(a.getmName());
            bio.setText(a.getmBiography());
            birth.setText(a.getmBirth());
            rating.setRating(a.getmScore());
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
                a.setmBirth(birth.getText().toString());
                a.setmBiography(bio.getText().toString());
                a.setmScore(rating.getRating());

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
