package coronago.coronalive.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

import coronago.coronalive.R
import coronago.coronalive.customview.CustomMarkerView
import coronago.coronalive.model.GraphInfo
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_graph.view.*

/**
 * A simple [Fragment] subclass.
 */
class GraphFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_graph, container, false)


        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getAllStatsData()
        val entries=ArrayList<Entry>()
        val entries2=ArrayList<Entry>()

        viewModel.graphInfoLiveData.observe(viewLifecycleOwner, Observer {


            for(i in 0 until it.xAxis.size )
                entries.add(Entry(it.xAxis[i].toFloat(), it.yAsix[i].toFloat()))
            setChart(entries,it,view.dailyCasesChart)

            for(i in 0 until it.xAxis.size )
                entries2.add(Entry(it.xAxis[i].toFloat(), it.dailyDeceaseCasesList[i].toFloat()))
            setChart(entries2,it,view.deceasedChart)

            setListeners(view.dailyCasesChart,view.deceasedChart)



        })
            return view
    }


    fun  setListeners(casesChart:LineChart, deathsChart:LineChart){
        casesChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                TODO("Not yet implemented")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    deathsChart.highlightValue(e.getX(),e.getY(),0,false)
                };
            }

        })

        deathsChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                TODO("Not yet implemented")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    casesChart.highlightValue(e.getX(),e.getY(),0,false)
                };
            }

        })


    }
    fun setChart(entries: List<Entry>, graphInfo: GraphInfo, chartArg: LineChart){

        val chart=chartArg

        //    deathsChart.setBackgroundColor(resources.getColor(R.color.red))

        val lineDataSet= LineDataSet(entries,"")
        val lineData= LineData(lineDataSet)
        chart.data=lineData
        lineData.setDrawValues(false)
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth= 5F
        lineDataSet.setDrawHighlightIndicators(false)
        chart.axisRight.setDrawGridLines(false);
        chart.axisLeft.setDrawGridLines(false);
        chart.xAxis.setDrawGridLines(false);
        chart.setScaleEnabled(false)
        chart.setTouchEnabled(true);
        chart.xAxis.textSize = 8F;//set DP size
        chart.invalidate()
        val mv: IMarker =
            CustomMarkerView(activity, R.layout.custom_marker_view_layout)
        chart.marker=mv
        chart.xAxis.position= XAxis.XAxisPosition.BOTTOM
        //chart.axisLeft.axisMinimum=5f
        //chart.axisRight.axisMinimum=5f

        chart.zoomOut()

        chart.xAxis.setValueFormatter(object: IndexAxisValueFormatter(){
            override fun getFormattedValue(value: Float): String? {
                return graphInfo.dateList[value.toInt()]
            }

        } )



    }

}
