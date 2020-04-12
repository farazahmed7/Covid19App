package coronago.coronalive.ui.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coronago.coronalive.api.CovidRepository
import coronago.coronalive.api.response.ApiResponse
import coronago.coronalive.api.response.DistrictRespnose
import coronago.coronalive.model.CombinedStatsModel
import coronago.coronalive.model.DistrictModel
import coronago.coronalive.model.GraphInfo
import coronago.coronalive.model.Statewise
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import java.util.ArrayList

class MainViewModel: ViewModel() {

    val covidRepo=CovidRepository()
    val combinedLiveData=MutableLiveData<Pair<Response<ApiResponse>,HashMap<String,List<DistrictModel>>>>()
    val districtLiveData=MutableLiveData<HashMap<String,List<DistrictModel>>>()
     val graphInfoLiveData = MutableLiveData<GraphInfo>()
    val stateWiseLiveData= MutableLiveData<List<Statewise>>()

    @SuppressLint("CheckResult")
    fun getAllStatsData()
    {
        covidRepo.getAllStatsTillDdate().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                val xList: ArrayList<Int> = arrayListOf()
                val yList: ArrayList<Int> = arrayListOf()
                val deceasedList: ArrayList<Int> = arrayListOf()
                val totalRecovered: ArrayList<Int> = arrayListOf()
                val totalCaseList: ArrayList<Int> = arrayListOf()
                val dateList: ArrayList<String> = arrayListOf()
                val dailyCasesList: ArrayList<Int> = arrayListOf()
                val dailyDeceasedList: ArrayList<Int> = arrayListOf()
                val dailyRecoveredList: ArrayList<Int> = arrayListOf()

                val size=it.body()?.cases_time_series?.size
                val casesTimeSeries= it.body()?.cases_time_series
                for(i in 0 until size!!){
                    if(casesTimeSeries!![i].totalconfirmed=="") continue
                    yList.add(casesTimeSeries!![i].totalconfirmed.toInt())
                    xList.add(i)
                    totalCaseList.add(casesTimeSeries[i].dailyconfirmed.toInt())
                    dateList.add(casesTimeSeries[i].date)
                    deceasedList.add(casesTimeSeries[i].totaldeceased.toInt())
                    totalRecovered.add(casesTimeSeries[i].totalrecovered.toInt())
                    dailyRecoveredList.add(casesTimeSeries[i].dailyrecovered.toInt())
                    dailyCasesList.add(casesTimeSeries[i].dailyconfirmed.toInt())
                    dailyDeceasedList.add(casesTimeSeries[i].dailydeceased.toInt())

            }
                val graphInfo=GraphInfo(xList,yList,totalCaseList,dailyCasesList,dailyDeceasedList,dailyRecoveredList,deceasedList,dateList,totalRecovered)
                graphInfoLiveData.value=graphInfo

            },
                {
                    Log.d("rxerror",it.message)
                })
    }

    fun getDistrictWiseData():MutableLiveData<HashMap<String,List<DistrictModel>>>{
        covidRepo.getDistrictWise().flatMap {
           val  map=HashMap<String,List<DistrictModel>>()
            val list=it.body()
            for(i in 0 until list!!.size)
              map.put(list!![i].state, list!![i].districtData)
        Observable.just(map)

        }.
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

        return districtLiveData

    }
    fun getStatewiseData(): MutableLiveData<List<Statewise>>{
        val stateObservable=covidRepo.getAllStatsTillDdate().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


        val districtObservable=covidRepo.getDistrictWise().flatMap {
            val  map=HashMap<String,List<DistrictModel>>()
            val list=it.body()
            for(i in 0 until list!!.size)
                map.put(list!![i].state, list!![i].districtData)
            Observable.just(map)

        }.
        subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())


        val combined=Observable.zip(stateObservable,districtObservable, BiFunction<Response<ApiResponse>,HashMap<String,List<DistrictModel>>, Pair<Response<ApiResponse>,HashMap<String,List<DistrictModel>>>>
            { response: Response<ApiResponse>, hashMap: HashMap<String, List<DistrictModel>> ->
                  stateWiseLiveData.postValue(response.body()!!.statewise)
                  districtLiveData.postValue(hashMap)
               Pair(response,hashMap)
            }
        ).subscribe({
            combinedLiveData.value=it
        },
            {
                Log.d("Rxerror",it.message)
            })
        return stateWiseLiveData
            }
    }
