package com.gustavohidalgo.quaiscalingudum.widgets;


import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.gustavohidalgo.quaiscalingudum.R;

public class BusNotificationWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RemoteViews rv = getNotificationListRemoteView(context);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        //Start the intent service update widget action, the service takes care of updating the widgets UI
        BusNotificationEnablingService.startActionUpdateNotificationWidgets(context);
    }

    /**
     * Updates all widget instances given the widget Ids and display information
     *
     * @param context          The calling context
     * @param appWidgetManager The widget manager
     * @param appWidgetIds     Array of widget Ids to be updated
     */
    public static void updateNotificationWidgets(Context context, AppWidgetManager appWidgetManager,
                                                 int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    /**
     * Creates and returns the RemoteViews to be displayed in the ListView mode widget
     *
     * @param context The context
     * @return The RemoteViews for the ListView mode widget
     */
    private static RemoteViews getNotificationListRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bus_notification_widget);

        // Set the ListWidgetService intent to act as the adapter for the ListView
        Intent intent = new Intent(context, ListWidgetService.class);
        views.setRemoteAdapter(R.id.notification_list_lv, intent);

        return views;
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        BusNotificationEnablingService.startActionUpdateNotificationWidgets(context);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // Perform any action when one or more AppWidget instances have been deleted
    }

    @Override
    public void onEnabled(Context context) {
        // Perform any action when an AppWidget for this provider is instantiated
    }

    @Override
    public void onDisabled(Context context) {
        // Perform any action when the last AppWidget instance for this provider is deleted
    }

}
