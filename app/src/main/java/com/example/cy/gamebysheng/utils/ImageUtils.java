package com.example.cy.gamebysheng.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

import com.example.cy.gamebysheng.R;
import com.example.cy.gamebysheng.activity.MainActivity;
import com.example.cy.gamebysheng.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CY on 2018/3/24.
 */

public class ImageUtils {



    public static void createInitBitmap(int type, Bitmap picSelected, Context context){
        Bitmap bitmap = null;
        List<Bitmap> bitmapItems = new ArrayList<>();
        int itemWidth = picSelected.getWidth() / 3;
        int itemHeight = picSelected.getHeight() / 3;
        for (int i = 1; i <= 3; i++) {
            for (int j = 1; j <= 3; j++) {
                bitmap = Bitmap.createBitmap(picSelected,(j-1)*itemWidth,(i-1)*itemHeight,itemWidth,itemHeight);
                bitmapItems.add(bitmap);
                ItemBean itemBean = new ItemBean((i - 1) * type + j, (i - 1) * type + j, bitmap);
                GameUtil.mItemBeans.add(itemBean);
            }
        }
        int lastItemIndex = 8;
        //把这最后一张单独的保存起来，然后，在最终拼完的时候给加在最后的位置上就ok了
        MainActivity.lastItem = bitmapItems.get(lastItemIndex);
        bitmapItems.remove(8);
        GameUtil.mItemBeans.remove(8);
        Bitmap blankItem = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
        blankItem = Bitmap.createBitmap(blankItem,0,0,itemWidth,itemHeight);
        bitmapItems.add(blankItem);
        GameUtil.mItemBeans.add(new ItemBean(9,0,blankItem));
        GameUtil.mBlankItemBean = GameUtil.mItemBeans.get(8);
    }

    public static Bitmap resizeBitmap(float newWidth, float newHeight, Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postScale(newWidth/bitmap.getWidth(),newHeight/bitmap.getHeight());
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return newBitmap;
    }
}
