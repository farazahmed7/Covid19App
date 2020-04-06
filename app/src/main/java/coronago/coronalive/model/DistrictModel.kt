package coronago.coronalive.model

class DistrictModel(

    val district: String,
    val confirmed: String,
    val delta: Delta
)

class Delta(
    val confirmed: String
)