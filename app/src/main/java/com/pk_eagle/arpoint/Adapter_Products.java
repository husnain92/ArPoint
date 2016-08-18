package com.pk_eagle.arpoint;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Eagle on 12-Apr-16.
 */
public class Adapter_Products extends BaseAdapter {

    Activity activity;
    List<ModelProducts> prodlistdata;
    viewHolder vh;
    private LayoutInflater inflater;

    public Adapter_Products(Activity activity, List<ModelProducts> Listdata) {
        this.activity = activity;
        this.prodlistdata = Listdata;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            vh = new viewHolder();
            if (inflater == null)
                inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_products, null);

            vh.tvProductTitle = (TextView) view.findViewById(R.id.tvProduct_Title);
            vh.tvProductDetails=(TextView) view.findViewById(R.id.tvProduct_Details);

            vh.imgProduct=(ImageView) view.findViewById(R.id.imgProduct);

            vh.btnBuy=(Button) view.findViewById(R.id.btnBuyProduct);

            view.setTag(vh);

        } else {
            vh = (viewHolder) view.getTag();
        }

        vh.tvProductDetails.setText(prodlistdata.get(i).getProduct_Details());
        vh.tvProductTitle.setText(prodlistdata.get(i).getProduct_Name());
if(!prodlistdata.get(i).getProduct_Image().equals("")) {
    Picasso.with(activity)
            .load(prodlistdata.get(i).getProduct_Image())
            .placeholder(R.drawable.no_img)
            .into(vh.imgProduct);
}


        vh.btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle b=new Bundle();
                b.putString("ProdID",prodlistdata.get(i).getProduct_ID());
                FragmentTransaction ft = activity.getFragmentManager().beginTransaction();

                Product_Details rFragment=new Product_Details();
                rFragment.setArguments(b);

                // Adding a fragment to the fragment transaction
                ft.add(R.id.content_frame, rFragment, "MainPlay");
ft.addToBackStack(null);
                // Committing the transaction
                ft.commit();
            }
        });

        return view;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return prodlistdata.get(i);
    }

    @Override
    public int getCount() {
        return prodlistdata.size();
    }

    public class viewHolder {

        ImageView imgProduct;
        TextView tvProductTitle, tvProductDetails;
        Button btnBuy;

    }
}