package com.gustavohidalgo.quaiscalingudum.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
import com.google.gson.Gson;
import com.gustavohidalgo.quaiscalingudum.R;
import com.gustavohidalgo.quaiscalingudum.models.BusNotification;
import com.gustavohidalgo.quaiscalingudum.utils.NotificationUtils;
import com.gustavohidalgo.quaiscalingudum.utils.ParcelableUtils;

import java.util.ArrayList;

import static com.gustavohidalgo.quaiscalingudum.utils.Constants.ALARM_TIME;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.ARRIVE_TIME;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.NOTIFICATION;
import static com.gustavohidalgo.quaiscalingudum.utils.Constants.ONE_MINUTE;

/**
 * Created by Gustavo on 01/04/2018.
 */

public class NotificationJobService extends JobService {
    private final String TAG = NotificationJobService.class.getSimpleName();

    private static final String WATER_REMINDER_NOTIFICATION_CHANNEL_ID = "reminder_notification_channel";
    private static final int WATER_REMINDER_NOTIFICATION_ID = 1138;
    private AsyncTask<Void, Void, ArrayList<Integer>> mBackgroundTask;
    private BusNotification mBusNotification;

    @Override
    public boolean onStartJob(final JobParameters job) {
        Log.i("gugu", "onStartJob");
        final Bundle bundle = job.getExtras();

        //Executar todo trabalho no background
        mBackgroundTask = new AsyncTask<Void, Void, ArrayList<Integer>>() {
            @Override
            protected ArrayList<Integer> doInBackground(Void... voids) {
                Log.i("gugu", "ASTdoInBackground");
                //Executar qualquer trabalho que for necessário
                if (mBusNotification == null){
                    String json = bundle.getString(NOTIFICATION);
                    Gson g = new Gson();
                    mBusNotification = g.fromJson(json, BusNotification.class);
                }

                // consultar olho vivo
                return NotificationUtils.secondsToAlarm(mBusNotification);
            }

            @Override
            protected void onPostExecute(ArrayList<Integer> secondsToAlarm) {
                notificar(secondsToAlarm, mBusNotification, NotificationJobService.this);
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true; //Ainda há algo a ser executado?
        //Retornamos true porque o trabalho ainda está sendo executado dentro
        //do AsyncTask.
        //Se fosse uma operação mais simples retornavamos false.
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        Log.i("gugu", "onStopJob");
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }

    public static void notificar(ArrayList<Integer> secondsToAlarm, BusNotification busNotification,
                                 Context context) {
        Log.i("gugu", "notificar");
        int arrivalTimeMinutes = secondsToAlarm.get(ARRIVE_TIME) / ONE_MINUTE;

        StringBuilder title = new StringBuilder();
        title.append(busNotification.getName()).append(" - ")
                .append(busNotification.getTrip().getTripHeadsign());

        StringBuilder content = new StringBuilder();
        content.append("The bus ").append(busNotification.getTrip().getShapeId())
                .append(" ").append(busNotification.getTrip().getTripHeadsign())
                .append(" may arrive in ").append(arrivalTimeMinutes).append(" minutes.");

        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    WATER_REMINDER_NOTIFICATION_CHANNEL_ID,
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
    }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(context, WATER_REMINDER_NOTIFICATION_CHANNEL_ID)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                //.setLargeIcon(largeIcon(context))
                .setContentTitle(title.toString())
                .setContentText(content.toString())
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content.toString()))
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE)
                //.setContentIntent(contentIntent(context))
//                .addAction(drinkWaterAction(context))
//                .addAction(ignoreReminderAction(context))
                .setAutoCancel(true);

        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);

        Log.i("gugu", "notify");
        notificationManager.notify(WATER_REMINDER_NOTIFICATION_ID, notificationBuilder.build());
        NotificationUtils.deleteJob(context, busNotification);

        if (secondsToAlarm.get(ARRIVE_TIME) > (2*ONE_MINUTE)) {
            Log.i("gugu", "mais q 2 min pro embarque");
            NotificationUtils.scheduleJob(context, busNotification,
                    secondsToAlarm.get(ALARM_TIME));
        } else {
            Log.i("gugu", "menos q 2 min pro embarque");
        }
    }
}
