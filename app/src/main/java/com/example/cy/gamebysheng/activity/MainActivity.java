package com.example.cy.gamebysheng.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cy.gamebysheng.R;
import com.example.cy.gamebysheng.adapter.GridViewAdapter;
import com.example.cy.gamebysheng.bean.ItemBean;
import com.example.cy.gamebysheng.utils.GameUtil;
import com.example.cy.gamebysheng.utils.ImageUtils;
import com.example.cy.gamebysheng.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    public static Bitmap lastItem;
    private Bitmap photoSelected;
    private Button back;
    private Button origin;
    private Button reset;
    private TextView timer;
    private TextView steper;
    private boolean showOriginalImage;
    private GridView gridView;
    private int stepCount = 0;
    private int timerSeconds = 0;
    private List<Bitmap> bitmapItemList = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    timerSeconds++;
                    timer.setText("计时："+timerSeconds + "");
                    timer.setGravity(Gravity.CENTER);
                    timer.setPadding(10,5,10,5);
                    break;
                default:
                    break;
            }
        }
    };
    private ImageView imageView;
    private Timer timerClock;
    private TimerTask timerTask;
    private GridViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化界面
        initData();
        initView();
        //生成游戏
        generateGame();

    }

    /**
     * 加载图片
     */
    private void initData() {
        //自适应
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        photoSelected =selfAdaptionImage(bitmap);
    }

    /**
     * 生成游戏
     */
    private void generateGame() {
        ImageUtils.createInitBitmap(3, photoSelected, MainActivity.this);
        GameUtil.getPuzzleGenerator();
        //获取Bitmap集合
        for (ItemBean itemBean : GameUtil.mItemBeans) {
            bitmapItemList.add(itemBean.getmBitmap());
        }
        adapter = new GridViewAdapter(MainActivity.this, bitmapItemList);
        gridView.setAdapter(adapter);

        //启动计时器
        timerClock = new Timer(true);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };
        //注意连个时间安排器的区别
        timerClock.schedule(timerTask, 0,1000);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        back = (Button) findViewById(R.id.back);
        origin = (Button) findViewById(R.id.origin);
        reset = (Button) findViewById(R.id.reset);
        timer = (TextView) findViewById(R.id.timer);
        steper = (TextView) findViewById(R.id.steper);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetImg();
            }
        });
        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                originimg();
            }
        });
        //是否显示原图
        showOriginalImage = false;
        gridView = (GridView) findViewById(R.id.gv_puzzle_main_detail);
        gridView.setNumColumns(3);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(photoSelected.getWidth(), photoSelected.getHeight());
        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.BELOW, R.id.ll_puzzle_main_spinner);

        gridView.setLayoutParams(params);
        gridView.setHorizontalSpacing(0);
        gridView.setVerticalSpacing(0);
        // GridView点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long arg3) {
                // 判断是否可移动
                if (GameUtil.isMoveable(position)) {
                    // 交换点击Item与空格的位置
                    GameUtil.swapItem(
                            GameUtil.mItemBeans.get(position),
                            GameUtil.mBlankItemBean);
                    // 重新获取图片
                    recreateData();
                    // 通知GridView更改UI
                    adapter.notifyDataSetChanged();
                    // 更新步数
                    stepCount++;
                    steper.setPadding(10,5,10,5);
                    steper.setGravity(Gravity.CENTER);
                    steper.setText("步数：" + stepCount);
                    // 判断是否成功
                    if (GameUtil.isSuccess()) {
                        // 将最后一张图显示完整
                        recreateData();
                        bitmapItemList.remove(8);
                        bitmapItemList.add(lastItem);
                        // 通知GridView更改UI
                        adapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "拼图成功!",
                                Toast.LENGTH_LONG).show();
                        gridView.setEnabled(false);
                        timerClock.cancel();
                        timerTask.cancel();
                    }
                }
            }
        });

        steper.setText("步数："+stepCount++);
        timer.setText("0秒");

        addShowOriginalPhotoView();
    }

    private void resetImg() {
//        Toast.makeText(this, "点击了充值按钮", Toast.LENGTH_SHORT).show();
        //清除所有的config配置
        cleanConfig();
        //重新生成游戏
        generateGame();
        recreateData();
        //通知UI改变界面
        steper.setText("步数："+stepCount+"");
        adapter.notifyDataSetChanged();
        gridView.setEnabled(true);
    }

    private void originimg() {
//        Toast.makeText(this, "点击了原图按钮", Toast.LENGTH_SHORT).show();
        //两个动画
        Animation animShow = AnimationUtils.loadAnimation(
                MainActivity.this, R.anim.image_show_anim);
        Animation animHide = AnimationUtils.loadAnimation(
                MainActivity.this, R.anim.image_hide_anim);
        if (showOriginalImage) {
            imageView.startAnimation(animHide);
            imageView.setVisibility(View.GONE);
//            gridView.setVisibility(View.VISIBLE);
            showOriginalImage = false;
        } else {
            imageView.startAnimation(animShow);
            imageView.setVisibility(View.VISIBLE);
//            gridView.setVisibility(View.GONE);
            showOriginalImage = true;
        }
    }

    private void back() {
//        Toast.makeText(this, "点击了推出按钮", Toast.LENGTH_SHORT).show();
        finish();
    }

    /**
     * 展示原图的View
     */
    private void addShowOriginalPhotoView() {
//        RelativeLayout layout = (RelativeLayout) findViewById(R.id.rl_puzzle_main_main_layout);
//        imageView = new ImageView(MainActivity.this);
//        imageView.setImageBitmap(photoSelected);
//        int x = (int) (photoSelected.getWidth() * 0.9f);
//        int y = (int) (photoSelected.getHeight() * 0.9f);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(x, y);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT);
//        layout.addView(imageView);
        imageView = (ImageView) findViewById(R.id.image_origin);
        imageView.setVisibility(View.GONE);
    }




    @Override
    protected void onStop() {
        super.onStop();
        cleanConfig();
        MainActivity.this.finish();
    }

    private void recreateData() {
        bitmapItemList.clear();
        for (ItemBean itembean :
                GameUtil.mItemBeans
                ) {
            bitmapItemList.add(itembean.getmBitmap());
        }
    }

    private void cleanConfig() {
        GameUtil.mItemBeans.clear();
        timerClock.cancel();
        timerTask.cancel();
        stepCount = 0;
        timerSeconds = 0;
        //因为这块从始至终就只有这么一幅图片，所以，不需要清空图片
    }

    /**
     * 图片自适应处理
     * @param bitmap
     * @return
     */
    private Bitmap selfAdaptionImage(Bitmap bitmap) {
        // 将图片放大到固定尺寸
        int screenWidth = ScreenUtil.getScreenSize(this).widthPixels;
        int screenHeigt = ScreenUtil.getScreenSize(this).heightPixels;
        return ImageUtils.resizeBitmap(
                screenWidth * 0.8f, screenHeigt * 0.6f, bitmap);
    }
}
