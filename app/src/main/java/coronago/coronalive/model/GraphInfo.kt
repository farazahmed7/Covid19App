package coronago.coronalive.model

import java.util.ArrayList

class GraphInfo(
    val xAxis: List<Int>,
    val yAsix: List<Int>,
    val totalCasesList: List<Int>,
    val dailyCasesList:List<Int>,
    val dailyDeaths:List<Int>,
    val dailyRecovered:List<Int>,
    val totalDeceaseCasesList: List<Int>,
    val dateList: List<String>,
    val totalRecovered: List<Int>
)