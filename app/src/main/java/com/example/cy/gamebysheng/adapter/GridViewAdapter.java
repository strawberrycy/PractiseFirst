package com.example.cy.gamebysheng.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.cy.gamebysheng.activity.MainActivity;

import java.util.List;

/**
 * Created by CY on 2018/4/1.
 */

public class GridViewAdapter extends BaseAdapter {
    private Context context;
    private List<Bitmap> bitmapItemList;

    public GridViewAdapter() {
    }

    public GridViewAdapter(Context context, List<Bitmap> bitmapItemList) {
        this.context = context;
        this.bitmapItemList = bitmapItemList;
    }

    @Override
    public int getCount() {
        return bitmapItemList.size();
    }

    @Override
    public Object getItem(int i) {
        return bitmapItemList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(bitmapItemList.get(i).getWidth(),bitmapItemList.get(i).getHeight()));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        }else{
            imageView = (ImageView) view;
        }
        imageView.setImageBitmap(bitmapItemList.get(i));
        return imageView;
    }
}
