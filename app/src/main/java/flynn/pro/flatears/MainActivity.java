package flynn.pro.flatears;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.support.constraint.ConstraintLayout;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Arrays;


//import android.Manifest;
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;

import com.google.android.gms.common.api.GoogleApiClient;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "FLATEARS";
    public static int MY_PERMISSIONS_REQUEST = 0;
    private static MainActivity instance;


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    public MainActivity() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }


    private Handler thandler = new Handler(Looper.getMainLooper());

    public static final int BITRATE_AMR = 2 * 1024 * 8; // bits/sec

    public static final int BITRATE_3GPP = 20 * 1024 * 8; // bits/sec


//   // boolean permissionReadContacts;
//    boolean permissionAccessphone;
////    boolean permisionrecordaudio;
//
//
//    public void checkPermissions_for_call_phone() {
//       // permissionReadContacts = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
//        permissionAccessphone = ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED;
//
//        if (!permissionAccessphone)
//            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//
//                // Should we show an explanation?
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
//
//                    // Show an expanation to the user *asynchronously* -- don't block
//                    // this thread waiting for the user's response! After the user
//                    // sees the explanation, try again to request the permission.
//                    new android.app.AlertDialog.Builder(this).setCancelable(true).setMessage(R.string.action_settings).setPositiveButton(R.string.action_restart, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.CALL_PHONE},
//                                    0x01);
//                        }
//                    }).show();
//
//
//
//                } else {
//
//                    // No explanation needed, we can request the permission.
//
//                    ActivityCompat.requestPermissions(this,
//                            new String[]{Manifest.permission.CALL_PHONE},
//                            0x01);
//
//
//
//                }
//            }
//    }
//
////    public void checkPermissions_for_record_audio() {
////        // permissionReadContacts = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
////        permisionrecordaudio = ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
////
////        if (!permisionrecordaudio)
////            if (ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
////
////                // Should we show an explanation?
////                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
////
////                    // Show an expanation to the user *asynchronously* -- don't block
////                    // this thread waiting for the user's response! After the user
////                    // sees the explanation, try again to request the permission.
////                    new android.app.AlertDialog.Builder(this).setCancelable(true).setMessage(R.string.action_settings).setPositiveButton(R.string.action_restart, new DialogInterface.OnClickListener() {
////                        @Override
////                        public void onClick(DialogInterface dialog, int which) {
////                            ActivityCompat.requestPermissions(MainActivity.this,
////                                    new String[]{Manifest.permission.RECORD_AUDIO},
////                                    0x01);
////                        }
////                    }).show();
////
////
////
////                } else {
////
////                    // No explanation needed, we can request the permission.
////
////                    ActivityCompat.requestPermissions(this,
////                            new String[]{Manifest.permission.RECORD_AUDIO},
////                            0x01);
////
////
////
////                }
////            }
////    }
//
//    public  boolean checkPermissions_for_write_external() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v(TAG,"Permission is granted");
//                return true;
//            } else {
//
//                Log.v(TAG,"Permission is revoked");
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                return false;
//            }
//        }
//        else { //permission is automatically granted on sdk<23 upon installation
//            Log.v(TAG,"Permission is granted");
//            return true;
//        }
//    }
//
////    public  boolean checkPermissions_record_audio() {
////        if (Build.VERSION.SDK_INT >= 23) {
////            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO)
////                    == PackageManager.PERMISSION_GRANTED) {
////                Log.v(TAG,"Permission is granted");
////                return true;
////            } else {
////
////                Log.v(TAG,"Permission is revoked");
////                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
////                return false;
////            }
////        }
////        else { //permission is automatically granted on sdk<23 upon installation
////            Log.v(TAG,"Permission is granted");
////            return true;
////        }
////    }
//
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case 0x01: {  // validateRequestPermissionsRequestCode in FragmentActivity requires requestCode to be of 8 bits, meaning the range is from 0 to 255.
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0) { // we only asked for one permission
//
//                    permissionAccessphone = grantResults[0] == PackageManager.PERMISSION_GRANTED;
////                    permisionrecordaudio = grantResults[0] == PackageManager.PERMISSION_GRANTED;
////                    permissionwriteexternalstorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
////                    permissionreadexternalstorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//
//
//                }
//            }
//        }
//    }



    @Override
    @TargetApi(24)
    protected void onCreate(Bundle savedInstanceState) {




//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE,
//                Manifest.permission.RECORD_AUDIO,
//                Manifest.permission.RECEIVE_BOOT_COMPLETED,
//                //   Manifest.permission.PROCESS_OUTGOING_CALLS,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);



        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CALL_PHONE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);


//        checkPermissions_for_call_phone();
//        checkPermissions_record_audio();

//        checkPermissions_for_write_external();

        Log.v(TAG, " !! Запуск программы..");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.v(TAG, "Toolbar создан");

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        Log.v(TAG, "PagerAdapter создан");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);


        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
        Log.v(TAG, "PagerAdapter установлен ViewPager");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(mViewPager);
            // :: TAB FIXED
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
            tabLayout.setSelectedTabIndicatorHeight(6);
            // :: TAB DRAWABLE
            tabLayout.getTabAt(0).setIcon(R.drawable.icn_norm);
            tabLayout.getTabAt(1).setIcon(R.drawable.icn_pref);
            tabLayout.getTabAt(2).setIcon(R.drawable.icn_play);
        }
        Log.v(TAG, "Вкладки созданы");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Инициирована процедура выгрузки данных", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Log.d(TAG, "Процедура выгрузки инициирована кнопкой");
                NetworkUtil.updateStatus();
                Transmitter.transmit();
            }
        });
        Log.v(TAG, "Плавающая кнопка создана");


        P.initializeInstance();
        //Инициализация SIMID
        try {
            P.getInstance().initPrefs();
        } catch (Exception e) {
            e.printStackTrace();
        }


        Log.v(TAG, "Настройки прочитаны");

        NetworkUtil.updateStatus();
        Log.v(TAG, "Статус сети обновлён");

//         runnable.run();
        //L.v(TAG, "Запущен таймер");

        // TODO :: ПРОВЕРЯТЬ, ПРОИЗОШЁЛ ЛИ ОТВЕТ
        // TODO :: ПЕРЕВОДИТЬ В ФОНОВЫЙ / БЕСШУМНЫЙ


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.


//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Log.v(TAG, "Toolbar создан");

//        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        Log.v(TAG, "PagerAdapter создан");



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
        Log.v(TAG, "Созданы пункты меню");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.showLog) {
            //ConstraintLayout mConstraintLayout = (ConstraintLayout) findViewById(R.id.drawer_layout);
            //TabItem mTabItem = (TabItem) findViewById(R.id.tabItem);

            DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (mDrawerLayout != null) mDrawerLayout.openDrawer(GravityCompat.START);

            return true;
        }

        //restart app
        if (id == R.id.restart) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Main Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        Debug.stopMethodTracing();
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//
//
//        client.disconnect();
//
//
//    }

//    Данный метод делает внезапные остановки приложения
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        if(intent.getBooleanExtra("close_activity",false)){
//            this.finish();
//
//        }
//    }
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Log.d("TMRHNDLR", "Вызов планировщика по таймеру..");
            Transmitter.transmit();
            thandler.postDelayed(this, 60000);
        }
    };

}
