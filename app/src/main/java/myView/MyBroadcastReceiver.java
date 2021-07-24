package myView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import Helper.MediaPlayerHelper;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
            KeyEvent event = (KeyEvent) intent
                    .getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (event == null) return;
            int keycode = event.getKeyCode();
            switch (keycode) {
                case KeyEvent.KEYCODE_MEDIA_NEXT:
                    MediaPlayerHelper.getInstance(context).next();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                    MediaPlayerHelper.getInstance(context).pre();
                    break;
                case KeyEvent.KEYCODE_MEDIA_PAUSE:
                    if(event.getAction()!=KeyEvent.ACTION_UP){
                        MediaPlayerHelper.getInstance(context).playOrPause();
                    }
                    break;
                case KeyEvent.KEYCODE_MEDIA_PLAY:
                    if(event.getAction()!=KeyEvent.ACTION_UP){
                        MediaPlayerHelper.getInstance(context).playOrPause();
                    }
                    break;
            }
        }
    }

}
