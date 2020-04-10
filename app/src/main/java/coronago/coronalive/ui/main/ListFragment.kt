package coronago.coronalive.ui.main

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager

import coronago.coronalive.R
import coronago.coronalive.adapter.StateListAdapter
import coronago.coronalive.model.CombinedStatsModel
import coronago.coronalive.utility.PreferenceUtility
import coronago.coronalive.widget.CovidWidget
import kotlinx.android.synthetic.main.fragment_list.*
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.fragment_list.view.recyclerView

/**
 * A simple [Fragment] subclass.
 */
class ListFragment : Fragment() {

    private lateinit var viewModel:MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_list, container, false)

        v.recyclerView.layoutManager= LinearLayoutManager(activity)
        val adapter=StateListAdapter()
        v.recyclerView.adapter=adapter

        viewModel= ViewModelProviders.of(activity!!).get(MainViewModel::class.java)
        viewModel.getStatewiseData().observe(viewLifecycleOwner, Observer {
         /*   v.deathsTextview.setText(it[0].deaths)
            v.recoveredTextview.setText(it[0].recovered)
            v.ActiveTextview.setText(it[0].active)
            v.confirmedTextview.setText(it[0].confirmed)
            adapter.submitList(it)*/

        })


        val prefUtilty=PreferenceUtility(activity!!)
        viewModel.getStatewiseData()
        viewModel.combinedLiveData.observe(viewLifecycleOwner, Observer {combined->
            val it=combined.first.body()!!.statewise
            val districtMap=combined.second
            prefUtilty.saveConfirmedPref(it[0].confirmed)
            adapter.setDistrictMap(districtMap)
            adapter.submitList(it)

        })


        return v

    }



}
