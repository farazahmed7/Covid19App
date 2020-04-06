package coronago.coronalive.api.response

import coronago.coronalive.model.CasesByTime
import coronago.coronalive.model.Statewise

class ApiResponse (val cases_time_series: List<CasesByTime>, val statewise: List<Statewise>  )


