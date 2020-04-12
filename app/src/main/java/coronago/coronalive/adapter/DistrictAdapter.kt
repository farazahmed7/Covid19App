package coronago.coronalive.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coronago.coronalive.R
import coronago.coronalive.model.DistrictModel
import kotlinx.android.synthetic.main.item_list_district.view.*
import retrofit2.http.HEAD

class DistrictAdapter(): ListAdapter<DistrictModel, RecyclerView.ViewHolder>(DistrictDiffUtility()) {
    override fun getItemCount(): Int {
        return super.getItemCount()+1
    }
     val HEADER=0
     val ITEM=1
    override fun getItemViewType(position: Int): Int {
        if (position==0) return HEADER
        else
            return ITEM
    }

    class DistrictItenHeaderViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

    }
    class DistrictViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(district: DistrictModel){
            itemView.districtTextView.setText(district.district)
            itemView.confirmedTextview.setText(district.confirmed)
            if(!district.delta.confirmed.equals("0"))
            itemView.deltaConfirmedTextview.setText("+"+district.delta.confirmed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val v= inflater.inflate(R.layout.item_list_district,parent,false)

        if(viewType==HEADER)
        {
            val v= inflater.inflate(R.layout.item_list_district_header,parent,false)
            return DistrictItenHeaderViewHolder(v)
        }
        return DistrictViewHolder(v)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is DistrictItenHeaderViewHolder)
        {
            //nothing
        }
            else {
            val holder=holder as DistrictViewHolder
            holder.bind(getItem(position-1))

        }
    }
}

class DistrictDiffUtility: DiffUtil.ItemCallback<DistrictModel>()
{
    override fun areItemsTheSame(oldItem: DistrictModel, newItem: DistrictModel): Boolean {
        return oldItem.district.trim() == newItem.district.trim()
    }

    override fun areContentsTheSame(oldItem: DistrictModel, newItem: DistrictModel): Boolean {
        return oldItem.district.trim() == newItem.district.trim()
    }

}