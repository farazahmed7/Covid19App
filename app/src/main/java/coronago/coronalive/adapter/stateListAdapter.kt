package coronago.coronalive.adapter

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coronago.coronalive.R
import coronago.coronalive.model.DistrictModel
import coronago.coronalive.model.Statewise
import kotlinx.android.synthetic.main.fragment_list.view.*
import kotlinx.android.synthetic.main.item_list.view.*
import kotlinx.android.synthetic.main.item_list.view.confirmedTextview
import kotlinx.android.synthetic.main.item_list.view.recoveredTextview

class StateListAdapter(): ListAdapter<Statewise, RecyclerView.ViewHolder>(StateDiffUtility()) {

    private lateinit var districtMap:HashMap<String,List<DistrictModel>>
    val stateMap= HashMap<Int,Boolean>()
    val viewPool= RecyclerView.RecycledViewPool()
    fun setDistrictMap(hashmap:HashMap<String,List<DistrictModel>>)
    {
        districtMap=hashmap
    }
     inner class StateListViewHolder(val view: View): RecyclerView.ViewHolder(view){
        fun bind(state: Statewise, position: Int){
                if(position%2==0)
                    itemView.parentLayout.setBackgroundColor(itemView.context.resources.getColor(R.color.gray))
            else
            itemView.parentLayout.setBackgroundColor(Color.TRANSPARENT)

                view.stateTextView.setText(state.state)
                view.confirmedTextview.setText(state.confirmed)
              if(state.deltaconfirmed.toInt()!=0)
                view.deltaConfirmedTextview.setText("+"+state.deltaconfirmed)
            else
                  view.deltaConfirmedTextview.setText("")
                view.recoveredTextview.setText(state.recovered)
                view.activeTextview.setText(state.active)
                view.deceasedTextview.setText(state.deaths)

            val districtList=districtMap.get(state.state.trim())
                val lm=LinearLayoutManager(view.context)
            view.recylerview.layoutManager=lm
                view.recylerview.isNestedScrollingEnabled=false
            val adapter= DistrictAdapter()
            view.recylerview.setRecycledViewPool(viewPool)
            view.recylerview.adapter=adapter
            if(stateMap.containsKey(position) && stateMap[position]==true) {
                view.arrowImageView.animate().setDuration(0).rotation(180F)
                adapter.submitList(districtList)
                view.expandable_layout.expand(false)
                }


          view.parentLayout.setOnClickListener {
                view.expandable_layout.toggle()
                adapter.submitList(districtList)
              if(view.expandable_layout.isExpanded) {
                  stateMap.put(position, true)
                  view.arrowImageView.animate().setDuration(600).rotation(180F)

              }
              else{
                  stateMap.put(position,false)
              view.arrowImageView.animate().setDuration(600).rotation(0F)}


            }


        }
    }

    override fun getItemViewType(position: Int): Int {
        if(position==0) return 0;
        else return 1;
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.item_list, parent, false);

                if(viewType==0){
                val view = layoutInflater.inflate(R.layout.list_item_header, parent, false);
                return HeaderViwHolder(view)}



        return StateListViewHolder(v)}



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if(getItemViewType(position)==1)
        with(holder as StateListViewHolder){

            bind(getItem(position),position)

        }
    }

    class StateDiffUtility: DiffUtil.ItemCallback<Statewise>()
    {
        override fun areItemsTheSame(oldItem: Statewise, newItem: Statewise): Boolean {
            return oldItem.state.trim() == newItem.state.trim()
        }

        override fun areContentsTheSame(oldItem: Statewise, newItem: Statewise): Boolean {
            return oldItem.state.trim() == newItem.state.trim()
        }

    }

    class HeaderViwHolder(val view: View): RecyclerView.ViewHolder(view){

    }
}