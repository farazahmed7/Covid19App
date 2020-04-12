package coronago.coronalive.worker

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.RxWorker
import androidx.work.WorkerParameters
import coronago.coronalive.api.CovidApiService
import coronago.coronalive.utility.PreferenceUtility
import coronago.coronalive.widget.CovidWidget
import io.reactivex.Single
import java.util.*

class WidgetUpdateWorker(context: Context,
                                  workerParams: WorkerParameters
) : RxWorker(context, workerParams) {

    override fun createWork(): Single<Result> {

        val apiSchedulers=CovidApiService.createService()
        val prefUtility=PreferenceUtility(applicationContext)
       val single=Single.fromObservable(apiSchedulers.getAllData())
        Log.d("worker","123")

        val date=Calendar.getInstance().getTime();
       return  single.doOnSuccess{
                if(it.code()==200){
                    val resp=it.body()!!.statewise[0]
                   val totalConfirmedText= it.body()!!.statewise[0].confirmed
                    val totalDeaths=resp.deaths
                    prefUtility.saveConfirmedPref(totalConfirmedText)
                    prefUtility.saveDatePref(date.toString())
                    prefUtility.saveDatePref(totalDeaths)
                    val man = AppWidgetManager.getInstance(applicationContext)
                    // 2
                    val ids = man.getAppWidgetIds(
                        ComponentName(applicationContext, CovidWidget::class.java)
                    )
                    // 3
                    val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
// 4
                    updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
// 5
                    applicationContext.sendBroadcast(updateIntent)
                }
    }.map { Result.success() }
           .onErrorReturn { Result.failure() }

}
}