package activity;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.service.media.CameraPrewarmService;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.freeplayer.R;
import Adapter.PlayListDetailAdapter;
import Dao.MyInterFaceMgr;
import Dao.SongInfo;
import Helper.MediaPlayerHelper;
import Helper.PopWindowHelper;
import Service.WidgetService;
import fragment.MainActivityMvFragment;
import fragment.FirstPageFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import fragment.SongManagementFragment;
import fragment.UserPageFragment;
import myView.CircularProgressView;
import Adapter.TabFragmentPagerAdapter;
import myView.MyBroadcastReceiver;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static Context context;
    private List<Fragment> list;
    private CircularProgressView progressBar;
    private ViewPager myViewPager;
    private FrameLayout firstPageLayout;
    private FrameLayout searchPageLayout;
    private FrameLayout mvPageLayout;
    private FrameLayout userPageLayout;
    private Button firstPageBut;
    private RelativeLayout relativeLayout;
    private RelativeLayout toolbarLayout;
    static MediaPlayerHelper mediaPlayer;
    private Button searchPageBut;
    private Button mvPageBut;
    private Button userPageBut;
    private ImageButton toolbarPlayBut;
    private ImageButton toolbarPlayListBut;
    private ImageView toolbarSongImage;
    private TextView toolbarSongNameText;
    public static List<SongInfo> musicList;
    int lastIndex=0;
    MediaSession session;
    private AudioManager audioManager ;
    TelephonyManager telephonyManager;
    private ComponentName mComponentName;
    private PopupWindow mPopWindow;
    private CameraPrewarmService camera;

    final String TAG =this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: "+"唤醒了主活动");
        setContentView(R.layout.activity_main);
        initView();
        init();
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setViewPlayStatus();
                    break;
                case 2:
                    onPlayMethod();
                    break;
                case 3:
                    toolbarPlayBut.setEnabled(false);
                    progressBar.setProgress(0);
                    Glide.with(context).asGif().load(R.drawable.huanchong).into(toolbarPlayBut);
                    toolbarSongNameText.setText(mediaPlayer.playingSongInfo.getName()+"-"+mediaPlayer.playingSongInfo.getArtist());
                    toolbarSongImage.setImageResource(R.drawable.qqmusic);
                    break;
                case 4:
                    setViewPauseStatus();
                    break;
            }
        }
    };

    public void setViewPlayStatus(){
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        toolbarPlayBut.setImageResource(R.drawable.play_w);
        toolbarPlayBut.setEnabled(true);
        toolbarSongNameText.setSelected(true);
        Glide.with(context).load(mediaPlayer.playingSongInfo.getPic120()).into(toolbarSongImage);
    }
    public void setViewPauseStatus(){
        toolbarPlayBut.setImageResource(R.drawable.pause_w);
    }



    Timer timer=new Timer();

    TimerTask task = new TimerTask(){

        @Override
        public void run() {
            if(false)
            {
                return;
            }
            if(mediaPlayer.isPlaying())
            {
                handler.sendEmptyMessage(2);
            }
        }
    };

    private void initView(){
        myViewPager = findViewById(R.id.main_view_paper);
        mediaPlayer=MediaPlayerHelper.getInstance(this);
        progressBar=findViewById(R.id.main_toolbar_progressbar);
        firstPageLayout=findViewById(R.id.first_page_layout);
        searchPageLayout=findViewById(R.id.search_page_layout);
        mvPageLayout=findViewById(R.id.mv_page_layout);
        userPageLayout=findViewById(R.id.user_page_layout);
        firstPageBut=findViewById(R.id.first_page_but);
        searchPageBut=findViewById(R.id.search_page_but);
        mvPageBut=findViewById(R.id.mv_page_but);
        userPageBut=findViewById(R.id.user_page_but);
        toolbarPlayBut=findViewById(R.id.main_toolbar_play_but);
        toolbarPlayListBut=findViewById(R.id.main_toolbar_play_list_but);
        toolbarSongImage=findViewById(R.id.main_toolbar_song_image_pic);
        toolbarSongNameText=findViewById(R.id.main_toolbar_song_name_text);
        toolbarLayout=findViewById(R.id.main_toolbar_main_layout);
        relativeLayout=findViewById(R.id.main_music_right_layout);
    }

    private void init(){
        context=this;
        firstPageLayout.setOnClickListener(this);
        searchPageLayout.setOnClickListener(this);
        mvPageLayout.setOnClickListener(this);
        userPageLayout.setOnClickListener(this);
        toolbarSongImage.setOnClickListener(this);
        toolbarPlayListBut.setOnClickListener(this);
        toolbarSongImage.setOnClickListener(this);
        toolbarLayout.setOnClickListener(this);
        if(mediaPlayer.isPlaying()){
            Glide.with(this).asGif().load(R.drawable.huanchong).into(toolbarPlayBut);
        }else{
            toolbarPlayBut.setImageResource(R.drawable.pause_w);
        }
        //tabs=findViewById(R.id.tabs);
        //绑定点击事件
        myViewPager.setOnPageChangeListener(new MyPagerChangeListener()) ;
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new FirstPageFragment());
        list.add(new SongManagementFragment());
        list.add(new MainActivityMvFragment());
        list.add(new UserPageFragment());
        FragmentManager fragmentManager = getSupportFragmentManager();
        myViewPager.setAdapter(new TabFragmentPagerAdapter(fragmentManager,null,list));
        //tabs.setViewPager(myViewPager);
        //初始化显示第一个页面
        myViewPager.setCurrentItem(0);
        myViewPager.setOffscreenPageLimit(4);
        //myViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        mediaPlayer.setmOnInitMusicListener(new MediaPlayerHelper.OnInitMusicListener() {
            @Override
            public void initMode() {
                handler.sendEmptyMessage(3);
            }
        });

        mediaPlayer.setmOnMediaHelperPrepareListener(new MediaPlayerHelper.OnMediaHelperPrepareListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                handler.sendEmptyMessage(1);
            }
        });

        mediaPlayer.setmOnMediaHelperStatusChangeListener(new MediaPlayerHelper.OnMediaHelperStatusChangeListener() {
            @Override
            public void statusChangeListener() {
                if(mediaPlayer.isPlaying()){
                    handler.sendEmptyMessage(1);
                }else{
                    handler.sendEmptyMessage(4);
                }
            }
        });

        timer.schedule(task,0,200);
        mediaPlayer.setmOnMediaHelperCompletionListener(new MediaPlayerHelper.OnMediaHelperCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.next();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer.playOrPause();
            }
        });
        toolbarPlayListBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopWindowHelper.showPlayListPopWindow(context,mediaPlayer);
            }
        });
        setBroadCastReciver();
        Intent serviceIntent=new Intent(this,WidgetService.class);
        //startService(serviceIntent);
    }

    private void onPlayMethod(){
        int a=mediaPlayer.getCurrentPosition()*100/mediaPlayer.getDuration();
        progressBar.setProgress(a);
        int index=mediaPlayer.getLrcPlayIndex();
        if(index!=lastIndex){
            toolbarSongNameText.setText(mediaPlayer.playingSongInfo.getName()+"-"+mediaPlayer.playingSongInfo.getArtist()+"  "+mediaPlayer.lrc1);
            lastIndex=index;
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d(TAG, "onPostResume: "+"唤醒了主活动");
        if(mediaPlayer.isPlaying()){
            toolbarPlayBut.setImageResource(R.drawable.play_w);
        }else{
            toolbarPlayBut.setImageResource(R.drawable.pause_w);
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.first_page_layout:
                firstPageBut.setBackgroundResource(R.drawable.first_page_on);
                myViewPager.setCurrentItem(0);
                break;
            case R.id.search_page_layout:
                myViewPager.setCurrentItem(1);
                searchPageBut.setBackgroundResource(R.drawable.music_page_on);
                break;
            case R.id.mv_page_layout:
                myViewPager.setCurrentItem(2);
                mvPageBut.setBackgroundResource(R.drawable.mv_on);
                break;
            case R.id.user_page_layout:
                myViewPager.setCurrentItem(3);
                userPageBut.setBackgroundResource(R.drawable.user_on);
                break;
            case R.id.main_toolbar_main_layout:
                Intent intent=new Intent(this, MainPlayActivity.class);
                startActivity(intent);
                break;
        }
    }

    /*
     * 设置一个ViewPager的侦听事件，当左右滑动ViewPager时菜单栏被选中状态跟着改变
     *
     */

    public class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            changeBottomViewStatus(arg0);
        }
    }

    private void changeBottomViewStatus(int id){
        reastButtonAndTextView();
        switch (id){
            case 0:
                Log.d("ceshi", "onPageSelected: "+"切换到1");
                firstPageBut.setBackgroundResource(R.drawable.first_page_on);
                break;
            case 1:
                Log.d("ceshi", "onPageSelected: "+"切换到2");
                searchPageBut.setBackgroundResource(R.drawable.music_page_on);
                break;
            case 2:
                Log.d("ceshi", "onPageSelected: "+"切换到3");
                mvPageBut.setBackgroundResource(R.drawable.list_page_on);
                break;
            case 3:
                Log.d("ceshi", "onPageSelected: "+"切换到4");
                userPageBut.setBackgroundResource(R.drawable.user_on);
                break;
        }
    }

    private void reastButtonAndTextView(){
        firstPageBut.setBackgroundResource(R.drawable.first_page_off);
        searchPageBut.setBackgroundResource(R.drawable.music_page_off);
        mvPageBut.setBackgroundResource(R.drawable.mv_off);
        userPageBut.setBackgroundResource(R.drawable.user_off);
    }

    private  void setBroadCastReciver(){
        audioManager = (AudioManager)this.getSystemService(Context.AUDIO_SERVICE);
        mComponentName = new ComponentName(this.getPackageName(), MyBroadcastReceiver.class.getName());
        audioManager.registerMediaButtonEventReceiver(mComponentName);
    }

    private  void setTelBroadCastReciver(){
        telephonyManager = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener listener = new PhoneStateListener()
        {
            @Override
            public void onCallStateChanged(int state, String number)
            {
                Log.d(TAG, "onCallStateChanged: "+state);
                switch (state)
                {
                    // 无任何状态
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    // 来电铃响时
                    case TelephonyManager.CALL_STATE_RINGING:
                        break;
                    default:
                        break;
                }
                super.onCallStateChanged(state, number);
            }
        };
        telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void updatePlaybackState(boolean isPlaying) {
        PlaybackState.Builder stateBuilder = new PlaybackState.Builder()
                .setActions(PlaybackState.ACTION_PLAY
                        | PlaybackState.ACTION_PLAY_PAUSE
                        | PlaybackState.ACTION_PLAY_FROM_MEDIA_ID
                        | PlaybackState.ACTION_PAUSE
                        | PlaybackState.ACTION_SKIP_TO_NEXT
                        | PlaybackState.ACTION_SKIP_TO_PREVIOUS);
        if (isPlaying) {
            stateBuilder.setState(PlaybackState.STATE_PLAYING,
                    PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                    SystemClock.elapsedRealtime());
        } else {
            stateBuilder.setState(PlaybackState.STATE_PAUSED,
                    PlaybackState.PLAYBACK_POSITION_UNKNOWN,
                    SystemClock.elapsedRealtime());
        }
        session.setPlaybackState(stateBuilder.build());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void updateMediaCenterInfo(String title, String artist) {
        if (session == null) return;
        MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, title);//歌曲名
        metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, artist);//歌手
        session.setMetadata(metadataBuilder.build());
        updatePlaybackState(true);
    }

}
