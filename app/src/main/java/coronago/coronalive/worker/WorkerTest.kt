package coronago.coronalive.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class WorkerTest(context: Context,
                 workerParams: WorkerParameters
) :Worker(context, workerParams){
    override fun doWork(): Result {
                Log.d("test","worker")
        return Result.success()
    }
}