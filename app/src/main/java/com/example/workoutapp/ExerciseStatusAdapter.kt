package com.example.workoutapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.workoutapp.databinding.ItemExerciseStatusBinding

class ExerciseStatusAdapter(val items : ArrayList<ExerciseModel>) :RecyclerView.Adapter<ExerciseStatusAdapter.ExerciseStatusHolder>() {
         //The first line has inflated the item view(item_exercise_status.xml) meaning that its components are now accessible
    inner class ExerciseStatusHolder(val itemBinding : ItemExerciseStatusBinding) : RecyclerView.ViewHolder(itemBinding.root){

             //Accessing the textview from the item_exercise_status.xml
        val tvItem : TextView = itemBinding.tvItem

             //User defined method that places the id of the current exercise at the text to be seen in the textview
        fun bindExerciseStatus(exerciseModel: ExerciseModel){
            itemBinding.tvItem.text = exerciseModel.getId().toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseStatusHolder {
        return ExerciseStatusHolder(ItemExerciseStatusBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ExerciseStatusHolder, position: Int) {
        //Get current exercise
        val model : ExerciseModel = items[position]
        //Binding the data to the recycler view item
        holder.bindExerciseStatus(model)
        when{
            model.getIsSelected() -> {
                //itemView is an internal variable representing a single item in the ViewHolder
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_exercise_status_selected)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
            model.getIsCompleted() -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.circular_color_background)
                holder.tvItem.setTextColor(Color.parseColor("#ffffff"))
            }
            else -> {
                holder.tvItem.background = ContextCompat.getDrawable(holder.itemView.context,R.drawable.item_circular_background)
                holder.tvItem.setTextColor(Color.parseColor("#212121"))
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}