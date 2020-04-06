package coronago.coronalive.api

import androidx.lifecycle.LiveData
import coronago.coronalive.api.response.ApiResponse
import coronago.coronalive.api.response.DistrictRespnose
import coronago.coronalive.model.DistrictModel
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface CovidApiService {

    @GET("data.json")
    fun getAllData(): Observable<Response<ApiResponse>>

    @GET("v2/state_district_wise.json")
    fun getDistrictWiseData():Observable<Response<List<DistrictRespnose>>>

    companion object {

        private const val BASE_URL = "https://api.covid19india.org/"

        fun createService(): CovidApiService{
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(CovidApiService::class.java)
        }
    }
}