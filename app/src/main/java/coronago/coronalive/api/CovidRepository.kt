package coronago.coronalive.api

import androidx.lifecycle.LiveData
import coronago.coronalive.api.response.ApiResponse
import coronago.coronalive.api.response.DistrictRespnose
import io.reactivex.Observable
import retrofit2.Response

class CovidRepository {

    val covidApiService=CovidApiService.createService()

    fun getAllStatsTillDdate(): Observable<Response<ApiResponse>>{
        return covidApiService.getAllData()
    }

    fun getDistrictWise(): Observable<Response<List<DistrictRespnose>>>{
        return covidApiService.getDistrictWiseData()
    }
}