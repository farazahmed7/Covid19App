package coronago.coronalive.ui.main

import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.jem.fliptabs.FlipTab
import coronago.coronalive.R
import coronago.coronalive.customview.CustomMarkerView
import coronago.coronalive.model.GraphInfo
import coronago.coronalive.utility.MyValueFormatter
import coronago.coronalive.utility.ViewInScrollViewTouchHelper
import kotlinx.android.synthetic.main.fragment_graph.*
import kotlinx.android.synthetic.main.fragment_graph.view.*

/**
 * A simple [Fragment] subclass.
 */
class GraphFragment : Fragment() {
    private lateinit var viewModel: MainViewModel

    private var loaded :Boolean =false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_graph, container, false)

        val viewInScrollView:LinearLayout=view.findViewById(R.id.viewInSv)



   //   viewInScrollView.setOnTouchListener(ViewInScrollViewTouchHelper(viewInScrollView))
        viewModel= activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        viewModel.getAllStatsData()
        val entries=ArrayList<Entry>()
        val entries2=ArrayList<Entry>()
        val entries3=ArrayList<Entry>()
        val entries4=ArrayList<Entry>()
        val entries5=ArrayList<Entry>()
        val entries6=ArrayList<Entry>()
        var graphInfo: GraphInfo? =null


        view.fliptab.setTabSelectedListener(object: FlipTab.TabSelectedListener {
            override fun onTabSelected(isLeftTab: Boolean, tabTextValue: String) {
                if(isLeftTab && loaded )
                    setCumilative(entries,entries2,entries3,graphInfo!!)
                else
                    if(!isLeftTab && loaded )
                setDaily(entries4,entries5,entries6,graphInfo!!)


            }
            override fun onTabReselected(isLeftTab: Boolean, tabTextValue: String) {

            }
        })

        viewModel.graphInfoLiveData.observe(viewLifecycleOwner, Observer {

            var downX:Int=0
            var downY:Int=0
            val dragthreshold=100
            view.dailyCasesChart.setOnTouchListener(ViewInScrollViewTouchHelper(viewInScrollView))
            view.deceasedChart.setOnTouchListener(ViewInScrollViewTouchHelper(viewInScrollView))
            view.recoveredChart.setOnTouchListener(ViewInScrollViewTouchHelper(viewInScrollView))

            setCumilative(entries,entries2,entries3,it)
             graphInfo=it
             setListeners(view.dailyCasesChart,view.deceasedChart,graphInfo!!,view!!)
            loaded=true
            view.swipeToRefresh.isRefreshing=false



        })

        view.swipeToRefresh.setOnRefreshListener {
            viewModel.getAllStatsData()

        }
            return view
    }

    fun setCumilative(entries:ArrayList<Entry>, entries2:ArrayList<Entry>,entries3:ArrayList<Entry>,it: GraphInfo){
        entries.clear(); entries2.clear(); entries3.clear()
        for(i in 0 until it.xAxis.size )
            entries.add(Entry(it.xAxis[i].toFloat(), it.yAsix[i].toFloat()))
        setChart(entries,it,view!!.dailyCasesChart)

        for(i in 0 until it.xAxis.size )
            entries2.add(Entry(it.xAxis[i].toFloat(), it.totalDeceaseCasesList[i].toFloat()))
        setChart(entries2,it,view!!.deceasedChart)

        for(i in 0 until it.xAxis.size )
            entries3.add(Entry(it.xAxis[i].toFloat(), it.totalRecovered[i].toFloat()))
        setChart(entries3,it,view!!.recoveredChart)

    }

    fun setDaily(entries:ArrayList<Entry>, entries2:ArrayList<Entry>,entries3:ArrayList<Entry>,it: GraphInfo){

        entries.clear(); entries2.clear(); entries3.clear()
        for(i in 0 until it.xAxis.size )
            entries.add(Entry(it.xAxis[i].toFloat(), it.dailyCasesList[i].toFloat()))
        setChart(entries,it,view!!.dailyCasesChart)

        for(i in 0 until it.xAxis.size )
            entries2.add(Entry(it.xAxis[i].toFloat(), it.dailyDeaths[i].toFloat()))
        setChart(entries2,it,view!!.deceasedChart)

        for(i in 0 until it.xAxis.size )
            entries3.add(Entry(it.xAxis[i].toFloat(), it.dailyRecovered[i].toFloat()))
        setChart(entries3,it,view!!.recoveredChart)

    }
    fun  setListeners(casesChart:LineChart, deathsChart:LineChart,graphInfo: GraphInfo, view: View){
        casesChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    deathsChart.highlightValue(e.getX(),e.getY(),0,false)
                    recoveredChart.highlightValue(e.getX(),e.getY(),0,false)
                    casesChart.parent.parent.requestDisallowInterceptTouchEvent(true)
                    val index=Math.round(e.x)
                    view.confirmedDate.text=graphInfo.dateList[index].toString()
                    view.recoveredDate.text=graphInfo.dateList[index].toString()
                    view.deathsDate.text=graphInfo.dateList[index].toString()
                };
            }

        })

        deathsChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    casesChart.highlightValue(e.getX(),e.getY(),0,false)
                    recoveredChart.highlightValue(e.getX(),e.getY(),0,false)
                    val index=Math.round(e.x)
                    view.confirmedDate.text=graphInfo.dateList[index].toString()
                    view.recoveredDate.text=graphInfo.dateList[index].toString()
                    view.deathsDate.text=graphInfo.dateList[index].toString()
                    deathsChart.parent.parent.requestDisallowInterceptTouchEvent(true)

                };
            }

        })

        recoveredChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                if (e != null) {
                    casesChart.highlightValue(e.getX(),e.getY(),0,false)
                    deathsChart.highlightValue(e.getX(),e.getY(),0,false)
                    recoveredChart.parent.parent.requestDisallowInterceptTouchEvent(true)
                    val index=Math.round(e.x)
                    view.confirmedDate.text=graphInfo.dateList[index].toString()
                    view.recoveredDate.text=graphInfo.dateList[index].toString()
                    view.deathsDate.text=graphInfo.dateList[index].toString()

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
        lineDataSet.lineWidth= 3F
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

        chart.axisLeft.valueFormatter=LargeValueFormatter()
        chart.axisRight.isEnabled=false
        chart.animateX(600)

    }

}
