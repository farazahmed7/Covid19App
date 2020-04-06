package coronago.coronalive.model

import coronago.coronalive.api.response.ApiResponse
import retrofit2.Response

class CombinedStatsModel (
    val stateWise:Response<ApiResponse>,
    val districtWise:HashMap<String,List<DistrictModel>>
)