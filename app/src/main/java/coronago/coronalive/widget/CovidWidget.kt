package coronago.coronalive.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.util.Log
import android.widget.RemoteViews
import androidx.work.*
import coronago.coronalive.R
import coronago.coronalive.utility.PreferenceUtility
import coronago.coronalive.worker.WidgetUpdateWorker
import java.util.concurrent.TimeUnit

/**
 * Implementation of App Widget functionality.
 */
class CovidWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {

        for (appWidgetId in appWidgetIds) {
            CovidWidget.updateAppWidget(context, appWidgetManager, appWidgetId)
        }

    }

    override fun onEnabled(context: Context) {
        Log.d("enabled","widget")
        val workManager = WorkManager.getInstance()
        val periodicRequest=PeriodicWorkRequest.Builder(WidgetUpdateWorker::class.java,16,TimeUnit.MINUTES).build()
        workManager.enqueueUniquePeriodicWork("worker",ExistingPeriodicWorkPolicy.REPLACE,periodicRequest)    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object{
        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Construct the RemoteViews object
            val preferenceUtility=PreferenceUtility(context)
            val confirmedText=preferenceUtility.loadConfirmedPref()
            val views = RemoteViews(context.packageName, R.layout.covid_widget)
            views.setTextViewText(R.id.confirmedTextview, confirmedText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}
