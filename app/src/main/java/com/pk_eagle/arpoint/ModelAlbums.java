package com.pk_eagle.arpoint;

import java.util.ArrayList;

/**
 * Created by Eagle on 13-Apr-16.
 */
public class ModelAlbums {


    private String Album_ID;
    private String Album_Name;
    private String Album_Image;
    private String SongURL;
    private String SongName;
    private ArrayList<ModelAlbumSongs> ListSongs ;

    public String getAlbum_ID() {
        return Album_ID;
    }

    public void setAlbum_ID(String album_ID) {
        Album_ID = album_ID;
    }

    public String getAlbum_Name() {
        return Album_Name;
    }

    public void setAlbum_Name(String album_Name) {
        Album_Name = album_Name;
    }

    public String getAlbum_Image() {
        return Album_Image;
    }

    public void setAlbum_Image(String album_Image) {
        Album_Image = album_Image;
    }

    public String getSongURL() {
        return SongURL;
    }

    public void setSongURL(String songURL) {
        SongURL = songURL;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public ArrayList<ModelAlbumSongs> getListSongs() {
        return ListSongs;
    }

    public void setListSongs(ArrayList<ModelAlbumSongs> listSongs) {
        ListSongs = listSongs;
    }


}
