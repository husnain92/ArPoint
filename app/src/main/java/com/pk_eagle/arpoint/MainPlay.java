package com.pk_eagle.arpoint;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by Eagle on 20-Mar-16.
 */
public class MainPlay extends Fragment {
    private ArrayList<GameEntity> mData = new ArrayList<>(0);
    private ArrayList<ModelAlbums> dataAlbum = new ArrayList<>(0);
    private ArrayList<ModelAlbums> dataAlbum1 = new ArrayList<>(0);
    private ArrayList<ModelAlbumSongs> dataAlbumSongs;
    FeatureCoverFlow mCoverFlow;
    ImageView btnMenu, btnPlay, imgShare, imgShop, imgEq, imgWeb, imgFB, imgInsta;
    TextView tvA, tvB, tvC, tvD, tvSeek;
    GridView gvAllSongs;
    MediaPlayer mPlayer;
    SeekBar playSeekBar;
    int timeElapsed, durationInMillis, curVolume = 0;
    private Handler durationHandler = new Handler();

    CoverFlowAdapter mAdapter;
    StringBuilder stringBuilder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.content_main, container, false);


        mData.add(new GameEntity(R.drawable.buy_now));
        mData.add(new GameEntity(R.drawable.social_facebook));
//        mData.add(new GameEntity(R.drawable.share));
//        mData.add(new GameEntity(R.drawable.website));
//        mData.add(new GameEntity(R.drawable.buy_now));
//        mData.add(new GameEntity(R.drawable.social_facebook));

        ModelAlbums mAlbum1 = new ModelAlbums();
        mAlbum1.setAlbum_ID("1");
        mAlbum1.setAlbum_Name("Asdf");
        mAlbum1.setAlbum_Image("http://www.arpointapp.com/albums_media/goodnight-my-love-i-miss-you-1.jpg");
dataAlbum1.add(mAlbum1);

        mCoverFlow = (FeatureCoverFlow) v.findViewById(R.id.coverflow);
        tvA = (TextView) v.findViewById(R.id.tvA);
        tvB = (TextView) v.findViewById(R.id.tvB);
        tvC = (TextView) v.findViewById(R.id.tvC);
        tvD = (TextView) v.findViewById(R.id.tvD);
        tvSeek = (TextView) v.findViewById(R.id.tvSeekText);


        btnMenu = (ImageView) v.findViewById(R.id.btnMenu);
        btnPlay = (ImageView) v.findViewById(R.id.btnPlay);
        imgShare = (ImageView) v.findViewById(R.id.imgShare);
        imgShop = (ImageView) v.findViewById(R.id.imgShop);
        imgEq = (ImageView) v.findViewById(R.id.imgEqualizer);
        imgFB = (ImageView) v.findViewById(R.id.imgFB);
        imgWeb = (ImageView) v.findViewById(R.id.imgWebSite);
        imgInsta = (ImageView) v.findViewById(R.id.imgInstagram);


        playSeekBar = (SeekBar) v.findViewById(R.id.seekBar);

        gvAllSongs=(GridView) v.findViewById(R.id.gridAlbumSongs);
        mPlayer = new MediaPlayer();

        new getAlbums().execute();


        mAdapter = new CoverFlowAdapter(getActivity());
        mAdapter.setData(dataAlbum1);
        mCoverFlow.setAdapter(mAdapter);
//        btnMenu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                displayPopupWindow(v);
//
//            }
//        });
        tvA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
//                startActivity(new Intent(getActivity(), testdrawer.class));

            }
        });
        tvB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Sign_Up.class));
            }
        });
        tvC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Profile.class));
            }
        });
        tvD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), Search_Music.class));
            }
        });


        imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Share App", Toast.LENGTH_SHORT).show();
            }
        });


        imgShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();

                Products rFragment = new Products();

                // Creating a fragment transaction
                FragmentTransaction ft = fragmentManager.beginTransaction();

                // Adding a fragment to the fragment transaction
                ft.replace(R.id.content_frame, rFragment, "Products");

                // Committing the transaction
                ft.commit();
//                Toast.makeText(MainActivity.this, "Products", Toast.LENGTH_SHORT).show();
                MainActivity.CurrentFragment="Products";
            }
        });


        imgEq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "EQ", Toast.LENGTH_SHORT).show();
            }
        });


        imgWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Artist WebSite", Toast.LENGTH_SHORT).show();
            }
        });


        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Artist FB", Toast.LENGTH_SHORT).show();
            }
        });


        imgInsta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Artist Instagram", Toast.LENGTH_SHORT).show();
            }
        });


//        mCoverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //TODO CoverFlow item clicked
//
////                if(dataAlbumSongs.get(position).getSongType().equals("free"))
//                if (AppUtils.progressDialog == null) {
//                    AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Streaming...", false);
//                    AppUtils.progressDialog.show();
//                }
//                Toast.makeText(getActivity(), "Streaming...", Toast.LENGTH_SHORT).show();
//                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                try {
//                    mPlayer.setDataSource(dataAlbum.get(0).getListSongs().get(0).getSongURL());
//                } catch (IllegalArgumentException e) {
//                    Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//                } catch (SecurityException e) {
//                    Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//                } catch (IllegalStateException e) {
//                    Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                try {
//
//                    mPlayer.prepare();
//
//                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                        @Override
//                        public void onPrepared(MediaPlayer mp) {
//                            if (AppUtils.progressDialog != null) {
//                                AppUtils.closeDialog();
//                                AppUtils.progressDialog = null;
//                            }
//
//                            setProgressText();
//                            mPlayer.start();
//                            btnPlay.setImageResource(R.drawable.pause);
//                            durationHandler.postDelayed(updateSeekBarTime, 100);
//
//
//                        }
//                    });
//
//                } catch (IllegalStateException e) {
//                    Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//                } catch (IOException e) {
//                    Toast.makeText(getActivity(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
//                }
//            }
//        });




//        playSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                SeekBar sb = (SeekBar)seekBar;
//                int playPositionInMillisecconds = (durationInMillis / 100) * sb.getProgress();
//                mPlayer.seekTo(playPositionInMillisecconds);
//            }
//        });

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayer.isPlaying()) {
                    btnPlay.setImageResource(R.drawable.play);
                    mPlayer.pause();
                } else {
                    btnPlay.setImageResource(R.drawable.pause);
                    mPlayer.start();

                }
            }
        });

        gvAllSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (AppUtils.progressDialog == null) {
                    AppUtils.progressDialog = AppUtils.createDialog(getActivity(), "Streaming...", false);
                    AppUtils.progressDialog.show();
                }
                if(mPlayer!=null) {
                    if (mPlayer.isPlaying())
                        mPlayer.reset();
                }
                if (dataAlbumSongs.get(position).getSongType().equals("free")) {

                    Toast.makeText(getActivity(), "Streaming...", Toast.LENGTH_SHORT).show();
                    mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    try {
                        mPlayer.setDataSource(dataAlbumSongs.get(position).getSongURL());
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!\n"+e, Toast.LENGTH_LONG).show();
                    } catch (SecurityException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!\n"+e, Toast.LENGTH_LONG).show();
                    } catch (IllegalStateException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!\n"+e, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {

                        mPlayer.prepare();

                        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mp) {
                                if (AppUtils.progressDialog != null) {
                                    AppUtils.closeDialog();
                                    AppUtils.progressDialog = null;
                                }

                                setProgressText();
                                mPlayer.start();
                                btnPlay.setImageResource(R.drawable.pause);
                                durationHandler.postDelayed(updateSeekBarTime, 100);


                            }
                        });

                    } catch (IllegalStateException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!\n"+e, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), "You might not set the URI correctly!\n"+e, Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(), "Song is not free. Please Buy First\nPayment Process Ahead", Toast.LENGTH_SHORT).show();
                }
            }

        });

        return v;
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
            Toast.makeText(getActivity(), " 1 = " + String.format("%02d:%02d:%02d/%02d:%02d:%02d",
                    currentHour, currentMint, currentSec, durationHour, durationMint, durationSec), Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(" 1 = " + String.format("%02d:%02d/%02d:%02d",
                    currentMint, currentSec, durationMint, durationSec));
            Toast.makeText(getActivity(), " 1 = " + String.format("%02d:%02d/%02d:%02d",
                    currentMint, currentSec, durationMint, durationSec), Toast.LENGTH_SHORT).show();
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
                            mAlbum.setAlbum_Image("http://"+j.getString("album_picture"));
                            JSONArray jary = new JSONArray();
                            jary = j.getJSONArray("album_songs");
                            if (!jary.getJSONObject(0).has("status")) {
                                dataAlbumSongs = new ArrayList<>(0);
                                for(int v=0;v<jary.length();v++) {
                                   ModelAlbumSongs a= new ModelAlbumSongs();
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
                        gvAllSongs.setAdapter(new Adapter_AlbumSongs(getActivity(),dataAlbum.get(0).getListSongs()));
                    }

                    mCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
                        @Override
                        public void onScrolledToPosition(int position) {
                            //TODO CoverFlow stopped to position
                            Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                gvAllSongs.setAdapter(new Adapter_AlbumSongs(getActivity(), dataAlbum.get(position).getListSongs()));

                        }

                        @Override
                        public void onScrolling() {
                            //TODO CoverFlow began scrolling
                        }
                    });



                    mAdapter = new CoverFlowAdapter(getActivity());
                    mAdapter.setData(dataAlbum);
                    mCoverFlow.setAdapter(mAdapter);

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

}
