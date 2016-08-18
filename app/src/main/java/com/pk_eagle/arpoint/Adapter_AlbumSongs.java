package com.pk_eagle.arpoint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eagle on 17-Apr-16.
 */
public class Adapter_AlbumSongs extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    ArrayList<ModelAlbumSongs> modelAlbumsArrayList=new ArrayList();
    viewHolder vh;

    public Adapter_AlbumSongs(Context context,ArrayList<ModelAlbumSongs> modelAlbumsArrayList) {
        this.context = context;
        this.modelAlbumsArrayList=modelAlbumsArrayList;
    }

    public int getCount() {
        return modelAlbumsArrayList.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            vh=new viewHolder();
            if (inflater == null)
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.adapter_grid_songs, null);


            vh.tv=(TextView) view.findViewById(R.id.tvGridSongName);

            view.setTag(vh);

        }
        else
        {
            vh=(viewHolder) view.getTag();
        }

        vh.tv.setText((position+1)+": " +modelAlbumsArrayList.get(position).getSongName()+"\n ");
        return view;
    }

    public class viewHolder

    {
        TextView tv;
    }
}
