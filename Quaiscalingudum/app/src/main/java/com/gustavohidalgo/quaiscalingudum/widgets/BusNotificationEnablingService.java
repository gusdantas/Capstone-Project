package com.gustavohidalgo.quaiscalingudum.widgets;


import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.views.MainActivity;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class BusNotificationEnablingService extends IntentService {

    public static final String ACTION_ENABLE_NOTIFICATION = "com.gustavohidalgo.quaiscalingudum.action.enable_notification";
    public static final String ACTION_UPDATE_NOTIFICATION_WIDGETS = "com.gustavohidalgo.quaiscalingudum.action.update_notification_widgets";

    public BusNotificationEnablingService() {
        super("BusNotificationEnablingService");
    }

    /**
     * Starts this service to perform WaterPlant action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionEnableNotification(Context context) {
        Intent intent = new Intent(context, BusNotificationEnablingService.class);
        intent.setAction(ACTION_ENABLE_NOTIFICATION);
        context.startService(intent);
    }

    /**
     * Starts this service to perform UpdateNotificationWidgets action with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateNotificationWidgets(Context context) {
        Intent intent = new Intent(context, BusNotificationEnablingService.class);
        intent.setAction(ACTION_UPDATE_NOTIFICATION_WIDGETS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }

    /**
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        startForeground(10, getNotification());
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_ENABLE_NOTIFICATION.equals(action)) {
                handleActionEnableNotification();
            } else if (ACTION_UPDATE_NOTIFICATION_WIDGETS.equals(action)) {
                handleActionUpdateNotificationWidgets();
            }
        }
        stopForeground(true);
    }

    /**
     * Handle action EnableNotification in the provided background thread with the provided
     * parameters.
     */
    private void handleActionEnableNotification() {
        startActionUpdateNotificationWidgets(this);
    }

    /**
     * Handle action UpdateNotificationWidgets in the provided background thread
     */
    private void handleActionUpdateNotificationWidgets() {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BusNotificationWidgetProvider.class));
        //Trigger data update to handle the ListView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.notification_list_lv);
        //Now update all widgets
        BusNotificationWidgetProvider.updateNotificationWidgets(this, appWidgetManager, appWidgetIds);
    }

    // https://stackoverflow.com/questions/6397754/android-implementing-startforeground-for-a-service/4
    public Notification getNotification() {

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,0);

        NotificationCompat.Builder foregroundNotification = new NotificationCompat.Builder(this);
        foregroundNotification.setOngoing(true);

        foregroundNotification.setContentTitle("Quaiscalingudum Notification")
                .setContentText("Widget recalculating")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentIntent(pendingIntent);

        return foregroundNotification.build();
    }
}
