package com.pk_eagle.arpoint;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ArrayList<GameEntity> mData = new ArrayList<>(0);
    FeatureCoverFlow mCoverFlow;
    boolean doubleBackToExitPressedOnce = false;

    File file;

    public static String CurrentFragment = "MainPlay";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView icon = (ImageView) toolbar.findViewById(R.id.btnMenu);

        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setHomeButtonEnabled(false);

//        actionBar.setTitle("Ar's MidKnight Point");
//        actionBar.hide();
//actionBar.setCustomView(R.layout.custom_actionbar);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
//drawer.openDrawer(Gravity.RIGHT);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(0);

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });


        FragmentManager fragmentManager = getFragmentManager();

        MainPlaySongs rFragment = new MainPlaySongs();

        // Creating a fragment transaction
        FragmentTransaction ft = fragmentManager.beginTransaction();

        // Adding a fragment to the fragment transaction
        ft.replace(R.id.content_frame, rFragment, "MainPlay");
//ft.addToBackStack(null);
//        // Committing the transaction
        ft.commit();



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            this.doubleBackToExitPressedOnce = true;
            AppUtils.showToast(this,"Please click BACK again to exit", 2000);


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            if (CurrentFragment.equals("MainPlay")) {
            } else {


//                getFragmentManager().popBackStack();
                // Handle the camera action
//                Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getFragmentManager();

                MainPlaySongs rFragment = new MainPlaySongs();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment, "MainPlay");

                // Committing the transaction
                ft.commit();
                CurrentFragment = "MainPlay";
            }
        } else if (id == R.id.nav_logout) {


            String a = UtilSharedPref.getStringVal(getApplicationContext(), UtilSharedPref.USERID);
            UtilSharedPref.updateVal(getApplicationContext(), UtilSharedPref.USERID, "");
            UtilSharedPref.updateVal(getApplicationContext(), UtilSharedPref.USEREMAIL, "");


            if(MainPlaySongs.mPlayer!=null) {
                if(MainPlaySongs.mPlayer.isPlaying()) {
                    MainPlaySongs.mPlayer.stop();
                    MainPlaySongs.mPlayer.reset();
                }

            }


            UtilSharedPref.U_ID = "";
            UtilSharedPref.U_EMAIL = "";

            startActivity(new Intent(getApplicationContext(), Activity_Login.class));
            finish();

        } else if (id == R.id.nav_merch) {
            if (CurrentFragment.equals("Products")) {
            } else {
                FragmentManager fragmentManager = getFragmentManager();

                Products rFragment = new Products();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment, "Products");

                // Committing the transaction
                ft.commit();
//                Toast.makeText(MainActivity.this, "Products", Toast.LENGTH_SHORT).show();
                CurrentFragment = "Products";
            }
        } else if (id == R.id.nav_artist_profile) {
            if (CurrentFragment.equals("Artist")) {
            } else {
//                Toast.makeText(MainActivity.this, "Artist's Profile", Toast.LENGTH_SHORT).show();
                FragmentManager fragmentManager = getFragmentManager();

                Artist_Profile rFragment = new Artist_Profile();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment, "Artist");

                // Committing the transaction
                ft.commit();
                CurrentFragment = "Artist";
            }
        }

        else if(id==R.id.nav_pp)
        {
//            File fileBrochure = new File("/sdcard/ar_pp.pdf");
//            if (!fileBrochure.exists()) {
//                CopyAssetsbrochure();
//            }
//
//            /** PDF reader code */
//            file = new File("/sdcard/ar_pp.pdf");
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            try {
//                getApplicationContext().startActivity(intent);
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        file.delete();
//                    }
//                }, 60000);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(getApplicationContext(), "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
//            }

            AppUtils.showToast(getApplicationContext(),"PP",2000);

        }

        else if(id==R.id.nav_tos)
        {
//            File fileBrochure = new File("/sdcard/ar_pp.pdf");
//            if (!fileBrochure.exists()) {
//                CopyAssetsbrochure();
//            }
//
//            /** PDF reader code */
//            file = new File("/sdcard/ar_pp.pdf");
//
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            try {
//                getApplicationContext().startActivity(intent);
//                new Handler().postDelayed(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        file.delete();
//                    }
//                }, 60000);
//            } catch (ActivityNotFoundException e) {
//                Toast.makeText(getApplicationContext(), "NO Pdf Viewer", Toast.LENGTH_SHORT).show();
//            }

            AppUtils.showToast(getApplicationContext(),"TOS",2000);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



    private void CopyAssetsbrochure() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try
        {
            files = assetManager.list("");
        }
        catch (IOException e)
        {
            Log.e("tag", e.getMessage());
        }
        for(int i=0; i<files.length; i++)
        {
            String fStr = files[i];
            if(fStr.equalsIgnoreCase("ar_pp.pdf"))
            {
                InputStream in = null;
                OutputStream out = null;
                try
                {
                    in = assetManager.open(files[i]);
                    out = new FileOutputStream("/sdcard/" + files[i]);
                    copyFile(in, out);
                    in.close();
                    in = null;
                    out.flush();
                    out.close();
                    out = null;
                    break;
                }
                catch(Exception e)
                {
                    Log.e("tag", e.getMessage());
                }
            }
        }
    }


    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
