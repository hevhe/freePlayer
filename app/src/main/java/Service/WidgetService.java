package Service;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.provider.MediaStore;
import android.widget.RemoteViews;

import com.example.freeplayer.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import Helper.MediaPlayerHelper;
import activity.MusicAppWidget;

public class WidgetService extends Service {

    private Timer timer;
    MediaPlayerHelper mediaPlayerHelper ;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    AppWidgetManager manager;
    ComponentName cn;
    RemoteViews rv;


    public WidgetService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayerHelper = MediaPlayerHelper.getInstance(this);
        manager = AppWidgetManager.getInstance(getApplicationContext());
        cn = new ComponentName(getApplicationContext(), MusicAppWidget.class);
        rv = new RemoteViews(getPackageName(), R.layout.music_app_widget);
        mediaPlayerHelper.setmOnInitMusicListener(new MediaPlayerHelper.OnInitMusicListener() {
            @Override
            public void initMode() {
                rv.setTextViewText(R.id.widget_song_name_text, mediaPlayerHelper.playingSongInfo.getName());
                rv.setTextViewText(R.id.widget_singer_name_text, mediaPlayerHelper.playingSongInfo.getArtist());
                manager.updateAppWidget(cn, rv);
            }
        });


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //updateViews();
            }
        }, 0, 1000);
    }

    private void updateViews() {
        if (mediaPlayerHelper.playingSongInfo.getRid() != "" && mediaPlayerHelper.playingSongInfo.getRid() != null) {
            rv.setTextViewText(R.id.widget_song_name_text, mediaPlayerHelper.playingSongInfo.getName());
            rv.setTextViewText(R.id.widget_singer_name_text, mediaPlayerHelper.playingSongInfo.getArtist());
            manager.updateAppWidget(cn, rv);
        }
    }

    private void updateMusicViews() {
        mediaPlayerHelper = MediaPlayerHelper.getInstance(this);
        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.music_app_widget);
        if (mediaPlayerHelper.playingSongInfo.getRid() != "" && mediaPlayerHelper.playingSongInfo.getRid() != null) {
            rv.setTextViewText(R.id.widget_song_name_text, mediaPlayerHelper.playingSongInfo.getName());
            rv.setTextViewText(R.id.widget_singer_name_text, mediaPlayerHelper.playingSongInfo.getArtist());
        }


    }
}
