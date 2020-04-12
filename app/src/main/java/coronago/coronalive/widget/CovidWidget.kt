package coronago.coronalive.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.work.*
import coronago.coronalive.R
import coronago.coronalive.ui.main.MainActivity
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
        val workManager = WorkManager.getInstance()
        val periodicRequest=PeriodicWorkRequest.Builder(WidgetUpdateWorker::class.java,28,TimeUnit.MINUTES).build()
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
            val confirmedDeaths= preferenceUtility.loadDeathsPref()
            val views = RemoteViews(context.packageName, R.layout.covid_widget)
            views.setTextViewText(R.id.confirmedTextview, confirmedText)
            views.setTextViewText(R.id.confirmedDeathTextview, confirmedDeaths)
            views.setOnClickPendingIntent(R.id.widgetLayout,getPendingIntent(context))


            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context): PendingIntent? {
            val intent = Intent(context, MainActivity::class.java)

            return PendingIntent.getActivity(context, 0, intent, 0)
        }
    }



}
