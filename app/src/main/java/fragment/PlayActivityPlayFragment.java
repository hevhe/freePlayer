package fragment;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.freeplayer.R;

import java.util.Timer;
import java.util.TimerTask;

import Helper.MediaPlayerHelper;
import Helper.PopWindowHelper;
import activity.MainPlayActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class PlayActivityPlayFragment extends Fragment {

    CircleImageView imageView;
    MediaPlayerHelper mediaPlayerHelper;
    SeekBar seekBar;
    ImageView playBut;
    ImageView preBut;
    ImageView nextBut;
    ImageView listBut;
    ImageView zhiZhenImage;
    ImageView playModeBut;
    ImageView downloadBut;
    ImageView loveBut;
    ImageView moreInfoBut;
    TextView lrc1;
    TextView lrc2;
    TextView nowPlayTime;
    TextView remainPlayTime;
    final String TAG=this.getClass().getName();
    boolean isChangeSeekbar=false;
    Timer timer=new Timer();

    TimerTask task = new TimerTask(){

        @Override
        public void run() {
            if(isChangeSeekbar)
            {
                return;
            }
            if(mediaPlayerHelper.isPlaying())
            {
                handler.sendEmptyMessage(2);
            }
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setViewPlayStatus();
                    if(getContext()!=null){
                        ((MainPlayActivity)getActivity()).loadViews();
                    }
                    break;
                case 2:
                    onPlayMethod();
                    break;
                case 3:
                    imageView.clearAnimation();
                    zhiZhenImage.clearAnimation();
                    zhiZhenImage.setAnimation(getPauseAnimation());
                    playBut.setImageResource(R.drawable.play);
                    break;
                case 4:
                    imageView.clearAnimation();
                    zhiZhenImage.clearAnimation();
                    imageView.setAnimation(getRotateAllAnimation());
                    zhiZhenImage.setAnimation(getPlayAnimation());
                    playBut.setImageResource(R.drawable.pause);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View getView() {
        return super.getView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.play_activity_play_fragment,container,false) ;
        initView(view);
        timer.schedule(task,0,200);
        if(mediaPlayerHelper.playingSongInfo.getPic()!=""||mediaPlayerHelper.playingSongInfo.getPic()!=null){
            Glide.with(this).load(MediaPlayerHelper.playingSongInfo.getPic()).into(imageView);
            ((MainPlayActivity)getActivity()).loadViews();
        }

        if(mediaPlayerHelper.isPlaying()){
            imageView.setAnimation(getRotateAllAnimation());
            zhiZhenImage.setAnimation(getPlayAnimation());
            playBut.setImageResource(R.drawable.pause);
            nowPlayTime.setText(getSeconds(mediaPlayerHelper.getCurrentPosition()));
        }
        mediaPlayerHelper.setmOnMediaHelperPrepareListener(new MediaPlayerHelper.OnMediaHelperPrepareListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                handler.sendEmptyMessage(1);
                mp.start();
            }
        });

        mediaPlayerHelper.setmOnMediaHelperCompletionListener(new MediaPlayerHelper.OnMediaHelperCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayerHelper.next();
            }
        });
        mediaPlayerHelper.setmOnMediaHelperStatusChangeListener(new MediaPlayerHelper.OnMediaHelperStatusChangeListener() {
            @Override
            public void statusChangeListener() {
                if(mediaPlayerHelper.isPlaying()){
                    handler.sendEmptyMessage(4);
                }
                else{
                    handler.sendEmptyMessage(3);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                nowPlayTime.setText(getSeconds(seekBar.getProgress() *mediaPlayerHelper.getDuration()/100));
                remainPlayTime.setText(getSeconds((100-seekBar.getProgress()) *mediaPlayerHelper.getDuration()/100));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                isChangeSeekbar=true;
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayerHelper.seekTo(seekBar.getProgress()*mediaPlayerHelper.getDuration()/100);
                isChangeSeekbar=false;
            }
        });

        playBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerHelper.playOrPause();
            }
        });
        preBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerHelper.pre();
            }
        });
        listBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopWindowHelper.showPlayListPopWindow(getContext(),mediaPlayerHelper);
            }
        });
        nextBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerHelper.next();
            }
        });
        return view;
    }

    private void initView(View view){
        imageView=view.findViewById(R.id.play_activity_play_mid_image_view);
        playBut=view.findViewById(R.id.play_activity_bottom_play_but);
        nextBut=view.findViewById(R.id.play_activity_bottom_next_but);
        preBut=view.findViewById(R.id.play_activity_bottom_pre_but);
        listBut=view.findViewById(R.id.play_activity_bottom_play_list_but);
        playModeBut=view.findViewById(R.id.play_activity_bottom_play_mode_but);
        downloadBut=view.findViewById(R.id.play_activity_bottom_download_but);
        loveBut=view.findViewById(R.id.play_activity_bottom_love_but);
        moreInfoBut=view.findViewById(R.id.play_activity_bottom_more_info_but);
        zhiZhenImage=view.findViewById(R.id.play_activity_play_mid_image_zhizhen_view);
        lrc1=view.findViewById(R.id.play_activity_mid_lrc1);
        lrc2=view.findViewById(R.id.play_activity_mid_lrc2);
        seekBar=view.findViewById(R.id.play_activity_play_bottom_seekbar);
        nowPlayTime=view.findViewById(R.id.play_activity_play_bottom_now_play_time_text);
        remainPlayTime=view.findViewById(R.id.play_activity_play_bottom_remain_play_time_text);
    }

    private Animation getPlayAnimation(){
        Animation tempRotate  = new RotateAnimation(0f, 15f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.1f);
        LinearInterpolator lin=new LinearInterpolator();
        tempRotate.setInterpolator(lin);
        tempRotate.setDuration(300);//设置动画持续时间
        tempRotate.setRepeatCount(0);//设置重复次数
        tempRotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        tempRotate.setStartOffset(10);//执行前的等待时间
        return tempRotate;
    }

    private Animation getPauseAnimation(){
        Animation tempRotate  = new RotateAnimation(15f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.1f);
        LinearInterpolator lin=new LinearInterpolator();
        tempRotate.setInterpolator(lin);
        tempRotate.setDuration(300);//设置动画持续时间
        tempRotate.setRepeatCount(0);//设置重复次数
        tempRotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        tempRotate.setStartOffset(10);//执行前的等待时间
        return tempRotate;
    }

    private Animation getRotateAllAnimation(){
        Animation tempRotate  = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        LinearInterpolator lin=new LinearInterpolator();
        tempRotate.setInterpolator(lin);
        tempRotate.setDuration(30000);//设置动画持续时间
        tempRotate.setRepeatCount(-1);//设置重复次数
        tempRotate.setFillAfter(true);//动画执行完后是否停留在执行完的状态
        tempRotate.setStartOffset(10);//执行前的等待时间
        return tempRotate;
    }


    @Override
    public void onAttach(Context context) {
        mediaPlayerHelper=MediaPlayerHelper.getInstance(getActivity());
        mediaPlayerHelper.setmOnInitMusicListener(new MediaPlayerHelper.OnInitMusicListener() {
            @Override
            public void initMode() {
                if(getContext()!=null){
                    ((MainPlayActivity)getActivity()).loadViews();
                    playBut.setEnabled(false);
                    Glide.with(getContext()).load(MediaPlayerHelper.playingSongInfo.getPic()).into(imageView);
                }
                Log.d(TAG, "initMode: "+"播放器缓冲中");
            }
        });
        super.onAttach(context);
    }

    private void onPlayMethod(){
        int a=mediaPlayerHelper.getCurrentPosition()*100/mediaPlayerHelper.getDuration();
        seekBar.setProgress(a);
        lrc1.setText(mediaPlayerHelper.lrc1);
        lrc2.setText(mediaPlayerHelper.lrc2);
        nowPlayTime.setText(getSeconds(mediaPlayerHelper.getCurrentPosition()));
        remainPlayTime.setText(getSeconds(mediaPlayerHelper.getDuration()-mediaPlayerHelper.getCurrentPosition()));
    }

    public void setViewPlayStatus(){
        playBut.setImageResource(R.drawable.pause);
        playBut.setEnabled(true);
        handler.sendEmptyMessage(4);
    }

    public static String getSeconds(int time) {
        int seconds=(time/1000)%60;
        int minite=(time/1000)/60;
        return String.format("%02d:%02d", minite,seconds);
    }

    @Override
    public void onDestroyView() {
        imageView.clearAnimation();
        zhiZhenImage.clearAnimation();
        super.onDestroyView();
    }
}
