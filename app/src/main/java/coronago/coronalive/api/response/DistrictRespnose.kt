package coronago.coronalive.api.response

import coronago.coronalive.model.DistrictModel

class DistrictRespnose(
    val state: String,
    val districtData: List<DistrictModel>
)