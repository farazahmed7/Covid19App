package coronago.coronalive.ui.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.IMarker
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import coronago.coronalive.R
import coronago.coronalive.customview.CustomMarkerView
import coronago.coronalive.model.GraphInfo
import coronago.coronalive.utility.CoupleChartGestureListener
import coronago.coronalive.utility.PreferenceUtility
import coronago.coronalive.widget.CovidWidget
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var viewModel:MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        val prefUtilty= PreferenceUtility(this)
        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getStatewiseData()
        viewModel.combinedLiveData.observe(this, Observer {combined->
            val it=combined.first.body()!!.statewise

            deathsTextview.text = it[0].deaths
            recoveredTextview.text = it[0].recovered
            ActiveTextview.text = it[0].active
            confirmedTextview.text = it[0].confirmed
            textView.text=it[0].deltaconfirmed
            prefUtilty.saveConfirmedPref(it[0].confirmed)
            sendBroadcastToWidget()


        })
        supportFragmentManager.beginTransaction().add(R.id.container,ListFragment()).commit()

        bottomNavigation.setOnNavigationItemSelectedListener(this)


        }

    fun sendBroadcastToWidget(){
        val man = AppWidgetManager.getInstance(this)
        val ids = man.getAppWidgetIds(
            ComponentName(this, CovidWidget::class.java)
        )
        val updateIntent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
        updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
        sendBroadcast(updateIntent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId){
            R.id.navigation_graph -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,GraphFragment()).commit()
                return true

            }
            R.id.navigation_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.container,ListFragment()).commit()
                return true
            }
        }
        return true
    }




}


