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

class DistrictAdapter(): ListAdapter<DistrictModel, DistrictAdapter.DistrictViewHolder>(DistrictDiffUtility()) {

    class DistrictViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun bind(district: DistrictModel){
            itemView.districtTextView.setText(district.district)
            itemView.confirmedTextview.setText(district.confirmed)
            itemView.deltaConfirmedTextview.setText(district.delta.confirmed)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DistrictViewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val v= inflater.inflate(R.layout.item_list_district,parent,false)
        return DistrictViewHolder(v)
    }

    override fun onBindViewHolder(holder: DistrictViewHolder, position: Int) {
        holder.bind(getItem(position))
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