package com.tiara.newsapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by idn on 3/8/2017.
 */
public class CustomAdapter extends BaseAdapter {
    Activity c;
    ArrayList<Berita> datas;

    public CustomAdapter(MainActivity mainActivity, ArrayList<Berita> data) {
        datas = data;
        c = mainActivity;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.grid_item, null);
        ViewHolder v = new ViewHolder(view);
        v.textViewItem.setText(datas.get(i).getTittle());

        Picasso.with(c).load(datas.get(i).getGambar()).into(v.imageViewItem);
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.textViewItem)
        TextView textViewItem;
        @BindView(R.id.imageViewItem)
        ImageView imageViewItem;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
