package coronago.coronalive.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import coronago.coronalive.R
import coronago.coronalive.adapter.StateListAdapter
import coronago.coronalive.utility.PreferenceUtility
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_graph.*
import kotlinx.android.synthetic.main.fragment_list.view.*


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

        viewModel= activity?.run {
            ViewModelProviders.of(this).get(MainViewModel::class.java)
        } ?: throw Exception("Invalid Activity")



        val prefUtilty=PreferenceUtility(activity!!)
     //   viewModel.getStatewiseData()
        viewModel.combinedLiveData.observe(viewLifecycleOwner, Observer {combined->
            val it=combined.first.body()!!.statewise
            val districtMap=combined.second
            prefUtilty.saveConfirmedPref(it[0].confirmed)
            adapter.setDistrictMap(districtMap)
            val animationController: LayoutAnimationController = AnimationUtils.loadLayoutAnimation(v.recyclerView.context, R.anim.layout_animation)
            v.recyclerView.layoutAnimation = animationController

            adapter.submitList(it)
            v.swipeToRefresh.isRefreshing=false

        })
        viewModel.errorLivedata.observe(this, Observer {
                swipeToRefresh.isRefreshing=false
        })
        v.swipeToRefresh.setOnRefreshListener {
            viewModel.getStatewiseData()

        }


        return v

    }



}
