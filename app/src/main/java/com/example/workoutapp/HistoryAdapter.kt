package com.example.workoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ItemHistoryRowBinding

class HistoryAdapter(private val items : ArrayList<String>): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    class HistoryViewHolder(binding : ItemHistoryRowBinding):RecyclerView.ViewHolder(binding.root){
        val llHistoryItemMain = binding.llHistoryItemMain
        val tvPosition = binding.tvPosition
        val tvDate = binding.tvDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(ItemHistoryRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val date : String = items[position]
        holder.tvPosition.text = (position + 1).toString()
        holder.tvDate.text = date

        if(position % 2 == 0){
            //The context is needed if the color value is gotten from our resource file
            holder.llHistoryItemMain.setBackgroundColor(ContextCompat.getColor(holder.itemView.context,R.color.light_grey))
        }else{
            //Parsing a color does not need a context
            holder.llHistoryItemMain.setBackgroundColor(Color.parseColor(("#FFFFFF")))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}