package activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.freeplayer.R;
import java.util.ArrayList;
import java.util.List;
import Adapter.TabFragmentPagerAdapter;
import Helper.MediaPlayerHelper;
import fragment.PlayActivityPlayFragment;
import myView.BlurTransformation;

public class MainPlayActivity extends AppCompatActivity {
    ImageView imageView;
    ViewPager myViewPager;
    private List<Fragment> list;
    TextView songNameText;
    TextView singerNameText;
    TextView tabText1;
    TextView tabText2;
    TextView tabText3;
    TextView tabText4;
    TextView tabText5;
    ImageButton backBut;
    MediaPlayerHelper mediaPlayer;
    final String TAG=this.getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_play_actvity);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        initViews();
        init();
//        if(MediaPlayerHelper.playingMusic.getBackImageUrl()!=null||MediaPlayerHelper.playingMusic.getBackImageUrl()!=""){
//            loadViews();
//        }

    }

    private void initViews(){
        imageView=findViewById(R.id.image_view);
        myViewPager=findViewById(R.id.play_activity_view_pager);
        tabText1=findViewById(R.id.fragment_layout_text1);
        tabText2=findViewById(R.id.fragment_layout_text2);
        tabText3=findViewById(R.id.fragment_layout_text3);
        tabText4=findViewById(R.id.fragment_layout_text4);
        tabText5=findViewById(R.id.fragment_layout_text5);
        songNameText=findViewById(R.id.play_activity_top_layout_song_name_text);
        singerNameText=findViewById(R.id.play_activity_top_layout_singer_name_text);
        backBut=findViewById(R.id.play_activity_back_but);
        mediaPlayer=MediaPlayerHelper.getInstance(this);
        backBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadViews(){
        Glide.with(this).load(MediaPlayerHelper.playingSongInfo.getPic()).apply(RequestOptions.bitmapTransform(new BlurTransformation(this,25,6))).into(imageView);
        songNameText.setText(MediaPlayerHelper.playingSongInfo.getName());
        singerNameText.setText(MediaPlayerHelper.playingSongInfo.getArtist());
    }

    private void init(){
        //myViewPager.setOnPageChangeListener() ;
        //把Fragment添加到List集合里面
        list = new ArrayList<>();
        list.add(new PlayActivityPlayFragment());
        FragmentManager fragmentManager = getSupportFragmentManager();
        myViewPager.setAdapter(new TabFragmentPagerAdapter(fragmentManager,null,list));
        //tabs.setViewPager(myViewPager);
        //初始化显示第一个页面
        myViewPager.setCurrentItem(1);
        myViewPager.setOffscreenPageLimit(1);
    }
}
