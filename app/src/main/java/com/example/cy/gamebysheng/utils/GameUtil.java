package com.example.cy.gamebysheng.utils;

import android.util.Log;

import com.example.cy.gamebysheng.activity.MainActivity;
import com.example.cy.gamebysheng.bean.GridItem;
import com.example.cy.gamebysheng.bean.ItemBean;

import java.util.ArrayList;
import java.util.List;


/**
 * 拼图工具类：实现拼图的交换与生成算法
 */
public class GameUtil {

    // 游戏信息单元格Bean
    public static List<ItemBean> mItemBeans = new ArrayList<ItemBean>();
    private static int index;
    //空白的哪一格
    public static ItemBean mBlankItemBean;
    private static ItemBean tempItemBean;

    /**
     * 图片转化随机的矩阵
     */
    public static void getPuzzleGenerator() {
        for (int i = 0; i < mItemBeans.size(); i++) {
            index = (int) (Math.random() * 3 * 3);
            swapItem(mItemBeans.get(index), GameUtil.mBlankItemBean);
        }
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < mItemBeans.size(); i++) {
            data.add(mItemBeans.get(i).getmBitmapId());
        }
        //判断生成的游戏是否有解
        if (canSolve(data)) {
            return;
        } else {
            //回调自己
            getPuzzleGenerator();
        }
    }

    private static boolean canSolve(List<Integer> data) {
        //空白的地方的Id
        int blankId = GameUtil.mBlankItemBean.getmBitmapId();
        //可行性原则 ：根据算法判断是否有解
        if (data.size() % 2 == 1) {
            return getInversions(data) % 2 == 0;
        } else {
            if (((blankId - 1) / 3) % 2 == 1) {
                return getInversions(data) % 2 == 0;
            } else {
                return getInversions(data) % 2 == 1;
            }
        }
    }

    /**
     * 计算序列倒置和
     *
     * @param data
     * @return
     */
    private static int getInversions(List<Integer> data) {
        int inversionCount = 0;
        int inversions = 0;

        for (int i = 0; i < data.size(); i++) {
            for (int j = i + 1; j < data.size(); j++) {
                int index = data.get(i);
                if (data.get(j) != 0 && data.get(j) < index) {
                    inversionCount++;
                }
            }
            inversions += inversionCount;
            inversionCount = 0;
        }
        return inversions;
    }

    /**
     * 交换相邻的两个方框内的图片，在这块就采取最基本的这个方式，采用第三方的临时的变量存储，从而进行交换
     *
     * @param itemBean
     * @param mBlankItemBean
     */
    public static void swapItem(ItemBean itemBean, ItemBean mBlankItemBean) {
        tempItemBean = new ItemBean();
        //三步交换
        tempItemBean.setmBitmapId(itemBean.getmBitmapId());
        itemBean.setmBitmapId(mBlankItemBean.getmBitmapId());

        tempItemBean.setmBitmap(itemBean.getmBitmap());
        itemBean.setmBitmap(mBlankItemBean.getmBitmap());
        mBlankItemBean.setmBitmap(tempItemBean.getmBitmap());

        GameUtil.mBlankItemBean = itemBean;
    }

    public static boolean isMoveable(int position) {
        int blankId = GameUtil.mBlankItemBean.getmItemId() - 1;
        if (Math.abs(blankId - position) == 3) {
            return true;
        }

        if ((blankId / 3 == position / 3) && Math.abs(blankId - position) == 1) {
            return true;
        }
        return false;
    }

    public static boolean isSuccess() {
        for (ItemBean itemBean:
             GameUtil.mItemBeans){
            if (tempItemBean.getmBitmapId() != 0 && (tempItemBean.getmItemId())==tempItemBean.getmBitmapId()) {
                continue;
            }else if (tempItemBean.getmBitmapId() == 0 && tempItemBean.getmItemId() == 9){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

//
//    // 游戏信息单元格Bean
//    public static List<GridItem> gridItemList = new ArrayList<GridItem>();
//    // 空格单元格
//    public static GridItem blankGridItem = new GridItem();
//
//    /**
//     * 判断点击的Item是否可移动
//     *
//     * @param position position
//     * @return 能否移动
//     */
//    public static boolean isMoveable(int position) {
//        int type = PuzzleMain.TYPE;
//        // 获取空格Item
//        int blankId = GameUtil.blankGridItem.getItemId() - 1;
//        // 不同行 相差为type
//        if (Math.abs(blankId - position) == type) {
//            return true;
//        }
//        // 相同行 相差为1
//        return (blankId / type == position / type) &&
//                Math.abs(blankId - position) == 1;
//    }
//
//    /**
//     * 交换空格与点击Item的位置
//     *
//     * @param from  交换图
//     * @param blank 空白图
//     */
//    public static void swapItems(GridItem from, GridItem blank) {
//        GridItem tempGridItem = new GridItem();
//        // 交换BitmapId
//        tempGridItem.setBitmapId(from.getBitmapId());
//        from.setBitmapId(blank.getBitmapId());
//        blank.setBitmapId(tempGridItem.getBitmapId());
//        // 交换Bitmap
//        tempGridItem.setBitmap(from.getBitmap());
//        from.setBitmap(blank.getBitmap());
//        blank.setBitmap(tempGridItem.getBitmap());
//        // 设置新的Blank
//        GameUtil.blankGridItem = from;
//    }
//
//    /**
//     * 生成随机的Item
//     */
//    public static void getPuzzleGenerator() {
//        int index = 0;
//        // 随机打乱顺序
//        for (int i = 0; i < gridItemList.size(); i++) {
//            index = (int) (Math.random() *
//                    PuzzleMain.TYPE * PuzzleMain.TYPE);
//            swapItems(gridItemList.get(index), GameUtil.blankGridItem);
//        }
//        List<Integer> data = new ArrayList<Integer>();
//        for (int i = 0; i < gridItemList.size(); i++) {
//            data.add(gridItemList.get(i).getBitmapId());
//        }
//        // 判断生成是否有解
//        if (canSolve(data)) {
//            return;
//        } else {
//            Log.i("GameUtil","No answer,next generator...");
//            getPuzzleGenerator();
//        }
//    }
//
//    /**
//     * 是否拼图成功
//     *
//     * @return 是否拼图成功
//     */
//    public static boolean isSuccess() {
//        for (GridItem gridItem : GameUtil.gridItemList) {
//            if (gridItem.getBitmapId() != 0 &&
//                    (gridItem.getItemId()) == gridItem.getBitmapId()) {
//                continue;
//            } else if (gridItem.getBitmapId() == 0 &&
//                    gridItem.getItemId() == PuzzleMain.TYPE * PuzzleMain.TYPE) {
//                continue;
//            } else {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /**
//     * 该数据是否有解
//     *
//     * @param dataList 拼图数组数据
//     * @return 该数据是否有解
//     */
//    public static boolean canSolve(List<Integer> dataList) {
//        // 获取空格Id
//        int blankId = GameUtil.blankGridItem.getItemId();
//        // 可行性原则
//        if (dataList.size() % 2 == 1) {
//            return getInversions(dataList) % 2 == 0;
//        } else {
//            // 从底往上数,空格位于奇数行
//            if (((blankId - 1) / PuzzleMain.TYPE) % 2 == 1) {
//                return getInversions(dataList) % 2 == 0;
//            } else {
//                // 从底往上数,空位位于偶数行
//                return getInversions(dataList) % 2 == 1;
//            }
//        }
//    }
//
//    /**
//     * 计算倒置和算法
//     *
//     * @param dataList 拼图数组数据
//     * @return 该序列的倒置和
//     */
//    public static int getInversions(List<Integer> dataList) {
//        int inversions = 0;
//        int inversionCount = 0;
//        for (int i = 0; i < dataList.size(); i++) {
//            for (int j = i + 1; j < dataList.size(); j++) {
//                int index = dataList.get(i);
//                if (dataList.get(j) != 0 && dataList.get(j) < index) {
//                    inversionCount++;
//                }
//            }
//            inversions += inversionCount;
//            inversionCount = 0;
//        }
//        return inversions;
//    }
}