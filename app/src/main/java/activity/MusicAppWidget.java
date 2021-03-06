package activity;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.example.freeplayer.R;

import Service.WidgetService;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link MusicAppWidgetConfigureActivity MusicAppWidgetConfigureActivity}
 */
public class MusicAppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.music_app_widget);// Instruct the widget manager to update the widget
        //views.setOnClickPendingIntent(R.id.widget_play_img_but,getPendingIntent(context,""));
        Intent serviceIntent=new Intent();
        serviceIntent.setClass(context,WidgetService.class);
        context.startService(serviceIntent);
        //PendingIntent pendingIntent=PendingIntent.getService(context,0,serviceIntent,0);
        //views.setOnClickPendingIntent(R.id.widget_play_img_but,pendingIntent );
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            MusicAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    private static PendingIntent getPendingIntent(Context context, String url){
        Intent intent = new Intent();
        intent.setClass(context, MusicAppWidget.class);//????????????????????????
        intent.setAction("PLAY");
        //??????data????????????????????????id?????????????????????
        // ??????????????????????????????????????????id?????????????????????????????????????????????????????????intent???data??????id?????????????????????id
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent,0);
        return pendingIntent;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"action"+intent.getAction(),Toast.LENGTH_LONG).show();
        if(intent.getAction().equals("PLAY")){
            Toast.makeText(context,"????????????"+intent.getAction(),Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(context,"123123"+intent.getAction(),Toast.LENGTH_LONG).show();
            super.onReceive(context, intent);
        }
    }
}

