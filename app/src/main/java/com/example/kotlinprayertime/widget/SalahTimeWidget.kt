package com.example.kotlinprayertime.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.widget.RemoteViews
import com.example.kotlinprayertime.R

/**
 * Implementation of App Widget functionality.
 */
class SalahTimeWidget : AppWidgetProvider() {

    private val TAG = "SalahTimeWidget"
    private var sharedPref:SharedPreferences? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "onReceive: called")
        setAndUpdateSalahTime(context!!)
    }

    private fun setAndUpdateSalahTime(context: Context) {
        Log.d(TAG, "setAndUpdateSalahTime: ")
        val views = RemoteViews(context.packageName, R.layout.salah_time_widget)
        updateSalahTime(context.applicationContext!!, views)
        val appWidget = ComponentName(context, SalahTimeWidget::class.java)
        val appWidgetManager = AppWidgetManager.getInstance(context)
        appWidgetManager.updateAppWidget(appWidget, views)
    }

    private fun updateSalahTime(applicationContext: Context, views: RemoteViews) {
        sharedPref = applicationContext.getSharedPreferences("com.example.kotlinprayertime.TIMING_KEY", Context.MODE_PRIVATE)!!
        val fajar = sharedPref?.getString("FajarTime", "")
        val zhor = sharedPref?.getString("ZohrTime", "")
        val asar = sharedPref?.getString("AsarTime", "")
        val maghrib = sharedPref?.getString("MaghribTime", "")
        val isha = sharedPref?.getString("IshaTime", "")

        Log.i(TAG, "updateSalahTime: fajar: $fajar")

        views.setTextViewText(R.id.tv_widget_fajar, fajar)
        views.setTextViewText(R.id.tv_widget_zohr, zhor)
        views.setTextViewText(R.id.tv_widget_asar, asar)
        views.setTextViewText(R.id.tv_widget_maghrib, maghrib)
        views.setTextViewText(R.id.tv_widget_isha, isha)

    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        Log.d(TAG, "onUpdate: called")
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        Log.d(TAG, "onEnabled: called")
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
        Log.d(TAG, "onDisabled: called")
    }

    fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        Log.d(TAG, "**updateAppWidget: called")
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.salah_time_widget)
        updateSalahTime(context.applicationContext, views)
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

}
