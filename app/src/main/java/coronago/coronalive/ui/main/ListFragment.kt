package coronago.coronalive.ui.main

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
        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(v.toolbar)
        }
        v.recyclerView.layoutManager= LinearLayoutManager(activity)
        val adapter=StateListAdapter()
        v.recyclerView.adapter=adapter

        viewModel= ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.getStatewiseData().observe(viewLifecycleOwner, Observer {
         /*   v.deathsTextview.setText(it[0].deaths)
            v.recoveredTextview.setText(it[0].recovered)
            v.ActiveTextview.setText(it[0].active)
            v.confirmedTextview.setText(it[0].confirmed)
            adapter.submitList(it)*/

        })



        viewModel.getStatewiseData()
        viewModel.combinedLiveData.observe(viewLifecycleOwner, Observer {combined->
            val it=combined.first.body()!!.statewise
            val districtMap=combined.second

            v.deathsTextview.text = it[0].deaths
            v.recoveredTextview.text = it[0].recovered
            v.ActiveTextview.text = it[0].active
            v.confirmedTextview.text = it[0].confirmed
            v.textView.text=it[0].deltaconfirmed
            adapter.setDistrictMap(districtMap)
            adapter.submitList(it)

        })


        return v

    }

}
