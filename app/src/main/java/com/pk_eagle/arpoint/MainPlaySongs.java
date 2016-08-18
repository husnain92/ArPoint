package com.pk_eagle.arpoint;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.etsy.android.grid.StaggeredGridView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import me.crosswall.lib.coverflow.CoverFlow;
import me.crosswall.lib.coverflow.core.PagerContainer;

/**
 * Created by Eagle on 15-May-16.
 */
public class MainPlaySongs extends Fragment {


    private ArrayList<ModelAlbums> dataAlbum = new ArrayList<>(0);
    private ArrayList<ModelAlbumSongs> dataAlbumSongs;

    StaggeredGridView gridHome;

    static int finalTime = 0;

    Spinner spEQ;
    TextView tvBandSpinner,tvBandSeekbar;
    SeekBar seekBand;
    Equalizer trackEq;

    static RemoteViews notificationView;
    static NotificationManager notificationManager;
    static NotificationCompat.Builder builder;

    boolean repeat = true;

    ImageView imgMedia, imgShare, imgShop, imgEq, imgWeb, imgFB, imgInsta, imgNext, imgPrevious, imgRepeat;
    static ImageView btnPlay;
    TextView tvSeek, tvCurrentSongName;
    TextView tvSeekTextNotif;
    //    GridView gvAllSongs;
    public static MediaPlayer mPlayer;
    SeekBar playSeekBar;
    int timeElapsed, durationInMillis, curVolume = 0;
    private Handler durationHandler = new Handler();
    private Handler durationHandler1 = new Handler();

    CoverFlowAdapter mAdapter;
    StringBuilder stringBuilder;
    String myE = "No Error";

    ViewPager pager;
    int CurrentSong = 0;
    int AlbumPos = 0;
    short mBandNo;

    String CurrentSongURL="";

    boolean isPrepared = false;
    ArrayList<ModelAlbumSongs> modelAlbumsArrayList = new ArrayList();
    Adapter_AlbumSongs adapterSongs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.main_play_songs, container, false);


        PagerContainer mContainer = (PagerContainer) v.findViewById(R.id.pager_container);
        tvSeek = (TextView) v.findViewById(R.id.tvSeekText);
        tvCurrentSongName = (TextView) v.findViewById(R.id.tvCurrentPlaying);


        imgShare = (ImageView) v.findViewById(R.id.imgShare);
        imgShop = (ImageView) v.findViewById(R.id.imgShop);
        imgEq = (ImageView) v.findViewById(R.id.imgEqualizer);
        imgFB = (ImageView) v.findViewById(R.id.imgFB);
        imgWeb = (ImageView) v.findViewById(R.id.imgWebSite);
        imgInsta = (ImageView) v.findViewById(R.id.imgInstagram);

        imgRepeat = (ImageView) v.findViewById(R.id.btnRepeat);
        btnPlay = (ImageView) v.findViewById(R.id.btnPlay);
        imgNext = (ImageView) v.findViewById(R.id.btnNext);
        imgPrevious = (ImageView) v.findViewById(R.id.btnPrevious);
        imgMedia = (ImageView) v.findViewById(R.id.imgMedia);

        playSeekBar = (SeekBar) v.findViewById(R.id.seekBar);


//        gvAllSongs = (GridView) v.findViewById(R.id.gridAlbumSongs);
        gridHome = (StaggeredGridView) v.findViewById(R.id.gridAlbumSongs);


        gridHome.setSelected(true);

        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);


        new getAlbums().execute();

        pager = mContainer.getViewPager();

        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Share", Toast.LENGTH_SHORT).show();

            }
        });

        imgRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(repeat) {
                    imgRepeat.setImageDrawable(getResources().getDrawable(R.drawable.repeat_inactive));
                    repeat=false;
                    Toast.makeText(getActivity(), "Repeat Song Deactivated!", Toast.LENGTH_SHORT).show();
                }
                else {
                    imgRepeat.setImageDrawable(getResources().getDrawable(R.drawable.repeate_active));
                    repeat=true;
                    Toast.makeText(getActivity(), "Repeat Song Activated!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Toast.makeText(getActivity(), "On Scrolled "+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {
                modelAlbumsArrayList = dataAlbum.get(position).getListSongs();
                adapterSongs = new Adapter_AlbumSongs(getActivity(), modelAlbumsArrayList);
                gridHome.setAdapter(adapterSongs);

                tvCurrentSongName.setText(dataAlbum.get(position).getAlbum_Name());
                AlbumPos = position;
                CurrentSong = 0;

                Picasso.with(getActivity())
                        .load((dataAlbum.get(position).getAlbum_Image()))
//                    .placeholder(R.drawable.person_placeholder_big)
                        .fit()
//                    .transform(transformation)
                        .into(imgMedia);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();

                Frag_Equalizer rFragment = new Frag_Equalizer();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.add(R.id.content_frame, rFragment, "Products");

                // Committing the transaction
                ft.addToBackStack(null);
                ft.commit();
                Toast.makeText(getActivity(), "Products", Toast.LENGTH_SHORT).show();
                MainActivity.CurrentFragment = "Products";


//                startActivity(new Intent(MainPlaySongs.this,Equalizer.class));

            }
        });

        imgEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "EQ", Toast.LENGTH_SHORT).show();




                final Dialog dialog = new Dialog(getActivity());
                // Include dialog.xml file
                dialog.setContentView(R.layout.equalizer);
                // Set dialog title
                dialog.setTitle("Equalizer");

                // set values for custom dialog components - text, image and button
                spEQ=(Spinner) dialog.findViewById(R.id.spEQ);
                tvBandSpinner=(TextView) dialog.findViewById(R.id.tvBandSpinner);
                tvBandSeekbar= (TextView) dialog.findViewById(R.id.tvBandSeekbar);



                seekBand=(SeekBar) dialog.findViewById(R.id.seekBand);

                tvBandSeekbar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekBand.setVisibility(View.VISIBLE);
                        spEQ.setVisibility(View.GONE);

                        short[] levelRange = trackEq.getBandLevelRange();
                        final int mMinLevel = levelRange[0];
                        final int mMaxLevel = levelRange[1];
                         mBandNo = (short) spEQ.getSelectedItemPosition();

                        seekBand.setMax(mMaxLevel - mMinLevel);


                        seekBand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                                if(fromUser)
                                {
                                    Toast.makeText(getActivity(), "Max: "+mMaxLevel+" , Min: "+mMinLevel+"\nSeek to: "+progress+" BandNo: "+mBandNo, Toast.LENGTH_SHORT).show();
                                    trackEq = new Equalizer(0, mPlayer.getAudioSessionId());
                                    trackEq.setEnabled(true);
                                    trackEq.setBandLevel(mBandNo, (short) progress);
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                    }
                });

                tvBandSpinner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        seekBand.setVisibility(View.GONE);
                        spEQ.setVisibility(View.VISIBLE);
                    }
                });






                int xx=mPlayer.getAudioSessionId();
                trackEq = new Equalizer(0, mPlayer.getAudioSessionId());

                trackEq.setEnabled(true);

                int noPresets = trackEq.getNumberOfPresets();
                Toast.makeText(getActivity(), ""+noPresets, Toast.LENGTH_SHORT).show();
                List<String> presetNames = new ArrayList<String>();
                for(short presetValue=0; presetValue<noPresets; presetValue++)
                {
                    presetNames.add(trackEq.getPresetName(presetValue));
                }

                Toast.makeText(getActivity(), ""+presetNames, Toast.LENGTH_SHORT).show();


                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, presetNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spEQ.setAdapter(spinnerAdapter);


                spEQ.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        Toast.makeText(getActivity(), ""+mPlayer.getAudioSessionId(), Toast.LENGTH_SHORT).show();

                        trackEq = new Equalizer(0, mPlayer.getAudioSessionId());
                        trackEq.setEnabled(true);
                        trackEq.usePreset((short)position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                dialog.show();



//                FragmentManager fragmentManager = getFragmentManager();
//
//                Frag_Equalizer rFragment = new Frag_Equalizer();
//
//                // Creating a fragment transaction
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//
//                // Adding a fragment to the fragment transaction
//                ft.add(R.id.content_frame, rFragment, "Products");
//
//                // Committing the transaction
//                ft.addToBackStack(null);
//                ft.commit();

            }
        });

        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "FB", Toast.LENGTH_SHORT).show();

            }
        });

        imgWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "WEB", Toast.LENGTH_SHORT).show();

            }
        });

        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(), "Insta", Toast.LENGTH_SHORT).show();

            }
        });


        gridHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.stop();
                        mPlayer.reset();
                        btnPlay.setImageResource(R.drawable.play);
                    }
                }

                if (modelAlbumsArrayList.get(position).getSongType().equals("free")) {

                    int pos = position + 1;
                    tvCurrentSongName.setText(dataAlbum.get(AlbumPos).getAlbum_Name() + ": " + modelAlbumsArrayList.get(position).getSongName() + " - " + pos + " of " + modelAlbumsArrayList.size());
                    CurrentSong = position;
//                    new playNow().execute(modelAlbumsArrayList.get(position).getSongURL());
                    CurrentSongURL=modelAlbumsArrayList.get(position).getSongURL();
                    PlaySong(CurrentSongURL);
//                    Toast.makeText(getActivity(), "Previous Song\n"+CurrentSong+"\n"+modelAlbumsArrayList.get(CurrentSong).getSongURL(), Toast.LENGTH_SHORT).show();


                    gridHome.setSelection(position);

                } else {
                    Toast.makeText(getActivity(), "Song is not free. Please Buy First\nPayment Process Ahead", Toast.LENGTH_SHORT).show();
                }


            }
        });


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer != null) {
                    if (!mPlayer.isPlaying()) {
                        mPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);
                    } else {
                        mPlayer.pause();
                        btnPlay.setImageResource(R.drawable.play);
                    }
                } else {
//                    new playNow().execute(modelAlbumsArrayList.get(CurrentSong).getSongURL());
                    PlaySong(modelAlbumsArrayList.get(CurrentSong).getSongURL());
                    Toast.makeText(getActivity(), "Play Now First Song\n" + CurrentSong + "\n" + modelAlbumsArrayList.get(CurrentSong).getSongURL(), Toast.LENGTH_SHORT).show();

                }
            }
        });

        imgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                int startTime = mPlayer.getCurrentPosition();
//                int temp = (int) startTime;
//
//                if ((temp + 5000) <= finalTime) {
//                    startTime = startTime + 5000;
//                    mPlayer.seekTo((int) startTime);
//                    Toast.makeText(getActivity(), "Forward 5 Seconds", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
//                }
                PlaySong(modelAlbumsArrayList.get(CurrentSong+1).getSongURL());


//                if(CurrentSong<dataAlbumSongs.size())
//                {
//                    if (mPlayer != null) {
//                        if (mPlayer.isPlaying())
//                        {
//                            mPlayer.stop();
//                            mPlayer.reset();
//                            btnPlay.setImageResource(R.drawable.play);
//
//                        }
//                    }
//
//                    CurrentSong++;
//                    int temppos=CurrentSong+1;
//                    tvCurrentSongName.setText(dataAlbum.get(AlbumPos).getAlbum_Name()+": "+modelAlbumsArrayList.get(CurrentSong).getSongName()+" - "+temppos+" of "+modelAlbumsArrayList.size());
////                    new playNow().execute(modelAlbumsArrayList.get(CurrentSong).getSongURL());
////                    gvAllSongs.setSelection(CurrentSong);
//
//                    PlaySong(modelAlbumsArrayList.get(CurrentSong).getSongURL());
////                    Toast.makeText(getActivity(), "Next Song\n"+CurrentSong+"\n"+modelAlbumsArrayList.get(CurrentSong).getSongURL(), Toast.LENGTH_SHORT).show();
//
//
//                }

            }
        });

        imgPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                int startTime = mPlayer.getCurrentPosition();
                int temp = (int) startTime;

                if ((temp - 5000) > 0) {
                    startTime = startTime - 5000;

                    mPlayer.seekTo((int) startTime);
                    Toast.makeText(getActivity(), "Backward 5 Seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }


//                if(CurrentSong>0)
//                {
//                    if (mPlayer != null) {
//                        if (mPlayer.isPlaying())
//                        {
//                            mPlayer.stop();
//                            mPlayer.reset();
//                            btnPlay.setImageResource(R.drawable.play);
//
//                        }
//                    }
//                    CurrentSong--;
//                    int temppos=CurrentSong+1;
//                    tvCurrentSongName.setText(dataAlbum.get(AlbumPos).getAlbum_Name()+": "+modelAlbumsArrayList.get(CurrentSong).getSongName()+" - "+temppos+" of "+modelAlbumsArrayList.size());
////                    new playNow().execute(modelAlbumsArrayList.get(CurrentSong).getSongURL());
////                    gvAllSongs.setSelection(CurrentSong);
//
//                    PlaySong(modelAlbumsArrayList.get(CurrentSong).getSongURL());
////                    Toast.makeText(getActivity(), "Previous Song\n"+CurrentSong+"\n"+modelAlbumsArrayList.get(CurrentSong).getSongURL(), Toast.LENGTH_SHORT).show();
//
//                }

            }
        });


        return v;
    }


    public void PlaySong(String SongURL) {

        if (mPlayer != null) {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.reset();
                btnPlay.setImageResource(R.drawable.play);
            }
            new playNow().execute(SongURL);
        } else {
            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            new playNow().execute(SongURL);
        }


    }


    public class playNow extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Streaming...", false);
                AppUtils.progressDialog.show();
            }

        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            Boolean prepared;
            try {

                mPlayer.setDataSource(params[0]);

                mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // TODO Auto-generated method stub
//                        intialStage = true;
//                        playPause=false;
                        btnPlay.setImageResource(R.drawable.play);
                        mPlayer.stop();
                        mPlayer.reset();
                        durationHandler.removeCallbacks(updateSeekBarTime);
                        durationHandler1.removeCallbacks(updateSeekBarTime1);
                        if(repeat)
                            PlaySong(CurrentSongURL);

                    }
                });
                mPlayer.prepare();

                mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                        finalTime = mp.getDuration();
                    }
                });

                prepared = true;
            } catch (IllegalArgumentException e) {
                // TODO Auto-generated catch block
                Log.d("IllegarArgument", e.getMessage());
                prepared = false;
                e.printStackTrace();
                myE = e.toString() + "\n1";
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
                myE = e.toString() + "\n2";
            } catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
                String a = "";
                if (mPlayer == null) {
                    a = "NULL PLAYER";
                } else {
                    a = "NOT NULL";
                }
                myE = e.toString() + "\n3\n" + a;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                prepared = false;
                e.printStackTrace();
                myE = e.toString() + "\n4";
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (AppUtils.progressDialog != null) {
                AppUtils.closeDialog();
                AppUtils.progressDialog = null;
            }
            Log.d("Prepared", "//" + result);
            if (result) {
//                mPlayer.start();

                setProgressText();
                btnPlay.setImageResource(R.drawable.pause);
                durationHandler.postDelayed(updateSeekBarTime, 100);
                CustomNotification();
            } else {
                Toast.makeText(getActivity(), "" + myE, Toast.LENGTH_SHORT).show();
            }
//            intialStage = false;
        }
    }


    protected void setProgressText() {

        final int HOUR = 60 * 60 * 1000;
        final int MINUTE = 60 * 1000;
        final int SECOND = 1000;

        durationInMillis = mPlayer.getDuration();
        curVolume = mPlayer.getCurrentPosition();

        int durationHour = durationInMillis / HOUR;
        int durationMint = (durationInMillis % HOUR) / MINUTE;
        int durationSec = (durationInMillis % MINUTE) / SECOND;

        int currentHour = curVolume / HOUR;
        int currentMint = (curVolume % HOUR) / MINUTE;
        int currentSec = (curVolume % MINUTE) / SECOND;

        if (durationHour > 0) {
            System.out.println(" 1 = " + String.format("%02d:%02d:%02d/%02d:%02d:%02d",
                    currentHour, currentMint, currentSec, durationHour, durationMint, durationSec));
//            Toast.makeText(getActivity(), " 1 = " + String.format("%02d:%02d:%02d/%02d:%02d:%02d",
//                    currentHour, currentMint, currentSec, durationHour, durationMint, durationSec), Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(" 1 = " + String.format("%02d:%02d/%02d:%02d",
                    currentMint, currentSec, durationMint, durationSec));
//            Toast.makeText(getActivity(), " 1 = " + String.format("%02d:%02d/%02d:%02d",
//                    currentMint, currentSec, durationMint, durationSec), Toast.LENGTH_SHORT).show();
        }

        playSeekBar.setMax(durationInMillis);

    }


    //handler to change seekBarTime
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mPlayer.getCurrentPosition();
            //set seekbar progress
            playSeekBar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = timeElapsed;
            tvSeek.setText(String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)), TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis), TimeUnit.MILLISECONDS.toSeconds((long) durationInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis))));
//            tvSeekTextNotif.setText(String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)), TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis), TimeUnit.MILLISECONDS.toSeconds((long) durationInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis))));

            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };


    public class getAlbums extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (AppUtils.progressDialog == null) {
                AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Loading...", false);
                AppUtils.progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
            nameValuePairs.add(new BasicNameValuePair("get", "true"));

            nameValuePairs.add(new BasicNameValuePair("app_id", "1"));
            nameValuePairs.add(new BasicNameValuePair("limit", ""));
            nameValuePairs.add(new BasicNameValuePair("offset", ""));

            ServiceHandler sh = new ServiceHandler();
            stringBuilder = new StringBuilder();
            stringBuilder = sh.makeServiceCall("http://arpointapp.com/api/get-albums.php", ServiceHandler.POST, nameValuePairs);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            try {
                if (stringBuilder != null || !stringBuilder.equals("")) {
                    JSONObject jobj = new JSONObject(stringBuilder.toString());
                    JSONArray jarray = jobj.getJSONArray("response");


                    JSONObject j = jarray.getJSONObject(0);
                    if (j.has("status")) {
                        if (AppUtils.progressDialog != null) {
                            AppUtils.closeDialog();
                            AppUtils.progressDialog = null;
                        }
                        AppUtils.showToast(getActivity().getApplicationContext(), j.getString("message"), 2000);


                    } else

                    {

                        for (int x = 0; x < jarray.length(); x++) {
                            j = jarray.getJSONObject(x);
                            ModelAlbums mAlbum = new ModelAlbums();
                            mAlbum.setAlbum_ID(j.getString("album_id"));
                            mAlbum.setAlbum_Name(j.getString("album_name"));
                            mAlbum.setAlbum_Image("http://" + j.getString("album_picture"));
                            JSONArray jary = new JSONArray();
                            jary = j.getJSONArray("album_songs");
                            if (!jary.getJSONObject(0).has("status")) {
                                dataAlbumSongs = new ArrayList<>(0);
                                for (int v = 0; v < jary.length(); v++) {
                                    ModelAlbumSongs a = new ModelAlbumSongs();
                                    a.setSongID(jary.getJSONObject(v).getString("song_id"));
                                    a.setSongURL("http://" + jary.getJSONObject(v).getString("song_url"));
                                    a.setSongName(jary.getJSONObject(v).getString("song_name"));
                                    a.setSongType(jary.getJSONObject(v).getString("song_type"));
                                    a.setSongPrice(jary.getJSONObject(v).getString("song_price"));
                                    a.setIsBought(jary.getJSONObject(v).getString("song_buyed"));
                                    dataAlbumSongs.add(a);
                                }
                                mAlbum.setListSongs(dataAlbumSongs);
                            }

                            dataAlbum.add(mAlbum);

                        }
                        modelAlbumsArrayList = dataAlbum.get(0).getListSongs();
                        adapterSongs = new Adapter_AlbumSongs(getActivity(), modelAlbumsArrayList);
                        gridHome.setAdapter(adapterSongs);
                    }

                    tvCurrentSongName.setText(dataAlbum.get(0).getAlbum_Name() + ": " + dataAlbumSongs.get(0).getSongName() + " - 0 of " + dataAlbumSongs.size());

//                    mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
//                        @Override
//                        public void onScrolledToPosition(int position) {
//                            //TODO CoverFlow stopped to position
//                            Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                            gvAllSongs.setAdapter(new Adapter_AlbumSongs(getActivity(), dataAlbum.get(position).getListSongs()));
//
//                        }
//
//                        @Override
//                        public void onScrolling() {
//                            //TODO CoverFlow began scrolling
//                        }
//                    });
//
//
//
//                    mAdapter = new CoverFlowAdapter(getActivity());
//                    mAdapter.setData(dataAlbum);
//                    mCoverFlow.setAdapter(mAdapter);


                    PagerAdapter adapter = new MyPagerAdapter();
                    pager.setAdapter(adapter);

                    pager.setOffscreenPageLimit(adapter.getCount());

                    pager.setClipChildren(false);

                    Picasso.with(getActivity())
                            .load((dataAlbum.get(0).getAlbum_Image()))
//                    .placeholder(R.drawable.person_placeholder_big)
                            .fit()
//                    .transform(transformation)
                            .into(imgMedia);
                    new CoverFlow.Builder()
                            .with(pager)
                            .scale(0.5f)
                            .pagerMargin(0f)
                            .spaceSize(0f)
                            .rotationY(10f)
                            .build();

                    tvCurrentSongName.setText(dataAlbum.get(0).getAlbum_Name());

                }
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            } catch (Exception e) {
                if (AppUtils.progressDialog != null) {
                    AppUtils.closeDialog();
                    AppUtils.progressDialog = null;
                }
            }


        }
    }


    public void CustomNotification() {


        Intent PlayButton = new Intent("Play");
        PlayButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PlayButton.putExtra("DO", "play");
        PendingIntent pendingSwitchIntentPlay = PendingIntent.getBroadcast(getActivity(), 0, PlayButton, 0);


        Intent ForwardButton = new Intent("Farward");
        ForwardButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        ForwardButton.putExtra("DO", "forward");
        PendingIntent pendingSwitchIntentForward = PendingIntent.getBroadcast(getActivity(), 0, ForwardButton, 0);

        Intent BackwardButton = new Intent("Backward");
        BackwardButton.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        BackwardButton.putExtra("DO", "backward");
        PendingIntent pendingSwitchIntentBackward = PendingIntent.getBroadcast(getActivity(), 0, BackwardButton, 0);


        notificationView = new RemoteViews(getActivity().getPackageName(), R.layout.mediaplayer_notif);

        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        builder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.ar_logo).setAutoCancel(false).setTicker("MidKnight's ArPoint").setContent(notificationView);


        notificationView.setOnClickPendingIntent(R.id.btnPlayNotif, pendingSwitchIntentPlay);
        notificationView.setOnClickPendingIntent(R.id.btnSeekForwardNotif, pendingSwitchIntentForward);
        notificationView.setOnClickPendingIntent(R.id.btnSeekBackwardNotif, pendingSwitchIntentBackward);

//        Bitmap bitmap = ((BitmapDrawable) imgMedia.getDrawable()).getBitmap();
//        notificationView.setImageViewBitmap(R.id.imgNotifMedia, bitmap);
        notificationView.setTextViewText(R.id.tvSongNamePlayerNotif, tvCurrentSongName.getText().toString());


        notificationManager.notify(1, builder.build());

        durationHandler1.postDelayed(updateSeekBarTime1, 100);


    }


    private Runnable updateSeekBarTime1 = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mPlayer.getCurrentPosition();
            //set seekbar progress
            playSeekBar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = timeElapsed;
//            tvSeek.setText(String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)), TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis), TimeUnit.MILLISECONDS.toSeconds((long) durationInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis))));
//            tvSeekTextNotif.setText(String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)), TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis), TimeUnit.MILLISECONDS.toSeconds((long) durationInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis))));
            notificationView.setTextViewText(R.id.tvSeekTextNotif, String.format("%02d:%02d / %02d:%02d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining)), TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis), TimeUnit.MILLISECONDS.toSeconds((long) durationInMillis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) durationInMillis))));
            builder = new NotificationCompat.Builder(getActivity()).setSmallIcon(R.drawable.ar_logo).setTicker("MidKnight's ArPoint").setContent(notificationView);
            notificationManager.notify(1, builder.build());
            //repeat yourself that again in 100 miliseconds
            durationHandler1.postDelayed(this, 100);

        }
    };


    public static class DownloadCancelReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


            String DO = intent.getAction();

            if (DO.equals("Play")) {
                if (mPlayer != null) {
                    if (mPlayer.isPlaying()) {
                        mPlayer.pause();
                        btnPlay.setImageResource(R.drawable.play);


                        notificationView.setImageViewResource(R.id.btnPlayNotif, R.drawable.play);
                        builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ar_logo).setTicker("MidKnight's ArPoint").setContent(notificationView);
                        notificationManager.notify(1, builder.build());
                    } else {
                        mPlayer.start();
                        btnPlay.setImageResource(R.drawable.pause);


                        notificationView.setImageViewResource(R.id.btnPlayNotif, R.drawable.pause);
                        builder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ar_logo).setTicker("MidKnight's ArPoint").setContent(notificationView);
                        notificationManager.notify(1, builder.build());
                    }
                }
            } else if (DO.equals("Farward")) {
                int startTime = mPlayer.getCurrentPosition();
                int temp = (int) startTime;

                if ((temp + 5000) <= finalTime) {
                    startTime = startTime + 5000;
                    mPlayer.seekTo((int) startTime);
                    Toast.makeText(context, "Song Forward 5 Seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cannot forward 5 seconds", Toast.LENGTH_SHORT).show();
                }

            } else if (DO.equals("Backward")) {
                int startTime = mPlayer.getCurrentPosition();
                int temp = (int) startTime;

                if ((temp - 5000) > 0) {
                    startTime = startTime - 5000;

                    mPlayer.seekTo((int) startTime);
                    Toast.makeText(context, "Song Backward 5 Seconds", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Cannot backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Noting", Toast.LENGTH_SHORT).show();
            }


        }
    }


    private class MyPagerAdapter extends PagerAdapter {


        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_cover, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image_cover);
//            imageView.setImageDrawable(getResources().getDrawable(DemoData.covers[position]));
            Picasso.with(getActivity())
                    .load((dataAlbum.get(position).getAlbum_Image()))
//                    .placeholder(R.drawable.person_placeholder_big)
                    .fit()
//                    .transform(transformation)
                    .into(imageView);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return dataAlbum.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
