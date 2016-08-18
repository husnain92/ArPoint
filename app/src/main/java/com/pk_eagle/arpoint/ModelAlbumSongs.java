package com.pk_eagle.arpoint;

/**
 * Created by Eagle on 17-Apr-16.
 */
public class ModelAlbumSongs
{
    private String SongID;
    private String SongName;
    private String SongURL;
    private String SongType;
    private String SongPrice;
    private String IsBought;

    public String getSongID() {
        return SongID;
    }

    public void setSongID(String songID) {
        SongID = songID;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getSongURL() {
        return SongURL;
    }

    public void setSongURL(String songURL) {
        SongURL = songURL;
    }

    public String getSongType() {
        return SongType;
    }

    public void setSongType(String songType) {
        SongType = songType;
    }

    public String getSongPrice() {
        return SongPrice;
    }

    public void setSongPrice(String songPrice) {
        SongPrice = songPrice;
    }

    public String getIsBought() {
        return IsBought;
    }

    public void setIsBought(String isBought) {
        IsBought = isBought;
    }
}
