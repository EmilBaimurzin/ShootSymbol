package com.shoot.game.domain.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shoot.game.databinding.ItemScoresBinding

class ScoresAdapter : RecyclerView.Adapter<ScoresViewHolder>() {
    var items = mutableListOf<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScoresViewHolder {
        return ScoresViewHolder(
            ItemScoresBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ScoresViewHolder, position: Int) {
        holder.bind(position + 1, items[position])
    }
}

class ScoresViewHolder(private val binding: ItemScoresBinding) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(place: Int, score: Int) {
        binding.place.text = place.toString()
        binding.score.text = score.toString()
    }
}